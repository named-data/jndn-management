/*
 * File name: ControlResponse.java
 * 
 * Purpose: Represent a ControlResponse, a Data packet sent in response to a 
 * ControlCommand to the NFD, see http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import java.util.List;
import net.named_data.jndn.ControlParameters;
import net.named_data.jndn.Data;
import net.named_data.jndn.encoding.EncodingException;

/**
 * 
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ControlResponse {
	public int StatusCode;
	public String StatusText;
	public List<ControlParameters> Body;
	
	/**
	 * Decode input as a ControlResponse in NDN-TLV and set the fields of the
	 * new object
	 * 
	 * @param data
	 * @return 
	 */
	public static ControlResponse decode(Data data) throws EncodingException{
		ControlResponseDecoder decoder = new ControlResponseDecoder();
		ControlResponse response = new ControlResponse();
		decoder.decodeControlResponse(response, data.getContent().buf());
		return response;
	}
}
