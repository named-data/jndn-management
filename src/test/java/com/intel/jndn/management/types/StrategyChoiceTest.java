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
package com.intel.jndn.management.types;

import com.intel.jndn.management.TestHelper;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Test StrategyChoice encoding/decoding.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StrategyChoiceTest {
  private ByteBuffer testStrategyChoiceWire;

  @Before
  public void setUp() throws Exception {
    testStrategyChoiceWire = TestHelper.bufferFromIntArray(new int[]{
      0x80, 0x39, 0x07, 0x0e, 0x08, 0x05, 0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x08, 0x05, 0x77,
      0x6f, 0x72, 0x6c, 0x64, 0x6b, 0x27, 0x07, 0x25, 0x08, 0x04, 0x73, 0x6f, 0x6d, 0x65,
      0x08, 0x03, 0x6e, 0x6f, 0x6e, 0x08, 0x08, 0x65, 0x78, 0x69, 0x73, 0x74, 0x69, 0x6e,
      0x67, 0x08, 0x08, 0x73, 0x74, 0x72, 0x61, 0x74, 0x65, 0x67, 0x79, 0x08, 0x04, 0x6e,
      0x61, 0x6d, 0x65
    });
  }

  @Test
  public void testEncode() {
    StrategyChoice strategyChoice = new StrategyChoice();
    strategyChoice
      .setName(new Name("/hello/world"))
      .setStrategy(new Name("/some/non/existing/strategy/name"));

    ByteBuffer wire = strategyChoice.wireEncode().buf();
    assertEquals(testStrategyChoiceWire, wire);
  }

  @Test
  public void testDecode() throws EncodingException {
    StrategyChoice strategyChoice = new StrategyChoice(testStrategyChoiceWire);
    assertEquals("/hello/world", strategyChoice.getName().toUri());
    assertEquals("/some/non/existing/strategy/name", strategyChoice.getStrategy().toUri());
  }
}
