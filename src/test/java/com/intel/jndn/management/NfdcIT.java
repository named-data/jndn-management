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

import com.intel.jndn.management.enums.Strategies;
import com.intel.jndn.management.types.ForwarderStatus;
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.management.types.StrategyChoice;
import com.intel.jndn.mock.MockKeyChain;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.Tlv0_3WireFormat;
import net.named_data.jndn.encoding.WireFormat;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Testing Nfdc with real NFD instance (NFD must be run locally while executing the test).
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NfdcIT {
  private Face face;
  private Face noKeyChainFace;

  @Before
  public void setUp() throws SecurityException {
    WireFormat.setDefaultWireFormat(Tlv0_3WireFormat.get());

    face = new Face("localhost");
    KeyChain keyChain = MockKeyChain.configure(new Name("/tmp/identity"));
    face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());
    noKeyChainFace = new Face("localhost"); // don't set command signing info
  }

  @Test
  public void testGetForwarderStatus() throws Exception {
    ForwarderStatus status = Nfdc.getForwarderStatus(face);
    assertTrue(status.getStartTimestamp() > 0);
    assertTrue(status.getCurrentTimestamp() > 0);
    assertTrue(status.getNFibEntries() > 0);
    assertTrue(status.getNInInterests() > 0);
    assertTrue(status.getNOutData() > 0);
  }

  @Test
  public void testGetChannelStatusList() throws Exception {
    assertFalse(Nfdc.getChannelStatusList(face).isEmpty());
  }

  @Test
  public void testGetFaceList() throws Exception {
    assertFalse(Nfdc.getFaceList(face).isEmpty());
  }

  @Test
  public void testGetFibList() throws Exception {
    assertFalse(Nfdc.getFibList(face).isEmpty());
  }

  @Test
  public void testGetRouteList() throws Exception {
    assertFalse(Nfdc.getRouteList(face).isEmpty());
  }

  @Test
  public void testRoutes() throws Exception {
    Nfdc.register(face, new Name("/my/route/to/app/face"), 333);
    int faceId = Nfdc.createFace(face, "udp4://127.0.0.1:56363");
    Nfdc.register(face, "udp4://127.0.0.1:56363", new Name("/my/test/route"), 999);
    Nfdc.register(face, faceId, new Name("/"), 555);

    Thread.sleep(1000); // NFD registers the route asynchronously

    // check that route is created
    boolean found = false;
    for (RibEntry route : Nfdc.getRouteList(face)) {
      if (route.getName().equals(new Name("/my/test/route"))) {
        found = true;
      }
    }
    assertTrue(found);

    Nfdc.unregister(face, new Name("/my/route/to/app/face"));

    // remove the route
    Nfdc.unregister(face, new Name("/my/test/route"), "udp4://127.0.0.1:56363");

    // remove face
    Nfdc.destroyFace(face, faceId);
    Thread.sleep(1000); // wait for face to be destroyed

    Exception exception = assertThrows(ManagementException.class, () -> {
                                         Nfdc.unregister(face, new Name("/my/test/route"), "udp4://127.0.0.1:56363");
                                       });
    assertEquals("Face not found: udp4://127.0.0.1:56363", exception.getMessage());
  }

  @Test
  public void testStrategies() throws Exception {
    Name prefix = new Name("/test/strategy").append("random" + new Random().nextInt());
    List<StrategyChoice> choices = Nfdc.getStrategyList(face);
    assertFalse(choices.isEmpty());
    int oldSize = choices.size();

    Nfdc.setStrategy(face, prefix, Strategies.RANDOM);
    Thread.sleep(1000); // strategy takes a while to register

    choices = Nfdc.getStrategyList(face);
    assertEquals(oldSize + 1, choices.size());

    Nfdc.unsetStrategy(face, prefix);
    Thread.sleep(1000);

    choices = Nfdc.getStrategyList(face);
    assertEquals(oldSize, choices.size());
  }

  @Test
  public void testFailOfSetStrategyWithoutKeychain() throws Exception {
    assertThrows(NullPointerException.class, () -> {
                   Nfdc.setStrategy(noKeyChainFace, new Name("/test"), Strategies.BEST_ROUTE);
                 });
  }
}
