/*    */ package com.resiltech.soapui;
/*    */ 
/*    */ class ParameterInstrumentation { private String _name;
/*    */   
/*  5 */   public static enum ContentType { TEXT, 
/*  6 */     XML;
/*    */   }
/*    */   
/*    */   private ContentType _type;
/*    */   private String _value;
/*    */   private String _typeHint;
/*    */   public ParameterInstrumentation(String name, String value, ContentType type, String typeHint)
/*    */   {
/* 14 */     this._name = name;
/* 15 */     this._type = type;
/* 16 */     this._value = value;
/* 17 */     this._typeHint = typeHint;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 21 */     return this._name;
/*    */   }
/*    */   
/*    */   public ContentType getType() {
/* 25 */     return this._type;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 29 */     return this._value;
/*    */   }
/*    */   
/*    */   public String getTypeHint() {
/* 33 */     return this._typeHint;
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\ParameterInstrumentation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */