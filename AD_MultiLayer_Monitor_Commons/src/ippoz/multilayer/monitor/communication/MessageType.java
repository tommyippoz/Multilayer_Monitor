/**
 * 
 */
package ippoz.multilayer.monitor.communication;

/**
 * @author Tommy
 *
 */
public enum MessageType {
	PING,
	OK,
	
	ADD_PROBE,
	CHECK_PROBE,
	START_PROBE,
	
	SETUP_SUT,
	START_EXPERIMENT,
	END_EXPERIMENT,
	
	START_SUT,
	SHUTDOWN_SUT,
	RESTART_SUT, 
	
	START_CAMPAIGN,
	END_CAMPAIGN
}
