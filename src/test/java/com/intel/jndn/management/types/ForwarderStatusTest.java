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

import com.intel.jndn.management.TestHelper;
import net.named_data.jndn.encoding.EncodingException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * Test encoding/decoding of ForwarderStatus.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ForwarderStatusTest {
  private ByteBuffer testForwarderStatusWire;

  @Before
  public void setUp() throws Exception {
    testForwarderStatusWire = TestHelper.bufferFromIntArray(new int[] {
      0x80, 0x11, 0x30, 0x2e, 0x32, 0x2e, 0x30, 0x2d, 0x36, 0x35,
      0x2d, 0x67, 0x37, 0x35, 0x61, 0x62, 0x36, 0x62, 0x37, 0x81, 0x08, 0x00,
      0x00, 0x00, 0x57, 0x5b, 0x42, 0xa6, 0x2d, 0x82, 0x08, 0x00, 0x00, 0x00,
      0xce, 0x50, 0x36, 0xd7, 0x20, 0x83, 0x04, 0x6e, 0x43, 0xe4, 0x78, 0x84,
      0x04, 0x25, 0x0e, 0xfe, 0xe4, 0x85, 0x04, 0x1c, 0xbc, 0xb7, 0x4d, 0x86,
      0x04, 0x69, 0x9a, 0x61, 0xf2, 0x87, 0x04, 0x4b, 0x65, 0xe3, 0xf0, 0x90,
      0x04, 0x24, 0x86, 0xc3, 0x5f, 0x91, 0x04, 0x6d, 0xe2, 0xbc, 0xf2, 0x97,
      0x02, 0x04, 0xd2, 0x92, 0x04, 0x38, 0xc0, 0x92, 0x3d, 0x93, 0x04, 0x08,
      0x3c, 0xbf, 0x2a, 0x98, 0x02, 0x10, 0xe1,
    });
  }

  @Test
  public void testEncode() {
    ForwarderStatus status = new ForwarderStatus();
    status.setNfdVersion("0.2.0-65-g75ab6b7");
    status.setStartTimestamp(375193249325L);
    status.setCurrentTimestamp(886109034272L);
    status.setNNameTreeEntries(1849943160);
    status.setNFibEntries(621739748);
    status.setNPitEntries(482129741);
    status.setNMeasurementsEntries(1771725298);
    status.setNCsEntries(1264968688);
    status.setNInInterests(612811615);
    status.setNInDatas(1843576050);
    status.setNInNacks(1234);
    status.setNOutInterests(952144445);
    status.setNOutDatas(138198826);
    status.setNOutNacks(4321);

    ByteBuffer wire = status.wireEncode().buf();
    assertEquals(testForwarderStatusWire, wire);
  }

  @Test
  public void testDecode() throws EncodingException {
    ForwarderStatus status = new ForwarderStatus(testForwarderStatusWire);

    assertEquals("0.2.0-65-g75ab6b7", status.getNfdVersion());
    assertEquals(375193249325L,       status.getStartTimestamp());
    assertEquals(886109034272L,       status.getCurrentTimestamp());
    assertEquals(1849943160,          status.getNNameTreeEntries());
    assertEquals(621739748,           status.getNFibEntries());
    assertEquals(482129741,           status.getNPitEntries());
    assertEquals(1771725298,          status.getNMeasurementsEntries());
    assertEquals(1264968688,          status.getNCsEntries());
    assertEquals(612811615,           status.getNInInterests());
    assertEquals(1843576050,          status.getNInDatas());
    assertEquals(1234,                status.getNInNacks());
    assertEquals(952144445,           status.getNOutInterests());
    assertEquals(138198826,           status.getNOutDatas());
    assertEquals(4321,                status.getNOutNacks());
  }
}
