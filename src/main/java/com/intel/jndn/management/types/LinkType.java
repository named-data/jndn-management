/*
 * File name: LinkType.java
 * 
 * Purpose: Indicate the type of communication link; used by FaceStatus
 * See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

/**
 * Indicate the type of communication link; used by FaceStatus
 * See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public enum LinkType {

	POINT_TO_POINT(0),
	MULTI_ACCESS(1);

	LinkType(int value) {
		value_ = value;
	}

	public final int getNumericValue() {
		return value_;
	}
	private final int value_;
}
