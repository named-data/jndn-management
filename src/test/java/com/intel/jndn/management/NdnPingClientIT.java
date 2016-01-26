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
import com.intel.jndn.mock.MockKeyChain;
import net.named_data.jndn.Face;
import net.named_data.jndn.KeyLocator;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.SecurityException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Testing basic pining using real NFD instance (NFD must be run locally while executing the test)
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NdnPingClientIT {
  private Face face;

  @Before
  public void setUp() throws SecurityException {
    face = new Face("localhost");
  }

  @Test
  public void testPingLocal() throws IOException, ManagementException {
    boolean hasSucceeded = NdnPingClient.pingLocal(face);
    assertTrue(hasSucceeded);
  }
}
