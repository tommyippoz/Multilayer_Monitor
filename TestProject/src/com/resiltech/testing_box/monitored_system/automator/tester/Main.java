/*    */ package com.resiltech.testing_box.monitored_system.automator.tester;
/*    */ 
/*    */ import com.resiltech.testing_box.monitored_system.automator.AutomatorController;
/*    */ import com.resiltech.testing_box.monitored_system.automator.AutomatorController.DbConfiguration;
/*    */ import java.io.IOException;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args) throws java.net.UnknownHostException, IOException, SQLException
/*    */   {
/* 12 */     AutomatorController a = new AutomatorController(
/* 13 */       "localhost", 5588, 
/* 14 */       new AutomatorController.DbConfiguration("localhost", 3306, "testingbox_olap_db", "root", "resiltech"), 
/* 15 */       "select * from (dim_workloads INNER JOIN activable_faults USING(dim_workload_id)) INNER JOIN dim_injectable_faults USING(dim_injectable_fault_id) where fileName=?");
/*    */     
/* 17 */     a.injectFaults("MessagecreateForumThread.log");
/* 18 */     a.start();
/* 19 */     a.files();
/* 20 */     a.shutdown();
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\monitored_system\automator\tester\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */