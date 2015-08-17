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

import com.intel.jndn.management.types.LocalControlHeader;
import com.intel.jndn.utils.client.impl.SimpleClient;
import java.util.logging.Logger;
import junit.framework.Assert;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.KeyChain;

/**
 * Test functionality for LocalControlHeader
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class LocalControlHeaderTest {

  private static final Logger logger = Logger.getLogger(IntegrationSuite.class.getName());

  /**
   * Integration test to run on actual system
   *
   * @param args
   * @throws EncodingException
   */
  public static void main(String[] args) throws Exception {
    // setup forwarder face
    Face forwarder = new Face("localhost");
    KeyChain keyChain = IntegrationSuite.buildTestKeyChain();
    forwarder.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());

    // enable incoming face ID header
    NFD.enableLocalControlHeader(forwarder, LocalControlHeader.INCOMING_FACE_ID);

    // use and verify
    Data data = SimpleClient.getDefault().getSync(forwarder, new Name("/localhost/nfd"));
    long faceId = data.getIncomingFaceId();
    Assert.assertTrue(faceId != -1); // this verifies that the headers are working correctly
    logger.info("Face ID for this client on the forwarder: " + faceId);
  }
}
