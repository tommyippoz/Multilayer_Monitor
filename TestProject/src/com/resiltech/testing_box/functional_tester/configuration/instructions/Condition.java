/*     */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
import java.util.HashMap;
import java.util.LinkedList;

/*     */ 
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class Condition implements com.resiltech.testing_box.functional_tester.execution.IStatement
/*     */ {
/*     */   private String _variableName;
/*     */   private Operation _operator;
/*     */   private String _value;
/*     */   private CompoundStatement _ifTrue;
/*     */   private CompoundStatement _ifFalse;
/*     */   
/*     */   private static enum Operation {
/*  14 */     greaterOrEqualThan, 
/*  15 */     lessOrEqualThan, 
/*  16 */     greterThan, 
/*  17 */     lessThan, 
/*  18 */     equal, 
/*  19 */     notEqual, 
/*  20 */     startsWith, 
/*  21 */     endsWith;
/*     */     
/*     */     public static Operation fromString(String str) { 
/*     */       Operation op;
/*  25 */       if (str.equals(">=")) {
/*  26 */         op = greaterOrEqualThan; } else { 
/*  27 */         if (str.equals("<=")) {
/*  28 */           op = lessOrEqualThan; } else { 
/*  29 */           if (str.equals(">")) {
/*  30 */             op = greterThan; } else { 
/*  31 */             if (str.equals("<")) {
/*  32 */               op = lessThan; } else { 
/*  33 */               if (str.equals("==")) {
/*  34 */                 op = equal; } else { 
/*  35 */                 if (str.equals("!=")) {
/*  36 */                   op = notEqual; } else { 
/*  37 */                   if (str.equals("startsWith")) {
/*  38 */                     op = startsWith; } else { 
/*  39 */                     if (str.equals("endsWith")) {
/*  40 */                       op = endsWith;
/*     */                     } else
/*  42 */                       op = null;
/*     */                   }
/*     */                 } } } } } }
/*  45 */       return op;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Condition(String variableName, String operator, String value)
/*     */   {
/*  57 */     this._variableName = variableName;
/*  58 */     this._operator = Operation.fromString(operator);
/*  59 */     this._value = value;
/*  60 */     if (this._operator == null) {
/*  61 */       throw new RuntimeException("Operator " + operator + " hasn't been recognized");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public LinkedList<HashMap<String, String>> execute(com.resiltech.testing_box.functional_tester.execution.IRequestInvoker invoker, com.resiltech.testing_box.functional_tester.execution.IScope scope)
/*     */   {
/*  68 */     String checkedValue = scope.getValue(this._variableName);
/*  69 */     double doubleValueOfCheckedValue = 0.0D;
/*  70 */     double doubleValueOfUserValue = 0.0D;
/*     */     boolean areNumeric;
/*     */     try {
/*  73 */       doubleValueOfCheckedValue = Double.parseDouble(checkedValue);
/*  74 */       doubleValueOfUserValue = Double.parseDouble(this._value);
/*  75 */       areNumeric = true;
/*     */     } catch (Exception e) { 
/*  77 */       areNumeric = false; }
/*     */     boolean evaluationOfCondition;
/*     */     switch (this._operator) {
/*     */     case startsWith: 
/*  82 */       evaluationOfCondition = checkedValue.endsWith(this._value);
/*  83 */       break;
/*     */     case lessOrEqualThan: 
/*  85 */       evaluationOfCondition = checkedValue.equals(this._value);
/*  86 */       break;
/*     */     case endsWith: 
/*  88 */       if (areNumeric) {
/*  89 */         evaluationOfCondition = doubleValueOfCheckedValue >= doubleValueOfUserValue;
/*     */       } else {
/*  91 */         evaluationOfCondition = checkedValue.compareTo(this._value) >= 0;
/*     */       }
/*  93 */       break;
/*     */     case greaterOrEqualThan: 
/*  95 */       if (areNumeric) {
/*  96 */         evaluationOfCondition = doubleValueOfCheckedValue >= doubleValueOfUserValue;
/*     */       } else {
/*  98 */         evaluationOfCondition = checkedValue.compareTo(this._value) > 0;
/*     */       }
/* 100 */       break;
/*     */     case equal: 
/* 102 */       if (areNumeric) {
/* 103 */         evaluationOfCondition = doubleValueOfCheckedValue >= doubleValueOfUserValue;
/*     */       } else {
/* 105 */         evaluationOfCondition = checkedValue.compareTo(this._value) <= 0;
/*     */       }
/* 107 */       break;
/*     */     case greterThan: 
/* 109 */       if (areNumeric) {
/* 110 */         evaluationOfCondition = doubleValueOfCheckedValue >= doubleValueOfUserValue;
/*     */       } else {
/* 112 */         evaluationOfCondition = checkedValue.compareTo(this._value) <= 0;
/*     */       }
/* 114 */       break;
/*     */     case lessThan: 
/* 116 */       evaluationOfCondition = !checkedValue.equals(this._value);
/* 117 */       break;
/*     */     case notEqual: 
/* 119 */       evaluationOfCondition = checkedValue.startsWith(this._value);
/* 120 */       break;
/*     */     default: 
/* 122 */       throw new RuntimeException(); }
/* 124 */     if (evaluationOfCondition) {
/* 125 */       this._ifTrue.execute(invoker, scope);
/*     */     }
/* 127 */     else if (this._ifFalse != null) {
/* 128 */       this._ifFalse.execute(invoker, scope);
/*     */     }
				return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor getXmlConstructor()
/*     */   {
/* 135 */     return new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Condition", new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor.DefaultHandlerProducingObject()
/*     */     {
/*     */       private Condition _condition;
/*     */       
/*     */       public void startCondition(Attributes attribute) throws org.xml.sax.SAXException {
/* 140 */         String referedVariable = attribute.getValue("variable");
/* 141 */         String operator = attribute.getValue("operator");
/* 142 */         String value = attribute.getValue("value");
/*     */         
/* 144 */         this._condition = new Condition(referedVariable, operator, value);
/*     */       }
/*     */       
/*     */       public void processedIftrue(Object obj) throws org.xml.sax.SAXException
/*     */       {
/* 149 */         this._condition._ifTrue = ((CompoundStatement)obj);
/*     */       }
/*     */       
/*     */       public void processedIffalse(Object obj) throws org.xml.sax.SAXException {
/* 153 */         this._condition._ifFalse = ((CompoundStatement)obj);
/*     */       }
/*     */       
/*     */       public Object getObject()
/*     */       {
/* 158 */         return this._condition;
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Condition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */