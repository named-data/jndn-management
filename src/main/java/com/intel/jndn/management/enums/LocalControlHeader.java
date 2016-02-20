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
package com.intel.jndn.management.enums;

/**
 * Define constants for local control header options.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">Face Management</a>
 */
public enum LocalControlHeader {

  INCOMING_FACE_ID(1),
  NEXT_HOP_FACE_ID(2),
  CACHING_POLICY(3);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Create enum using NFD's LocalControlHeader code.
   *
   * @param value NFD's LocalControlHeader code
   */
  LocalControlHeader(final int value) {
    this.value = value;
  }

  /**
   * Convert LocalControlHeader to the NFD code.
   *
   * @return NFD's LocalControlHeader code
   */
  public final int toInteger() {
    return value;
  }
}
