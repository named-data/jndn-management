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
 * Indicate whether the face is local for scope control purposes; used by
 * FaceStatus.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt">Face Management</a>
 */
public enum FaceScope {

  NONE(-1), // invalid value
  NON_LOCAL(0),
  LOCAL(1);

  private final int value;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Create enum using NFD's FaceScope code.
   *
   * @param value NFD's FaceScope code
   */
  FaceScope(final int value) {
    this.value = value;
  }

  /**
   * Convert FaceScope to the NFD code.
   *
   * @return NFD's FaceScope code
   */
  public final int toInteger() {
    return value;
  }

  /**
   * Convert FaceScope to human-readable string.
   * @return string
   */
  public final String toString() {
    switch (value) {
      case 0:
        return "non-local";
      case 1:
        return "local";
      default:
        return "none";
    }
  }

  /**
   * Convert NFD code to FaceScope enum.
   *
   * @param value NFD's FaceScope code
   * @return enum value
   */
  public static FaceScope fromInteger(final int value) {
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
