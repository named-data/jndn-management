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
import com.intel.jndn.management.enums.RouteFlags;
import net.named_data.jndn.Name;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test RibEntry and Route encoding/decoding.
 */
public class RibEntryTest {
  private ByteBuffer testRouteDataWire;
  private ByteBuffer testRouteInfiniteExpirationPeriodWire;
  private ByteBuffer testRibEntryDataWire;
  private ByteBuffer testRibEntryInfiniteExpirationPeriodWire;
  private ByteBuffer testRibEntryForRootDataWire;

  @Before
  public void setUp() throws Exception {
    testRouteDataWire = TestHelper.bufferFromIntArray(new int[]{
      0x81, 0x10, 0x69, 0x01, 0x01, 0x6f, 0x01, 0x80, 0x6a, 0x01, 0x64, 0x6c, 0x01, 0x02,
      0x6d, 0x02, 0x27, 0x10
    });
    testRouteInfiniteExpirationPeriodWire = TestHelper.bufferFromIntArray(new int[]{
      0x81, 0x0C, 0x69, 0x01, 0x01, 0x6f, 0x01, 0x80, 0x6a, 0x01, 0x64, 0x6c, 0x01, 0x02
    });
    testRibEntryDataWire = TestHelper.bufferFromIntArray(new int[]{
      // Header + Name (ndn:/hello/world)
      0x80, 0x34, 0x07, 0x0e, 0x08, 0x05, 0x68, 0x65, 0x6c, 0x6c, 0x6f,
      0x08, 0x05, 0x77, 0x6f, 0x72, 0x6c, 0x64,
      // Route
      0x81, 0x10, 0x69, 0x01, 0x01, 0x6f, 0x01, 0x80, 0x6a, 0x01, 0x64, 0x6c, 0x01, 0x02,
      0x6d, 0x02, 0x27, 0x10,
      // Route
      0x81, 0x10, 0x69, 0x01, 0x02, 0x6f, 0x01, 0x00, 0x6a, 0x01, 0x20, 0x6c, 0x01, 0x01,
      0x6d, 0x02, 0x13, 0x88
    });
    testRibEntryInfiniteExpirationPeriodWire = TestHelper.bufferFromIntArray(new int[]{
      // Header + Name (ndn:/hello/world)
      0x80, 0x30, 0x07, 0x0e, 0x08, 0x05, 0x68, 0x65, 0x6c, 0x6c, 0x6f,
      0x08, 0x05, 0x77, 0x6f, 0x72, 0x6c, 0x64,
      // Route
      0x81, 0x10, 0x69, 0x01, 0x01, 0x6f, 0x01, 0x80, 0x6a, 0x01, 0x64, 0x6c, 0x01, 0x02,
      0x6d, 0x02, 0x27, 0x10,
      // Route with no ExpirationPeriod
      0x81, 0x0C, 0x69, 0x01, 0x02, 0x6f, 0x01, 0x00, 0x6a, 0x01, 0x20, 0x6c, 0x01, 0x01,
    });
    testRibEntryForRootDataWire = TestHelper.bufferFromIntArray(new int[]{
      // Header + Name (ndn:/)
      0x80, 0x26, 0x07, 0x00,
      // Route
      0x81, 0x10, 0x69, 0x01, 0x01, 0x6f, 0x01, 0x80, 0x6a, 0x01, 0x64, 0x6c, 0x01, 0x02,
      0x6d, 0x02, 0x27, 0x10,
      // Route
      0x81, 0x10, 0x69, 0x01, 0x02, 0x6f, 0x01, 0x00, 0x6a, 0x01, 0x20, 0x6c, 0x01, 0x01,
      0x6d, 0x02, 0x13, 0x88
    });
  }

  @Test
  public void testRouteEncode() throws Exception {
    Route route = new Route();
    route.setFaceId(1);
    route.setOrigin(128);
    route.setCost(100);
    route.setFlags(RouteFlags.CAPTURE.toInteger());
    route.setExpirationPeriod(10000);

    assertEquals(testRouteDataWire, route.wireEncode().buf());
  }

  @Test
  public void testRouteDecode() throws Exception {
    Route route = new Route(testRouteDataWire);

    assertEquals(route.getFaceId(), 1);
    assertEquals(route.getOrigin(), 128);
    assertEquals(route.getCost(), 100);
    assertEquals(RouteFlags.CAPTURE.toInteger(), route.getFlags());
    assertEquals(route.getExpirationPeriod(), 10000);
    assertEquals(route.hasInfiniteExpirationPeriod(), false);
  }

  @Test
  public void testRouteInfiniteExpirationPeriodEncode() throws Exception {
    Route route = new Route();
    route.setFaceId(1);
    route.setOrigin(128);
    route.setCost(100);
    route.setFlags(RouteFlags.CAPTURE.toInteger());
    route.setExpirationPeriod(Route.INFINITE_EXPIRATION_PERIOD);

    assertEquals(testRouteInfiniteExpirationPeriodWire, route.wireEncode().buf());
  }

  @Test
  public void testRouteInfiniteExpirationPeriodDecode() throws Exception {
    Route route = new Route(testRouteInfiniteExpirationPeriodWire);

    assertEquals(route.getFaceId(), 1);
    assertEquals(route.getOrigin(), 128);
    assertEquals(route.getCost(), 100);
    assertEquals(RouteFlags.CAPTURE.toInteger(), route.getFlags());
    assertEquals(route.getExpirationPeriod(), Route.INFINITE_EXPIRATION_PERIOD);
    assertEquals(route.hasInfiniteExpirationPeriod(), true);
  }

  @Test
  public void testRouteOutputStream() throws Exception {
    Route route = new Route();
    route.setFaceId(1);
    route.setOrigin(128);
    route.setCost(100);
    route.setFlags(RouteFlags.CAPTURE.toInteger());
    route.setExpirationPeriod(10000);

    assertEquals(route.toString(), "Route(FaceId: 1, Origin: 128, Cost: 100, " +
      "Flags: 2, ExpirationPeriod: 10000 milliseconds)");
  }

  @Test
  public void testRibEntryEncode() throws Exception {
    RibEntry entry = newRibEntry("/hello/world", 2, false);

    assertEquals(testRibEntryDataWire, entry.wireEncode().buf());
  }

  @Test
  public void testRibEntryDecode() throws Exception {
    RibEntry entry = new RibEntry(testRibEntryDataWire);

    assertRibEntry(entry, "/hello/world", 2, false);
  }

  @Test
  public void testRibEntryForRootEncode() throws Exception {
    RibEntry entry = newRibEntry("/", 2, false);

    assertEquals(testRibEntryForRootDataWire, entry.wireEncode().buf());
  }

  @Test
  public void testRibEntryForRootDecode() throws Exception {
    RibEntry entry = new RibEntry(testRibEntryForRootDataWire);

    assertRibEntry(entry, "/", 2, false);
  }

  @Test
  public void testRibEntryInfiniteExpirationPeriodEncode() throws Exception {
    RibEntry entry = newRibEntry("/hello/world", 2, true);
    assertEquals(testRibEntryInfiniteExpirationPeriodWire, entry.wireEncode().buf());
  }

  @Test
  public void testRibEntryInfiniteExpirationPeriodDecode() throws Exception {
    RibEntry entry = new RibEntry();

    entry.wireDecode(testRibEntryInfiniteExpirationPeriodWire);
    assertRibEntry(entry, "/hello/world", 2, true);
  }

  @Test
  public void testRibEntryClear() throws Exception {
    RibEntry entry = newRibEntry("/hello/world", 2, true);
    assertEquals(entry.getRoutes().size(), 2);
    assertRibEntry(entry, "/hello/world", 2, true);

    entry.clearRoutes();
    assertEquals(entry.getRoutes().size(), 0);
  }

  @Test
  public void testRibEntryOutputStream() throws Exception {
    RibEntry entry = newRibEntry("/hello/world", 2, true);

    assertEquals("RibEntry{\n" +
        "  Name: /hello/world\n" +
        "  Route(FaceId: 1, Origin: 128, Cost: 100, Flags: 2, ExpirationPeriod: 10000 milliseconds)\n" +
        "  Route(FaceId: 2, Origin: 0, Cost: 32, Flags: 1, ExpirationPeriod: Infinity)\n" +
        "}",
      entry.toString());
  }

  @Test
  public void testRibEntryAddRoutes() {
    RibEntry a = newRibEntry("/hello/world", 2, true);

    RibEntry b = new RibEntry();
    b.setName(new Name("/another/prefix"));
    b.setRoutes(a.getRoutes());

    assertEquals("RibEntry{\n" +
        "  Name: /another/prefix\n" +
        "  Route(FaceId: 1, Origin: 128, Cost: 100, Flags: 2, ExpirationPeriod: 10000 milliseconds)\n" +
        "  Route(FaceId: 2, Origin: 0, Cost: 32, Flags: 1, ExpirationPeriod: Infinity)\n" +
        "}",
      b.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  private RibEntry newRibEntry(final String name, final int nRoutes, final boolean isInfiniteSecond) {
    RibEntry entry = new RibEntry();
    entry.setName(new Name(name));

    Route route1 = new Route();
    route1.setFaceId(1);
    route1.setOrigin(128);
    route1.setCost(100);
    route1.setFlags(RouteFlags.CAPTURE.toInteger());
    route1.setExpirationPeriod(10000);
    entry.addRoute(route1);

    if (nRoutes > 1) {
      Route route2 = new Route();
      route2.setFaceId(2);
      route2.setOrigin(0);
      route2.setCost(32);
      route2.setFlags(RouteFlags.CHILD_INHERIT.toInteger());
      if (isInfiniteSecond) {
        route2.setExpirationPeriod(Route.INFINITE_EXPIRATION_PERIOD);
      } else {
        route2.setExpirationPeriod(5000);
      }
      entry.addRoute(route2);
    }

    return entry;
  }

  private void assertRibEntry(final RibEntry entry, final String name, final int nRoutes,
                              final boolean isInfiniteSecond) throws Exception {
    assertEquals(entry.getName().toUri(), name);
    assertEquals(entry.getRoutes().size(), nRoutes);

    List<Route> routes = entry.getRoutes();

    ListIterator<Route> it = routes.listIterator();
    assertTrue(it.hasNext());
    Route item = it.next();
    assertEquals(item.getFaceId(), 1);
    assertEquals(item.getOrigin(), 128);
    assertEquals(item.getCost(), 100);
    assertEquals(RouteFlags.CAPTURE.toInteger(), item.getFlags());
    assertEquals(item.getExpirationPeriod(), 10000);
    assertEquals(item.hasInfiniteExpirationPeriod(), false);

    if (nRoutes > 1) {
      assertTrue(it.hasNext());
      item = it.next();
      assertEquals(item.getFaceId(), 2);
      assertEquals(item.getOrigin(), 0);
      assertEquals(item.getCost(), 32);
      assertEquals(RouteFlags.CHILD_INHERIT.toInteger(), item.getFlags());

      if (isInfiniteSecond) {
        assertEquals(item.getExpirationPeriod(), Route.INFINITE_EXPIRATION_PERIOD);
        assertEquals(item.hasInfiniteExpirationPeriod(), true);
      } else {
        assertEquals(item.getExpirationPeriod(), 5000);
        assertEquals(item.hasInfiniteExpirationPeriod(), false);
      }
    }
  }
}
