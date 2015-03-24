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

import com.intel.jndn.management.types.ForwarderStatus;
import junit.framework.Assert;
import net.named_data.jndn.util.Blob;
import org.junit.Test;

/**
 * Test encoding/decoding of ForwarderStatus.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ForwarderStatusTest {

  /**
   * Test encoding/decoding
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeDecode() throws Exception {
    ForwarderStatus status = new ForwarderStatus();
    status.setNfdVersion("1.0");
    status.setCurrentTimestamp(System.currentTimeMillis());
    status.setNumInDatas(42);

    // encode
    Blob encoded = status.wireEncode();

    // decode
    ForwarderStatus decoded = new ForwarderStatus();
    decoded.wireDecode(encoded.buf());

    // test
    Assert.assertEquals(status.getNfdVersion(), decoded.getNfdVersion());
    Assert.assertEquals(status.getCurrentTimestamp(), decoded.getCurrentTimestamp());
    Assert.assertEquals(status.getStartTimestamp(), decoded.getStartTimestamp());
    Assert.assertEquals(status.getNumInDatas(), decoded.getNumInDatas());
  }
}
