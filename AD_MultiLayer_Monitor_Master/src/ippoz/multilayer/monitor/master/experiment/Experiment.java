/**
 * 
 */
package ippoz.multilayer.monitor.master.experiment;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ippoz.multilayer.monitor.communication.CommunicationManager;
import ippoz.multilayer.monitor.communication.MessageType;
import ippoz.multilayer.monitor.layers.LayerType;
import ippoz.multilayer.monitor.master.database.DatabaseManager;
import ippoz.multilayer.monitor.master.observation.ObservationCollector;
import ippoz.multilayer.monitor.master.performance.ExperimentTiming;
import ippoz.multilayer.monitor.master.probes.receiver.JSONSocketProbeReceiver;
import ippoz.multilayer.monitor.master.probes.receiver.ProbeReceiver;
import ippoz.multilayer.monitor.master.probes.receiver.ProbeReceiverManager;
import ippoz.multilayer.monitor.master.services.Service;
import ippoz.multilayer.monitor.master.workload.Workload;
import ippoz.multilayer.monitor.support.AppLogger;


/**
 * @author Tommy
 *
 */
public class Experiment {
	
	private Workload workload;
	private ExperimentType expType;
	protected DatabaseManager dbManager;
	private int iterations;
	private int minObs;
	private int maxObs;
	private HashMap<Failure, Long> injections;
	
	public Experiment(Workload workload, ExperimentType expType, DatabaseManager dbManager, int iterations, HashMap<Failure, Long> injections, int minObs, int maxObs) {
		this.workload = workload;
		this.expType = expType;
		this.dbManager = dbManager;
		this.iterations = iterations;
		this.injections = injections;
		this.minObs = minObs;
		this.maxObs = maxObs;
		checkDbInfo();
	}

	private void checkDbInfo() {
		if(workload != null) {
			for(Service service : workload.usedServices()){
				dbManager.hasService(service, true);
			}
		}
	}

	public Workload getWorkload() {
		return workload;
	}

	public ExperimentType getExpType() {
		return expType;
	}
	
	public int getIterations(){
		return iterations;
	}
	
	public boolean canExecute(){
		for(Service service : workload.usedServices()){
			if(service.needTest(dbManager))
				return false;
		}
		return true;
	}
	
	public LinkedList<Experiment> getNeededTests(LinkedList<Workload> availableWorkloads, int testIterations, int testMinObs, int testMaxObs){
		LinkedList<Experiment> tests = new LinkedList<Experiment>();
		for(Service service : workload.usedServices()){
			if(service.needTest(dbManager)) {
				tests.add(new ServiceTestExperiment(service, availableWorkloads, dbManager, testIterations, testMinObs, testMaxObs));
				if(tests.getLast().getWorkload() == null)
					tests.removeLast();
			}
		}
		return tests;
	}
	
	private void setupProbes(ObservationCollector obCollector, ProbeReceiverManager prManager, CommunicationManager cManager) throws IOException {
		addProbe(cManager, prManager, new JSONSocketProbeReceiver("JMXReceiver", obCollector, LayerType.JVM, 9858, true), LayerType.JVM, 9858);
		addProbe(cManager, prManager, new JSONSocketProbeReceiver("CentOSReceiver", obCollector, LayerType.CENTOS, 9859, true), LayerType.CENTOS, 9859);
		addProbe(cManager, prManager, new JSONSocketProbeReceiver("UnixNetworkReceiver", obCollector, LayerType.UNIX_NETWORK, 9860, true), LayerType.UNIX_NETWORK, 9860);
	}
	
	private void addProbe(CommunicationManager cManager, ProbeReceiverManager prManager, ProbeReceiver receiver, LayerType probeLayer, int port) throws IOException {
		prManager.addReceiver(receiver);
		cManager.send(new Object[]{MessageType.ADD_PROBE, probeLayer, port});
		cManager.waitForConfirm();
	}
	
	public void executeExperiment(CommunicationManager cManager){
		ObservationCollector obCollector;
		ProbeReceiverManager prManager;
		Workload baseWorkload;
		try {
			for(int expRun=1; expRun<=iterations; expRun++) {
				do {
					baseWorkload = workload.cloneWorkload();
					obCollector = new ObservationCollector(dbManager, minObs, maxObs);
					prManager = new ProbeReceiverManager();
					AppLogger.logInfo(getClass(), expType + " Experiment started: Run " + expRun + "/" + iterations);
					setupProbes(obCollector, prManager, cManager);
					startExperiment(baseWorkload, cManager, obCollector);
					executeWorkload(baseWorkload, obCollector, prManager);
				} while(!endExperiment(baseWorkload, cManager, obCollector, prManager));
				baseWorkload.flush();
			}
		} catch(Exception ex){
			AppLogger.logException(getClass(), ex, "Unable to archieve experiment");
		}
	}
	
	private void startExperiment(Workload baseWorkload, CommunicationManager cManager, ObservationCollector obCollector) throws IOException{
		dbManager.newExperiment(obCollector, expType, baseWorkload);
		AppLogger.logOngoingInfo(getClass(), "Starting SUT ..... ");
		ExperimentTiming.resetTimings();
		cManager.send(buildStartMessage());
		cManager.waitForConfirm();
		System.out.println("DONE");
	}
	
	private Object[] buildStartMessage() {
		Object[] startMessage;
		int i = 1;
		if(injections != null) {
			startMessage = new Object[injections.size() + 1];
			startMessage[0] = MessageType.START_EXPERIMENT;
			for(Failure failure : injections.keySet()){
				startMessage[i++] = failure.getFailureType() + ";" + failure.getFailureTag() + ";" + injections.get(failure) + ";" + failure.getFailureDetails();
			}
			return startMessage;
		} else return new Object[]{MessageType.START_EXPERIMENT};
	}

	private void executeWorkload(Workload baseWorkload, ObservationCollector obCollector, ProbeReceiverManager prManager) throws InterruptedException{
		prManager.startReceivers();
		AppLogger.logInfo(getClass(), "Executing workload \"" + baseWorkload.getName() + "\"");
		baseWorkload.runWorkload(false);
		Thread.sleep(2000);
	}
	
	private boolean endExperiment(Workload baseWorkload, CommunicationManager cManager, ObservationCollector obCollector, ProbeReceiverManager prManager) throws IOException {
		boolean validFlag;
		cManager.send(MessageType.END_EXPERIMENT);
		cManager.waitFor(MessageType.CHECK_PROBE);
		prManager.closeReceivers();
		AppLogger.logOngoingInfo(getClass(), "Shutdowning SUT ..... ");
		parseReceivedArray(cManager.receive());
		System.out.println("DONE");
		AppLogger.logInfo(getClass(), "Collecting Data on Database .....");
		validFlag = obCollector.isValid();
		if(validFlag){
			obCollector.storeObservations();
			if(injections != null)
				dbManager.storeInjActivations(injections.keySet(), obCollector.getRunId());
			dbManager.logExperimentInfo(baseWorkload);
			dbManager.saveMonitorPerformance(obCollector.getRunId());
		} else {
			dbManager.undoExperiment();
			AppLogger.logInfo(getClass(), "Experiment needs to be repeated: collected " + obCollector.getObservationNumber() + " observations");
		}
		obCollector.flush();
		prManager.closeReceivers();
		return validFlag;
	}

	private void parseReceivedArray(Collection<Object> received) {
		Iterator<Object> iterator = received.iterator();
		String readed;
		MessageType message = (MessageType) iterator.next();
		if(!message.equals(MessageType.OK))
			AppLogger.logError(getClass(), "UnrecognizedMessageType", "Not a valid END_EXPERIMENT outcome");
		else {
			while(iterator.hasNext()){
				readed = (String) iterator.next();
				if(readed.split(";").length == 3){
					for(Failure failure : injections.keySet()){
						if(readed.split(";")[0].equals(failure.getFailureType()) && readed.split(";")[1].equals(failure.getFailureTag())){
							failure.setFailureActivation(Long.valueOf(readed.split(";")[2]));
						}
					}
				}
			}
		}
	}	
	
}