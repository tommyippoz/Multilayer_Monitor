/*     */ package com.resiltech.testing_box.function_tester.socket;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketClient
/*     */ {
/*  14 */   private Socket clientSocket = null;
/*  15 */   private int sizeFile = 0;
/*     */   private DataInputStream inFromServer;
/*     */   
/*     */   public SocketClient(String address, int port)
/*     */     throws IOException
/*     */   {
/*  21 */     this.clientSocket = new Socket(address, port);
/*  22 */     this.clientSocket.setSoTimeout(5000);
/*     */   }
/*     */   
/*     */   public void closeSocket() throws IOException
/*     */   {
/*  27 */     if (this.clientSocket != null) {
/*  28 */       this.clientSocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public String sendMsg(String message) throws IOException, InterruptedException
/*     */   {
/*  34 */     if (this.clientSocket == null) {
/*  35 */       return null;
/*     */     }
/*     */     
/*  38 */     DataOutputStream outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
/*  39 */     this.inFromServer = new DataInputStream(this.clientSocket.getInputStream());
/*     */     
/*     */ 
/*  42 */     System.out.println("Invio messaggio: " + message);
/*  43 */     MethodsForSocket.write(outToServer, message);
/*     */     
/*     */ 
/*     */ 
/*  47 */     int timerSocket = 0;
/*     */     for (;;)
/*     */     {
/*  50 */       if (timerSocket > 15000)
/*     */       {
/*  52 */         System.out.println("Passato troppo tempo senza comunicazione sul socket -> Termino");
/*  53 */         this.clientSocket.close();
/*  54 */         return null;
/*     */       }
/*     */       
/*  57 */       String serverSentence = MethodsForSocket.read(this.inFromServer);
/*  58 */       if (serverSentence.length() > 0)
/*     */       {
/*  60 */         System.out.println("Arrivato messaggio dal server: <" + serverSentence + ">");
/*  61 */         timerSocket = 0;
/*  62 */         return serverSentence;
/*     */       }
/*     */       
/*  65 */       Thread.sleep(200L);
/*  66 */       timerSocket += 200;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void getFile(String remoteFile, String localFile)
/*     */     throws IOException, InterruptedException
/*     */   {
/*  74 */     System.out.println("Download file remoto " + remoteFile + " -> " + localFile);
/*  75 */     String response = sendMsg("DOWNLOAD_FILE|" + remoteFile);
/*     */     
/*  77 */     System.out.println("Analisi risposta dal server <" + response + ">");
/*  78 */     String[] parts = response.split("\\|");
/*  80 */     if (parts[0].equals("DOWNLOAD_FILE"))
/*     */     {
/*     */ 
/*     */ 
/*  84 */       this.sizeFile = Integer.parseInt(parts[1]);
/*     */       int sizeBuff;
/*  86 */       if (this.sizeFile > 10240) {
/*  87 */         sizeBuff = 10240;
/*     */       } else {
/*  89 */         sizeBuff = this.sizeFile;
/*     */       }
/*  91 */       System.out.println("In arrivo file di " + parts[1] + " bytes");
/*     */       try
/*     */       {
/*  94 */         FileOutputStream outStream = new FileOutputStream(localFile);
/*  95 */         byte[] buffer = new byte[sizeBuff];
/*  96 */         int bytesRead = 0;int counter = 0;
/*     */         
/*  98 */         while (bytesRead >= 0)
/*     */         {
/* 100 */           bytesRead = this.inFromServer.read(buffer);
/* 101 */           if (bytesRead >= 0)
/*     */           {
/* 103 */             outStream.write(buffer, 0, bytesRead);
/* 104 */             counter += bytesRead;
/* 105 */             System.out.println("Byte totali ricevuti: " + counter + " (" + this.sizeFile + ")");
/* 106 */             outStream.flush();
/*     */           }
/*     */           
/* 109 */           if (counter >= this.sizeFile) {
/*     */             break;
/*     */           }
/*     */         }
/* 113 */         System.out.println("Download completato!");
/* 114 */         outStream.close();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 118 */         System.out.println("Errore download file!");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\function_tester\socket\SocketClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */