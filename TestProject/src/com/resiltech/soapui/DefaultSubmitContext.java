/*    */ package com.resiltech.soapui;
/*    */ 
/*    */ import com.eviware.soapui.support.types.StringToObjectMap;
/*    */ import java.util.Set;
/*    */ 
/*    */ final class DefaultSubmitContext implements com.eviware.soapui.model.iface.SubmitContext
/*    */ {
/*  8 */   private StringToObjectMap _properties = new StringToObjectMap();
/*    */   
/*    */   public void setProperty(String arg0, Object arg1)
/*    */   {
/* 12 */     this._properties.put(arg0, arg1);
/*    */   }
/*    */   
/*    */   public Object removeProperty(String arg0)
/*    */   {
/* 17 */     return this._properties.remove(arg0);
/*    */   }
/*    */   
/*    */   public boolean hasProperty(String arg0)
/*    */   {
/* 22 */     return this._properties.containsKey(arg0);
/*    */   }
/*    */   
/*    */   public String[] getPropertyNames()
/*    */   {
/* 27 */     return (String[])this._properties.keySet().toArray(new String[this._properties.keySet().size()]);
/*    */   }
/*    */   
/*    */   public Object getProperty(String arg0)
/*    */   {
/* 32 */     return this._properties.get(arg0);
/*    */   }
/*    */   
/*    */   public StringToObjectMap getProperties()
/*    */   {
/* 37 */     return this._properties;
/*    */   }
/*    */   
/*    */   public com.eviware.soapui.model.ModelItem getModelItem()
/*    */   {
/* 42 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String expand(String arg0)
/*    */   {
/* 48 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\DefaultSubmitContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */