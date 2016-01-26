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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.intel.jndn.management.enums.NfdTlv;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a FibEntry returned from /localhost/nfd/fib/list
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/FibMgmt#FIB-Dataset">FIB Dataset</a>
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FibEntry implements Decodable {
  private Name name = new Name();
  private List<NextHopRecord> records = new ArrayList<>();

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor
   */
  public FibEntry() {
    // nothing to do
  }

  /**
   * Constructor from wire format
   * @param input wire format
   * @throws EncodingException
   */
  public FibEntry(ByteBuffer input) throws EncodingException {
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
   * Encode as part of an existing encode context
   */
  public final void wireEncode(TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    ListIterator<NextHopRecord> nh = records.listIterator(records.size());
    while (nh.hasPrevious()) {
      nh.previous().wireEncode(encoder);
    }
    EncodingHelper.encodeName(name, encoder);
    encoder.writeTypeAndLength(NfdTlv.FibEntry, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
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
  public final void wireDecode(TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.FibEntry);
    name = EncodingHelper.decodeName(decoder);
    while (decoder.getOffset() < endOffset) {
      NextHopRecord record = new NextHopRecord();
      record.wireDecode(decoder);
      records.add(record);
    }
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * Get name
   */
  public Name getPrefix() {
    return name;
  }

  /**
   * Set name
   */
  public FibEntry setPrefix(Name name) {
    this.name = name;
    return this;
  }

  /**
   * Get records
   */
  public List<NextHopRecord> getNextHopRecords() {
    return records;
  }

  /**
   * Set records
   */
  public FibEntry setNextHopRecords(List<NextHopRecord> records) {
    this.records = records;
    return this;
  }

  public FibEntry addNextHopRecord(NextHopRecord record) {
    this.records.add(record);
    return this;
  }
}
