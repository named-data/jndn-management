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

import com.intel.jndn.management.enums.NfdTlv;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;

/**
 * Represent a NextHopRecord in a FibEntry.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/FibMgmt#FIB-Dataset">FIB Dataset</a>
 */
public class NextHopRecord implements Decodable {
  private int faceId;
  private int cost;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor.
   */
  public NextHopRecord() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public NextHopRecord(final ByteBuffer input) throws EncodingException {
    wireDecode(input);
  }

  /**
   * Encode using a new TLV encoder.
   *
   * @return The encoded buffer
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
    int saveLength = encoder.getLength();
    encoder.writeNonNegativeIntegerTlv(Tlv.ControlParameters_Cost, cost);
    encoder.writeNonNegativeIntegerTlv(Tlv.ControlParameters_FaceId, faceId);
    encoder.writeTypeAndLength(NfdTlv.NextHopRecord, encoder.getLength() - saveLength);
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
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.NextHopRecord);
    this.faceId = (int) decoder.readNonNegativeIntegerTlv(Tlv.ControlParameters_FaceId);
    this.cost = (int) decoder.readNonNegativeIntegerTlv(Tlv.ControlParameters_Cost);
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * @return face ID
   */
  public int getFaceId() {
    return faceId;
  }

  /**
   * Set face ID.
   *
   * @param faceId face ID
   * @return this
   */
  public NextHopRecord setFaceId(final int faceId) {
    this.faceId = faceId;
    return this;
  }

  /**
   * @return next hop record cost
   */
  public int getCost() {
    return cost;
  }

  /**
   * Set next hop record cost.
   *
   * @param cost next hop record cost
   * @return this
   */
  public NextHopRecord setCost(final int cost) {
    this.cost = cost;
    return this;
  }
}
