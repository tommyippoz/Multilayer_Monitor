/**
 * 
 */
package ippoz.multilayer.monitor.master.workload;

import ippoz.multilayer.monitor.master.experiment.Invocation;
import ippoz.multilayer.monitor.master.services.Service;

import java.util.LinkedList;

/**
 * @author Tommy
 *
 */
public interface Workload {
	
	public String getName();
	
	public boolean isValid();
	
	public LinkedList<Service> usedServices();
	
	public boolean isRunning();
	
	public void runWorkload(boolean seeOutput);
	
	public LinkedList<Invocation> executedInvocations();
	
	public Workload cloneWorkload();

	public void flush();
	
}
