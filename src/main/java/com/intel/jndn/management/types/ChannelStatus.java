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
import com.intel.jndn.management.helpers.EncodingHelper;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;

/**
 * Represent a ChannelStatus object.
 *
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt#Channel-Dataset">Face Management</a>
 */
public class ChannelStatus implements Decodable {
  private String localUri = "";
  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor.
   */
  public ChannelStatus() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public ChannelStatus(final ByteBuffer input) throws EncodingException {
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
    encoder.writeBlobTlv(NfdTlv.LocalUri, new Blob(localUri).buf());
    encoder.writeTypeAndLength(NfdTlv.ChannelStatus, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
   * @throws EncodingException when decoding fails
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
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.ChannelStatus);
    this.localUri = EncodingHelper.toString(decoder.readBlobTlv(NfdTlv.LocalUri));
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * @return channel URI
   */
  public String getLocalUri() {
    return localUri;
  }

  /**
   * Set channel URI.
   *
   * @param localUri channel URI
   * @return this
   */
  public ChannelStatus setLocalUri(final String localUri) {
    this.localUri = localUri;
    return this;
  }

  @Override
  public String toString() {
    return "ChannelStatus(" + getLocalUri() + ")";
  }
}
