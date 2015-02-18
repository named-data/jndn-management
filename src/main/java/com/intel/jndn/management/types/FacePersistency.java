/*
 * File name: FacePersistency.java
 * 
 * Purpose: Indicate whether the face is persistent; used by FaceStatus
 * See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

/**
 * Indicate whether the face is persistent; used by FaceStatus
 * See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public enum FacePersistency {

	PERSISTENT(0),
	ON_DEMAND(1),
	PERMANENT(2);

	FacePersistency(int value) {
		value_ = value;
	}

	public final int getNumericValue() {
		return value_;
	}
	private final int value_;
}
