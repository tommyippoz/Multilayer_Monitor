package com.resiltech.testing_box.functional_tester;

import java.io.File;
import java.util.Collection;

import com.resiltech.testing_box.functional_tester.configuration.XmlWorkload;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
import com.resiltech.testing_box.functional_tester.runner.FunctionalTestRunner;

public class XmlWorkloadInterface {

	private File workloadFile;
	private FunctionalTestRunner tester;
	
	public XmlWorkloadInterface(File file, Object preferences){
		workloadFile = file;
		tester = new FunctionalTestRunner(file);
	}
	
	public Collection<XmlWorkload> listWorkloads(){
		return tester.listWorkloads();
	}
	
	public boolean executeWorkload(){
		return tester.execute();
	}
	
	public String getPreference(String prefTag){
		return null;
	}
	
	
}
