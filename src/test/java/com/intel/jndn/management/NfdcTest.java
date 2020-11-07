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
import com.intel.jndn.mock.MockFace;
import com.intel.jndn.mock.MockKeyChain;
import net.named_data.jndn.ControlResponse;
import net.named_data.jndn.Data;
import net.named_data.jndn.Interest;
import net.named_data.jndn.KeyLocator;
import net.named_data.jndn.KeyLocatorType;
import net.named_data.jndn.MetaInfo;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.Tlv0_3WireFormat;
import net.named_data.jndn.encoding.WireFormat;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;
import net.named_data.jndn.security.SigningInfo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Nfdc unit tests.
 */
public class NfdcTest {
  private MockFace mockFace;
  private KeyChain keyChain;
  private MockFace.SignalOnSendInterest replyWithEmptyData;

  @Before
  public void setUp() throws SecurityException {
    WireFormat.setDefaultWireFormat(Tlv0_3WireFormat.get());

    mockFace = new MockFace(new MockFace.Options());
    keyChain = MockKeyChain.configure(new Name("/tmp/identity"));

    replyWithEmptyData = new MockFace.SignalOnSendInterest() {
      @Override
      public void emit(final Interest interest) {
        Data data = new Data();
        data.setName(new Name(interest.getName()).appendVersion(0).appendSegment(0));
        MetaInfo meta = new MetaInfo();
        meta.setFinalBlockId(data.getName().get(-1));
        data.setMetaInfo(meta);

        try {
          keyChain.sign(data);
        } catch (Exception e) {
          fail("Failed to sign data: " + e);
        }

        try {
          mockFace.receive(data);
        } catch (Exception e) {
          fail("Failed to receive data on mock face: " + e);
        }
      }
    };
  }

  @Test
  public void testGetKeyLocator() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);

    KeyLocator keyLocator = Nfdc.getKeyLocator(mockFace);
    assertEquals(KeyLocatorType.KEYNAME, keyLocator.getType());
    assertNotEquals(0, keyLocator.getKeyName().size());
  }

  @Test
  public void testGetKeyLocatorWithDigestSha256() throws Exception {
    mockFace.onSendInterest.add(new MockFace.SignalOnSendInterest() {
      @Override
      public void emit(final Interest interest) {
        Data data = new Data();
        data.setName(new Name(interest.getName()).appendVersion(0).appendSegment(0));
        MetaInfo meta = new MetaInfo();
        meta.setFinalBlockId(data.getName().get(-1));
        data.setMetaInfo(meta);

        try {
          keyChain.sign(data, new SigningInfo(SigningInfo.SignerType.SHA256));
        } catch (Exception e) {
          fail("Failed to sign data: " + e);
        }

        try {
          mockFace.receive(data);
        } catch (Exception e) {
          fail("Failed to receive data on mock face: " + e);
        }
      }
    });

    Exception exception = assertThrows(ManagementException.class, () -> Nfdc.getKeyLocator(mockFace));
    assertEquals("No key locator available.", exception.getMessage());
  }

  @Test
  public void testGetChannelStatusList() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.getChannelStatusList(mockFace));

    mockFace.onSendInterest.add(replyWithEmptyData);
    assertTrue(Nfdc.getChannelStatusList(mockFace).isEmpty());
  }

  @Test
  public void testGetFaceList() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.getFaceList(mockFace));

    mockFace.onSendInterest.add(replyWithEmptyData);
    assertTrue(Nfdc.getFaceList(mockFace).isEmpty());
  }

  @Test
  public void testFailOfCreateFace() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);
    assertThrows(ManagementException.class, () -> Nfdc.createFace(mockFace, "udp4://127.0.0.1:56363"));
  }

  @Test
  public void testFailOfDestroyFace() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);
    assertThrows(ManagementException.class, () -> Nfdc.destroyFace(mockFace, 1));
  }

  @Test
  public void testGetFibList() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.getFibList(mockFace));

    mockFace.onSendInterest.add(replyWithEmptyData);
    assertTrue(Nfdc.getFibList(mockFace).isEmpty());
  }

  @Test
  public void testGetRouteList() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.getRouteList(mockFace));

    mockFace.onSendInterest.add(replyWithEmptyData);
    assertTrue(Nfdc.getRouteList(mockFace).isEmpty());
  }

  @Test
  public void testFailOfRegister() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);
    assertThrows(ManagementException.class, () -> Nfdc.register(mockFace, new Name("/my/route/to/app/face"), 333));
  }

  @Test
  public void testFailOfUnregister() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);
    assertThrows(ManagementException.class, () -> Nfdc.unregister(mockFace, new Name("/my/route/to/app/face")));
  }

  @Test
  public void testGetStrategyList() throws Exception {
    assertThrows(ManagementException.class, () -> Nfdc.getStrategyList(mockFace));

    mockFace.onSendInterest.add(replyWithEmptyData);
    assertTrue(Nfdc.getStrategyList(mockFace).isEmpty());
  }

  @Test
  public void testFailOfSetStrategyWithNon200Code() throws Exception {
    mockFace.onSendInterest.add(new MockFace.SignalOnSendInterest() {
      @Override
      public void emit(final Interest interest) {
        ControlResponse response = new ControlResponse();
        response.setStatusCode(400);
        response.setStatusText("test error");

        Data data = new Data();
        data.setName(interest.getName());
        data.setContent(response.wireEncode());

        try {
          mockFace.receive(data);
        } catch (Exception e) {
          fail("Failed to receive data on mock face: " + e);
        }
      }
    });

    Exception exception = assertThrows(ManagementException.class, () -> {
                                         Nfdc.setStrategy(mockFace, new Name("/"), Strategies.MULTICAST);
                                       });
    assertEquals("Action failed, forwarder returned: 400 test error", exception.getMessage());
  }

  @Test
  public void testFailOfUnsetStrategy() throws Exception {
    mockFace.onSendInterest.add(replyWithEmptyData);
    assertThrows(ManagementException.class, () -> Nfdc.unsetStrategy(mockFace, new Name("/")));
  }
}
