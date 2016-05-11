/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;

import java.util.HashMap;
import java.util.LinkedList;

/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class Choreography extends CompoundStatement
/*    */ {
/*    */   public static class ChoreographyXmlConstructor extends CompoundStatement.CompoundStatementHandler
/*    */   {
/*    */     private Choreography _choreography;
/*    */     
/*    */     public void startChoreography(Attributes attribute) throws org.xml.sax.SAXException
/*    */     {
/* 16 */       this._choreography = new Choreography();
/*    */     }
/*    */     
/*    */     protected CompoundStatement getCompoundStatement()
/*    */     {
/* 21 */       return this._choreography;
/*    */     }
/*    */   }
/*    */   
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*    */   {
/* 27 */     return executeStatements(invoker, scope);

/*    */   }

			 
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor() {
/* 31 */     return new XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography", new ChoreographyXmlConstructor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Choreography.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */