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
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.management.types.StrategyChoice;
import com.intel.jndn.mock.MockFace;
import com.intel.jndn.mock.MockKeyChain;
import net.named_data.jndn.ControlResponse;
import net.named_data.jndn.Data;
import net.named_data.jndn.DigestSha256Signature;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.KeyLocator;
import net.named_data.jndn.MetaInfo;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.Tlv0_3WireFormat;
import net.named_data.jndn.encoding.WireFormat;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Testing Nfdc with real NFD instance (NFD must be run locally while executing the test).
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NfdcIT {
  private static final Logger LOG = Logger.getLogger(NfdcIT.class.getName());
  private Face face;
  private MockFace mockFace;
  private Face noKeyChainFace;

  @Before
  public void setUp() throws SecurityException {
    WireFormat.setDefaultWireFormat(Tlv0_3WireFormat.get());

    face = new Face("localhost");
    mockFace = new MockFace(new MockFace.Options().setEnablePacketLogging(false).setEnableRegistrationReply(false));
    noKeyChainFace = new Face("localhost"); // don't set command signing info
    KeyChain keyChain = MockKeyChain.configure(new Name("/tmp/identity"));
    face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());
  }

  @Test
  public void testGetKeyLocator() throws Exception {
    KeyLocator keyLocator = Nfdc.getKeyLocator(face);
    assertNotNull(keyLocator);
    LOG.info("Connected to NFD with key locator: " + keyLocator.getKeyName().toUri());

    assertThrows(ManagementException.class, () -> Nfdc.getKeyLocator(mockFace));
  }

  @Test
  public void testFailOfGetKeyLocator() throws Exception {
    mockFace.onSendInterest.add(new MockFace.SignalOnSendInterest() {
      @Override
      public void emit(final Interest interest) {
        Data data = new Data();
        data.setName(new Name(interest.getName()).appendVersion(0).appendSegment(0));

        MetaInfo meta = new MetaInfo();
        meta.setFinalBlockId(data.getName().get(-1));
        data.setMetaInfo(meta);

        data.setSignature(new DigestSha256Signature());

        LOG.info(data.getSignature().toString());

        // don't set anything else
        try {
          mockFace.receive(data);
        } catch (EncodingException e) {
          LOG.severe("Failed to set receive data: " + e);
        }
      }
    });

    Exception exception = assertThrows(ManagementException.class, () -> Nfdc.getKeyLocator(mockFace));
    assertEquals("No key locator available.", exception.getMessage());
  }

  @Test
  public void testGetForwarderStatus() throws Exception {
    assertTrue(Nfdc.getForwarderStatus(face).getStartTimestamp() > 0);
    assertThrows(ManagementException.class, () -> Nfdc.getForwarderStatus(mockFace));
  }

  @Test
  public void testGetFaceList() throws Exception {
    assertFalse(Nfdc.getFaceList(face).isEmpty());
    assertThrows(ManagementException.class, () -> Nfdc.getFaceList(mockFace));
  }

  @Test
  public void testGetFibList() throws Exception {
    assertFalse(Nfdc.getFibList(face).isEmpty());
    assertThrows(ManagementException.class, () -> Nfdc.getFibList(mockFace));
  }

  @Test
  public void testGetRouteList() throws Exception {
    assertFalse(Nfdc.getRouteList(face).isEmpty());
    assertThrows(ManagementException.class, () -> Nfdc.getRouteList(mockFace));
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

    Thread.sleep(1000); // wait for face to be destroyed

    Exception exception = assertThrows(ManagementException.class, () -> {
                                         Nfdc.unregister(face, new Name("/my/test/route"), "udp4://127.0.0.1:56363");
                                       });
    assertEquals("Face not found: udp4://127.0.0.1:56363", exception.getMessage());
  }

  @Test
  public void testFailOfRegister() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.register(mockFace, new Name("/my/route/to/app/face"), 333));
  }

  @Test
  public void testFailOfUnregister() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.unregister(mockFace, new Name("/my/route/to/app/face")));
  }

  @Test
  public void testFailOfCreateFace() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.createFace(mockFace, "udp4://127.0.0.1:56363"));
  }

  @Test
  public void testFailOfDestroyFace() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.destroyFace(mockFace, 1));
  }

  @Test
  public void testStrategies() throws Exception {
    Name prefix = new Name("/test/strategy").append("random" + new Random().nextInt());
    List<StrategyChoice> choices = Nfdc.getStrategyList(face);
    int oldSize = choices.size();

    Nfdc.setStrategy(face, prefix, Strategies.RANDOM);
    Thread.sleep(1000); // strategy takes a while to register

    choices = Nfdc.getStrategyList(face);
    assertEquals(oldSize + 1, choices.size());

    Nfdc.unsetStrategy(face, prefix);
    Thread.sleep(1000);

    choices = Nfdc.getStrategyList(face);
    assertEquals(oldSize, choices.size());

    assertThrows(ManagementException.class, () -> Nfdc.getStrategyList(mockFace));
  }

  @Test
  public void testFailOfUnsetStrategy() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.unsetStrategy(mockFace, new Name("/")));
  }

  @Test
  public void testFailOfSetStrategyWithoutKeychain() throws Exception {
    assertThrows(NullPointerException.class, () -> {
                   Nfdc.setStrategy(noKeyChainFace, new Name("/test"), Strategies.BEST_ROUTE);
                 });
  }

  @Test
  public void testFailOfSetStrategyWithNon200Code() throws Exception {
    mockFace.onSendInterest.add(new MockFace.SignalOnSendInterest() {
      @Override
      public void emit(final Interest interest) {
        ControlResponse response = new ControlResponse();
        response.setStatusCode(300);
        response.setStatusText("Test FAIL");

        Data data = new Data();
        data.setName(interest.getName());
        data.setContent(response.wireEncode());

        try {
          mockFace.receive(data);
        } catch (EncodingException e) {
          LOG.severe("Failed to set receive data: " + e);
        }
      }
    });

    Exception exception = assertThrows(ManagementException.class, () -> {
                                         Nfdc.setStrategy(mockFace, new Name("/"), Strategies.MULTICAST);
                                       });
    assertEquals("Action failed, forwarder returned: 300 Test FAIL", exception.getMessage());
  }

  @Test
  public void testGetChannelStatus() throws Exception {
    assertFalse(Nfdc.getChannelStatusList(face).isEmpty());
    assertThrows(ManagementException.class, () -> Nfdc.getChannelStatusList(mockFace));
  }
}
