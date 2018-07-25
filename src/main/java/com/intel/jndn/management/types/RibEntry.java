/*
 * jndn-management
 * Copyright (c) 2015-2018, Intel Corporation.
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

import com.intel.jndn.management.enums.NfdTlv;
import com.intel.jndn.management.helpers.EncodingHelper;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represent a entry in the RIB.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/RibMgmt#RIB-Dataset">RIB Dataset</a>
 */
public class RibEntry implements Decodable {
  private Name name = new Name();
  private List<Route> routes = new ArrayList<>();

  /**
   * Default constructor.
   */
  public RibEntry() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public RibEntry(final ByteBuffer input) throws EncodingException {
    wireDecode(input);
  }

  /**
   * Encode using a new TLV encoder.
   *
   * @return The encoded buffer.
   */
  public final Blob wireEncode() {
    TlvEncoder encoder = new TlvEncoder();
    wireEncode(encoder);
    return new Blob(encoder.getOutput(), false);
  }

  /**
   * Encode as part of an existing encode context.
   *
   * @param encoder TlvEncoder instance
   */
  public final void wireEncode(final TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    ListIterator<Route> route = routes.listIterator(routes.size());
    while (route.hasPrevious()) {
      route.previous().wireEncode(encoder);
    }
    EncodingHelper.encodeName(name, encoder);
    encoder.writeTypeAndLength(NfdTlv.RibEntry, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
   * @throws EncodingException For invalid encoding.
   */
  public final void wireDecode(final ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wireDecode(final TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.RibEntry);
    name = EncodingHelper.decodeName(decoder);
    while (decoder.getOffset() < endOffset) {
      Route route = new Route();
      route.wireDecode(decoder);
      routes.add(route);
    }
    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * @return RIB entry name.
   */
  public Name getName() {
    return name;
  }

  /**
   * Set RIB entry name.
   *
   * @param name New name for the RIB entry
   * @return this
   */
  public RibEntry setName(final Name name) {
    this.name = name;
    return this;
  }

  /**
   * @return List of associated routes to the RIB entry
   */
  public List<Route> getRoutes() {
    return routes;
  }

  /**
   * Add route.
   *
   * @param route Route to add to the RIB entry
   * @return this
   */
  public RibEntry addRoute(final Route route) {
    getRoutes().add(route);
    return this;
  }

  /**
   * Clear all routes.
   */
  public void clearRoutes() {
    getRoutes().clear();
  }

  /**
   * Set routes.
   *
   * @param routes List of routes to associate with RIB entry.  Will replace previously associated routes.
   */
  public void setRoutes(final List<Route> routes) {
    this.routes = routes;
  }

  /**
   * @return Human-readable representation of RibEntry
   */
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append("RibEntry{\n");
    out.append("  Name: ").append(getName().toUri()).append("\n");

    for (Route route : getRoutes()) {
      out.append("  ").append(route.toString()).append("\n");
    }
    out.append("}");
    return out.toString();
  }
}
