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
 * NFD route flags
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/RibMgmt">RibMgmt</a>
 */
public enum RouteFlags {
  NONE          (0),
  CHILD_INHERIT (1),
  CAPTURE       (2);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  RouteFlags(int value) {
    this.value = value;
  }

  public final int toInteger() {
    return value;
  }
}
