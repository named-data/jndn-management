/*
 * jndn-management
 * Copyright (c) 2015, Intel Corporation.
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

import java.nio.ByteBuffer;

import com.intel.jndn.management.enums.NfdTlv;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a ForwarderStatus object
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/ForwarderStatus">ForwarderStatus</a>
 *
 * @author Andrew Brown <andrew.brown@intel.com>
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
  private long nInDatas = 0;
  private long nInNacks = 0;
  private long nOutInterests = 0;
  private long nOutDatas = 0;
  private long nOutNacks = 0;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor
   */
  public ForwarderStatus() {
    // nothing to do
  }

  /**
   * Constructor from wire format
   * @param input wire format
   * @throws EncodingException
   */
  public ForwarderStatus(ByteBuffer input) throws EncodingException {
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
   */
  public final void wireEncode(TlvEncoder encoder) {
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutNacks, nOutNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutDatas, nOutDatas);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutInterests, nOutInterests);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInNacks, nInNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInDatas, nInDatas);
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
   * limit(), but does not change the position.
   * @throws EncodingException For invalid encoding.
   */
  public final void wireDecode(ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * Decode as part of an existing decode context
   */
  @Override
  public void wireDecode(TlvDecoder decoder) throws EncodingException {
    this.nfdVersion = new Blob(decoder.readBlobTlv(NfdTlv.NfdVersion), true).toString();
    this.startTimestamp = decoder.readNonNegativeIntegerTlv(NfdTlv.StartTimestamp);
    this.currentTimestamp = decoder.readNonNegativeIntegerTlv(NfdTlv.CurrentTimestamp);
    this.nNameTreeEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NNameTreeEntries);
    this.nFibEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NFibEntries);
    this.nPitEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NPitEntries);
    this.nMeasurementEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NMeasurementsEntries);
    this.nCsEntries = decoder.readNonNegativeIntegerTlv(NfdTlv.NCsEntries);
    this.nInInterests = decoder.readNonNegativeIntegerTlv(NfdTlv.NInInterests);
    this.nInDatas = decoder.readNonNegativeIntegerTlv(NfdTlv.NInDatas);
    this.nInNacks = decoder.readNonNegativeIntegerTlv(NfdTlv.NInNacks);
    this.nOutInterests = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutInterests);
    this.nOutDatas = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutDatas);
    this.nOutNacks = decoder.readNonNegativeIntegerTlv(NfdTlv.NOutNacks);
  }

  public String getNfdVersion() {
    return nfdVersion;
  }

  public void setNfdVersion(String nfdVersion) {
    this.nfdVersion = nfdVersion;
  }

  public long getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(long startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public long getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(long currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }

  public long getNNameTreeEntries() {
    return nNameTreeEntries;
  }

  public void setNNameTreeEntries(long nNameTreeEntries) {
    this.nNameTreeEntries = nNameTreeEntries;
  }

  public long getNFibEntries() {
    return nFibEntries;
  }

  public void setNFibEntries(long nFibEntries) {
    this.nFibEntries = nFibEntries;
  }

  public long getNPitEntries() {
    return nPitEntries;
  }

  public void setNPitEntries(long nPitEntries) {
    this.nPitEntries = nPitEntries;
  }

  public long getNMeasurementsEntries() {
    return nMeasurementEntries;
  }

  public void setNMeasurementsEntries(long nMeasurementEntries) {
    this.nMeasurementEntries = nMeasurementEntries;
  }

  public long getNCsEntries() {
    return nCsEntries;
  }

  public void setNCsEntries(long nCsEntries) {
    this.nCsEntries = nCsEntries;
  }

  public long getNInInterests() {
    return nInInterests;
  }

  public void setNInInterests(long nInInterests) {
    this.nInInterests = nInInterests;
  }

  public long getNInDatas() {
    return nInDatas;
  }

  public void setNInDatas(long nInDatas) {
    this.nInDatas = nInDatas;
  }

  public long getNOutInterests() {
    return nOutInterests;
  }

  public void setNOutInterests(long nOutInterests) {
    this.nOutInterests = nOutInterests;
  }

  public long getNOutDatas() {
    return nOutDatas;
  }

  public void setNOutDatas(long nOutDatas) {
    this.nOutDatas = nOutDatas;
  }

  public long getNInNacks() {
    return nInNacks;
  }

  public void setNInNacks(long nInNacks) {
    this.nInNacks = nInNacks;
  }

  public long getNOutNacks() {
    return nOutNacks;
  }

  public void setNOutNacks(long nOutNacks) {
    this.nOutNacks = nOutNacks;
  }
}
