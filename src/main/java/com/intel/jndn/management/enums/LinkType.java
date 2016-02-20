/*
 * jndn-management
 * Copyright (c) 2015-2016, Intel Corporation.
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
 * NFD face link type.
 *
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">Face Management</a>
 */
public enum LinkType {

  NONE(-1),
  POINT_TO_POINT(0),
  MULTI_ACCESS(1);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Create enum using NFD's LinkType code.
   *
   * @param value NFD's LinkType code
   */
  LinkType(final int value) {
    this.value = value;
  }

  /**
   * Convert LinkType to the NFD code.
   *
   * @return NFD's LinkType code
   */
  public final int toInteger() {
    return value;
  }

  /**
   * Convert NFD code to LinkType enum.
   *
   * @param value NFD's LinkType code
   * @return enum value
   */
  public static LinkType
  fromInteger(final int value) {
    switch (value) {
      case 0:
        return POINT_TO_POINT;
      case 1:
        return MULTI_ACCESS;
      default:
        return NONE;
    }
  }
}
