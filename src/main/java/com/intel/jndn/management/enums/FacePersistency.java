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
 * Indicate whether the face is persistent; used by FaceStatus.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="http://redmine.named-data.net/projects/nfd/widi/FaceMgmt">Face Management</a>
 */
public enum FacePersistency {
  NONE(-1), // invalid value

  PERSISTENT(0),
  ON_DEMAND(1),
  PERMANENT(2);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Create enum using NFD's FacePersistency code.
   *
   * @param value NFD's FacePersistency code
   */
  FacePersistency(final int value) {
    this.value = value;
  }

  /**
   * Convert FacePersistency to the NFD code.
   *
   * @return NFD's FacePersistency code
   */
  public final int toInteger() {
    return value;
  }

  /**
   * Convert NFD code to FacePersistency enum.
   *
   * @param value NFD's FacePersistency code
   * @return enum value
   */
  public static FacePersistency
  fromInteger(final int value) {
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
