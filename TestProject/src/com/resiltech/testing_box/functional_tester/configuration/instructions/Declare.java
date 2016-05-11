/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
/*    */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor.DefaultHandlerProducingObject;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IStatement;

import java.util.HashMap;
import java.util.LinkedList;

/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class Declare implements IStatement
/*    */ {
/*    */   private String _variableName;
/*    */   private String _value;
/*    */   
/*    */   public Declare(String variableName)
/*    */   {
/* 17 */     this._variableName = variableName;
/* 18 */     this._value = "";
/*    */   }
/*    */   
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*    */   {
/* 23 */     scope.declare(this._variableName);
/* 24 */     scope.setValue(this._variableName, this._value);
return null;
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor()
/*    */   {
/* 29 */     return new XmlObjectConstructor("/FunctionalTest/*/Declare", new XmlObjectConstructor.DefaultHandlerProducingObject()
/*    */     {
/*    */       private Declare _declare;
/*    */       
/*    */       public void startDeclare(Attributes attribute) throws org.xml.sax.SAXException {
/* 34 */         String variableName = attribute.getValue("name");
/* 35 */         this._declare = new Declare(variableName);
/*    */       }
/*    */       
/*    */       public void contentDeclare(String str) {
/* 39 */         this._declare._value += str;
/*    */       }
/*    */       
/*    */       public Object getObject()
/*    */       {
/* 44 */         return this._declare;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Declare.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */