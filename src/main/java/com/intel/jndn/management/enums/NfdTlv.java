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

package com.intel.jndn.management.enums;

/**
 * NFD Management protocol TLV codes.
 *
 * @see <a href="https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt">Face Management</a>
 */
@SuppressWarnings("CheckStyle")
public final class NfdTlv {
  /**
   * Prevent creation of NfdTlv instances
   */
  private NfdTlv() {
  }

  // ControlParameters
  // https://redmine.named-data.net/projects/nfd/wiki/ControlCommand
  public static final int ControlParameters = 104;
  public static final int FaceId = 105;
  public static final int Uri = 114;
  public static final int LocalUri = 129;
  public static final int Origin = 111;
  public static final int Cost = 106;
  public static final int Capacity = 131;
  public static final int Count = 132;
  public static final int BaseCongestionMarkingInterval = 135;
  public static final int DefaultCongestionThreshold = 136;
  public static final int Mtu = 137;
  public static final int Flags = 108;
  public static final int Mask = 112;
  public static final int Strategy = 107;
  public static final int ExpirationPeriod = 109;
  public static final int ControlResponse = 101;
  public static final int StatusCode = 102;
  public static final int StatusText = 103;

  // ForwarderStatus
  // https://redmine.named-data.net/projects/nfd/wiki/ForwarderStatus
  public static final int NfdVersion = 128;
  public static final int StartTimestamp = 129;
  public static final int CurrentTimestamp = 130;
  public static final int NNameTreeEntries = 131;
  public static final int NFibEntries = 132;
  public static final int NPitEntries = 133;
  public static final int NMeasurementsEntries = 134;
  public static final int NCsEntries = 135;

  // Face Management
  // https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt
  public static final int FaceStatus = 128;
  public static final int ChannelStatus = 130;
  public static final int UriScheme = 131;
  public static final int FaceScope = 132;
  public static final int FacePersistency = 133;
  public static final int LinkType = 134;

  public static final int FaceQueryFilter = 150;
  public static final int FaceEventNotification = 192;
  public static final int FaceEventKind = 193;

  // ForwarderStatus and FaceStatus counters
  // https://redmine.named-data.net/projects/nfd/wiki/ForwarderStatus
  // https://redmine.named-data.net/projects/nfd/wiki/FaceMgmt
  public static final int NInInterests = 144;
  public static final int NInData = 145;
  public static final int NInNacks = 151;
  public static final int NOutInterests = 146;
  public static final int NOutData = 147;
  public static final int NOutNacks = 152;
  public static final int NInBytes = 148;
  public static final int NOutBytes = 149;

  // FIB Management
  // https://redmine.named-data.net/projects/nfd/wiki/FibMgmt
  public static final int FibEntry = 128;
  public static final int NextHopRecord = 129;

  // CS Management
  // https://redmine.named-data.net/projects/nfd/wiki/CsMgmt
  public static final int CsInfo = 128;
  public static final int NHits = 129;
  public static final int NMisses = 130;

  // Strategy Choice Management
  // https://redmine.named-data.net/projects/nfd/wiki/StrategyChoice
  public static final int StrategyChoice = 128;

  // RIB Management
  // https://redmine.named-data.net/projects/nfd/wiki/RibMgmt
  public static final int RibEntry = 128;
  public static final int Route = 129;
}
