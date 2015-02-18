/*
 * File name: Decodable.java
 * 
 * Purpose: Interface used by StatusDataset to decode generic message types; if
 * they are Decodable, then StatusDataset will instantiate and decode them.
 * 
 * Purpose: 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;

/**
 * Interface used by StatusDataset to decode generic message types; if they are
 * Decodable, then StatusDataset will instantiate and decode them.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public interface Decodable {
	public void wireDecode(TlvDecoder decoder) throws EncodingException;
}
