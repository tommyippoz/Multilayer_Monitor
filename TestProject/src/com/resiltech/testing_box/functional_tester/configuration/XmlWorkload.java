/*    */ package com.resiltech.testing_box.functional_tester.configuration;
/*    */ 
/*    */ import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Choreography;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;
import com.resiltech.testing_box.functional_tester.execution.IStatement;

import org.xml.sax.Attributes;
/*    */ 
/*    */ public class XmlWorkload
/*    */ {
/*    */   private String _name;
/*    */   private Choreography _choreography;
/*    */   
/*    */   public XmlWorkload(String name)
/*    */   {
/* 15 */     this._name = name;
/* 16 */     this._choreography = null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 20 */     return this._name;
/*    */   }

			public ArrayList<IStatement> getStatements(){
				return _choreography.get_statements();
			}
			
			public ArrayList<Call> getCalls(){
				ArrayList<IStatement> statements =  _choreography.get_statements();
				ArrayList<Call> calls = new ArrayList<Call>();
				for(IStatement statement : statements){
					if(statement instanceof Call){
						calls.add((Call)statement);
					}
				}
				return calls;
			}
/*    */   
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope) {
/* 24 */     return this._choreography.execute(invoker, scope);
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor()
/*    */   {
/* 29 */     return new XmlObjectConstructor("/FunctionalTest/Workloads/Workload", new XmlObjectConstructor.DefaultHandlerProducingObject()
/*    */     {
/*    */       private XmlWorkload _workload;
/*    */       
/*    */       public void startWorkload(Attributes attribute) throws org.xml.sax.SAXException {
/* 34 */         String name = attribute.getValue("name");
/* 35 */         this._workload = new XmlWorkload(name);
/*    */       }
/*    */       
/*    */       public void processedChoreography(Object obj) {
/* 39 */         this._workload._choreography = ((Choreography)obj);
/*    */       }
/*    */       
/*    */       public Object getObject()
/*    */       {
/* 44 */         return this._workload;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\Workload.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */