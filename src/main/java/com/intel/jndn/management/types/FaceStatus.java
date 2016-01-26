/*
 * jndn-management
 * Copyright (c) 2015, Intel Corporation.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms and conditions of the GNU Lesser General Public License,
 * version 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 */
package com.intel.jndn.management.types;

import java.nio.ByteBuffer;
import com.intel.jndn.management.enums.NfdTlv;
import com.intel.jndn.management.enums.FacePersistency;
import com.intel.jndn.management.enums.FaceScope;
import com.intel.jndn.management.enums.LinkType;
import com.intel.jndn.management.helpers.EncodingHelper;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

/**
 * Represent a FaceStatus object from /localhost/nfd/faces/list
 * @see <a href="http://redmine.named-data.net/projects/nfd/wiki/FaceMgmt">Face Management</a>
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class FaceStatus implements Decodable {
  private int faceId = 0;
  private String remoteUri = "";
  private String localUri = "";
  private FaceScope faceScope = FaceScope.LOCAL;
  private FacePersistency facePersistency = FacePersistency.PERSISTENT;
  private LinkType linkType = LinkType.POINT_TO_POINT;

  private int expirationPeriod = 0;
  private int inInterests = 0;
  private int inDatas = 0;
  private int inNacks = 0;

  private int outInterests = 0;
  private int outDatas = 0;
  private int outNacks = 0;

  private int inBytes = 0;
  private int outBytes = 0;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor
   */
  public FaceStatus() {
    // nothing to do
  }

  /**
   * Constructor from wire format
   * @param input wire format
   * @throws EncodingException
   */
  public FaceStatus(ByteBuffer input) throws EncodingException {
    wireDecode(input);
  }

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
   * Encode as part of an existing encode context
   */
  public final void wireEncode(TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutBytes, outBytes);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInBytes, inBytes);

    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutNacks, outNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutDatas, outDatas);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutInterests, outInterests);

    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInNacks, inNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInDatas, inDatas);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInInterests, inInterests);

    encoder.writeNonNegativeIntegerTlv(NfdTlv.LinkType, linkType.toInteger());
    encoder.writeNonNegativeIntegerTlv(NfdTlv.FacePersistency, facePersistency.toInteger());
    encoder.writeNonNegativeIntegerTlv(NfdTlv.FaceScope, faceScope.toInteger());

    encoder.writeOptionalNonNegativeIntegerTlv(NfdTlv.ExpirationPeriod, expirationPeriod);
    encoder.writeBlobTlv(NfdTlv.LocalUri, new Blob(localUri).buf());
    encoder.writeBlobTlv(NfdTlv.Uri, new Blob(remoteUri).buf());

    encoder.writeNonNegativeIntegerTlv(NfdTlv.FaceId, faceId);
    encoder.writeTypeAndLength(NfdTlv.FaceStatus, encoder.getLength() - saveLength);
  }

  /**
   * Decode the input from its TLV format.
   *
   * @param input The input buffer to decode. This reads from position() to
   *              limit(), but does not change the position.
   * @throws net.named_data.jndn.encoding.EncodingException
   */
  public final void wireDecode(ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * Decode as part of an existing decode context
   */
  @Override
  public void wireDecode(TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.FaceStatus);
    // parse
    this.faceId = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.FaceId);

    this.remoteUri = EncodingHelper.toString(decoder.readBlobTlv(NfdTlv.Uri));
    this.localUri = EncodingHelper.toString(decoder.readBlobTlv(NfdTlv.LocalUri));

    this.expirationPeriod = (int) decoder.readOptionalNonNegativeIntegerTlv(NfdTlv.ExpirationPeriod, endOffset);
    this.faceScope = FaceScope.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.FaceScope));
    this.facePersistency = FacePersistency.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.FacePersistency));
    this.linkType = LinkType.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.LinkType));

    this.inInterests = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInInterests);
    this.inDatas = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInDatas);
    this.inNacks = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInNacks);

    this.outInterests = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutInterests);
    this.outDatas = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutDatas);
    this.outNacks = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutNacks);

    this.inBytes = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInBytes);
    this.outBytes = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutBytes);

    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * Get face ID
   */
  public int getFaceId() {
    return faceId;
  }

  /**
   * Set face ID
   */
  public FaceStatus setFaceId(int faceId) {
    this.faceId = faceId;
    return this;
  }

  /**
   * Get face ID
   */
  public String getRemoteUri() {
    return remoteUri;
  }

  /**
   * Set URI
   */
  public FaceStatus setRemoteUri(String uri) {
    this.remoteUri = uri;
    return this;
  }

  /**
   * Get face ID
   */
  public String getLocalUri() {
    return localUri;
  }

  /**
   * Set local URI
   */
  public FaceStatus setLocalUri(String localUri) {
    this.localUri = localUri;
    return this;
  }

  /**
   * Check if Face has expiration period set
   */
  public boolean hasExpirationPeriod() {
    return expirationPeriod > 0;
  }

  /**
   * Get expiration period
   */
  public int getExpirationPeriod() {
    return expirationPeriod;
  }

  /**
   * Set expiration period
   */
  public FaceStatus setExpirationPeriod(int expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
    return this;
  }

  /**
   * Get face scope value
   */
  public FaceScope getFaceScope() {
    return faceScope;
  }

  /**
   * Set face scope value
   */
  public FaceStatus setFaceScope(FaceScope faceScope) {
    this.faceScope = faceScope;
    return this;
  }

  /**
   * Get face persistency value
   */
  public FacePersistency getFacePersistency() {
    return facePersistency;
  }

  /**
   * Set face persistency value
   */
  public FaceStatus setFacePersistency(FacePersistency facePersistency) {
    this.facePersistency = facePersistency;
    return this;
  }

  /**
   * Get link type
   */
  public LinkType getLinkType() {
    return linkType;
  }

  /**
   * Set link type
   */
  public FaceStatus setLinkType(LinkType linkType) {
    this.linkType = linkType;
    return this;
  }

  /**
   * Get number of received Interest packets
   */
  public int getNInInterests() {
    return inInterests;
  }

  /**
   * Set number of received Interest packets
   */
  public FaceStatus setNInInterests(int inInterests) {
    this.inInterests = inInterests;
    return this;
  }

  /**
   * Get number of sent Interest packets
   */
  public int getNOutInterests() {
    return outInterests;
  }

  /**
   * Set number of sent Interest packets
   */
  public FaceStatus setNOutInterests(int outInterests) {
    this.outInterests = outInterests;
    return this;
  }

  /**
   * Get number of received Data packets
   */
  public int getNInDatas() {
    return inDatas;
  }

  /**
   * Set number of received Data packets
   */
  public FaceStatus setNInDatas(int inDatas) {
    this.inDatas = inDatas;
    return this;
  }

  /**
   * Get number of sent Data packets
   */
  public int getNOutDatas() {
    return outDatas;
  }

  /**
   * Set number of sent Data packets
   */
  public FaceStatus setNOutDatas(int outDatas) {
    this.outDatas = outDatas;
    return this;
  }

  /**
   * Get number of received Data packets
   */
  public int getNInNacks() {
    return inNacks;
  }

  /**
   * Set number of received Data packets
   */
  public FaceStatus setNInNacks(int inNacks) {
    this.inNacks = inNacks;
    return this;
  }

  /**
   * Get number of sent Data packets
   */
  public int getNOutNacks() {
    return outNacks;
  }

  /**
   * Set number of sent Data packets
   */
  public FaceStatus setNOutNacks(int outNacks) {
    this.outNacks = outNacks;
    return this;
  }


  /**
   * Get number of input bytes
   */
  public int getNInBytes() {
    return inBytes;
  }

  /**
   * Set number of input bytes
   */
  public FaceStatus setNInBytes(int inBytes) {
    this.inBytes = inBytes;
    return this;
  }

  /**
   * Get number of output bytes
   */
  public int getNOutBytes() {
    return outBytes;
  }

  /**
   * Set number of output bytes
   */
  public FaceStatus setNOutBytes(int outBytes) {
    this.outBytes = outBytes;
    return this;
  }
}
