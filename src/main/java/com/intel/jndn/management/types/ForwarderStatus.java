/*
 * jndn-management
 * Copyright (c) 2015-2018, Intel Corporation.
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
package com.intel.jndn.management.types;

import com.intel.jndn.management.enums.NfdTlv;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;

/**
 * Represent a ForwarderStatus object.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/ForwarderStatus">ForwarderStatus</a>
 */
public class ForwarderStatus implements Decodable {
  private String nfdVersion = "";
  private long startTimestamp = 0;
  private long currentTimestamp = 0;
  private long nNameTreeEntries = 0;
  private long nFibEntries = 0;
  private long nPitEntries = 0;
  private long nMeasurementEntries = 0;
  private long nCsEntries = 0;
  private long nInInterests = 0;
  private long nInData = 0;
  private long nInNacks = 0;
  private long nOutInterests = 0;
  private long nOutData = 0;
  private long nOutNacks = 0;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor.
   */
  public ForwarderStatus() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public ForwarderStatus(final ByteBuffer input) throws EncodingException {
    wireDecode(input);
  }

  /**
   * Encode using a new TLV encoder.
   *
   * @return The encoded buffer.
   */
  public final Blob wireEncode() {
    TlvEncoder encoder = new TlvEncoder();
    wireEncode(encoder);
    return new Blob(encoder.getOutput(), false);
  }

  /**
   * Encode as part of an existing encode context.
   *
   * @param encoder TlvEncoder instance
   */
  public final void wireEncode(final TlvEncoder encoder) {
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutNacks, nOutNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutData, nOutData);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutInterests, nOutInterests);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInNacks, nInNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInData, nInData);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInInterests, nInInterests);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NCsEntries, nCsEntries);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NMeasurementsEntries, nMeasurementEntries);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NPitEntries, nPitEntries);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NFibEntries, nFibEntries);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NNameTreeEntries, nNameTreeEntries);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.CurrentTimestamp, currentTimestamp);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.StartTimestamp, startTimestamp);
    encoder.writeBlobTlv(NfdTlv.NfdVersion, new Blob(nfdVersion).buf());
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
   * @throws EncodingException For invalid encoding.
   */
  public final void wireDecode(final ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wireDecode(final TlvDecoder decoder) throws EncodingException {
    this.nfdVersion = new Blob(decoder.readBlobTlv(NfdTlv.NfdVersion), true).toString();
    this.startTimestamp = decoder.readNonNegativeIntegerTlv(NfdTlv.StartTimestamp);
    this.currentTimestamp = decoder.readNonNegativeIntegerTlv(NfdTlv.CurrentTimestamp);
    this.nNameTreeEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NNameTreeEntries);
    this.nFibEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NFibEntries);
    this.nPitEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NPitEntries);
    this.nMeasurementEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NMeasurementsEntries);
    this.nCsEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NCsEntries);
    this.nInInterests = decoder.readNonNegativeIntegerTlv(NfdTlv.NInInterests);
    this.nInData = decoder.readNonNegativeIntegerTlv(NfdTlv.NInData);
    this.nInNacks = decoder.readNonNegativeIntegerTlv(NfdTlv.NInNacks);
    this.nOutInterests = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutInterests);
    this.nOutData = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutData);
    this.nOutNacks = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutNacks);
  }

  /**
   * @return NFD version string
   */
  public String getNfdVersion() {
    return nfdVersion;
  }

  /**
   * Set NFD version string.
   *
   * @param nfdVersion NFD version string
   * @return this
   */
  public ForwarderStatus setNfdVersion(final String nfdVersion) {
    this.nfdVersion = nfdVersion;
    return this;
  }

  /**
   * @return NFD start timestamp (number of seconds since January 1, 1970)
   */
  public long getStartTimestamp() {
    return startTimestamp;
  }

  /**
   * Set NFD start timestamp (number of seconds since January 1, 1970).
   *
   * @param startTimestamp NFD start timestamp (number of seconds since January 1, 1970)
   * @return this
   */
  public ForwarderStatus setStartTimestamp(final long startTimestamp) {
    this.startTimestamp = startTimestamp;
    return this;
  }

  /**
   * @return NFD current timestamp (number of seconds since January 1, 1970)
   */
  public long getCurrentTimestamp() {
    return currentTimestamp;
  }

  /**
   * Set NFD current timestamp (number of seconds since January 1, 1970).
   *
   * @param currentTimestamp NFD current timestamp (number of seconds since January 1, 1970)
   * @return this
   */
  public ForwarderStatus setCurrentTimestamp(final long currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
    return this;
  }

  /**
   * @return Number of NameTree entries
   */
  public long getNNameTreeEntries() {
    return nNameTreeEntries;
  }

  /**
   * Set the number of NameTree entries.
   *
   * @param nNameTreeEntries the number of NameTree entries.
   * @return this
   */
  public ForwarderStatus setNNameTreeEntries(final long nNameTreeEntries) {
    this.nNameTreeEntries = nNameTreeEntries;
    return this;
  }

  /**
   * @return Number of FIB entries
   */
  public long getNFibEntries() {
    return nFibEntries;
  }

  /**
   * Set the number of FIB entries.
   *
   * @param nFibEntries the number of FIB entries.
   * @return this
   */
  public ForwarderStatus setNFibEntries(final long nFibEntries) {
    this.nFibEntries = nFibEntries;
    return this;
  }

  /**
   * @return Number of PIT entries
   */
  public long getNPitEntries() {
    return nPitEntries;
  }

  /**
   * Set the number of PIT entries.
   *
   * @param nPitEntries the number of PIT entries.
   * @return this
   */
  public ForwarderStatus setNPitEntries(final long nPitEntries) {
    this.nPitEntries = nPitEntries;
    return this;
  }

  /**
   * @return Number of Measurement entries
   */
  public long getNMeasurementsEntries() {
    return nMeasurementEntries;
  }

  /**
   * Set the number of Measurement entries.
   *
   * @param nMeasurementEntries the number of Measurement entries.
   * @return this
   */
  public ForwarderStatus setNMeasurementsEntries(final long nMeasurementEntries) {
    this.nMeasurementEntries = nMeasurementEntries;
    return this;
  }

  /**
   * @return Number of CS entries
   */
  public long getNCsEntries() {
    return nCsEntries;
  }

  /**
   * Set the number of CS entries.
   *
   * @param nCsEntries the number of CS entries.
   * @return this
   */
  public ForwarderStatus setNCsEntries(final long nCsEntries) {
    this.nCsEntries = nCsEntries;
    return this;
  }

  /**
   * @return The number of incoming Interests since NFD start
   */
  public long getNInInterests() {
    return nInInterests;
  }

  /**
   * Set the number of incoming Interests since NFD start.
   *
   * @param nInInterests the number of incoming Interests since NFD start
   * @return this
   */
  public ForwarderStatus setNInInterests(final long nInInterests) {
    this.nInInterests = nInInterests;
    return this;
  }

  /**
   * @return Number of incoming Data since NFD start
   */
  public long getNInData() {
    return nInData;
  }

  /**
   * Set the number of incoming Data since NFD start.
   *
   * @param nInData the number of incoming Interests since NFD start
   * @return this
   */
  public ForwarderStatus setNInData(final long nInData) {
    this.nInData = nInData;
    return this;
  }

  /**
   * @return Number of outgoing Interests since NFD start
   */
  public long getNOutInterests() {
    return nOutInterests;
  }

  /**
   * Set the number of outgoing Interests since NFD start.
   *
   * @param nOutInterests the number of outgoing Interests since NFD start
   * @return this
   */
  public ForwarderStatus setNOutInterests(final long nOutInterests) {
    this.nOutInterests = nOutInterests;
    return this;
  }

  /**
   * @return Number of outgoing Data since NFD start
   */
  public long getNOutData() {
    return nOutData;
  }

  /**
   * Set the number of outgoing Data since NFD start.
   *
   * @param nOutData the number of outgoing Data since NFD start
   * @return this
   */
  public ForwarderStatus setNOutData(final long nOutData) {
    this.nOutData = nOutData;
    return this;
  }

  /**
   * @return Number of incoming NACKs since NFD start
   */
  public long getNInNacks() {
    return nInNacks;
  }

  /**
   * Set the number of incoming NACKs since NFD start.
   *
   * @param nInNacks the number of incoming NACKs since NFD start
   * @return this
   */
  public ForwarderStatus setNInNacks(final long nInNacks) {
    this.nInNacks = nInNacks;
    return this;
  }

  /**
   * @return Number of outgoing NACKs since NFD start
   */
  public long getNOutNacks() {
    return nOutNacks;
  }

  /**
   * Set the number of outgoing NACKs since NFD start.
   *
   * @param nOutNacks the number of outgoing NACKs since NFD start
   * @return this
   */
  public ForwarderStatus setNOutNacks(final long nOutNacks) {
    this.nOutNacks = nOutNacks;
    return this;
  }
}
