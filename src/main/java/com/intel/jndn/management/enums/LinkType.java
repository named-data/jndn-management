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
package com.intel.jndn.management.enums;

/**
 * NFD face link type.
 *
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt">Face Management</a>
 */
public enum LinkType {

  NONE(-1),
  POINT_TO_POINT(0),
  MULTI_ACCESS(1),
  AD_HOC(2);

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
   * Convert LinkType to human-readable string.
   * @return string
   */
  public final String toString() {
    switch (value) {
      case 0:
        return "point-to-point";
      case 1:
        return "multi-access";
      case 2:
        return "adhoc";
      default:
        return "none";
    }
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
      case 2:
        return AD_HOC;
      default:
        return NONE;
    }
  }
}
