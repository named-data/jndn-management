/*
 * jndn-management
 * Copyright (c) 2015, Intel Corporation.
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

import net.named_data.jndn.ControlResponse;


/**
 * Represent a failure to correctly manage the NDN Forwarding Daemon (NFD).
 * Inspect this object with getCause() to see why the management operation
 * failed.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ManagementException extends Exception {
  /**
   * Constructor from the message.
   * @param message Error message
   */
  public ManagementException(final String message) {
    super(message);
  }

  /**
   * Constructor from the message and the cause.
   * @param message Error message
   * @param cause Chained exception
   */
  public ManagementException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Create exception from NFD's ControlResponse.
   * @param response ControlResponse from which to use status code and status text
   * @return new instance of ManagementException
   */
  public static ManagementException fromResponse(final ControlResponse response) {
    String message = "Action failed, forwarder returned: " + response.getStatusCode() + " " + response.getStatusText();
    return new ManagementException(message);
  }
}
