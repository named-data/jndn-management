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

import com.intel.jndn.management.types.RibEntry;
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
    NFD.register(face, "udp4://127.0.0.1:56363", new Name("/my/test/route"), 999);
    
    // check that route is created
    boolean found = false;
    for(RibEntry route : NFD.getRouteList(face)){
      if(route.getName().equals(new Name("/my/test/route"))){
        found = true;
      }
    }
    Assert.assertTrue(found);

    // remove the route
    NFD.unregister(face, new Name("/my/test/route"), "udp4://127.0.0.1:56363");
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
