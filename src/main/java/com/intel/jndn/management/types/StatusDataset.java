/*
 * File name: StatusDataset.java
 * 
 * Purpose: Helper class to handle StatusDatasets, see 
 * http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

import java.util.ArrayList;
import java.util.List;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.util.Blob;

/**
 * Helper class to handle StatusDatasets, see
 * http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class StatusDataset {

	/**
	 * Decode multiple status entries as part of a StatusDataset, see
	 * http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
	 *
	 * @param <T>
	 * @param statusDataset
	 * @param type
	 * @return
	 * @throws EncodingException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static final <T extends Decodable> List<T> wireDecode(Blob statusDataset, Class<T> type) throws EncodingException, InstantiationException, IllegalAccessException {
		List<T> entries = new ArrayList<>();
		int endOffset = statusDataset.size();
		TlvDecoder decoder = new TlvDecoder(statusDataset.buf());
		while (decoder.getOffset() < endOffset) {
			T entry = type.newInstance();
			entry.wireDecode(decoder);
			entries.add(entry);
		}
		return entries;
	}
}
