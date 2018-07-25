/*
 * jndn-management
 * Copyright (c) 2015-2018, Intel Corporation.
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

import com.intel.jndn.management.enums.FacePersistency;
import com.intel.jndn.management.enums.FaceScope;
import com.intel.jndn.management.enums.LinkType;
import com.intel.jndn.management.enums.NfdTlv;
import com.intel.jndn.management.helpers.EncodingHelper;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.encoding.tlv.TlvEncoder;
import net.named_data.jndn.util.Blob;

import java.nio.ByteBuffer;

/**
 * Represent a FaceStatus object from /localhost/nfd/faces/list.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt">Face Management</a>
 */
public class FaceStatus implements Decodable {
  private int faceId = 0;
  private String remoteUri = "";
  private String localUri = "";
  private int expirationPeriod = 0;
  private FaceScope faceScope = FaceScope.LOCAL;
  private FacePersistency facePersistency = FacePersistency.PERSISTENT;
  private LinkType linkType = LinkType.POINT_TO_POINT;

  private int baseCongestionMarkingInterval = -1;
  private int defaultCongestionThreshold = -1;
  private int mtu = -1;
  private int flags = 0;

  private int inInterests = 0;
  private int inData = 0;
  private int inNacks = 0;

  private int outInterests = 0;
  private int outData = 0;
  private int outNacks = 0;

  private int inBytes = 0;
  private int outBytes = 0;

  /////////////////////////////////////////////////////////////////////////////

  /**
   * Default constructor.
   */
  public FaceStatus() {
    // nothing to do
  }

  /**
   * Constructor from wire format.
   *
   * @param input wire format
   * @throws EncodingException when decoding fails
   */
  public FaceStatus(final ByteBuffer input) throws EncodingException {
    wireDecode(input);
  }

  /**
   * Encode using a new TLV encoder.
   *
   * @return The encoded buffer
   */
  public final Blob wireEncode() {
    TlvEncoder encoder = new TlvEncoder();
    wireEncode(encoder);
    return new Blob(encoder.getOutput(), false);
  }

  /**
   * Encode as part of an existing encode context.
   *
   * @param encoder TlvEncoder instance
   */
  public final void wireEncode(final TlvEncoder encoder) {
    int saveLength = encoder.getLength();
    encoder.writeNonNegativeIntegerTlv(NfdTlv.Flags, flags);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutBytes, outBytes);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInBytes, inBytes);

    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutNacks, outNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutData, outData);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NOutInterests, outInterests);

    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInNacks, inNacks);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInData, inData);
    encoder.writeNonNegativeIntegerTlv(NfdTlv.NInInterests, inInterests);

    encoder.writeOptionalNonNegativeIntegerTlv(NfdTlv.Mtu, mtu);
    encoder.writeOptionalNonNegativeIntegerTlv(NfdTlv.DefaultCongestionThreshold, defaultCongestionThreshold);
    encoder.writeOptionalNonNegativeIntegerTlv(NfdTlv.BaseCongestionMarkingInterval, baseCongestionMarkingInterval);

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
   * @throws EncodingException when decoding fails
   */
  public final void wireDecode(final ByteBuffer input) throws EncodingException {
    TlvDecoder decoder = new TlvDecoder(input);
    wireDecode(decoder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wireDecode(final TlvDecoder decoder) throws EncodingException {
    int endOffset = decoder.readNestedTlvsStart(NfdTlv.FaceStatus);
    // parse
    this.faceId = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.FaceId);

    this.remoteUri = EncodingHelper.toString(decoder.readBlobTlv(NfdTlv.Uri));
    this.localUri = EncodingHelper.toString(decoder.readBlobTlv(NfdTlv.LocalUri));

    this.expirationPeriod = (int) decoder.readOptionalNonNegativeIntegerTlv(NfdTlv.ExpirationPeriod, endOffset);
    this.faceScope = FaceScope.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.FaceScope));
    this.facePersistency = FacePersistency.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.FacePersistency));
    this.linkType = LinkType.fromInteger((int) decoder.readNonNegativeIntegerTlv(NfdTlv.LinkType));

    this.baseCongestionMarkingInterval =
      (int) decoder.readOptionalNonNegativeIntegerTlv(NfdTlv.BaseCongestionMarkingInterval, endOffset);
    this.defaultCongestionThreshold = (int) decoder.readOptionalNonNegativeIntegerTlv(
      NfdTlv.DefaultCongestionThreshold, endOffset);
    this.mtu = (int) decoder.readOptionalNonNegativeIntegerTlv(NfdTlv.Mtu, endOffset);

    this.inInterests = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInInterests);
    this.inData = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInData);
    this.inNacks = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInNacks);

    this.outInterests = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutInterests);
    this.outData = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutData);
    this.outNacks = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutNacks);

    this.inBytes = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NInBytes);
    this.outBytes = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.NOutBytes);

    this.flags = (int) decoder.readNonNegativeIntegerTlv(NfdTlv.Flags);

    decoder.finishNestedTlvs(endOffset);
  }

  /**
   * @return face ID
   */
  public int getFaceId() {
    return faceId;
  }

  /**
   * Set face ID.
   *
   * @param faceId face ID
   * @return this
   */
  public FaceStatus setFaceId(final int faceId) {
    this.faceId = faceId;
    return this;
  }

  /**
   * @return remote face URI
   */
  public String getRemoteUri() {
    return remoteUri;
  }

  /**
   * Set remote face URI.
   *
   * @param uri remote face URI
   * @return this
   */
  public FaceStatus setRemoteUri(final String uri) {
    this.remoteUri = uri;
    return this;
  }

  /**
   * @return remote face URI
   */
  public String getLocalUri() {
    return localUri;
  }

  /**
   * Set local face URI.
   *
   * @param localUri local face URI
   * @return this
   */
  public FaceStatus setLocalUri(final String localUri) {
    this.localUri = localUri;
    return this;
  }

  /**
   * Check if Face has expiration period set.
   * @return true if Face has expiration period set, false otherwise
   */
  public boolean hasExpirationPeriod() {
    return expirationPeriod > 0;
  }

  /**
   * @return expiration period
   */
  public int getExpirationPeriod() {
    return expirationPeriod;
  }

  /**
   * Set expiration period.
   *
   * @param expirationPeriod expiration period
   * @return this
   */
  public FaceStatus setExpirationPeriod(final int expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
    return this;
  }

  /**
   * @return face scope
   */
  public FaceScope getFaceScope() {
    return faceScope;
  }

  /**
   * Set face scope value.
   *
   * @param faceScope face scope
   * @return this
   */
  public FaceStatus setFaceScope(final FaceScope faceScope) {
    this.faceScope = faceScope;
    return this;
  }

  /**
   * @return face persistency
   */
  public FacePersistency getFacePersistency() {
    return facePersistency;
  }

  /**
   * Set face persistency.
   *
   * @param facePersistency face persistency
   * @return this
   */
  public FaceStatus setFacePersistency(final FacePersistency facePersistency) {
    this.facePersistency = facePersistency;
    return this;
  }

  /**
   * @return link type
   */
  public LinkType getLinkType() {
    return linkType;
  }

  /**
   * Set link type.
   *
   * @param linkType link type
   * @return this
   */
  public FaceStatus setLinkType(final LinkType linkType) {
    this.linkType = linkType;
    return this;
  }

  /**
   * Check if Face has BaseCongestionMarkingInterval set.
   * @return true if Face has BaseCongestionMarkingInterval set, false otherwise
   */
  public boolean hasBaseCongestionMarkingInterval() {
    return baseCongestionMarkingInterval >= 0;
  }

  /**
   * @return BaseCongestionMarkingInterval
   */
  public int getBaseCongestionMarkingInterval() {
    return baseCongestionMarkingInterval;
  }

  /**
   * @param baseCongestionMarkingInterval BaseCongestionMarkingInterval
   * @return this
   */
  public FaceStatus setBaseCongestionMarkingInterval(final int baseCongestionMarkingInterval) {
    this.baseCongestionMarkingInterval = baseCongestionMarkingInterval;
    return this;
  }

  /**
   * Check if Face has DefaultCongestionThreshold set.
   * @return true if Face has DefaultCongestionThreshold set, false otherwise
   */
  public boolean hasDefaultCongestionThreshold() {
    return defaultCongestionThreshold >= 0;
  }

  /**
   * @return DefaultCongestionThreshold
   */
  public int getDefaultCongestionThreshold() {
    return defaultCongestionThreshold;
  }

  /**
   * @param defaultCongestionThreshold DefaultCongestionThreshold
   * @return this
   */
  public FaceStatus setDefaultCongestionThreshold(final int defaultCongestionThreshold) {
    this.defaultCongestionThreshold = defaultCongestionThreshold;
    return this;
  }

  /**
   * Check if Face has MTU set.
   * @return true if Face has Mtu set, false otherwise
   */
  public boolean hasMtu() {
    return mtu >= 0;
  }

  /**
   * @return MTU
   */
  public int getMtu() {
    return mtu;
  }

  /**
   * @param mtu Face MTU
   * @return this
   */
  public FaceStatus setMtu(final int mtu) {
    this.mtu = mtu;
    return this;
  }

  /**
   * @return Face flags
   */
  public int getFlags() {
    return flags;
  }

  /**
   * @param flags Face flags
   * @return this
   */
  public FaceStatus setFlags(final int flags) {
    this.flags = flags;
    return this;
  }

  /**
   * @return number of received Interest packets
   */
  public int getNInInterests() {
    return inInterests;
  }

  /**
   * Set number of received Interest packets.
   *
   * @param inInterests number of received Interest packets
   * @return this
   */
  public FaceStatus setNInInterests(final int inInterests) {
    this.inInterests = inInterests;
    return this;
  }

  /**
   * @return number of sent Interest packets
   */
  public int getNOutInterests() {
    return outInterests;
  }

  /**
   * Set number of sent Interest packets.
   *
   * @param outInterests number of sent Interest packets
   * @return this
   */
  public FaceStatus setNOutInterests(final int outInterests) {
    this.outInterests = outInterests;
    return this;
  }

  /**
   * @return number of received Data packets
   */
  public int getNInData() {
    return inData;
  }

  /**
   * Set number of received Data packets.
   *
   * @param inData number of received Data packets
   * @return this
   */
  public FaceStatus setNInData(final int inData) {
    this.inData = inData;
    return this;
  }

  /**
   * @return number of sent Data packets
   */
  public int getNOutData() {
    return outData;
  }

  /**
   * Set number of sent Data packets.
   *
   * @param outData number of sent Data packets
   * @return this
   */
  public FaceStatus setNOutData(final int outData) {
    this.outData = outData;
    return this;
  }

  /**
   * @return number of received Data packets
   */
  public int getNInNacks() {
    return inNacks;
  }

  /**
   * Set number of received Data packets.
   *
   * @param inNacks number of received Data packets
   * @return this
   */
  public FaceStatus setNInNacks(final int inNacks) {
    this.inNacks = inNacks;
    return this;
  }

  /**
   * @return number of sent Data packets
   */
  public int getNOutNacks() {
    return outNacks;
  }

  /**
   * Set number of sent Data packets.
   *
   * @param outNacks number of sent Data packets
   * @return this
   */
  public FaceStatus setNOutNacks(final int outNacks) {
    this.outNacks = outNacks;
    return this;
  }

  /**
   * @return number of input bytes
   */
  public int getNInBytes() {
    return inBytes;
  }

  /**
   * Set number of input bytes.
   *
   * @param inBytes number of input bytes
   * @return this
   */
  public FaceStatus setNInBytes(final int inBytes) {
    this.inBytes = inBytes;
    return this;
  }

  /**
   * @return number of output bytes
   */
  public int getNOutBytes() {
    return outBytes;
  }

  /**
   * Set number of output bytes.
   *
   * @param outBytes number of output bytes
   * @return this
   */
  public FaceStatus setNOutBytes(final int outBytes) {
    this.outBytes = outBytes;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder os = new StringBuilder();

    os.append("Face(FaceId: ").append(this.getFaceId()).append(",\n")
      .append("     RemoteUri: ").append(this.getRemoteUri()).append(",\n")
      .append("     LocalUri: ").append(this.getLocalUri()).append(",\n");

    if (this.hasExpirationPeriod()) {
      os.append("     ExpirationPeriod: ").append(this.getExpirationPeriod()).append(" milliseconds,\n");
    } else {
      os.append("     ExpirationPeriod: infinite,\n");
    }

    os.append("     FaceScope: ").append(this.getFaceScope().toString()).append(",\n")
      .append("     FacePersistency: ").append(this.getFacePersistency().toString()).append(",\n")
      .append("     LinkType: ").append(this.getLinkType().toString()).append(",\n");

    if (this.hasBaseCongestionMarkingInterval()) {
      os.append("     BaseCongestionMarkingInterval: ")
        .append(this.getBaseCongestionMarkingInterval())
        .append(" nanoseconds,\n");
    }

    if (this.hasDefaultCongestionThreshold()) {
      os.append("     DefaultCongestionThreshold: ").append(this.getDefaultCongestionThreshold()).append(" bytes,\n");
    }

    if (this.hasMtu()) {
      os.append("     Mtu: ").append(this.getMtu()).append(" bytes,\n");
    }

    os.append("     Flags: ").append(String.format("0x%x", this.getFlags())).append(",\n")
      .append("     Counters: {Interests: {in: ").append(this.getNInInterests()).append(", ")
      .append("out: ").append(this.getNOutInterests()).append("},\n")
      .append("                Data: {in: ").append(this.getNInData()).append(", ")
      .append("out: ").append(this.getNOutData()).append("},\n")
      .append("                Nacks: {in: ").append(this.getNInNacks()).append(", ")
      .append("out: ").append(this.getNOutNacks()).append("},\n")
      .append("                bytes: {in: ").append(this.getNInBytes()).append(", ")
      .append("out: ").append(this.getNOutBytes()).append("}}\n");

    os.append("     )");
    return os.toString();
  }
}
