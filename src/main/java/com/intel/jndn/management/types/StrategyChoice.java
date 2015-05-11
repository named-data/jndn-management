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

import com.intel.jndn.management.EncodingHelper;
import static com.intel.jndn.management.types.RibEntry.TLV_RIB_ENTRY;
import java.nio.ByteBuffer;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a strategy choice entry.
 *
 * @see http://redmine.named-data.net/projects/nfd/wiki/StrategyChoice
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StrategyChoice implements Decodable {

  /**
   * TLV type, see
   * <a href="http://redmine.named-data.net/projects/nfd/wiki/StrategyChoice#TLV-TYPE-assignments">http://redmine.named-data.net/projects/nfd/wiki/StrategyChoice#TLV-TYPE-assignments</a>
   */
  public final static int TLV_STRATEGY_CHOICE = 128;

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
    EncodingHelper.encodeName(name, encoder);
    EncodingHelper.encodeName(strategy, encoder);
    encoder.writeTypeAndLength(TLV_STRATEGY_CHOICE, encoder.getLength() - saveLength);
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
    int endOffset = decoder.readNestedTlvsStart(TLV_RIB_ENTRY);
    strategy = EncodingHelper.decodeName(decoder);
    name = EncodingHelper.decodeName(decoder);
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
  public void setName(Name name) {
    this.name = name;
  }

  /**
   * @param strategy the {@link Name} to set
   */
  public void setStrategy(Name strategy) {
    this.strategy = strategy;
  }

  private Name name;
  private Name strategy;
}
