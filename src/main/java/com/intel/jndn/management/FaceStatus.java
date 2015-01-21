/*
 * File name: FaceStatus.java
 * 
 * Purpose: Represent a FaceStatus object from /localhost/nfd/faces/list;
 * see http://redmine.named-data.net/projects/nfd/wiki/FaceMgmt for details
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import java.util.List;
import net.named_data.jndn.Data;
import net.named_data.jndn.encoding.EncodingException;

/**
 * Represent a FaceStatus object from /localhost/nfd/faces/list;
 * see http://redmine.named-data.net/projects/nfd/wiki/FaceMgmt for details
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FaceStatus {

	public int faceId;
	public String uri; // can't use URI because some are invalid syntax
	public String localUri; // can't use URI because some are invalid syntax
	public int expirationPeriod;
	public FaceScope faceScope;
	public FacePersistency facePersistency;
	public LinkType linkType;
	public int inInterests;
	public int outInterests;
	public int inDatas;
	public int outDatas;
	public int inBytes;
	public int outBytes;

	/**
	 * Helper method for decoding a list of face statuses from
	 * /localhost/nfd/faces/list
	 * @param data
	 * @return
	 * @throws EncodingException 
	 */
	public static List<FaceStatus> decode(Data data) throws EncodingException{
		FaceStatusDecoder decoder = new FaceStatusDecoder();
		return decoder.decodeFaces(data.getContent().buf());
	}
}
