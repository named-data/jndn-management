/*
 * File name: ControlParametersDecoder.java
 * 
 * Purpose: See http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import net.named_data.jndn.ControlParameters;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.TlvWireFormat;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.util.Blob;

/**
 * 
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ControlResponseDecoder extends TlvWireFormat {

	/**
	 * Use TLV codes from jndn.encoding.tlv.Tlv.java See
	 * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
	 */
	public final static int ControlResponse = 101;
	public final static int ControlResponse_StatusCode = 102;
	public final static int ControlResponse_StatusText = 103;

	/**
	 * Decode a ControlResponse TLV object; see
	 * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
	 *
	 * @param controlResponse
	 * @param input
	 * @throws EncodingException
	 */
	public void decodeControlResponse(ControlResponse controlResponse, ByteBuffer input) throws EncodingException {
		TlvDecoder decoder = new TlvDecoder(input);
		int endOffset = decoder.readNestedTlvsStart(ControlResponse);

		// parse
		controlResponse.StatusCode = (int) decoder.readNonNegativeIntegerTlv(ControlResponse_StatusCode);
		Blob statusText = new Blob(decoder.readBlobTlv(ControlResponse_StatusText), true); // copy because buffer is immutable
		controlResponse.StatusText = statusText.toString();
		controlResponse.Body = new ArrayList<>();
		while (decoder.peekType(Tlv.ControlParameters_ControlParameters, endOffset)) {
			ByteBuffer copyInput = input.duplicate();
			copyInput.position(decoder.getOffset());
			int internalEndOffset = decoder.readNestedTlvsStart(Tlv.ControlParameters_ControlParameters);
			// decode
			ControlParameters copyParameters = new ControlParameters();
			copyParameters.wireDecode(copyInput);
			controlResponse.Body.add(copyParameters);
			decoder.seek(internalEndOffset);
			// 
			decoder.finishNestedTlvs(internalEndOffset);
		}

		// etc...
		decoder.finishNestedTlvs(endOffset);
	}
}
