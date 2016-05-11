/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.execution.IScope;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ class Scope implements IScope
/*    */ {
/*    */   private static final String VARIABLE_INITIALIZATION_VALUE = "";
/*    */   private HashMap<String, String> _variables;
/*    */   private IScope _parent;
/*    */   
/*    */   Scope(IScope parent)
/*    */   {
/* 16 */     this._variables = new HashMap();
/* 17 */     this._parent = parent;
/*    */   }
/*    */   
/*    */   public Set<String> getVariableNames()
/*    */   {
/* 22 */     HashSet<String> parenting = new HashSet();
/* 23 */     parenting.addAll(this._parent.getVariableNames());
/* 24 */     parenting.addAll(this._variables.keySet());
/*    */     
/* 26 */     return parenting;
/*    */   }
/*    */   
/*    */   public String getValue(String variableName)
/*    */   {
/* 31 */     String value = null;
/* 32 */     if (this._variables.containsKey(variableName)) {
/* 33 */       value = (String)this._variables.get(variableName);
/*    */     } else {
/* 35 */       value = this._parent.getValue(variableName);
/*    */     }
/* 37 */     return value;
/*    */   }
/*    */   
/*    */   public boolean hasVariable(String variableName)
/*    */   {
/* 42 */     return (this._variables.containsKey(variableName)) || (this._parent.hasVariable(variableName));
/*    */   }
/*    */   
/*    */   public void declare(String variableName)
/*    */   {
/* 47 */     this._variables.put(variableName, "");
/*    */   }
/*    */   
/*    */   public void setValue(String variableName, String value)
/*    */   {
/* 52 */     if (this._variables.containsKey(variableName)) {
/* 53 */       this._variables.put(variableName, value);
/*    */     } else {
/* 55 */       this._parent.setValue(variableName, value);
/*    */     }
/* 57 */     System.out.println(variableName + "=" + value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Scope.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */