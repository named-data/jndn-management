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

import com.intel.jndn.management.TestHelper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test encode/decode of FibEntry and NextHopRecord
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FibEntryTest {
  ByteBuffer TestNextHopRecord;
  ByteBuffer TestFibEntryNoNextHops;
  ByteBuffer TestFibEntry;

  @Before
  public void setUp() {
    TestNextHopRecord = TestHelper.bufferFromIntArray(new int[] {
      0x81, 0x06, 0x69, 0x01, 0x0a, 0x6a, 0x01, 0xc8
    });

    TestFibEntryNoNextHops = TestHelper.bufferFromIntArray(new int[] {
      0x80, 0x15, 0x07, 0x13, 0x08, 0x04, 0x74, 0x68, 0x69, 0x73,
      0x08, 0x02, 0x69, 0x73, 0x08, 0x01, 0x61, 0x08, 0x04, 0x74,
      0x65, 0x73, 0x74
    });
    TestFibEntry = TestHelper.bufferFromIntArray(new int[] {
      0x80, 0x38, 0x07, 0x13, 0x08, 0x04, 0x74, 0x68, 0x69, 0x73, 0x08, 0x02, 0x69, 0x73, 0x08, 0x01,
      0x61, 0x08, 0x04, 0x74, 0x65, 0x73, 0x74, 0x81, 0x06, 0x69, 0x01, 0x0a, 0x6a, 0x01, 0xc8, 0x81,
      0x07, 0x69, 0x01, 0x14, 0x6a, 0x02, 0x01, 0x2c, 0x81, 0x07, 0x69, 0x01, 0x1e, 0x6a, 0x02, 0x01,
      0x90, 0x81, 0x07, 0x69, 0x01, 0x28, 0x6a, 0x02, 0x01, 0xf4
    });
  }

  @Test
  public void testNextHopRecordEncode()
  {
    NextHopRecord record = new NextHopRecord();
    record.setFaceId(10);
    record.setCost(200);

    ByteBuffer wire = record.wireEncode().buf();
    assertEquals(TestNextHopRecord, wire);
  }

  @Test
  public void testNextHopRecordDecode() throws EncodingException {
    NextHopRecord record = new NextHopRecord(TestNextHopRecord);

    assertEquals(10, record.getFaceId());
    assertEquals(200, record.getCost());
  }

  @Test
  public void testFibEntryNoNextHopEncode()
  {
    FibEntry entry = new FibEntry();
    entry.setPrefix(new Name("/this/is/a/test"));

    ByteBuffer wire = entry.wireEncode().buf();
    assertEquals(TestFibEntryNoNextHops, wire);
  }

  @Test
  public void testFibEntryNoNextHopsDecode() throws EncodingException {
    FibEntry entry = new FibEntry(TestFibEntryNoNextHops);

    assertEquals("/this/is/a/test", entry.getPrefix().toString());
    assertEquals(0, entry.getNextHopRecords().size());
  }

  @Test
  public void testFibEntryEncode()
  {
    FibEntry entry = new FibEntry();
    entry.setPrefix(new Name("/this/is/a/test"));

    List<NextHopRecord> records = new ArrayList<>();
    for (int i = 1; i < 4; i++)
    {
      NextHopRecord record = new NextHopRecord();
      record.setFaceId(i * 10);
      record.setCost((i * 100) + 100);
      records.add(record);
    }

    entry.setNextHopRecords(records);

    NextHopRecord oneMore = new NextHopRecord();
    oneMore.setFaceId(40);
    oneMore.setCost(500);

    entry.addNextHopRecord(oneMore);

    ByteBuffer wire = entry.wireEncode().buf();
    assertEquals(TestFibEntry, wire);
  }

  @Test
  public void testFibEntryDecode() throws EncodingException {
    FibEntry entry = new FibEntry(TestFibEntry);

    List<NextHopRecord> records = entry.getNextHopRecords();

    assertEquals("/this/is/a/test", entry.getPrefix().toUri());
    assertEquals(4, entry.getNextHopRecords().size());

    int value = 1;

    for (NextHopRecord record : records) {
      assertEquals(value * 10, record.getFaceId());
      assertEquals((value * 100) + 100, record.getCost());
      ++value;
    }
  }
}
