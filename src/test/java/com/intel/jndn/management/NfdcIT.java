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

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.intel.jndn.management.enums.LocalControlHeader;
import com.intel.jndn.management.enums.Strategies;
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.management.types.StrategyChoice;
import com.intel.jndn.mock.MockKeyChain;
import net.named_data.jndn.Face;
import net.named_data.jndn.KeyLocator;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Testing Nfdc with real NFD instance (NFD must be run locally while executing the test)
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NfdcIT {
  private static final Logger LOG = Logger.getLogger(NfdcIT.class.getName());
  private Face face;

  @Before
  public void setUp() throws SecurityException {
    face = new Face("localhost");
    KeyChain keyChain = MockKeyChain.configure(new Name("/tmp/identity"));
    face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());
  }

  @Test
  public void testConnectivity() throws IOException, ManagementException {
    KeyLocator keyLocator = Nfdc.getKeyLocator(face);
    assertNotNull(keyLocator);
    LOG.info("Connected to NFD with key locator: " + keyLocator.getKeyName().toUri());
  }

  @Test
  public void testStatusDatasets() throws Exception {
    assertTrue(Nfdc.getForwarderStatus(face).getStartTimestamp() > 0);
    assertFalse(Nfdc.getFaceList(face).isEmpty());
    assertFalse(Nfdc.getFibList(face).isEmpty());
    assertFalse(Nfdc.getRouteList(face).isEmpty());
  }

  @Test
  public void testRoutes() throws EncodingException, IOException, ManagementException, InterruptedException {
    Nfdc.register(face, new Name("/my/route/to/app/face"), 333);
    int faceId = Nfdc.createFace(face, "udp4://127.0.0.1:56363");
    Nfdc.register(face, "udp4://127.0.0.1:56363", new Name("/my/test/route"), 999);
    Nfdc.register(face, faceId, new Name("/"), 555);

    // check that route is created
    Thread.sleep(1000); // NFD registers the route asynchronously

    boolean found = false;
    for (RibEntry route : Nfdc.getRouteList(face)) {
      LOG.info("Found route: " + route.getName().toUri());
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
  }

  @Test
  public void testStrategies() throws Exception {
    Name prefix = new Name("/test/strategy").append("random:" + new Random().nextInt());

    List<StrategyChoice> choices = Nfdc.getStrategyList(face);
    int oldSize = choices.size();

    Nfdc.setStrategy(face, prefix, Strategies.CLIENT_CONTROL);
    Thread.sleep(1000); // strategy takes a while to register

    choices = Nfdc.getStrategyList(face);
    assertEquals(oldSize + 1, choices.size());

    Nfdc.unsetStrategy(face, prefix);
  }

  /**
   * LocalControlHeader would work only with NFD < 0.3.4, broken otherwise
   */
  @Test(expected = ManagementException.class)
  public void testLocalControlHeader() throws Exception {
    Nfdc.enableLocalControlHeader(face, LocalControlHeader.INCOMING_FACE_ID);
    Thread.sleep(1000); // strategy takes a while to register

    // TODO: add asserts

    Nfdc.disableLocalControlHeader(face, LocalControlHeader.INCOMING_FACE_ID);
  }
}
