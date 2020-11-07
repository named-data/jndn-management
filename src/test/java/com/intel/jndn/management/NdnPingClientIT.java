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
package com.intel.jndn.management;

import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.Tlv0_3WireFormat;
import net.named_data.jndn.encoding.WireFormat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Testing basic pinging using real NFD instance (NFD must be run locally while executing the test).
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NdnPingClientIT {
  private Face face;

  @Before
  public void setUp() {
    WireFormat.setDefaultWireFormat(Tlv0_3WireFormat.get());

    face = new Face("localhost");
  }

  @Test
  public void testPingLocal() {
    boolean hasSucceeded = NdnPingClient.pingLocal(face);
    assertTrue(hasSucceeded);
  }

  @Test
  public void testFailedPing() {
    boolean hasSucceeded = NdnPingClient.ping(face, new Name("/non/existent/name/of/data"));
    assertFalse(hasSucceeded);
  }
}
