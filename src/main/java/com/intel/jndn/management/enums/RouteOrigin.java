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
 * NFD route origin.
 *
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/RibMgmt">RIB Management</a>
 */
public enum RouteOrigin {
  NONE(-1),
  APP(0),
  AUTOREG(64),
  CLIENT(65),
  AUTOCONF(66),
  NLSR(128),
  SELFLEARNING(129),
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
   * Convert RouteOrigin to human-readable string.
   * @return string
   */
  public final String toString() {
    if (value == APP.toInteger()) {
      return "app";
    } else if (value == AUTOREG.toInteger()) {
      return "autoreg";
    } else if (value == CLIENT.toInteger()) {
      return "client";
    } else if (value == AUTOCONF.toInteger()) {
      return "autoconf";
    } else if (value == NLSR.toInteger()) {
      return "nlsr";
    } else if (value == SELFLEARNING.toInteger()) {
      return "selflearning";
    } else if (value == STATIC.toInteger()) {
      return "static";
    } else {
      return "none";
    }
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
    } else if (value == SELFLEARNING.toInteger()) {
      return SELFLEARNING;
    } else if (value == STATIC.toInteger()) {
      return STATIC;
    } else {
      return NONE;
    }
  }
}
