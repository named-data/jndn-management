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
package com.intel.jndn.management;

import com.intel.jndn.management.helpers.FetchHelper;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simplistic checker for live connection to NFD forwarder.
 */
public final class NdnPingClient {
  public static final long DEFAULT_TIMEOUT = 2000;
  private static final Logger LOG = Logger.getLogger(NdnPingClient.class.getName());

  /**
   * Prevent creation of NdnPingClient instances.
   */
  private NdnPingClient() {
  }

  /**
   * Ping a forwarder on an existing face to verify that the forwarder is
   * working and responding to requests; this version sends a discovery packet
   * to /localhost/nfd which should always respond if the requestor is on the
   * same machine as the NDN forwarding daemon.
   *
   * @param face only a localhost Face
   * @return true if successful, false otherwise
   */
  public static boolean pingLocal(final Face face) {
    return ping(face, new Name("/localhost/nfd"));
  }

  /**
   * Request a name on an existing face to verify the forwarder is working and
   * responding to requests. Note that the name must be served or cached on the
   * forwarder for this to return true.
   *
   * @param face a {@link Face} to ping
   * @param name a known {@link Name} that the remote node will answer to
   * @return true if successful, false otherwise
   */
  public static boolean ping(final Face face, final Name name) {
    // build interest
    Interest interest = new Interest(name);
    interest.setCanBePrefix(true);
    interest.setMustBeFresh(true);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);

    // send packet
    try {
      Data data = FetchHelper.getData(face, interest);
      return data != null;
    } catch (IOException e) {
      LOG.log(Level.INFO, "Error sending ping interest", e);
      return false;
    }
  }
}
