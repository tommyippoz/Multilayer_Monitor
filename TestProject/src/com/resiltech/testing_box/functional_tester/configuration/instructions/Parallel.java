/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
import java.util.HashMap;
import java.util.LinkedList;

/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;
/*    */ 
/*    */ public class Parallel extends CompoundStatement
/*    */ {
/*    */   private int _numberOfThreads;
/*    */   
/*    */   public static class ParallelXmlConstructor extends CompoundStatement.CompoundStatementHandler
/*    */   {
/*    */     private Parallel _parallel;
/*    */     
/*    */     public void startParallel(org.xml.sax.Attributes attribute) throws org.xml.sax.SAXException
/*    */     {
/* 16 */       String numberOfThreadsOfString = attribute.getValue("numberOfThreads");
/* 17 */       int numberOfThreads = Integer.parseInt(numberOfThreadsOfString);
/* 18 */       this._parallel = new Parallel(numberOfThreads);
/*    */     }
/*    */     
/*    */     protected CompoundStatement getCompoundStatement()
/*    */     {
/* 23 */       return this._parallel;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Parallel(int numberOfThreads)
/*    */   {
/* 30 */     this._numberOfThreads = numberOfThreads;
/*    */   }
/*    */   
/*    */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*    */   {
/* 35 */     final IScope finalScope = scope;
/* 36 */     final IRequestInvoker finalInvoker = invoker;
/* 37 */     Thread[] paralleledExecution = new Thread[this._numberOfThreads];
/* 38 */     for (int i = 0; i < this._numberOfThreads; i++) {
/* 39 */       System.out.println("Starting thread n°" + i + " of " + this._numberOfThreads);
/* 40 */       paralleledExecution[i] = new Thread(new Runnable()
/*    */       {
/*    */         public void run()
/*    */         {
/* 44 */           Parallel.this.executeStatements(finalInvoker, finalScope);
/*    */         }
/* 46 */       });
/* 47 */       paralleledExecution[i].start();
/*    */     }
/*    */     
/* 50 */     for (int i = 0; i < this._numberOfThreads; i++) {
/* 51 */       System.out.println("Waiting for finishing thread n°" + i + " of " + this._numberOfThreads);
/*    */       try {
/* 53 */         paralleledExecution[i].join();
/*    */       }
/*    */       catch (InterruptedException localInterruptedException) {}
/*    */     }
return null;
/*    */   }
/*    */   
/*    */   public static com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor getXmlConstructor() {
/* 60 */     return new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Parallel", new ParallelXmlConstructor());
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Parallel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */