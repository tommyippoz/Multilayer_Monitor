/**
 * 
 */
package ippoz.multilayer.monitor.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import ippoz.multilayer.monitor.communication.CommunicationManager;
import ippoz.multilayer.monitor.communication.MessageType;
import ippoz.multilayer.monitor.master.database.DatabaseManager;
import ippoz.multilayer.monitor.master.experiment.Experiment;
import ippoz.multilayer.monitor.master.experiment.ExperimentType;
import ippoz.multilayer.monitor.master.experiment.Failure;
import ippoz.multilayer.monitor.master.workload.SoapXmlWorkload;
import ippoz.multilayer.monitor.master.workload.Workload;
import ippoz.multilayer.monitor.support.AppLogger;
import ippoz.multilayer.monitor.support.PreferencesManager;

/**
 * @author Tommy
 *
 */
public class MasterManager {
	
	private DatabaseManager dbManager;
	private CommunicationManager cManager;
	private PreferencesManager prefManager;
	private LinkedList<Experiment> expList;
	private LinkedList<Experiment> testList;
	private LinkedList<Workload> availableWorkloads;
	
	public MasterManager(File prefFile) throws IOException {
		this(new PreferencesManager(prefFile));
	}
	
	public MasterManager(PreferencesManager prefManager) throws IOException {
		this.prefManager = prefManager;
		dbManager = new DatabaseManager("C:\\Program Files\\MySQL\\MySQL Server 5.6", "experiment", false);
		cManager = new CommunicationManager(prefManager.getPreference("SLAVE_IP_ADDRESS"), Integer.parseInt(prefManager.getPreference("OUT_PORT")), Integer.parseInt(prefManager.getPreference("IN_PORT")));
	}
	
	public void setupEnvironment() throws IOException {
		readWorkloads();
		setupExperiments();
	}
	
	private void readWorkloads(){
		Workload currentWorkload;
		availableWorkloads = new LinkedList<Workload>();
		AppLogger.logOngoingInfo(getClass(), "Fetching workloads ");
		for(File wFile : new File(prefManager.getPreference("WORKLOAD_FOLDER")).listFiles()){
			currentWorkload = null;
			try {
				if(wFile.getName().endsWith(".xml") && wFile.getName().toUpperCase().contains("WORKLOAD")){
					currentWorkload = new SoapXmlWorkload(wFile, null);
				}
				if(currentWorkload != null){
					availableWorkloads.add(currentWorkload);
					System.out.print(".");
				} 
			} catch(Exception ex){
				AppLogger.logError(getClass(), ex.getClass().getName(), "Unable to load workload: " + wFile.getName());
			}	
		}
		System.out.println(" Available workloads: " + availableWorkloads.size());
	}
	
	private void setupExperiments(){
		File expFile = null;
		BufferedReader reader = null;
		String readed;
		String[] splitted;
		Experiment exp;
		try {
			expList = new LinkedList<Experiment>();
			testList = new LinkedList<Experiment>();
			expFile = new File(prefManager.getPreference("EXPERIMENT_FILE"));
			reader = new BufferedReader(new FileReader(expFile));
			while(reader.ready()){
				readed = reader.readLine();
				if(readed != null && readed.length() > 0 && !readed.startsWith("workload_name")){
					splitted = readed.split(",");
					if(splitted[0].endsWith(".xml")){
						if(ExperimentType.valueOf(readed.split(",")[1]) != ExperimentType.TEST) {
							if(splitted.length > 7){
								exp = new Experiment(new SoapXmlWorkload(new File(prefManager.getPreference("WORKLOAD_FOLDER") + "/" + splitted[0]), prefManager), ExperimentType.FAULTY, dbManager, Integer.parseInt(splitted[2]), parseFailures(readed), Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]));
							} else exp = new Experiment(new SoapXmlWorkload(new File(prefManager.getPreference("WORKLOAD_FOLDER") + "/" + splitted[0]), prefManager), ExperimentType.GOLDEN, dbManager, Integer.parseInt(splitted[2]), null, Integer.parseInt(splitted[3]), Integer.parseInt(splitted[4]));
							if(!exp.canExecute()){
								for(Experiment newExp : exp.getNeededTests(availableWorkloads, Integer.parseInt(prefManager.getPreference("TEST_ITERATIONS")), Integer.parseInt(splitted[5]), Integer.parseInt(splitted[6]))){
									if(!isInTestList(newExp))
										testList.add(newExp);
								}
							}
							expList.add(exp);
							AppLogger.logInfo(getClass(), "Readed '"+ exp.getExpType().toString() + "' experiment: " + exp.getWorkload().getName());
						} else {} // TODO
					}
					System.out.print(".");
				}
			}
			reader.close();	
		} catch(IOException ex){
			AppLogger.logException(getClass(), ex, "Unable to load experiments");
		}
		
	}
	
	private boolean isInTestList(Experiment newExp) {
		if(newExp.getExpType() == ExperimentType.TEST){
			for(Experiment exp : testList){
				if(exp.getExpType() == ExperimentType.TEST && exp.getWorkload().getName().equals(newExp.getWorkload().getName()))
					return true;
			}
		}
		return false;
	}

	private HashMap<Failure, Long> parseFailures(String readed) {
		String[] splitted = readed.split(",");
		String[] failureData;
		HashMap<Failure, Long> failMap = new HashMap<Failure, Long>();
		try {
			for(int i=7;i<splitted.length;i++){
				if(splitted[i].contains("#")) {
					failureData = splitted[i].split("#")[0].trim().split(";");
					failMap.put(new Failure(failureData[0], failureData[1], splitted[i].trim().substring(splitted[i].trim().indexOf("#")+1)), Long.valueOf(failureData[2]));
				} else {
					failureData = splitted[i].trim().split(";");
					failMap.put(new Failure(failureData[0], failureData[1], ""), Long.valueOf(failureData[2])); 
				}
			}
		} catch (Exception ex){
			AppLogger.logException(getClass(), ex, "Unable to parse Fault details");
		}
		return failMap;
	}

	private boolean setupCampaign() throws IOException {
		long beforeMillis;
		LinkedList<Object> response;
		cManager.send(new Object[]{MessageType.SETUP_SUT, "LiferaySUT"});
		cManager.waitForConfirm();
		cManager.send(MessageType.START_CAMPAIGN);
		beforeMillis = System.currentTimeMillis();
		response = cManager.receive();
		if(((MessageType)response.get(0)).equals(MessageType.OK)){
			if(response.size() == 2){
				if(((Long)response.get(1))/1000 >= beforeMillis/1000 && ((Long)response.get(1))/1000 <= System.currentTimeMillis()/1000){
					AppLogger.logInfo(getClass(), "NTP Synchronization checked");
					return true;
				} else {
					AppLogger.logError(getClass(), "UnSynchronizedClocksException", "NTP clocks are not synchronized");
					return false;
				}
			} else {
				AppLogger.logInfo(getClass(), "Unable to check NTP clock alignment");
				return true;
			}
			
		} else {
			AppLogger.logError(getClass(), "WrongResponseException", "Unable to initialize campaign: wrong response");
			return false;
		}
	}
	
	private void shutdownCampaign() throws IOException {
		cManager.send(MessageType.END_CAMPAIGN);
		cManager.waitForConfirm();
	}
	
	public void startExperimentalCampaign() throws IOException {
		int i = 1;
		if(setupCampaign()){
			for(Experiment currentTest : testList){
				AppLogger.logInfo(getClass(), "Executing test " + i + "/" + testList.size() + ": " + currentTest.getExpType() + "Repeated " + currentTest.getIterations() + " times");
				currentTest.executeExperiment(cManager);
				currentTest = null;
				i++;
			}
			i = 1;
			for(Experiment currentExp : expList){
				AppLogger.logInfo(getClass(), "Executing experiment " + i + "/" + expList.size() + ": " + currentExp.getExpType() + "Repeated " + currentExp.getIterations() + " times");
				currentExp.executeExperiment(cManager);
				currentExp = null;
				i++;
			}
		}
		shutdownCampaign();
	}
	
	public void flush() throws SQLException, IOException {
		dbManager.flush();
		cManager.flush();
	}

}