/*
 * jndn-management
 * Copyright (c) 2016, Regents of the University of California
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
package com.intel.jndn.management;

import java.nio.ByteBuffer;

/**
 * Helper methods for unit tests
 */
public class TestHelper {
  /**
   * Prevent instances of TestHelper
   */
  private TestHelper() {
  }

  /**
   * Construct ByteBuffer from int[]
   */
  public static ByteBuffer
  bufferFromIntArray(int[] array)
  {
    ByteBuffer result = ByteBuffer.allocate(array.length);
    for (int value : array) {
      result.put((byte) (value & 0xFF));
    }

    result.flip();
    return result;
  }
}
