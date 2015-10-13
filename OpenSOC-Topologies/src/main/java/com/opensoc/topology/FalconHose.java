package com.opensoc.topology;

import org.apache.commons.configuration.ConfigurationException;
import backtype.storm.generated.InvalidTopologyException;
import com.opensoc.topology.runner.FalconHoseRunner;
import com.opensoc.topology.runner.TopologyRunner;


/**
 * Topology for processing FalconHose events
 *
 */
public class FalconHose {

	public static void main(String[] args) throws ConfigurationException, Exception, InvalidTopologyException {
		
		TopologyRunner runner = new FalconHoseRunner();
		runner.initTopology(args, "falconhose");
	}
	
}
