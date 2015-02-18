/*
 * File name: RibEntryTest.java
 * 
 * Purpose: Test encoding/decoding for RibEntry.
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import com.intel.jndn.management.types.StatusDataset;
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.management.types.Route;
import com.intel.jndn.utils.Client;
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

		// build management Interest packet; see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
		Interest interest = new Interest(new Name("/localhost/nfd/rib/list"));
		interest.setMustBeFresh(true);
		interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
		interest.setInterestLifetimeMilliseconds(2000.0);

		// send packet
		Data data = Client.getDefault().getSync(forwarder, interest);

		// decode results
		List<RibEntry> results = StatusDataset.wireDecode(data.getContent(), RibEntry.class);
		assertTrue(results.size() > 0);
		assertEquals("/localhost/nfd", results.get(0).getName().toUri());
	}
}
