/*    */ package com.resiltech.soapui;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ class NamespaceContextByHashMap implements javax.xml.namespace.NamespaceContext
/*    */ {
/*    */   private HashMap<String, String> _namespaceMapper;
/*    */   
/*    */   public NamespaceContextByHashMap(HashMap<String, String> namespaceMapper)
/*    */   {
/* 12 */     this._namespaceMapper = namespaceMapper;
/*    */   }
/*    */   
/*    */   public Iterator<?> getPrefixes(String arg0)
/*    */   {
/* 17 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public String getPrefix(String arg0)
/*    */   {
/* 22 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public String getNamespaceURI(String arg0)
/*    */   {
/* 27 */     return (String)this._namespaceMapper.get(arg0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\NamespaceContextByHashMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */