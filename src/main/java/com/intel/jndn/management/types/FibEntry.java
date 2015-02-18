/*
 * File name: FibEntry.java
 * 
 * Purpose: Represent a FibEntry returned from /localhost/nfd/fib/list; see
 * http://redmine.named-data.net/projects/nfd/wiki/FibMgmt#FIB-Dataset
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management.types;

import com.intel.jndn.management.EncodingHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a FibEntry returned from /localhost/nfd/fib/list; see
 * http://redmine.named-data.net/projects/nfd/wiki/FibMgmt#FIB-Dataset
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FibEntry implements Decodable{

	public final static int TLV_FIB_ENTRY = 128;

	/**
	 * Encode using a new TLV encoder.
	 *
	 * @return The encoded buffer.
	 */
	public final Blob wireEncode() {
		TlvEncoder encoder = new TlvEncoder();
		wireEncode(encoder);
		return new Blob(encoder.getOutput(), false);
	}

	/**
	 * Encode as part of an existing encode context.
	 *
	 * @param encoder
	 */
	public final void wireEncode(TlvEncoder encoder) {
		int saveLength = encoder.getLength();
		for (NextHopRecord record : records) {
			record.wireEncode(encoder);
		}
		EncodingHelper.encodeName(name, encoder);
		encoder.writeTypeAndLength(TLV_FIB_ENTRY, encoder.getLength() - saveLength);
	}

	/**
	 * Decode the input from its TLV format.
	 *
	 * @param input The input buffer to decode. This reads from position() to
	 * limit(), but does not change the position.
	 * @throws EncodingException For invalid encoding.
	 */
	public final void wireDecode(ByteBuffer input) throws EncodingException {
		TlvDecoder decoder = new TlvDecoder(input);
		wireDecode(decoder);
	}

	/**
	 * Decode as part of an existing decode context.
	 *
	 * @param decoder
	 * @throws EncodingException
	 */
	@Override
	public final void wireDecode(TlvDecoder decoder) throws EncodingException {
		int endOffset = decoder.readNestedTlvsStart(TLV_FIB_ENTRY);
		name = EncodingHelper.decodeName(decoder);
		while (decoder.getOffset() < endOffset) {
			NextHopRecord record = new NextHopRecord();
			record.wireDecode(decoder);
			records.add(record);
		}
		decoder.finishNestedTlvs(endOffset);
	}

	/**
	 * Get name
	 *
	 * @return
	 */
	public Name getName() {
		return name;
	}

	/**
	 * Set name
	 *
	 * @param name
	 */
	public void setName(Name name) {
		this.name = name;
	}

	/**
	 * Get records
	 *
	 * @return
	 */
	public List<NextHopRecord> getRecords() {
		return records;
	}

	/**
	 * Set records
	 *
	 * @param records
	 */
	public void setRecords(List<NextHopRecord> records) {
		this.records = records;
	}

	private Name name = new Name();
	private List<NextHopRecord> records = new ArrayList<>();
}
