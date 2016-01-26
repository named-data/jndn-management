/*
 * jndn-management
 * Copyright (c) 2015-2016, Intel Corporation.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms and conditions of the GNU Lesser General Public License,
 * version 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 */
package com.intel.jndn.management.helpers;

import net.named_data.jndn.*;
import net.named_data.jndn.encoding.EncodingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FetchHelper implements OnData, OnTimeout {
  public static final long DEFAULT_TIMEOUT = 2000;

  private static final Logger LOG = Logger.getLogger(FetchHelper.class.getName());

  private State state;
  private Face face;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Prevent creation of FetchHelper instances: use getData or getSegmentedData
   */
  private FetchHelper()
  {
  }

  /**
   * Private constructor: use getData or getSegmentedData
   */
  private FetchHelper(Face face)
  {
    this.face = face;
  }

  /**
   * Get a single Data packet
   * @param face Face instance
   * @param interest Interest to retrieve Data
   * @return Data packet
   * @throws IOException if failed to retrieve packet, e.g., timeout occured
   */
  public static Data
  getData(Face face, Interest interest) throws IOException {
    FetchHelper fetcher = new FetchHelper(face);
    return fetcher.getData(interest);
  }

  /**
   * Get data using the exact name (without implicit digest)
   * @param face Face instance
   * @param name Exact name of the data packet to retrieve
   *
   * TODO: Allow authentication of retrieved data packets
   */
  public static Data
  getData(Face face, Name name) throws IOException {
    FetchHelper fetcher = new FetchHelper(face);

    Interest interest = new Interest(name);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);
    interest.setMustBeFresh(false); // this is bug in jndn
    // interest.setMinSuffixComponents(1); // implicit digest
    // interest.setMaxSuffixComponents(1); // implicit digest
    return fetcher.getData(interest);
  }

  /**
   * Get concatenated data from the segmented
   * @param face Face instance
   * @param prefix Prefix of the retrieved data. The retrieved data must have version and segment numbers after this
   *               prefix
   *
   * Note that this method will first send interest with MustBeFresh selector to discover "latest" version of the
   *               stream and then retrieve the rest of the stream
   *
   * TODO: Allow authentication of retrieved data packets
   */
  public static List<Data>
  getSegmentedData(Face face, Name prefix) throws IOException {
    FetchHelper fetcher = new FetchHelper(face);

    Interest interest = new Interest(new Name(prefix));
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);
    interest.setMustBeFresh(true);
    interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
    // interest.setMinSuffixComponents(3); // version, segment, implicit digest
    // interest.setMaxSuffixComponents(3); // version, segment, implicit digest

    Data data = fetcher.getData(interest);

    try {
      data.getName().get(-1).toSegment();
    }
    catch (EncodingException e) {
      throw new IOException("Retrieved data does not have segment number as the last name component", e);
    }
    if (data.getName().size() != prefix.size() + 2) {
      throw new IOException("Retrieved data is not part of segmented stream");
    }

    long finalBlockId = 0;
    try {
      finalBlockId = data.getMetaInfo().getFinalBlockId().toSegment();
    }
    catch (EncodingException e) {
      throw new IOException("Requested segmented stream is unbounded", e);
    }

    List<Data> segments = new ArrayList<>();
    segments.add(data);

    prefix.append(data.getName().get(-2));
    for (int i = 0; i < finalBlockId; i++) {
      interest = new Interest(new Name(prefix).appendSegment(i));
      interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);
      interest.setMustBeFresh(false);
      // interest.setMinSuffixComponents(1); // implicit digest
      // interest.setMaxSuffixComponents(1); // implicit digest
      segments.add(fetcher.getData(interest));
    }

    return segments;
  }


  /////////////////////////////////////////////////////////////////////////////

  private Data getData(Interest interest) throws IOException {
    this.state = new State();
    this.face.expressInterest(interest, this, this);

    while (!state.isDone) {
      try {
        face.processEvents();
      } catch (EncodingException e) {
        LOG.log(Level.INFO, "Decoding error: " + e.getMessage(), e);
      }
      try {
        Thread.sleep(20);
      } catch (InterruptedException e) {
        // ok
      }
    }

    if (state.response == null) {
      throw new IOException("Communication with NFD failed");
    }

    return state.response;
  }

  @Override
  public void onData(Interest interest, Data data) {
    state.response = data;
    state.isDone = true;
  }

  @Override
  public void onTimeout(Interest interest) {
    state.nRetries--;
    if (state.nRetries > 0) {
      try {
        face.expressInterest(new Interest(interest).setNonce(null), this, this);
      } catch (IOException e) {
        LOG.log(Level.INFO, "Error while expressing interest: " + e.toString(), e);
      }
    }
    else {
      state.isDone = true;
    }
  }

  /////////////////////////////////////////////////////////////////////////////

  private static class State {
    public int nRetries = 3;
    public Data response = null;
    public boolean isDone = false;
  }
}
