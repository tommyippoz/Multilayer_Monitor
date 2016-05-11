/*    */ package com.resiltech.testing_box.functional_tester.configuration;
/*    */ 
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ public class WorkloadReference
/*    */ {
/*    */   private String _name;
/*    */   
/*    */   public WorkloadReference(String name) {
/* 10 */     this._name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 14 */     return this._name;
/*    */   }
/*    */   
/*    */   public static XmlObjectConstructor getXmlConstructor()
/*    */   {
/* 19 */     return new XmlObjectConstructor("/FunctionalTest/Run/WorkloadReference", new XmlObjectConstructor.DefaultHandlerProducingObject()
/*    */     {
/*    */       private WorkloadReference _workloadReference;
/*    */       
/*    */       public void startWorkloadreference(Attributes attribute) throws org.xml.sax.SAXException {
/* 24 */         String name = attribute.getValue("name");
/* 25 */         this._workloadReference = new WorkloadReference(name);
/*    */       }
/*    */       
/*    */       public Object getObject()
/*    */       {
/* 30 */         return this._workloadReference;
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\WorkloadReference.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */