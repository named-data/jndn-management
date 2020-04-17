# jndn-management

This project consists of tools for managing an NDN forwarding daemon (NFD). It relies on the [NDN Protocol](https://named-data.net) and its associated [client library](https://github.com/named-data/jndn). It implements the NFD management specification outlined at https://redmine.named-data.net/projects/nfd/wiki/Management. Please note that this library does not yet implement all of the commands in the management specification.

## Use

With a Face that has command signing information set, call any of the following static methods:
 - __Nfdc.pingLocal(Face forwarder)__: ping a local NFD with /localhost/nfd to verify if it exists.
 - __Nfdc.getForwarderStatus(Face forwarder)__: retrieve the forwarder status information.
 - __Nfdc.getFaceList(Face forwarder)__: retrieve all connected faces.
 - __Nfdc.getFibList(Face forwarder)__: retrieve all forwarding entries in the Forwarding Information Base (FIB).
 - __Nfdc.getRibList(Face forwarder)__: retrieve all routing entries in the Routing Information Base (RIB).
 - __Nfdc.getChannelStatusList(Face forwarder)__: Retrieve the list of channel status entries from the NFD.
 - __Nfdc.createFace(Face forwarder, String uri)__: create a new face on the NFD opened to the given URI.
 - __Nfdc.register(Face forwarder, ...)__: includes several similar methods for registering a new route on the NFD.
 - __Nfdc.unregister(Face forwarder, Name route)__: unregister a route by name.
 - __Nfdc.setStrategy(Face forwarder, Name prefix, Name strategy)__: set the forwarding strategy for the given prefix.

See [full Javadoc documentation](http://01org.github.io/jndn-management/index.html)

## License

Copyright © 2015, Intel Corporation.

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License, version 3, as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the [GNU Lesser General Public License](LICENSE.md) for more details.
