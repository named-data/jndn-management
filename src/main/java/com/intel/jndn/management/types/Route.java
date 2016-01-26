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
import com.intel.jndn.management.enums.RouteFlags;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a Route object from /localhost/nfd/rib/list
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/RibMgmt#RIB-Dataset">RIB Dataset</a>
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class Route {
  public static final int INFINITE_EXPIRATION_PERIOD = -1;

  private int faceId = -1;
  private int origin = -1;
  private int cost = -1;
  private int flags = RouteFlags.CHILD_INHERIT.toInteger();
  private int expirationPeriod = INFINITE_EXPIRATION_PERIOD;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor
   */
  public Route() {
    // nothing to do
  }

  /**
   * Constructor from wire format
   * @param input wire format
   * @throws EncodingException
   */
  public Route(ByteBuffer input) throws EncodingException {
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
   * @param encoder
   */
  public final void wireEncode(TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    encoder.writeOptionalNonNegativeIntegerTlv(NfdTlv.ExpirationPeriod, expirationPeriod);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.Flags, flags);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.Cost, cost);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.Origin, origin);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.FaceId, faceId);
    encoder.writeTypeAndLength(NfdTlv.Route, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
   * @throws net.named_data.jndn.encoding.EncodingException
   */
  public final void wireDecode(ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * Decode as part of an existing decode context.
   *
   * @param decoder
   * @throws EncodingException
   */
  public final void wireDecode(TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.Route);
    this.faceId = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.FaceId);
    this.origin = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.Origin);
    this.cost = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.Cost);
    this.flags = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.Flags);
    this.expirationPeriod = (int) decoder.readOptionalNonNegativeIntegerTlv(NfdTlv.ExpirationPeriod, endOffset);
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * Get Face ID
   */
  public int getFaceId() {
    return faceId;
  }

  /**
   * Set Face ID
   */
  public Route setFaceId(int faceId) {
    this.faceId = faceId;
    return this;
  }

  /**
   * Get origin
   */
  public int getOrigin() {
    return origin;
  }

  /**
   * Set origin
   */
  public Route setOrigin(int origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get cost
   */
  public int getCost() {
    return cost;
  }

  /**
   * Set cost
   */
  public Route setCost(int cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get flags
   */
  public int getFlags() {
    return flags;
  }

  /**
   * Set flags
   */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /**
   * Get expiration period (in milliseconds)
   */
  public int getExpirationPeriod() {
    return expirationPeriod;
  }

  /**
   * Check if route should not expire
   */
  public boolean hasInfiniteExpirationPeriod() {
    return expirationPeriod < 0;
  }

  /**
   * Set expiration period
   *
   * @param expirationPeriod
   */
  public void setExpirationPeriod(int expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
  }

  /**
   * Get human-readable representation of Route
   */
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("Route(");
    out.append("FaceId: "); out.append(getFaceId()); out.append(", ");
    out.append("Origin: "); out.append(getOrigin()); out.append(", ");
    out.append("Cost: "); out.append(getCost()); out.append(", ");
    out.append("Flags: "); out.append(getFlags()); out.append(", ");

    if (!hasInfiniteExpirationPeriod()) {
      out.append("ExpirationPeriod: "); out.append(getExpirationPeriod()); out.append(" milliseconds");
    }
    else {
      out.append("ExpirationPeriod: Infinity");
    }
    out.append(")");
    return out.toString();
  }
}
