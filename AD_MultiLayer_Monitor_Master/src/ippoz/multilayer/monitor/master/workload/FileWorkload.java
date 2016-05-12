/**
 * 
 */
package ippoz.multilayer.monitor.master.workload;

import java.io.File;

/**
 * The Class FileWorkload.
 * Defines a workload that is described in a single file.
 *
 * @author Tommy
 */
public abstract class FileWorkload implements Workload {
	
	/** The workload file. */
	protected File workloadFile;
	
	/**
	 * Instantiates a new file workload.
	 *
	 * @param file the file
	 */
	public FileWorkload(File file){
		workloadFile = file;
	}

	/* (non-Javadoc)
	 * @see ippoz.multilayer.monitor.master.workload.Workload#getName()
	 */
	@Override
	public String getName() {
		return workloadFile.getName();
	}

	/* (non-Javadoc)
	 * @see ippoz.multilayer.monitor.master.workload.Workload#isValid()
	 */
	@Override
	public boolean isValid() {
		return workloadFile.exists();
	}
	
}
