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

import com.intel.jndn.management.types.StatusDataset;
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.management.types.Route;
import com.intel.jndn.utils.client.impl.SimpleClient;
import java.util.List;
import junit.framework.Assert;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.util.Blob;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test encoding/decoding for RibEntry.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class RibEntryTest {

  /**
   * Test encoding/decoding
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeDecode() throws Exception {
    Route route = new Route();
    route.setFaceId(42);
    route.setCost(100);
    route.setOrigin(0);
    RibEntry entry = new RibEntry();
    entry.setName(new Name("/rib/entry/test"));
    entry.getRoutes().add(route);

    // encode
    Blob encoded = entry.wireEncode();

    // decode
    RibEntry decoded = new RibEntry();
    decoded.wireDecode(encoded.buf());

    // test
    Assert.assertEquals(entry.getName().toUri(), decoded.getName().toUri());
    Assert.assertEquals(entry.getRoutes().get(0).getFaceId(), decoded.getRoutes().get(0).getFaceId());
    Assert.assertEquals(entry.getRoutes().get(0).getCost(), decoded.getRoutes().get(0).getCost());
    Assert.assertEquals(entry.getRoutes().get(0).getOrigin(), decoded.getRoutes().get(0).getOrigin());
  }

  /**
   * Integration test to run on actual system
   *
   * @param args
   * @throws EncodingException
   */
  public static void main(String[] args) throws Exception {
    Face forwarder = new Face("localhost");

    // build management Interest packet; see <a href="http://redmine.named-data.net/projects/nfd/wiki/StatusDataset">http://redmine.named-data.net/projects/nfd/wiki/StatusDataset</a>
    Interest interest = new Interest(new Name("/localhost/nfd/rib/list"));
    interest.setMustBeFresh(true);
    interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
    interest.setInterestLifetimeMilliseconds(2000.0);

    // send packet
    Data data = SimpleClient.getDefault().getSync(forwarder, interest);

    // decode results
    List<RibEntry> results = StatusDataset.wireDecode(data.getContent(), RibEntry.class);
    assertTrue(results.size() > 0);
    assertEquals("/localhost/nfd/rib", results.get(0).getName().toUri());
  }
}
