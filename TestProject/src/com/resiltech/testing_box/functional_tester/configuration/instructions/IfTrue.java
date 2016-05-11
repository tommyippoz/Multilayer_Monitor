/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;

import java.util.HashMap;
import java.util.LinkedList;

/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class IfTrue
/*    */   extends CompoundStatement
/*    */ {
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*    */   {
/* 14 */     return executeStatements(invoker, scope);
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor()
/*    */   {
/* 19 */     return new XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Condition/IfTrue", new CompoundStatement.CompoundStatementHandler()
/*    */     {
/*    */       private IfTrue _ifTrue;
/*    */       
/*    */       public void startIftrue(Attributes attribute) throws SAXException {
/* 24 */         this._ifTrue = new IfTrue();
/*    */       }
/*    */       
/*    */ 
/*    */       protected CompoundStatement getCompoundStatement()
/*    */       {
/* 30 */         return this._ifTrue;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\IfTrue.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */