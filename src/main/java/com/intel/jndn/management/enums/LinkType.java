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
 * NFD face link type
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">FaceMgmt</a>
 */
public enum LinkType {

  NONE(-1), // invalid value

  /**
   * Link is point-to-point
   */
  POINT_TO_POINT(0),

  /**
   * Link is multi-access
   */
  MULTI_ACCESS(1);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  LinkType(int value) {
    this.value = value;
  }

  public final int toInteger() {
    return value;
  }

  public static LinkType
  fromInteger(int value) {
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
