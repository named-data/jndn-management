/*
 * File name: FaceStatusTest.java
 * 
 * Purpose: Test whether the decoding for the face management service is 
 * working correctly
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import com.intel.jndn.management.types.StatusDataset;
import com.intel.jndn.management.types.FaceStatus;
import com.intel.jndn.utils.Client;
import java.util.List;
import junit.framework.Assert;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.util.Blob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test whether the decoding for the face management service is working
 * correctly
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FaceStatusTest {

  private static final Logger logger = LogManager.getLogger();

  /**
   * Test encoding/decoding
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testEncodeDecode() throws Exception {
    FaceStatus status = new FaceStatus();
    status.setFaceId(42);
    status.setUri("...");
    status.setLocalUri("...");

    // encode
    Blob encoded = status.wireEncode();

    // decode
    FaceStatus decoded = new FaceStatus();
    decoded.wireDecode(encoded.buf());

    // test
    Assert.assertEquals(status.getFaceId(), decoded.getFaceId());
    Assert.assertEquals(status.getUri(), decoded.getUri());
    Assert.assertEquals(status.getLocalUri(), decoded.getLocalUri());
    Assert.assertEquals(status.getExpirationPeriod(), decoded.getExpirationPeriod());
    Assert.assertEquals(status.getFaceScope(), decoded.getFaceScope());
    Assert.assertEquals(status.getFacePersistency(), decoded.getFacePersistency());
    Assert.assertEquals(status.getLinkType(), decoded.getLinkType());
    Assert.assertEquals(status.getInBytes(), decoded.getInBytes());
    Assert.assertEquals(status.getOutBytes(), decoded.getOutBytes());
  }

  /**
   * Test of decode method, of class FaceStatus.
   *
   * @throws java.lang.Exception
   */
  @Test
  public void testDecodeFakeData() throws Exception {
    Data data = getFaceData(true);
    List<FaceStatus> results = StatusDataset.wireDecode(data.getContent(), FaceStatus.class);
    assertTrue(results.size() > 4);
    for (FaceStatus f : results) {
      // the first face (face 1) should always be the internal face
      if (f.getFaceId() == 1) {
        assertEquals("internal://", f.getUri());
        assertEquals("internal://", f.getLocalUri());
      }
    }
  }

  /**
   * Integration test to run on actual system
   *
   * @param args
   * @throws EncodingException
   */
  public static void main(String[] args) throws Exception {
    Data data = getFaceData(false);
    List<FaceStatus> results = StatusDataset.wireDecode(data.getContent(), FaceStatus.class);
    assertTrue(results.size() > 4);
    for (FaceStatus f : results) {
      // the first face (face 1) should always be the internal face
      if (f.getFaceId() == 1) {
        assertEquals("internal://", f.getUri());
        assertEquals("internal://", f.getLocalUri());
      }
    }
  }

  /**
   * Retrieve a TLV encoded representation of the face list data
   *
   * @param usePreComputedData to avoid errors when local NFD is not present
   * @return
   */
  private static Data getFaceData(boolean usePreComputedData) {
    // use pre-computed data to avoid errors when local NFD is not present
    if (usePreComputedData) {
      Data data = new Data();
      data.setContent(new Blob(hexStringToByteArray(DATA)));
      return data;
    } // alternately, query the actual localhost for current data
    else {
      Face forwarder = new Face("localhost");

      // build management Interest packet; see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
      Interest interest = new Interest(new Name("/localhost/nfd/faces/list"));
      interest.setMustBeFresh(true);
      interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
      interest.setInterestLifetimeMilliseconds(2000.0);

      // send packet
      Data data = Client.getDefault().getSync(forwarder, interest);
      String hex = data.getContent().toHex();
      logger.info("Hex dump of face list: " + hex);
      return data;
    }
  }

  /**
   * Pre-computed face list from a vanilla NFD running on Ubuntu 14.04
   */
  private static final String DATA = "803a690101720b696e7465726e616c3a2f2f810b696e7465726e616c3a2f2f840101850100860100900100910201429202063993010094010095010080406901fe720f636f6e74656e7473746f72653a2f2f810f636f6e74656e7473746f72653a2f2f84010185010086010090010091010092010093010094010095010080306901ff72076e756c6c3a2f2f81076e756c6c3a2f2f8401018501008601009001009101009201009301009401009501008053690201007219756470343a2f2f3232342e302e32332e3137303a35363336338117756470343a2f2f31302e35342e31322e373a35363336338401008501008601009001009101009201009301009401009501008056690201017219756470343a2f2f3232342e302e32332e3137303a3536333633811a756470343a2f2f3139322e3136382e35302e35373a3536333633840100850100860100900100910100920100930100940100950100804869020102721b65746865723a2f2f5b30313a30303a35653a30303a31373a61615d810a6465763a2f2f65746830840100850100860100900100910100920100930100940100950100804969020103721b65746865723a2f2f5b30313a30303a35653a30303a31373a61615d810b6465763a2f2f776c616e30840100850100860100900100910100920100930100940100950100804669020104720766643a2f2f32328114756e69783a2f2f2f72756e2f6e66642e736f636b840101850101860100900206349101019201019302012e940400014079950400041903804e690201197216746370343a2f2f3132372e302e302e313a35363336358115746370343a2f2f3132372e302e302e313a36333633840101850101860100900101910100920100930100940132950100";

  /**
   * Convert hex string to bytes; special thanks to
   * http://stackoverflow.com/questions/140131
   *
   * @param s
   * @return
   */
  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
              + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

}
