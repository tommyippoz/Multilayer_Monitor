/**
 * 
 */
package ippoz.multilayer.monitor.master.workload;

import ippoz.multilayer.commons.support.PreferencesManager;
import ippoz.multilayer.monitor.master.experiment.Invocation;
import ippoz.multilayer.monitor.master.services.MethodCall;
import ippoz.multilayer.monitor.master.services.RemoteService;
import ippoz.multilayer.monitor.master.services.Service;
import ippoz.multilayer.monitor.master.services.SoapXmlService;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import com.resiltech.testing_box.functional_tester.configuration.XmlWorkload;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
import com.resiltech.testing_box.functional_tester.runner.FunctionalTestRunner;

/**
 * @author Tommy
 *
 */
public class SoapXmlWorkload extends FileWorkload {
	
	private static final String SERVICE_NAME_TAG = "SERVICE_NAME";
	private static final String METHOD_NAME_TAG = "METHOD_NAME";
	private static final String START_TIME_TAG = "START_TIME";
	private static final String END_TIME_TAG = "END_TIME";
	private static final String RESPONSE_CODE = "RESPONSE_CODE";
	
	private boolean isRunning;
	private PreferencesManager preferences;
	private FunctionalTestRunner soapTester;

	public SoapXmlWorkload(File file, PreferencesManager preferences) {
		super(file);
		this.preferences = preferences;
		soapTester = new FunctionalTestRunner(file);
		isRunning = false;
	}

	@Override
	public Workload cloneWorkload() {
		return new SoapXmlWorkload(workloadFile, preferences);
	}



	@Override
	public LinkedList<Service> usedServices() {
		RemoteService newService;
		LinkedList<Service> servicesList = new LinkedList<Service>();
		for(XmlWorkload wLoad : soapTester.listWorkloads()){
			newService = new SoapXmlService(wLoad.getName());
			if(!existsIn(newService, servicesList)){
				for(Call call : wLoad.getCalls()){
					newService.addMethodCall(new MethodCall(call.get_portlet(), call.get_method()));
				}
				servicesList.add(newService);
			}
		}
		return servicesList;
	}
	
	private boolean existsIn(Service service, LinkedList<Service> serviceList){
		for(Service currentService : serviceList){
			if(service.compareTo(currentService) == 0)
				return true;
		}
		return false;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void runWorkload(boolean seeOutput) {
		isRunning = true;
		soapTester.execute();
		isRunning = false;
	}

	@Override
	public LinkedList<Invocation> executedInvocations() {
		LinkedList<Invocation> invList = new LinkedList<Invocation>();
		for(HashMap<String, String> invMap : soapTester.listInvocations()){
			invList.add(new Invocation(getServiceByName(invMap.get(SERVICE_NAME_TAG), usedServices()), invMap.get(METHOD_NAME_TAG), invMap.get(START_TIME_TAG), invMap.get(END_TIME_TAG), invMap.get(RESPONSE_CODE)));
		}
		return invList;
	}
	
	public static Service getServiceByName(String serviceName, LinkedList<Service> servicesList){
		for(Service service : servicesList){
			if(service.getName().equals(serviceName))
				return service;
		}
		return null;
	}

	@Override
	public void flush() {
		preferences = null;
		soapTester = null;
	}

}
