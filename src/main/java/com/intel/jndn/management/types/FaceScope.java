/*
 * File name: FaceScope.java
 * 
 * Purpose: Indicate whether the face is local for scope control purposes; 
 * used by FaceStatus. See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

/**
 * Indicate whether the face is local for scope control purposes; used by FaceStatus
 * See http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
 *
 * @author andrew
 */
public enum FaceScope {

	LOCAL(0),
	NON_LOCAL(1);

	FaceScope(int value) {
		value_ = value;
	}

	public final int getNumericValue() {
		return value_;
	}
	private final int value_;
}
