/*     */ package com.resiltech.testing_box.functional_tester;
/*     */ 
import com.resiltech.testing_box.functional_tester.configuration.XmlWorkload;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
import com.resiltech.testing_box.functional_tester.execution.IStatement;

import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */  
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*     */     
				XmlWorkloadInterface wInt = new XmlWorkloadInterface(new File("workloads/Workload_Roberta.xml"), null);
				System.out.println("WORKLOADS: " + wInt.listWorkloads().size());
				for(XmlWorkload wload : wInt.listWorkloads()){
					for(Call statement : wload.getCalls()){
						System.out.println(wload.getName() + " || " + statement.get_method() + statement.get_portlet());
					}
				}
				//wInt.executeWorkload();
				
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */