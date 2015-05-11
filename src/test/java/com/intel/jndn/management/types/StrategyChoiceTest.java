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

import com.intel.jndn.management.Strategies;
import junit.framework.Assert;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.util.Blob;
import org.junit.Test;

/**
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StrategyChoiceTest {

  public StrategyChoiceTest() {
  }

  /**
   * Test of wireEncode method, of class StrategyChoice.
   */
  @Test
  public void testEncodeDecode() throws EncodingException {
    StrategyChoice choice = new StrategyChoice();
    choice.setName(new Name("/a/b"));
    choice.setStrategy(Strategies.NCC);

    // encode
    Blob encoded = choice.wireEncode();

    // decode
    StrategyChoice decoded = new StrategyChoice();
    decoded.wireDecode(encoded.buf());

    // test
    Assert.assertEquals(choice.getName().toUri(), decoded.getName().toUri());
    Assert.assertEquals(choice.getStrategy().toUri(), decoded.getStrategy().toUri());
  }
}
