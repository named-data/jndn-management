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
 * NFD route origin.
 *
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/RibMgmt">RIB Management</a>
 */
public enum RouteOrigin {
  UNKNOWN(-1),
  APP(0),
  AUTOREG(64),
  CLIENT(65),
  AUTOCONF(66),
  NLSR(128),
  STATIC(255);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Create enum using NFD's RouteOrigin code.
   *
   * @param value NFD's RouteOrigin code
   */
  RouteOrigin(final int value) {
    this.value = value;
  }

  /**
   * Convert RouteOrigin to the NFD code.
   *
   * @return NFD's RouteOrigin code
   */
  public final int toInteger() {
    return value;
  }

  /**
   * Convert NFD code to RouteOrigin enum.
   *
   * @param value NFD's RouteOrigin code
   * @return enum value
   */
  public static RouteOrigin
  fromInteger(final int value) {
    if (value == APP.toInteger()) {
      return APP;
    } else if (value == AUTOREG.toInteger()) {
      return AUTOREG;
    } else if (value == CLIENT.toInteger()) {
      return CLIENT;
    } else if (value == AUTOCONF.toInteger()) {
      return AUTOCONF;
    } else if (value == NLSR.toInteger()) {
      return NLSR;
    } else if (value == STATIC.toInteger()) {
      return STATIC;
    } else {
      return UNKNOWN;
    }
  }
}
