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
package com.intel.jndn.management.helpers;

import com.intel.jndn.management.ManagementException;
import com.intel.jndn.management.types.Decodable;
import net.named_data.jndn.Data;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to handle StatusDatasets.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/StatusDataset">StatusDataset</a>
 */
public final class StatusDatasetHelper {
  /**
   * Prevent instances of StatusDatasetHelper.
   */
  private StatusDatasetHelper() {
  }

  /**
   * Combine payload of Data packet segments into a single buffer.
   *
   * @param segments list of Data packets
   * @return single buffer containing combined payload
   */
  public static ByteBuffer
  combine(final List<Data> segments) {
    int size = 0;
    for (Data segment : segments) {
      size += segment.getContent().size();
    }
    ByteBuffer payloadBuffer = ByteBuffer.allocate(size);
    for (Data segment : segments) {
      payloadBuffer.put(segment.getContent().getImmutableArray());
    }
    payloadBuffer.flip();

    return payloadBuffer;
  }

  /**
   * Decode multiple status entries as part of a StatusDatasetHelper.
   *
   * @param <T>      Class implementing Decodable interface
   * @param segments list of Data packets
   * @param type     class implementing Decodable interface
   * @return List decoded status entries
   * @throws ManagementException when decoding fails
   * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/StatusDataset">StatusDataset</a>
   */
  public static <T extends Decodable> List<T>
  wireDecode(final List<Data> segments, final Class<T> type) throws ManagementException {
    Blob payload = new Blob(combine(segments), false);

    List<T> entries = new ArrayList<>();
    int endOffset = payload.size();
    TlvDecoder decoder = new TlvDecoder(payload.buf());
    while (decoder.getOffset() < endOffset) {
      try {
        T entry = type.newInstance();
        entry.wireDecode(decoder);
        entries.add(entry);
      } catch (IllegalAccessException | InstantiationException | EncodingException e) {
        throw new ManagementException("Failed to read status dataset.", e);
      }
    }
    return entries;
  }
}
