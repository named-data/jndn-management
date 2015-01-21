/*
 * File name: FaceStatusDecoder.java
 * 
 * Purpose: Decode lists of FaceStatus objects from /localhost/nfd/faces/list
 * 
 * © Copyright Intel Corporation. All rights reserved.
 * Intel Corporation, 2200 Mission College Boulevard,
 * Santa Clara, CA 95052-8119, USA
 */
package com.intel.jndn.management;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.util.Blob;

/**
 * Decode lists of FaceStatus objects from /localhost/nfd/faces/list
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FaceStatusDecoder {
	
	/**
	 * Spec from http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
	 */
	public static final int FACE_ID = 105; 
	public static final int URI = 114;
	public static final int EXPIRATION_PERIOD = 109;
	
	/**
	 * Spec from http://redmine.named-data.net/projects/nfd/widi/FaceMgmt
	 */
	public static final int FACE_STATUS = 128;
	public static final int LOCAL_URI = 129;
	public static final int CHANNEL_STATUS = 130;
	public static final int FACE_SCOPE = 132;
	public static final int FACE_PERSISTENCY = 133;
	public static final int LINK_TYPE = 134;
	public static final int N_IN_INTERESTS = 144;
	public static final int N_IN_DATAS = 145;
	public static final int N_OUT_INTERESTS = 146;
	public static final int N_OUT_DATAS = 147;
	public static final int N_IN_BYTES = 148;
	public static final int N_OUT_BYTES = 149;
	
	/**
	 * Decode a list of faces according to
	 * http://redmine.named-data.net/projects/nfd/wiki/FaceMgmt
	 * @param buffer
	 * @return
	 * @throws EncodingException 
	 */
	public List<FaceStatus> decodeFaces(ByteBuffer buffer) throws EncodingException{
		ArrayList<FaceStatus> faces = new ArrayList<>();
		TlvDecoder decoder = new TlvDecoder(buffer);
		int parentEndOffset;
		do{
			parentEndOffset = decoder.readNestedTlvsStart(FACE_STATUS);
			faces.add(decodeFaceStatus(decoder, parentEndOffset));
			decoder.finishNestedTlvs(parentEndOffset);
		}
		while(parentEndOffset < buffer.limit());
		return faces;
	}
	
	/**
	 * Decode one face status using the current decoder
	 * @param buffer
	 * @return
	 * @throws EncodingException 
	 */
	private static FaceStatus decodeFaceStatus(TlvDecoder decoder, int parentEndOffset) throws EncodingException{
		FaceStatus status = new FaceStatus();
		
		// parse
		status.faceId = (int) decoder.readNonNegativeIntegerTlv(FACE_ID);
		Blob uri = new Blob(decoder.readBlobTlv(URI), true); // copy because buffer is immutable
		status.uri = uri.toString();
		Blob localUri = new Blob(decoder.readBlobTlv(LOCAL_URI), true); // copy because buffer is immutable
		status.localUri = localUri.toString();
		status.expirationPeriod = (int) decoder.readOptionalNonNegativeIntegerTlv(EXPIRATION_PERIOD, parentEndOffset);
		status.faceScope = FaceScope.values()[(int) decoder.readNonNegativeIntegerTlv(FACE_SCOPE)];
		status.facePersistency = FacePersistency.values()[(int) decoder.readNonNegativeIntegerTlv(FACE_PERSISTENCY)];
		status.linkType = LinkType.values()[(int) decoder.readNonNegativeIntegerTlv(LINK_TYPE)];
		status.inInterests = (int) decoder.readNonNegativeIntegerTlv(N_IN_INTERESTS);
		status.inDatas = (int) decoder.readNonNegativeIntegerTlv(N_IN_DATAS);
		status.outInterests = (int) decoder.readNonNegativeIntegerTlv(N_OUT_INTERESTS);
		status.outDatas = (int) decoder.readNonNegativeIntegerTlv(N_OUT_DATAS);
		status.inBytes = (int) decoder.readNonNegativeIntegerTlv(N_IN_BYTES);
		status.outBytes = (int) decoder.readNonNegativeIntegerTlv(N_OUT_BYTES);
		
		return status;
	}

}
