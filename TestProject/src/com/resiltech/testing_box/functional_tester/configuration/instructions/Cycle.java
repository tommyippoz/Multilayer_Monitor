/*     */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
import java.util.HashMap;
import java.util.LinkedList;

/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.execution.IScope;
/*     */ 
/*     */ public class Cycle extends CompoundStatement
/*     */ {
/*     */   private int _numberOfCycles;
/*     */   private String _cycleVariable;
/*     */   private CycleMode _mode;
/*     */   
/*     */   private static enum CycleMode {
/*  12 */     TIMES, 
/*  13 */     SECONDS;
/*     */   }
/*     */   
/*  16 */   private static CycleMode fromString(String str) { 
				CycleMode cm = null;
/*  17 */     str = str.toLowerCase();
				switch (str.hashCode()) {
					case 110364486:  
						if (str.equals("times")) 
							break;  
					case 1970096767:  
						if (str.equals("seconds")) {
/*  20 */         			cm = CycleMode.SECONDS;
/*  21 */         			return cm;
	/*     */        }
/*  24 */       break;
/*     */     }
/*  26 */     cm = null;
/*     */     
/*     */ 
/*  29 */     return cm;
/*     */   }
/*     */   
/*     */   public static class CycleXmlConstructor extends CompoundStatement.CompoundStatementHandler
/*     */   {
/*     */     private Cycle _cycle;
/*     */     
/*     */     public void startCycle(org.xml.sax.Attributes attribute) throws org.xml.sax.SAXException {
/*  37 */       String numberOfCyclesAsString = attribute.getValue("cycles");
/*  38 */       Cycle.CycleMode mode = Cycle.fromString(attribute.getValue("mode"));
/*  39 */       int numberOfCycles = Integer.parseInt(numberOfCyclesAsString);
/*  40 */       int indexOfCycleVariableName = attribute.getIndex("cycle-variable-name");
/*  41 */       if (indexOfCycleVariableName == -1) {
/*  42 */         this._cycle = new Cycle(numberOfCycles, mode, null);
/*     */       } else {
/*  44 */         this._cycle = new Cycle(numberOfCycles, mode, attribute.getValue(indexOfCycleVariableName));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected CompoundStatement getCompoundStatement()
/*     */     {
/*  51 */       return this._cycle;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Cycle(int numberOfCycles, CycleMode mode, String cycleVariable)
/*     */   {
/*  61 */     this._numberOfCycles = numberOfCycles;
/*  62 */     this._cycleVariable = cycleVariable;
/*  63 */     this._mode = mode;
/*     */   }
/*     */   
/*     */   private Cycle(int numberOfCycles, CycleMode mode)
/*     */   {
/*  68 */     this._numberOfCycles = numberOfCycles;
/*  69 */     this._cycleVariable = null;
/*  70 */     this._mode = mode;
/*     */   }
/*     */   
/*     */ 
/*     */   public LinkedList<HashMap<String, String>> execute(com.resiltech.testing_box.functional_tester.execution.IRequestInvoker invoker, IScope scope)
/*     */   {
/*  76 */     int counter = 0;
/*  77 */     IScope cycleScope; if (this._cycleVariable != null) {
/*  78 */       cycleScope = new Scope(scope);
/*  79 */       cycleScope.declare(this._cycleVariable);
/*     */     } else {
/*  81 */       cycleScope = scope;
/*     */     }
/*  83 */     switch (this._mode) {
/*     */     case SECONDS: 
/*  85 */       while (counter < this._numberOfCycles) {
/*  86 */         System.out.println("Loop counter: " + counter + " out of " + this._numberOfCycles);
/*  87 */         if (this._cycleVariable != null) {
/*  88 */           cycleScope.setValue(this._cycleVariable, Integer.toString(counter));
/*     */         }
/*     */         
/*  91 */         executeStatements(invoker, cycleScope);
/*  92 */         counter++;
/*     */       }
/*  94 */       break;
/*     */     
/*     */     case TIMES: 
/*  97 */       long stopsWhen = System.currentTimeMillis() + this._numberOfCycles * 1000;
/*  98 */       while (System.currentTimeMillis() < stopsWhen) {
/*  99 */         System.out.println("Looping for " + Long.toString(stopsWhen - System.currentTimeMillis()) + " -> " + Long.toString(this._numberOfCycles * 1000));
/* 100 */         if (this._cycleVariable != null) {
/* 101 */           cycleScope.setValue(this._cycleVariable, Integer.toString(counter));
/*     */         }
/*     */         
/* 104 */         executeStatements(invoker, cycleScope);
/* 105 */         counter++;
/*     */       }
/*     */     }
return null;
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public static com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor getXmlConstructor()
/*     */   {
/* 114 */     return new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Cycle", new CycleXmlConstructor());
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Cycle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */