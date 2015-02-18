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
package com.intel.jndn.management.types;

import com.intel.jndn.management.EncodingHelper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.named_data.jndn.ControlParameters;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a ControlResponse, a Data packet sent in response to a
 * ControlCommand to the NFD, see
 * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class ControlResponse {

  /**
   * Use TLV codes from jndn.encoding.tlv.Tlv.java See
   * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand
   */
  public final static int TLV_CONTROL_RESPONSE = 101;
  public final static int TLV_CONTROL_RESPONSE_STATUS_CODE = 102;
  public final static int TLV_CONTROL_RESPONSE_STATUS_TEXT = 103;

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
    for (ControlParameters parameters : body) {
      EncodingHelper.encodeControlParameters(parameters, encoder);
    }
    encoder.writeBlobTlv(TLV_CONTROL_RESPONSE_STATUS_TEXT, new Blob(statusText).buf());
    encoder.writeNonNegativeIntegerTlv(TLV_CONTROL_RESPONSE_STATUS_CODE, statusCode);
    encoder.writeTypeAndLength(TLV_CONTROL_RESPONSE, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   * limit(), but does not change the position.
   * @throws net.named_data.jndn.encoding.EncodingException
   */
  public final void wireDecode(ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder, input);
  }

  /**
   * Decode as part of an existing decode context.
   *
   * @param decoder
   * @param input the WireFormat version that decodes ControlParameters does not
   * allow passing a TlvDecoder, so we must pass the buffer itself
   * @throws EncodingException
   */
  public void wireDecode(TlvDecoder decoder, ByteBuffer input) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(TLV_CONTROL_RESPONSE);

    // parse known TLVs
    this.statusCode = (int) decoder.readNonNegativeIntegerTlv(TLV_CONTROL_RESPONSE_STATUS_CODE);
    Blob statusText_ = new Blob(decoder.readBlobTlv(TLV_CONTROL_RESPONSE_STATUS_TEXT), true); // copy because buffer is immutable
    this.statusText = statusText_.toString();

    // use the already-written decoder for ControlParameters (but we have to copy the buffer)
    while (decoder.peekType(Tlv.ControlParameters_ControlParameters, endOffset)) {
      ByteBuffer copyInput = input.duplicate();
      copyInput.position(decoder.getOffset());
      int internalEndOffset = decoder.readNestedTlvsStart(Tlv.ControlParameters_ControlParameters);
      ControlParameters copyParameters = new ControlParameters();
      copyParameters.wireDecode(copyInput);
      this.body.add(copyParameters);
      decoder.seek(internalEndOffset);
      decoder.finishNestedTlvs(internalEndOffset);
    }

    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * Get status code
   *
   * @return
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Set status code
   *
   * @param statusCode
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Get status text
   *
   * @return
   */
  public String getStatusText() {
    return statusText;
  }

  /**
   * Set status text
   *
   * @param statusText
   */
  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  /**
   * Get body
   *
   * @return
   */
  public List<ControlParameters> getBody() {
    return body;
  }

  /**
   * Set body
   *
   * @param body
   */
  public void setBody(List<ControlParameters> body) {
    this.body = body;
  }

  private int statusCode = -1;
  private String statusText = "";
  private List<ControlParameters> body = new ArrayList<>();
}
