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
/*    */ public class IfFalse
/*    */   extends CompoundStatement
/*    */ {
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*    */   {
/* 14 */     return executeStatements(invoker, scope);
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor()
/*    */   {
/* 19 */     return new XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Condition/IfFalse", new CompoundStatement.CompoundStatementHandler()
/*    */     {
/*    */       private IfFalse _ifFalse;
/*    */       
/*    */       public void startIftrue(Attributes attribute) throws SAXException {
/* 24 */         this._ifFalse = new IfFalse();
/*    */       }
/*    */       
/*    */ 
/*    */       protected CompoundStatement getCompoundStatement()
/*    */       {
/* 30 */         return this._ifFalse;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\IfFalse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */