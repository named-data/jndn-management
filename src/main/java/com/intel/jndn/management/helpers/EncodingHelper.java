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

import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Provide helper methods to cover areas too protected in Tlv0_1_1WireFormat;
 * this class can be deprecated if WireFormats allow passing in an existing
 * TlvEncoder/TlvDecoder (currently these methods are protected).
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public final class EncodingHelper {

  /**
   * Prevent instances of EncodingHelper.
   */
  private EncodingHelper() {
  }

  /**
   * Helper to decode names using an existing decoding context; could be merged
   * to Tlv0_1_1WireFormat.java.
   *
   * @param decoder a current decoder context to use for decoding
   * @return a decoded {@link Name}
   * @throws EncodingException when decoding fails
   */
  public static Name decodeName(final TlvDecoder decoder) throws EncodingException {
    Name name = new Name();
    int endOffset = decoder.readNestedTlvsStart(Tlv.Name);
    while (decoder.getOffset() < endOffset) {
      name.append(new Blob(decoder.readBlobTlv(Tlv.NameComponent), true));
    }

    decoder.finishNestedTlvs(endOffset);
    return name;
  }

  /**
   * Helper to encode names using an existing encoding context; could be merged
   * to Tlv0_1_1WireFormat.java.
   *
   * @param name    the {@link Name} to encode
   * @param encoder the current {@link TlvEncoder} context to encode with
   */
  public static void encodeName(final Name name, final TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    for (int i = name.size() - 1; i >= 0; --i) {
      encoder.writeBlobTlv(Tlv.NameComponent, name.get(i).getValue().buf());
    }
    encoder.writeTypeAndLength(Tlv.Name, encoder.getLength() - saveLength);
  }

  /**
   * Helper to decode strategies using an existing decoding context; could be
   * merged to Tlv0_1_1WireFormat.java.
   *
   * @param decoder the current {@link TlvDecoder} context to decode with
   * @return a decoded strategy (e.g. {@link Name})
   * @throws EncodingException when decoding fails
   */
  public static Name decodeStrategy(final TlvDecoder decoder) throws EncodingException {
    int strategyEndOffset = decoder.readNestedTlvsStart(Tlv.ControlParameters_Strategy);
    Name strategy = decodeName(decoder);
    decoder.finishNestedTlvs(strategyEndOffset);
    return strategy;
  }

  /**
   * Helper to encode strategies using an existing decoding context; could be
   * merged to Tlv0_1_1WireFormat.java.
   *
   * @param strategy the {@link Name} to encode
   * @param encoder  the current {@link TlvEncoder} context to use
   */
  public static void encodeStrategy(final Name strategy, final TlvEncoder encoder) {
    int strategySaveLength = encoder.getLength();
    encodeName(strategy, encoder);
    encoder.writeTypeAndLength(Tlv.ControlParameters_Strategy,
      encoder.getLength() - strategySaveLength);
  }

  /**
   * Convert ByteBuffer to string, assuming UTF-8 encoding in the buffer.
   *
   * @param buffer buffer to convert
   * @return String representation of ByteBuffer (UTF-8 encoding)
   */
  public static String
  toString(final ByteBuffer buffer) {
    byte[] array = new byte[buffer.remaining()];
    buffer.get(array);
    return new String(array, Charset.forName("UTF-8"));
  }
}
