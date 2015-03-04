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
package com.intel.jndn.management;

import com.intel.jndn.management.types.StatusDataset;
import com.intel.jndn.management.types.ControlResponse;
import com.intel.jndn.management.types.FaceStatus;
import com.intel.jndn.management.types.FibEntry;
import com.intel.jndn.management.types.RibEntry;
import com.intel.jndn.utils.Client;
import com.intel.jndn.utils.SegmentedClient;
import java.io.IOException;
import java.util.List;
import net.named_data.jndn.ControlParameters;
import net.named_data.jndn.Data;
import net.named_data.jndn.Face;
import net.named_data.jndn.ForwardingFlags;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.security.SecurityException;
import java.util.logging.Logger;

/**
 * Helper class for interacting with an NDN forwarder daemon; see
 * http://redmine.named-data.net/projects/nfd/wiki/Management for explanations
 * of the various protocols used.
 *
 * @author Andrew Brown <andrew.brown@intel.com>
 */
public class NFD {

  public final static long DEFAULT_TIMEOUT = 2000;
  static private final Logger logger = Logger.getLogger(NFD.class.getName());

  /**
   * Ping a forwarder on an existing face to verify that the forwarder is
   * working and responding to requests; this version sends a discovery packet
   * to /localhost/nfd which should always respond if the requestor is on the
   * same machine as the NDN forwarding daemon.
   *
   * @param face
   * @return true if successful, false otherwise
   */
  public static boolean pingLocal(Face face) {
    return ping(face, new Name("/localhost/nfd"));
  }

  /**
   * Request a name on an existing face to verify the forwarder is working and
   * responding to requests. Note that the name must be served or cached on the
   * forwarder for this to return true.
   *
   * @param face
   * @param name
   * @return true if successful, false otherwise
   */
  public static boolean ping(Face face, Name name) {
    // build interest
    Interest interest = new Interest(name);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);
    interest.setMustBeFresh(true);

    // send packet
    Data data = Client.getDefault().getSync(face, interest);
    return data != null;
  }

  /**
   * Retrieve a list of faces and their status from the given forwarder; calls
   * /localhost/nfd/faces/list which requires a local Face (all non-local
   * packets are dropped)
   *
   * @param forwarder Only a localhost Face
   * @return
   * @throws Exception
   */
  public static List<FaceStatus> getFaceList(Face forwarder) throws Exception {
    // build management Interest packet; see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
    Interest interest = new Interest(new Name("/localhost/nfd/faces/list"));
    interest.setMustBeFresh(true);
    interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);

    // send packet
    Data data = SegmentedClient.getDefault().getSync(forwarder, interest);
    if (data == null) {
      throw new Exception("Failed to retrieve list of faces from the forwarder.");
    }

    // parse packet
    return StatusDataset.wireDecode(data.getContent(), FaceStatus.class);
  }

  /**
   * Retrieve a list of FIB entries and their NextHopRecords from the given
   * forwarder; calls /localhost/nfd/fib/list which requires a local Face (all
   * non-local packets are dropped).
   *
   * @param forwarder Only a localhost Face
   * @return
   * @throws Exception
   */
  public static List<FibEntry> getFibList(Face forwarder) throws Exception {
    // build management Interest packet; see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
    Interest interest = new Interest(new Name("/localhost/nfd/fib/list"));
    interest.setMustBeFresh(true);
    interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);

    // TODO verify that all faces are being returned; right now they don't
    // match up with the results from nfd-status-http-server but no 
    // additional segments are present;  
    // see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
    // send packet
    Data data = SegmentedClient.getDefault().getSync(forwarder, interest);
    if (data == null) {
      throw new Exception("Failed to retrieve list of fib entries from the forwarder.");
    }

    // parse packet
    return StatusDataset.wireDecode(data.getContent(), FibEntry.class);
  }

  /**
   * Retrieve a list of routing entries from the RIB; calls
   * /localhost/nfd/rib/list which requires a local Face (all non-local packets
   * are dropped)
   *
   * @param forwarder Only a localhost Face
   * @return
   * @throws Exception
   */
  public static List<RibEntry> getRouteList(Face forwarder) throws Exception {
    // build management Interest packet; see http://redmine.named-data.net/projects/nfd/wiki/StatusDataset
    Interest interest = new Interest(new Name("/localhost/nfd/rib/list"));
    interest.setMustBeFresh(true);
    interest.setChildSelector(Interest.CHILD_SELECTOR_RIGHT);
    interest.setInterestLifetimeMilliseconds(DEFAULT_TIMEOUT);

    // send packet
    Data data = SegmentedClient.getDefault().getSync(forwarder, interest);
    if (data == null) {
      throw new Exception("Failed to retrieve list of faces from the forwarder.");
    }

    // parse packet
    return StatusDataset.wireDecode(data.getContent(), RibEntry.class);
  }

  /**
   * Helper method to register a new face on the forwarder; as mentioned at
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html, this is more for
   * debugging; use 'register' instead
   *
   * @param forwarder Only a localhost Face
   * @param faceId
   * @param prefix
   * @return
   * @throws Exception
   */
  public static boolean addNextHop(Face forwarder, int faceId, Name prefix) throws Exception {
    // build command name
    Name command = new Name("/localhost/nfd/fib/add-nexthop");
    ControlParameters parameters = new ControlParameters();
    parameters.setName(prefix);
    parameters.setFaceId(faceId);
    command.append(parameters.wireEncode());

    // send the interest
    return sendCommandAndErrorCheck(forwarder, new Interest(command));
  }

  /**
   * Create a new face on the given forwarder. Ensure the forwarding face is on
   * the local machine (management requests are to /localhost/...) and that
   * command signing has been set up (e.g. forwarder.setCommandSigningInfo()).
   *
   * @param forwarder Only a localhost Face
   * @param uri
   * @return
   * @throws java.lang.Exception
   */
  public static int createFace(Face forwarder, String uri) throws Exception {
    Name command = new Name("/localhost/nfd/faces/create");
    ControlParameters parameters = new ControlParameters();
    parameters.setUri(uri);
    command.append(parameters.wireEncode());

    // send the interest
    ControlResponse response = sendCommand(forwarder, new Interest(command));

    // check for body and that status code is OK (TODO: 200 should be replaced with a CONSTANT like ControlResponse.STATUS_OK)
    if (response.getBody().isEmpty() || response.getStatusCode() != 200) {
      throw new Exception("Failed to create face: " + uri + " " + response.getStatusText());
    }

    // return
    return response.getBody().get(0).getFaceId();
  }

  /**
   * Register a route on the forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/RibMgmt for
   * protocol documentation. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()).
   *
   * @param forwarder Only a localhost Face
   * @param controlParameters
   * @return
   * @throws Exception
   */
  public static boolean register(Face forwarder, ControlParameters controlParameters) throws Exception {
    // build command name
    Name command = new Name("/localhost/nfd/rib/register");
    command.append(controlParameters.wireEncode());

    // send the interest
    return sendCommandAndErrorCheck(forwarder, new Interest(command));
  }

  /**
   * Register a route on a forwarder; this will create a new face on the
   * forwarder to the given URI/route pair. See register(Face,
   * ControlParameters) for more details documentation.
   *
   * @param forwarder Only a localhost Face
   * @param uri
   * @param cost
   * @param route
   * @return true for successful registration
   * @throws java.lang.Exception
   */
  public static boolean register(Face forwarder, String uri, Name route, int cost) throws Exception {
    // create the new face
    int faceId = createFace(forwarder, uri);

    // run base method
    return register(forwarder, faceId, route, cost);
  }

  /**
   * Register a route on a forwarder; this will not create a new face since it
   * is provided a faceId. See register(Face, ControlParameters) for full
   * documentation
   *
   * @param forwarder Only a localhost Face
   * @param faceId
   * @param route
   * @param cost
   * @return true for successful registration
   * @throws java.lang.Exception
   */
  public static boolean register(Face forwarder, int faceId, Name route, int cost) throws Exception {
    // build command name
    ControlParameters parameters = new ControlParameters();
    parameters.setName(route);
    parameters.setFaceId(faceId);
    parameters.setCost(cost);
    ForwardingFlags flags = new ForwardingFlags();
    flags.setCapture(true);
    flags.setChildInherit(true);
    parameters.setForwardingFlags(flags);

    // run base method
    return register(forwarder, parameters);
  }

  /**
   * Unregister a route on a forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/RibMgmt for
   * protocol documentation. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()
   *
   * @param forwarder
   * @param controlParameters
   * @return
   */
  public static boolean unregister(Face forwarder, ControlParameters controlParameters) throws Exception {
    // build command name
    Name command = new Name("/localhost/nfd/rib/unregister");
    command.append(controlParameters.wireEncode());

    // send the interest
    return sendCommandAndErrorCheck(forwarder, new Interest(command));
  }

  /**
   * Unregister a route on a forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/RibMgmt for
   * protocol documentation. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()
   *
   * @param forwarder
   * @param route
   * @return
   */
  public static boolean unregister(Face forwarder, Name route) throws Exception {
    // build command name
    ControlParameters controlParameters = new ControlParameters();
    controlParameters.setName(route);

    // send the interest
    return unregister(forwarder, controlParameters);
  }

  /**
   * Unregister a route on a forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/RibMgmt for
   * protocol documentation. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()
   *
   * @param forwarder
   * @param route
   * @param faceId
   * @return
   */
  public static boolean unregister(Face forwarder, Name route, int faceId) throws Exception {
    // build command name
    ControlParameters controlParameters = new ControlParameters();
    controlParameters.setName(route);
    controlParameters.setFaceId(faceId);

    // send the interest
    return unregister(forwarder, controlParameters);
  }

  /**
   * Unregister a route on a forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/RibMgmt for
   * protocol documentation. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()
   *
   * @param forwarder
   * @param route
   * @param uri
   * @return
   */
  public static boolean unregister(Face forwarder, Name route, String uri) throws Exception {
    int faceId = -1;
    for(FaceStatus face : getFaceList(forwarder)){
      if(face.getUri().matches(uri)){
        faceId = face.getFaceId();
        break;
      }
    }
    
    if(faceId == -1){
      throw new Exception("Face not found: " + uri);
    }

    // send the interest
    return unregister(forwarder, route, faceId);
  }

  /**
   * Set a strategy on the forwarder; see
   * http://named-data.net/doc/NFD/current/manpages/nfdc.html for command-line
   * usage and http://redmine.named-data.net/projects/nfd/wiki/StrategyChoice
   * for protocol documentation. Ensure the forwarding face is on the local
   * machine (management requests are to /localhost/...) and that command
   * signing has been set up (e.g. forwarder.setCommandSigningInfo()).
   *
   * @param forwarder Only a localhost Face
   * @param prefix
   * @param strategy
   * @return true for successful command
   * @throws Exception
   */
  public static boolean setStrategy(Face forwarder, Name prefix, Name strategy) throws Exception {
    // build command name
    Name command = new Name("/localhost/nfd/strategy-choice/set");
    ControlParameters parameters = new ControlParameters();
    parameters.setName(prefix);
    parameters.setStrategy(strategy);
    command.append(parameters.wireEncode());

    // send the interest
    return sendCommandAndErrorCheck(forwarder, new Interest(command));
  }

  /**
   * Send an interest as a command to the forwarder; this method will convert
   * the interest to a command interest and block until a response is received
   * from the forwarder. Ensure the forwarding face is on the local machine
   * (management requests are to /localhost/...) and that command signing has
   * been set up (e.g. forwarder.setCommandSigningInfo()).
   *
   * @param forwarder Only a localhost Face
   * @param interest As described at
   * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand, the
   * requested interest must have encoded ControlParameters appended to the
   * interest name
   * @return
   * @throws net.named_data.jndn.security.SecurityException
   * @throws java.io.IOException
   * @throws net.named_data.jndn.encoding.EncodingException
   */
  public static ControlResponse sendCommand(Face forwarder, Interest interest) throws SecurityException, IOException, EncodingException {
    forwarder.makeCommandInterest(interest);

    // send command packet
    Data data = Client.getDefault().getSync(forwarder, interest);
    if (data == null) {
      throw new IOException("Failed to receive command response.");
    }

    // return response
    ControlResponse response = new ControlResponse();
    response.wireDecode(data.getContent().buf());
    return response;
  }

  /**
   * Send an interest as a command to the forwarder; this method will convert
   * the interest to a command interest and block until a response is received
   * from the forwarder.
   *
   * @param forwarder Only a localhost Face
   * @param interest As described at
   * http://redmine.named-data.net/projects/nfd/wiki/ControlCommand, the
   * requested interest must have encoded ControlParameters appended to the
   * interest name
   * @return
   * @throws net.named_data.jndn.security.SecurityException
   * @throws java.io.IOException
   * @throws net.named_data.jndn.encoding.EncodingException
   */
  public static boolean sendCommandAndErrorCheck(Face forwarder, Interest interest) throws SecurityException, IOException, EncodingException {
    ControlResponse response = sendCommand(forwarder, interest);
    if (response.getStatusCode() < 400) {
      return true;
    } else {
      logger.warning("Command sent but failed: " + response.getStatusCode() + " " + response.getStatusText());
      return false;
    }
  }
}
