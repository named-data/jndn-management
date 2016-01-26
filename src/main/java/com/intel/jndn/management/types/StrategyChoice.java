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

import com.intel.jndn.management.helpers.EncodingHelper;
import java.nio.ByteBuffer;

import com.intel.jndn.management.enums.NfdTlv;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a strategy choice entry.
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/StrategyChoice">StrategyChoice</a>

 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StrategyChoice implements Decodable {
  private Name name;
  private Name strategy;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor
   */
  public StrategyChoice() {
    // nothing to do
  }

  /**
   * Constructor from wire format
   * @param input wire format
   * @throws EncodingException
   */
  public StrategyChoice(ByteBuffer input) throws EncodingException {
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
    EncodingHelper.encodeStrategy(strategy, encoder);
    EncodingHelper.encodeName(name, encoder);
    encoder.writeTypeAndLength(NfdTlv.StrategyChoice, encoder.getLength() - saveLength);
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
   * Decode as part of an existing decode context.
   *
   * @param decoder
   * @throws EncodingException
   */
  @Override
  public final void wireDecode(TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.StrategyChoice);
    name = EncodingHelper.decodeName(decoder);
    strategy = EncodingHelper.decodeStrategy(decoder);
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * @return the {@link Name} of the prefix the strategy is applied to
   */
  public Name getName() {
    return name;
  }

  /**
   * @return the {@link Name} of the strategy
   */
  public Name getStrategy() {
    return strategy;
  }

  /**
   * @param name the {@link Name} to set
   */
  public StrategyChoice setName(Name name) {
    this.name = name;
    return this;
  }

  /**
   * @param strategy the {@link Name} to set
   */
  public StrategyChoice setStrategy(Name strategy) {
    this.strategy = strategy;
    return this;
  }
}
