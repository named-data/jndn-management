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

import com.intel.jndn.management.types.ControlResponse;
import junit.framework.Assert;
import net.named_data.jndn.ControlParameters;
import net.named_data.jndn.util.Blob;
import org.junit.Test;

/**
 * Test encoding/decoding of ControlResponses.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ControlResponseTest {

  /**
   * Test encoding/decoding
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeDecode() throws Exception {
    ControlParameters parameters = new ControlParameters();
    parameters.setFaceId(3);
    ControlResponse response = new ControlResponse();
    response.setStatusCode(404);
    response.setStatusText("Not Found");
    response.getBody().add(parameters);

    // encode
    Blob encoded = response.wireEncode();

    // decode
    ControlResponse decoded = new ControlResponse();
    decoded.wireDecode(encoded.buf());

    // test
    Assert.assertEquals(response.getStatusCode(), decoded.getStatusCode());
    Assert.assertEquals(response.getStatusText(), decoded.getStatusText());
    Assert.assertEquals(response.getBody().size(), decoded.getBody().size());
    Assert.assertEquals(response.getBody().get(0).getFaceId(), decoded.getBody().get(0).getFaceId());
  }
}
