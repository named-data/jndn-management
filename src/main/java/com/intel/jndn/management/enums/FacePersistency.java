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
 * Indicate whether the face is persistent; used by FaceStatus
 *
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">FaceMgmt</a>
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public enum FacePersistency {
  NONE(-1), // invalid value

  PERSISTENT(0),
  ON_DEMAND(1),
  PERMANENT(2);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  FacePersistency(int value) {
    this.value = value;
  }

  public final int toInteger() {
    return value;
  }

  public static FacePersistency
  fromInteger(int value) {
    switch (value) {
      case 0:
        return PERSISTENT;
      case 1:
        return ON_DEMAND;
      case 2:
        return PERMANENT;
      default:
        return NONE;
    }
  }
}
