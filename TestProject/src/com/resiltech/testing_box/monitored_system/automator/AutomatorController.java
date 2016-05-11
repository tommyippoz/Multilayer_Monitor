/*     */ package com.resiltech.testing_box.monitored_system.automator;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class AutomatorController
/*     */ {
/*     */   private String _host;
/*     */   private int _port;
/*     */   
/*     */   public static class RemoteFile
/*     */   {
/*     */     private String _fileName;
/*     */     private String _content;
/*     */     
/*     */     public RemoteFile(String fileName, String content)
/*     */     {
/*  17 */       this._fileName = fileName;
/*  18 */       this._content = content;
/*     */     }
/*     */     
/*     */     public String fileName() {
/*  22 */       return this._fileName;
/*     */     }
/*     */     
/*     */ 
/*  26 */     public String content() { return this._content; }
/*     */   }
/*     */   
/*     */   public static class DbConfiguration { private String _host;
/*     */     private int _port;
/*     */     private String _dbName;
/*     */     private String _userName;
/*     */     private String _password;
/*     */     
/*  35 */     public DbConfiguration(String host, int port, String dbName, String userName, String password) { 
				this._host = host;
/*  36 */       this._port = port;
/*  37 */       this._dbName = dbName;
/*  38 */       this._userName = userName;
/*  39 */       this._password = password;
/*     */     }
/*     */     
/*     */     public String host() {
/*  43 */       return this._host;
/*     */     }
/*     */     
/*     */     public int port() {
/*  47 */       return this._port;
/*     */     }
/*     */     
/*     */     public String dbName() {
/*  51 */       return this._dbName;
/*     */     }
/*     */     
/*     */     public String userName() {
/*  55 */       return this._userName;
/*     */     }
/*     */     
/*     */     public String password() {
/*  59 */       return this._password;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AutomatorController(String host, int port, DbConfiguration conf, String query)
/*     */   {
/*  70 */     this._host = host;
/*  71 */     this._port = port;
/*     */     try {
/*  73 */       this._clientSocket = new java.net.Socket(this._host, this._port);
/*  74 */       this._os = this._clientSocket.getOutputStream();
/*  75 */       this._is = this._clientSocket.getInputStream();
/*     */     } catch (IOException e) {
/*  77 */       System.out.println("MonitoredSystemAutomator doesn't respond properly or at all: " + e.getMessage());
/*  78 */       e.printStackTrace(System.err);
/*  79 */       System.exit(1);
/*     */     }
/*     */     
/*  82 */     this._conf = conf;
/*  83 */     this._query = query;
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/*  87 */     println("start");
/*  88 */     readLine();
/*     */   }
/*     */   
/*  91 */   public void shutdown() throws IOException { println("shutdown");
/*  92 */     readLine();
/*     */   }
/*     */   
/*     */   private void println(String s) throws IOException {
/*  96 */     this._os.write(s.getBytes());
/*  97 */     this._os.write(new byte[] { 10 });
/*     */   }
/*     */   
/*     */   private String readLine() throws IOException {
/* 101 */     byte[] buffer = new byte[1];
/* 102 */     int readData = 0;
/* 103 */     boolean incompleteReply = true;
/* 104 */     StringBuilder s = new StringBuilder();
/*     */     
/* 106 */     while ((readData != -1) && (incompleteReply)) {
/* 107 */       readData = this._is.read(buffer);
/* 108 */       if (readData > 0) {
/* 109 */         if (buffer[0] == 10) {
/* 110 */           incompleteReply = false;
/*     */         } else {
/* 112 */           s.append((char)buffer[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 117 */     return s.toString();
/*     */   }
/*     */   
/*     */   public RemoteFile[] files() throws IOException {
/* 121 */     java.util.ArrayList<RemoteFile> files = new java.util.ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 126 */     println("files");
/* 127 */     String numberOfFilesAsString = readLine();
/* 128 */     int numberOfFiles = Integer.parseInt(numberOfFilesAsString);
/* 129 */     for (int i = 0; i < numberOfFiles; i++) {
/* 130 */       String fileName = readLine();
/* 131 */       String fileLengthAsString = readLine();
/* 132 */       int readContentData = 0;
/* 133 */       int totalContentData = 0;
/* 134 */       int fileLength = Integer.parseInt(fileLengthAsString);
/* 135 */       byte[] buffer = new byte[fileLength];
/*     */       
/* 137 */       while (totalContentData < fileLength) {
/* 138 */         readContentData = this._is.read(buffer, totalContentData, fileLength - totalContentData);
/* 139 */         totalContentData += readContentData;
/*     */       }
/* 141 */       files.add(new RemoteFile(fileName, new String(buffer)));
/*     */     }
/* 143 */     String reply = readLine();
/*     */     
/* 145 */     return (RemoteFile[])files.toArray(new RemoteFile[files.size()]);
/*     */   }
/*     */   
/*     */   public boolean injectFaults(String fileName) throws IOException {
/* 149 */     String dbUrl = "jdbc:mysql://" + this._conf.host() + ":" + this._conf.port() + "/" + this._conf.dbName();
/*     */     
/* 151 */     String reply = "";
/* 152 */     boolean finishedCorrectly = false;
/*     */     try
/*     */     {
/* 155 */       java.sql.Connection conn = java.sql.DriverManager.getConnection(dbUrl, this._conf.userName(), this._conf.password());
/*     */       
/* 157 */       java.sql.PreparedStatement st = conn.prepareStatement(this._query + " ORDER BY code ASC");
/*     */       
/* 159 */       st.setString(1, fileName);
/* 160 */       java.sql.ResultSet rs = st.executeQuery();
/*     */       
/* 162 */       println("clean");
/* 163 */       reply = readLine();
/*     */       
/* 165 */       boolean resetLastCode = true;
/* 166 */       int lastCode = 0;
/* 167 */       int lastEndSequence = 0;
/* 168 */       int lastWrittenCode = -1;
/* 169 */       while (rs.next())
/*     */       {
/* 171 */         int currentCode = rs.getInt("code");
/* 172 */         if (resetLastCode) {
/* 173 */           lastCode = currentCode;
/* 174 */           lastEndSequence = lastCode;
/* 175 */           resetLastCode = false;
/*     */         }
/* 177 */         else if (lastEndSequence + 1 != currentCode) {
/* 178 */           if (lastCode < lastEndSequence) {
/* 179 */             println("range");
/* 180 */             reply = readLine();
/* 181 */             println(Integer.toString(lastCode));
/* 182 */             reply = readLine();
/* 183 */             println(Integer.toString(lastEndSequence));
/* 184 */             reply = readLine();
/* 185 */             lastWrittenCode = lastEndSequence;
/* 186 */             resetLastCode = true;
/*     */           } else {
/* 188 */             println("set");
/* 189 */             reply = readLine();
/* 190 */             println(Integer.toString(lastCode));
/* 191 */             reply = readLine();
/*     */             
/* 193 */             println("set");
/* 194 */             reply = readLine();
/* 195 */             println(Integer.toString(currentCode));
/* 196 */             reply = readLine();
/*     */             
/* 198 */             lastWrittenCode = lastEndSequence;
/* 199 */             resetLastCode = true;
/*     */           }
/*     */         } else {
/* 202 */           lastEndSequence = currentCode;
/*     */         }
/*     */       }
/*     */       
/* 206 */       if (lastWrittenCode != lastEndSequence) {
/* 207 */         if (lastCode < lastEndSequence) {
/* 208 */           println("range");
/* 209 */           reply = readLine();
/* 210 */           println(Integer.toString(lastCode));
/* 211 */           reply = readLine();
/* 212 */           println(Integer.toString(lastEndSequence));
/* 213 */           reply = readLine();
/* 214 */           lastWrittenCode = lastEndSequence;
/*     */         } else {
/* 216 */           println("set");
/* 217 */           reply = readLine();
/* 218 */           println(Integer.toString(lastCode));
/* 219 */           reply = readLine();
/* 220 */           lastWrittenCode = lastEndSequence;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 225 */       st.close();
/* 226 */       finishedCorrectly = true;
/*     */     } catch (java.sql.SQLException e) {
/* 228 */       System.err.println("An exception has been raised on fetching activable_faults of: " + fileName);
/* 229 */       e.printStackTrace(System.err);
/*     */     }
/*     */     
/* 232 */     return finishedCorrectly;
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     try {
/* 237 */       println("close");
/* 238 */       readLine();
/* 239 */       this._os.close();
/* 240 */       this._is.close();
/* 241 */       this._clientSocket.close();
/*     */     } catch (IOException e) {
/* 243 */       e.printStackTrace(System.err);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 250 */   private java.net.Socket _clientSocket = null;
/* 251 */   private DbConfiguration _conf = null;
/* 252 */   private java.io.InputStream _is = null;
/* 253 */   private java.io.OutputStream _os = null;
/* 254 */   private String _query = "select * from (dim_workloads INNER JOIN activable_faults USING(dim_workload_id)) INNER JOIN dim_injectable_faults USING(dim_injectable_fault_id) where fileName=?";
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\monitored_system\automator\AutomatorController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */