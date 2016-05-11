/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
/*    */ import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;

/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class Sleep implements com.resiltech.testing_box.functional_tester.execution.IStatement
/*    */ {
/*    */   private long _sleepFor;
/*    */   
/*    */   public static class SleepXmlConstructor extends com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor.DefaultHandlerProducingObject
/*    */   {
/*    */     private Sleep _sleep;
/*    */     
/*    */     public void startSleep(Attributes attribute) throws org.xml.sax.SAXException
/*    */     {
/* 17 */       String sleepForAsString = attribute.getValue("for");
/* 18 */       double sleepFor = Double.parseDouble(sleepForAsString);
/* 19 */       this._sleep = new Sleep(sleepFor);
/*    */     }
/*    */     
/*    */     public Object getObject()
/*    */     {
/* 24 */       return this._sleep;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Sleep(double sleepFor)
/*    */   {
/* 32 */     this._sleepFor = (long) (Math.floor(sleepFor * 1000.0D));
/*    */   }
/*    */   
/*    */   public LinkedList<HashMap<String, String>> execute(com.resiltech.testing_box.functional_tester.execution.IRequestInvoker invoker, com.resiltech.testing_box.functional_tester.execution.IScope scope)
/*    */   {
/*    */     try {
/* 38 */       System.out.println("Falling asleep for " + Long.toString(this._sleepFor));
/*    */       
/* 40 */       Thread.sleep(this._sleepFor);
/*    */       
/* 42 */       System.out.println("Awake right now!!");
/*    */     }
/*    */     catch (InterruptedException localInterruptedException) {}
return null;
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor() {
/* 48 */     return new XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Sleep", new SleepXmlConstructor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Sleep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */