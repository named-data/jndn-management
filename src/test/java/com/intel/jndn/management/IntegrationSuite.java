/*
 * File name: IntegrationSuite.java
 * 
 * Purpose: Tests to run with a local NFD as part of integration testing; will
 * not run in the maven test phase, must be run manually.
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import java.util.logging.Logger;
import junit.framework.Assert;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.security.KeyChain;

/**
 * Tests to run with a local NFD as part of integration testing; will not run in
 * the maven test phase, must be run manually.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class IntegrationSuite {

  private static final Logger logger = Logger.getLogger(IntegrationSuite.class.getName());

  /**
   * Test NFD methods
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Face face = new Face("localhost");
    KeyChain keyChain = buildTestKeyChain();
    face.setCommandSigningInfo(keyChain, keyChain.getDefaultCertificateName());

    Assert.assertTrue(NFD.pingLocal(face));

    // grab datasets
    Assert.assertFalse(NFD.getFaceList(face).isEmpty());
    Assert.assertFalse(NFD.getFibList(face).isEmpty());
    Assert.assertFalse(NFD.getRouteList(face).isEmpty());

    // create a new route
    Assert.assertTrue(NFD.register(face, "udp4://127.0.0.1:56363", new Name("/my/test/route"), 999));

    // remove the route
    Assert.assertTrue(NFD.unregister(face, new Name("/my/test/route"), "udp4://127.0.0.1:56363"));
  }

  /**
   * Setup the KeyChain with a default identity; TODO move this to
   * MemoryIdentityStorage once it can handle getting/setting defaults
   *
   * @return
   * @throws net.named_data.jndn.security.SecurityException
   */
  public static KeyChain buildTestKeyChain() throws net.named_data.jndn.security.SecurityException {
    KeyChain keyChain = new KeyChain();
    try {
      keyChain.getDefaultCertificateName();
    } catch (net.named_data.jndn.security.SecurityException e) {
      keyChain.createIdentity(new Name("/test/identity"));
      keyChain.getIdentityManager().setDefaultIdentity(new Name("/test/identity"));
    }
    return keyChain;
  }
}
