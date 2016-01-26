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
 * NFD route origin
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/RibMgmt">RibMgmt</a>
 */
public enum RouteOrigin {
  UNKNOWN  (-1),
  APP      (0),
  AUTOREG  (64),
  CLIENT   (65),
  AUTOCONF (66),
  NLSR     (128),
  STATIC   (255);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  RouteOrigin(int value) {
    this.value = value;
  }

  public final int toInteger() {
    return value;
  }

  public static RouteOrigin
  fromInteger(int value) {
    switch (value) {
      case 0:
        return APP;
      case 64:
        return AUTOREG;
      case 65:
        return CLIENT;
      case 66:
        return AUTOCONF;
      case 128:
        return NLSR;
      case 255:
        return STATIC;
      default:
        return UNKNOWN;
    }
  }
}
