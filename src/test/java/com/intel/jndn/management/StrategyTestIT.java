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

import com.intel.jndn.management.types.StrategyChoice;
import com.intel.jndn.mock.MockKeyChain;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.security.KeyChain;
import org.junit.Test;

/**
 * Test strategy management on a real, local NFD
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StrategyTestIT {

  private static final Logger logger = Logger.getLogger(StrategyTestIT.class.getName());
  Name prefix;
  Face face;

  public StrategyTestIT() throws net.named_data.jndn.security.SecurityException {
    this.prefix = new Name("/test/strategy").append("random:" + new Random().nextInt());
    this.face = new Face("localhost"); // strategy commands only available on localhost
    KeyChain mockKeyChain = MockKeyChain.configure(new Name("/test/server"));
    face.setCommandSigningInfo(mockKeyChain, mockKeyChain.getDefaultCertificateName());
  }

  @Test
  public void testStrategySetUnset() throws Exception {
    List<StrategyChoice> choices = NFD.getStrategyList(face);
    int oldSize = choices.size();

    NFD.setStrategy(face, prefix, Strategies.CLIENT_CONTROL);
    Thread.sleep(1000); // strategy takes a while to register

    choices = NFD.getStrategyList(face);
    assertEquals(oldSize + 1, choices.size());

    NFD.unsetStrategy(face, prefix);
  }
}
