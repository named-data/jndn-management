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
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;

/**
 * Represent a strategy choice entry.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/StrategyChoice">StrategyChoice</a>
 */
public class StrategyChoice implements Decodable {
  private Name name;
  private Name strategy;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor.
   */
  public StrategyChoice() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public StrategyChoice(final ByteBuffer input) throws EncodingException {
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
    int saveLength = encoder.getLength();
    EncodingHelper.encodeStrategy(strategy, encoder);
    EncodingHelper.encodeName(name, encoder);
    encoder.writeTypeAndLength(NfdTlv.StrategyChoice, encoder.getLength() - saveLength);
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
   * @return this
   */
  public StrategyChoice setName(final Name name) {
    this.name = name;
    return this;
  }

  /**
   * @param strategy the {@link Name} to set
   * @return this
   */
  public StrategyChoice setStrategy(final Name strategy) {
    this.strategy = strategy;
    return this;
  }
}
