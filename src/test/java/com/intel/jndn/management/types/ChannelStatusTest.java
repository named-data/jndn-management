/*
 * jndn-management
 * Copyright (c) 2016, Regents of the University of California.
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
import net.named_data.jndn.util.Blob;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Test encode/decode of FaceStatus.
 */
public class ChannelStatusTest {
  private ByteBuffer testChannelStatusWire;

  @Before
  public void setUp() throws Exception {
    testChannelStatusWire = TestHelper.bufferFromIntArray(new int[]{
      0x82, 0x14, 0x81, 0x12, 0x75, 0x64, 0x70, 0x34, 0x3a, 0x2f, 0x2f, 0x31, 0x39, 0x32,
      0x2e, 0x31, 0x36, 0x38, 0x2e, 0x32, 0x2e, 0x31
    });
  }

  @Test
  public void testEncode() throws Exception {
    ChannelStatus status = new ChannelStatus();
    status.setLocalUri("udp4://192.168.2.1");

    // encode
    Blob encoded = status.wireEncode();
    assertEquals(testChannelStatusWire, encoded.buf());
  }

  @Test
  public void testDecode() throws Exception {
    ChannelStatus status = new ChannelStatus(testChannelStatusWire);

    assertEquals("udp4://192.168.2.1", status.getLocalUri());
  }

  @Test
  public void testToString() throws Exception {
    ChannelStatus status = new ChannelStatus(testChannelStatusWire);

    assertEquals("ChannelStatus(udp4://192.168.2.1)", status.toString());
  }
}
