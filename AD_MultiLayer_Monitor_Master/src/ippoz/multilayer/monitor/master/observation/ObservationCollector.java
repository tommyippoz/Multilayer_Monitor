/**
 * 
 */
package ippoz.multilayer.monitor.master.observation;

import ippoz.multilayer.monitor.master.database.DatabaseManager;

import java.util.TreeMap;

/**
 * @author Tommy
 *
 */
public class ObservationCollector {
	
	private DatabaseManager dbManager;
	private TreeMap<String, Observation> obsMap;
	private String runId;
	
	public ObservationCollector(DatabaseManager dbManager){
		this.dbManager = dbManager;
		obsMap = new TreeMap<String, Observation>();
	}

	public synchronized boolean addObservation(Observation obs) {
		if(obs != null && obs.size() > 0){
			//AppLogger.logInfo(getClass(), "Received Observation (" + obs.size() + " " + obs.getLayerType() + " indicators)");
			if(obsMap.containsKey(obs.getTimestamp())){
				obsMap.get(obs.getTimestamp()).mergeWith(obs);
			} else obsMap.put(obs.getTimestamp(), obs);
			return true;
		} else return false;
	}
	
	public void storeObservations(){
		int refLength = 0;
		Observation previous = null;
		for(String timestamp : obsMap.keySet()){
			if(obsMap.get(timestamp).getIndicators().length > refLength)
				refLength = obsMap.get(timestamp).getIndicators().length;
		}
		for(String timestamp : obsMap.keySet()){
			if(obsMap.get(timestamp).getIndicators().length == refLength){
				dbManager.storeObservation(obsMap.get(timestamp), previous, runId);
				previous = obsMap.get(timestamp);
			}
		}
	}

	public void setRunId(String runValue) {
		runId = runValue;
	}

	public void flush() {
		obsMap.clear();
	}

	public String getRunId() {
		return runId;
	}

}
