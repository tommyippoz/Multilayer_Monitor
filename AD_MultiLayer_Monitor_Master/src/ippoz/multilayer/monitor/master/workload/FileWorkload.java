/**
 * 
 */
package ippoz.multilayer.monitor.master.workload;

import java.io.File;

/**
 * @author Tommy
 *
 */
public abstract class FileWorkload implements Workload {
	
	protected File workloadFile;
	
	public FileWorkload(File file){
		workloadFile = file;
	}

	@Override
	public String getName() {
		return workloadFile.getName();
	}



	@Override
	public boolean isValid() {
		return workloadFile.exists();
	}
	
}
