/*     */ package com.resiltech.testing_box;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class DataLoggerClient {
/*     */   private PrintStream _outputStream;
/*     */   private java.io.BufferedReader _inputStream;
/*     */   private java.net.Socket _clientSocket;
/*     */   private String _host;
/*     */   private boolean _isConnected;
/*     */   private int _port;
/*     */   private java.io.File _saveFileInto;
/*     */   
/*     */   private static class LocalFileData {
/*     */     String _remoteFilePath;
/*     */     String _localFilePath;
/*     */     
/*     */     public LocalFileData(String remoteFilePath, String localFilePath) {
/*  19 */       this._remoteFilePath = remoteFilePath;
/*  20 */       this._localFilePath = localFilePath;
/*     */     }
/*     */     
/*     */     public String remoteFilePath() {
/*  24 */       return this._remoteFilePath;
/*     */     }
/*     */     
/*     */     public String localFilePath() {
/*  28 */       return this._localFilePath;
/*     */     }
/*     */   }
/*     */   
/*     */   public class FileCollectionSender
/*     */   {
/*     */     private java.util.ArrayList<DataLoggerClient.LocalFileData> _files;
/*     */     
/*     */     private FileCollectionSender() {
/*  37 */       this._files = new java.util.ArrayList();
/*     */     }
/*     */     
/*     */     public void appendFile(String remoteFilePath, byte[] fileContent) throws java.io.IOException {
/*  41 */       java.io.File saveFileTo = DataLoggerClient.this.getUniquePath(remoteFilePath);
/*     */       try {
/*  43 */         java.io.FileOutputStream fos = new java.io.FileOutputStream(saveFileTo);
/*  44 */         fos.write(fileContent);
/*  45 */         fos.close();
/*     */       } catch (java.io.IOException e) {
/*  47 */         System.out.println("An error occourred saving \"" + remoteFilePath + "\" into the local file: " + saveFileTo.getAbsolutePath() + "\nDue to: " + e.getMessage());
/*  48 */         e.printStackTrace(System.err);
/*  49 */         throw e;
/*     */       }
/*     */       
/*  52 */       this._files.add(new DataLoggerClient.LocalFileData(remoteFilePath, saveFileTo.getAbsolutePath()));
/*     */     }
/*     */     
/*     */     public synchronized void send() throws java.io.IOException {
/*  56 */       DataLoggerClient.this._outputStream.println("files");
/*  57 */       DataLoggerClient.this._outputStream.println(Integer.toString(this._files.size()));
/*  58 */       System.out.println("\tnumber of files: " + this._files.size());
/*  59 */       for (DataLoggerClient.LocalFileData f : this._files) {
/*  60 */         System.out.println("\t" + f.remoteFilePath());
/*  61 */         DataLoggerClient.this._outputStream.println(f.remoteFilePath());
/*  62 */         DataLoggerClient.this._outputStream.println(f.localFilePath().length());
/*  63 */         DataLoggerClient.this._outputStream.print(f.localFilePath());
/*     */       }
/*  65 */       DataLoggerClient.this.readResponse();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public DataLoggerClient(String host, int port, java.io.File saveFileInto)
/*     */   {
/*  72 */     this._host = host;
/*  73 */     this._port = port;
/*  74 */     this._saveFileInto = saveFileInto;
/*  75 */     this._isConnected = false;
/*     */   }
/*     */   
/*     */   public void open() throws java.net.UnknownHostException, java.io.IOException {
/*  79 */     System.out.print("Connecting to DataLogging: ");
/*  80 */     this._clientSocket = new java.net.Socket(this._host, this._port);
/*  81 */     this._outputStream = new PrintStream(this._clientSocket.getOutputStream());
/*  82 */     this._inputStream = new java.io.BufferedReader(new java.io.InputStreamReader(this._clientSocket.getInputStream()));
/*  83 */     System.out.println("Success");
/*  84 */     this._isConnected = true;
/*     */   }
/*     */   
/*     */   public synchronized void start(String workloadName) throws java.io.IOException {
/*  88 */     System.out.println("Executing start.sh on the SUT.");
/*  89 */     this._outputStream.println("START#" + workloadName + "#" + System.currentTimeMillis());
/*  90 */     readResponse();
/*     */   }
/*     */   
/*     */   public FileCollectionSender fileMessage() {
/*  94 */     return new FileCollectionSender();
/*     */   }
/*     */   
/*     */   public synchronized void notifyActualStart() throws java.io.IOException {
/*  98 */     this._outputStream.println("ACTUALSTART#" + System.currentTimeMillis());
/*  99 */     readResponse();
/*     */   }
/*     */   
/*     */   public synchronized void notifyActualEnd() throws java.io.IOException {
/* 103 */     this._outputStream.println("ACTUALEND#" + System.currentTimeMillis());
/* 104 */     readResponse();
/*     */   }
/*     */   
/*     */   public synchronized void notifyInvocation(java.net.URL requestedUrl, long requestTimestamp, String requestContent, long responseTimestamp, int responseCode, String responseContent) throws java.io.IOException
/*     */   {
/* 109 */     this._outputStream.println("invocation");
/* 110 */     this._outputStream.println(requestedUrl.toString());
/* 111 */     this._outputStream.println(Long.toString(requestTimestamp) + "#" + Integer.toString(requestContent.length()));
/* 112 */     this._outputStream.write(requestContent.getBytes());
/* 113 */     this._outputStream.println(Long.toString(responseTimestamp) + "#" + Integer.toString(responseCode) + "#" + Integer.toString(responseContent.length()));
/* 114 */     this._outputStream.write(responseContent.getBytes());
/* 115 */     readResponse();
/*     */   }
/*     */   
/*     */   public synchronized void notifyPenetrationResult(String field, String type, String code) throws java.io.IOException {
/* 119 */     this._outputStream.println("PENETRATION#" + field + "#" + type + "#" + code);
/* 120 */     readResponse();
/*     */   }
/*     */   
/*     */   public synchronized void notifyRobustnessResult(String field, String type, String code) throws java.io.IOException {
/* 124 */     this._outputStream.println("ROBUSTNESS#" + field + "#" + type + "#" + code);
/* 125 */     readResponse();
/*     */   }
/*     */   
/*     */   public synchronized void close() throws java.io.IOException {
/* 129 */     this._isConnected = false;
/* 130 */     this._outputStream.close();
/* 131 */     this._inputStream.close();
/* 132 */     this._clientSocket.close();
/*     */   }
/*     */   
/*     */   public synchronized void quit() {
/* 136 */     this._outputStream.println("QUIT");
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 140 */     return this._isConnected;
/*     */   }
/*     */   
/*     */   public synchronized void end(String workloadName) throws java.io.IOException {
/* 144 */     System.out.println("Executing shutdown.sh on the SUT.");
/* 145 */     this._outputStream.println("END#" + workloadName + "#" + System.currentTimeMillis());
/* 146 */     readResponse();
/*     */   }
/*     */   
/*     */   private void readResponse() throws java.io.IOException {
/* 150 */     String responseLine = this._inputStream.readLine();
/*     */     
/* 152 */     System.out.println("Response: " + responseLine);
/*     */   }
/*     */   
/*     */   private java.io.File getUniquePath(String fileName) {
/* 156 */     java.io.File readfile = new java.io.File(fileName);
/* 157 */     final String baseName = readfile.getName();
/* 158 */     return new java.io.File(this._saveFileInto, 
/* 159 */       readfile.getName() + this._saveFileInto.listFiles(new java.io.FileFilter()
/*     */       {
/*     */         public boolean accept(java.io.File pathname) {
/* 162 */           return pathname.getName().startsWith(baseName);
/*     */         }
/*     */       }).length);
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\DataLoggerClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */