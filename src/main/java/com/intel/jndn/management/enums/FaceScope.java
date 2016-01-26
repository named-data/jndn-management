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
 * Indicate whether the face is local for scope control purposes; used by
 * FaceStatus
 *
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">FaceMgmt</a>
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public enum FaceScope {

  NONE(-1), // invalid value

  NON_LOCAL(0),
  LOCAL(1);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  FaceScope(int value) {
    this.value = value;
  }

  public final int toInteger() {
    return value;
  }

  public static FaceScope
  fromInteger(int value) {
    switch (value) {
      case 0:
        return NON_LOCAL;
      case 1:
        return LOCAL;
      default:
        return NONE;
    }
  }
}
