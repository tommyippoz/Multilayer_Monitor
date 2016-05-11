/*     */ package com.resiltech.testing_box.functional_tester;
/*     */ 
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class WorkloadConfiguration { private static SUTConfiguration s_sut;
/*     */   private static FaultsDBConfiguration s_faults_db;
/*     */   private static FaultsConfiguration s_faults_cfg;
/*     */   private static AutomatorConfiguration s_automator;
/*     */   private static WorkloadEnvironmentConfiguration s_environment;
/*     */   
/*     */   public static class SUTConfiguration { private String _host;
/*     */     
/*  13 */     private SUTConfiguration(String host, int port, String screenName, String userId, String password) { 
				this._host = host;
/*  14 */       this._port = port;
/*  15 */       this._screenName = screenName;
/*  16 */       this._password = password;
/*  17 */       this._userId = userId;
/*     */     }

			private SUTConfiguration(String host, int port, String screenName, String userId, String password, Object par) { 
				this._host = host;
/*  14 */       this._port = port;
/*  15 */       this._screenName = screenName;
/*  16 */       this._password = password;
/*  17 */       this._userId = userId;
/*     */     }
/*     */     
/*     */     public String host() {
/*  21 */       return this._host;
/*     */     }
/*     */     
/*     */     public int port() {
/*  25 */       return this._port;
/*     */     }
/*     */     
/*     */     public String screenName() {
/*  29 */       return this._screenName;
/*     */     }
/*     */     
/*     */     private int _port;
/*  33 */     public String password() { return this._password; }
/*     */     
/*     */     private String _screenName;
/*     */     
/*  37 */     public String userId() { return this._userId; }
/*     */     
/*     */     private String _password;
/*     */     private String _userId;
/*     */   }
/*     */   
/*     */   public static class AutomatorConfiguration {
/*     */     private int _port;
/*     */     private long _start_wait;
/*     */     private long _shutdown_wait;
/*     */     
/*     */     private AutomatorConfiguration(int port, long start_wait, long shutdown_wait, boolean start_execute, boolean shutdown_execute, boolean faults_enabled, String saveFilesIn) {
/*  49 */       this._port = port;
/*  50 */       this._start_wait = start_wait;
/*  51 */       this._shutdown_wait = shutdown_wait;
/*  52 */       this._start_execute = start_execute;
/*  53 */       this._shutdown_execute = shutdown_execute;
/*  54 */       this._faults_enabled = faults_enabled;
/*  55 */       this._saveFilesIn = new java.io.File(saveFilesIn);
/*  56 */       if (!this._saveFilesIn.exists()) {
/*  57 */         System.out.println("Creating files repository in: " + this._saveFilesIn.getAbsolutePath());
/*  58 */         this._saveFilesIn.mkdirs();
/*     */       }
/*     */     }
/*     */     
/*     */     public int port() {
/*  63 */       return this._port;
/*     */     }
/*     */     
/*     */     public long startWaiting() {
/*  67 */       return this._start_wait;
/*     */     }
/*     */     
/*     */     public long shutdownWaiting() {
/*  71 */       return this._shutdown_wait;
/*     */     }
/*     */     
/*     */     public boolean executeStart() {
/*  75 */       return this._start_execute;
/*     */     }
/*     */     
/*     */     public boolean executeShutdown() {
/*  79 */       return this._shutdown_execute;
/*     */     }
/*     */     
/*     */     public boolean faultsEnabled() {
/*  83 */       return this._faults_enabled;
/*     */     }
/*     */     
/*     */     public java.io.File saveFilesIn() {
/*  87 */       return this._saveFilesIn;
/*     */     }
/*     */     
/*     */     private boolean _start_execute;
/*     */     private boolean _shutdown_execute;
/*     */     private boolean _faults_enabled;
/*     */     private java.io.File _saveFilesIn;
/*     */   }
/*     */   
/*     */   public static class WorkloadEnvironmentConfiguration {
/*     */     private int _iterations;
/*     */     private long _sleepTime;
/*     */     
/*     */     private WorkloadEnvironmentConfiguration(int iterations, long sleepTime) {
/* 101 */       this._iterations = iterations;
/* 102 */       this._sleepTime = sleepTime;
/*     */     }
/*     */     
/*     */     public int iterations() {
/* 106 */       return this._iterations;
/*     */     }
/*     */     
/*     */ 
/* 110 */     public long sleepTime() { return this._sleepTime; } }
/*     */   
/*     */   public static class FaultsDBConfiguration { private String _host;
/*     */     private int _port;
/*     */     private String _userName;
/*     */     private String _password;
/*     */     private String _dbName;
/*     */     private boolean _useTransaction;
/*     */     
/* 119 */     private FaultsDBConfiguration(String host, int port, String dbName, String userName, String password, boolean useTransaction) { this._host = host;
/* 120 */       this._port = port;
/* 121 */       this._userName = userName;
/* 122 */       this._password = password;
/* 123 */       this._dbName = dbName;
/*     */     }
/*     */     
/*     */     public String host() {
/* 127 */       return this._host;
/*     */     }
/*     */     
/*     */     public int port() {
/* 131 */       return this._port;
/*     */     }
/*     */     
/*     */     public String userName() {
/* 135 */       return this._userName;
/*     */     }
/*     */     
/*     */     public String password() {
/* 139 */       return this._password;
/*     */     }
/*     */     
/*     */     public String dbName() {
/* 143 */       return this._dbName;
/*     */     }
/*     */     
/*     */     public boolean useTransaction() {
/* 147 */       return this._useTransaction;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class FaultsConfiguration
/*     */   {
/*     */     private String _query;
/*     */     
/*     */ 
/*     */     private FaultsConfiguration(String query)
/*     */     {
/* 160 */       this._query = query;
/*     */     }
/*     */     
/*     */     public String query() {
/* 164 */       return this._query;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static
/*     */   {
/* 171 */     Properties prop = new Properties();
/* 172 */     java.io.InputStream input = null;
/*     */     try
/*     */     {
/* 175 */       input = new java.io.FileInputStream("workloadGenerator.properties");
/*     */       
/* 177 */       prop.load(input);
/*     */       
/* 179 */       s_sut = createSut(prop);
/* 180 */       s_faults_db = createFaultsDb(prop);
/* 181 */       s_faults_cfg = createFaultsCfg(prop);
/* 182 */       s_automator = createAutomator(prop);
/* 183 */       s_environment = createEnvironment(prop);
/*     */     } catch (java.io.FileNotFoundException e) {
/* 185 */       System.out.println("workloadGenerator.properties is missing. " + e.getMessage());
/* 186 */       System.exit(-1);
/*     */     } catch (java.io.IOException e) {
/* 188 */       System.out.println("workloadGenerator.properties can't be read. " + e.getMessage());
/* 189 */       e.printStackTrace(System.err);
/* 190 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static SUTConfiguration sut() {
/* 195 */     return s_sut;
/*     */   }
/*     */   
/*     */   public static AutomatorConfiguration automator() {
/* 199 */     return s_automator;
/*     */   }
/*     */   
/*     */   public static FaultsConfiguration faults() {
/* 203 */     return s_faults_cfg;
/*     */   }
/*     */   
/*     */   public static FaultsDBConfiguration faultsDb() {
/* 207 */     return s_faults_db;
/*     */   }
/*     */   
/*     */   public static WorkloadEnvironmentConfiguration environment() {
/* 211 */     return s_environment;
/*     */   }
/*     */   
/*     */   private static SUTConfiguration createSut(Properties prop) {
/* 215 */     String host = prop.getProperty("SUT.host");
/* 216 */     int port = Integer.parseInt(prop.getProperty("SUT.port"));
/* 217 */     String screenName = prop.getProperty("SUT.liferay.screenName");
/* 218 */     String password = prop.getProperty("SUT.liferay.password");
/* 219 */     String userId = prop.getProperty("SUT.liferay.userId");
/* 220 */     return new SUTConfiguration(host, port, screenName, userId, password, null);
/*     */   }
/*     */   
/*     */   private static FaultsDBConfiguration createFaultsDb(Properties prop) {
/* 224 */     String host = prop.getProperty("faults.db.host");
/* 225 */     String dbName = prop.getProperty("faults.db.name");
/* 226 */     int port = Integer.parseInt(prop.getProperty("faults.db.port"));
/* 227 */     String userName = prop.getProperty("faults.db.userName");
/* 228 */     String password = prop.getProperty("faults.db.password");
/* 229 */     return new FaultsDBConfiguration(host, port, dbName, userName, password, false);
/*     */   }
/*     */   
/*     */   private static FaultsConfiguration createFaultsCfg(Properties prop) {
/* 233 */     String query = prop.getProperty("faults.query", "select * from (dim_workloads INNER JOIN activable_faults USING(dim_workload_id)) INNER JOIN dim_injectable_faults USING(dim_injectable_fault_id) where fileName=?");
/* 234 */     return new FaultsConfiguration(query);
/*     */   }
/*     */   
/*     */   private static AutomatorConfiguration createAutomator(Properties prop) {
/* 238 */     int port = Integer.parseInt(prop.getProperty("automator.port"));
/* 239 */     long start_wait = Long.parseLong(prop.getProperty("automator.start.wait"));
/* 240 */     long shutdown_wait = Long.parseLong(prop.getProperty("automator.shutdown.wait"));
/* 241 */     boolean start_execute = Boolean.parseBoolean(prop.getProperty("automator.start.execute"));
/* 242 */     boolean shutdown_execute = Boolean.parseBoolean(prop.getProperty("automator.shutdown.execute"));
/* 243 */     boolean faults_enabled = Boolean.parseBoolean(prop.getProperty("automator.faults.enabled"));
/* 244 */     String saveFilesIn = prop.getProperty("automator.files.saveIn");
/* 245 */     return new AutomatorConfiguration(port, start_wait, shutdown_wait, start_execute, shutdown_execute, faults_enabled, saveFilesIn);
/*     */   }
/*     */   
/*     */   private static WorkloadEnvironmentConfiguration createEnvironment(Properties prop) {
/* 249 */     int iterations = Integer.parseInt(prop.getProperty("workload.environment.iterations", "1"));
/* 250 */     long sleepTime = Integer.parseInt(prop.getProperty("workload.environment.sleepTime", "0"));
/* 251 */     return new WorkloadEnvironmentConfiguration(iterations, sleepTime);
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\WorkloadConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */