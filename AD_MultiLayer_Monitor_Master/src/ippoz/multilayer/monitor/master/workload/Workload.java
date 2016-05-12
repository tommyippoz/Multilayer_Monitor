/**
 * 
 */
package ippoz.multilayer.monitor.master.workload;

import ippoz.multilayer.monitor.master.experiment.Invocation;
import ippoz.multilayer.monitor.master.services.Service;

import java.util.LinkedList;

/**
 * The Interface Workload.
 * Defines a basic type of workload.
 *
 * @author Tommy
 */
public interface Workload {
	
	/**
	 * Gets the name of the workload.
	 *
	 * @return the name of the workload
	 */
	public String getName();
	
	/**
	 * Checks if the workload is valid.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid();
	
	/**
	 * Services used by the workload.
	 *
	 * @return the list of services
	 */
	public LinkedList<Service> usedServices();
	
	/**
	 * Checks if the workload is running.
	 *
	 * @return true, if is running
	 */
	public boolean isRunning();
	
	/**
	 * Runs the workload.
	 *
	 * @param seeOutput flag that indicates if the output goes to the console
	 */
	public void runWorkload(boolean seeOutput);
	
	/**
	 * Executed invocations.
	 *
	 * @return the list of invocations
	 */
	public LinkedList<Invocation> executedInvocations();
	
	/**
	 * Clones workload.
	 *
	 * @return the cloned workload
	 */
	public Workload cloneWorkload();

	/**
	 * Flushes.
	 */
	public void flush();
	
}
