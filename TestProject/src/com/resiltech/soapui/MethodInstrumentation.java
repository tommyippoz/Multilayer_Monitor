/*    */ package com.resiltech.soapui;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ class MethodInstrumentation {
/*    */   private String _name;
/*    */   private ParameterInstrumentation[] _parameters;
/*    */   
/*    */   public MethodInstrumentation(String name, ArrayList<ParameterInstrumentation> parameters) {
/* 10 */     this._name = name;
/* 11 */     this._parameters = ((ParameterInstrumentation[])parameters.toArray(new ParameterInstrumentation[parameters.size()]));
/*    */   }
/*    */   
/*    */   public String getName() {
/* 15 */     return this._name;
/*    */   }
/*    */   
/*    */   public ParameterInstrumentation[] getParameters() {
/* 19 */     return this._parameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\MethodInstrumentation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */