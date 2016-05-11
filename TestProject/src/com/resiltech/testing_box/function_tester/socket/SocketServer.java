/*     */ package com.resiltech.testing_box.function_tester.socket;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class SocketServer
/*     */ {
/*     */   private static final int PORT = 21974;
/*     */   private static DataOutputStream outToClient;
/*     */   
/*     */   /* Error */
/*     */   public static void main(String[] args)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   3: ldc 28
/*     */     //   5: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   8: aconst_null
/*     */     //   9: astore_1
/*     */     //   10: new 36	java/net/ServerSocket
/*     */     //   13: dup
/*     */     //   14: sipush 21974
/*     */     //   17: invokespecial 38	java/net/ServerSocket:<init>	(I)V
/*     */     //   20: astore_1
/*     */     //   21: goto +8 -> 29
/*     */     //   24: astore_2
/*     */     //   25: aload_2
/*     */     //   26: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   29: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   32: ldc 46
/*     */     //   34: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   37: aconst_null
/*     */     //   38: astore_2
/*     */     //   39: aload_1
/*     */     //   40: invokevirtual 48	java/net/ServerSocket:accept	()Ljava/net/Socket;
/*     */     //   43: astore_2
/*     */     //   44: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   47: ldc 52
/*     */     //   49: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   52: new 54	java/io/DataInputStream
/*     */     //   55: dup
/*     */     //   56: aload_2
/*     */     //   57: invokevirtual 56	java/net/Socket:getInputStream	()Ljava/io/InputStream;
/*     */     //   60: invokespecial 62	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   63: astore_3
/*     */     //   64: new 65	java/io/DataOutputStream
/*     */     //   67: dup
/*     */     //   68: aload_2
/*     */     //   69: invokevirtual 67	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
/*     */     //   72: invokespecial 71	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   75: putstatic 74	com/resiltech/testing_box/function_tester/socket/SocketServer:outToClient	Ljava/io/DataOutputStream;
/*     */     //   78: iconst_0
/*     */     //   79: istore 4
/*     */     //   81: iload 4
/*     */     //   83: sipush 15000
/*     */     //   86: if_icmple +18 -> 104
/*     */     //   89: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   92: ldc 76
/*     */     //   94: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   97: aload_2
/*     */     //   98: invokevirtual 78	java/net/Socket:close	()V
/*     */     //   101: goto +156 -> 257
/*     */     //   104: aload_3
/*     */     //   105: invokestatic 81	com/resiltech/testing_box/function_tester/socket/MethodsForSocket:read	(Ljava/io/DataInputStream;)Ljava/lang/String;
/*     */     //   108: astore 5
/*     */     //   110: aload 5
/*     */     //   112: invokevirtual 87	java/lang/String:length	()I
/*     */     //   115: ifle +19 -> 134
/*     */     //   118: iconst_0
/*     */     //   119: istore 4
/*     */     //   121: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   124: ldc 93
/*     */     //   126: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   129: aload 5
/*     */     //   131: invokestatic 95	com/resiltech/testing_box/function_tester/socket/SocketServer:parseMessage	(Ljava/lang/String;)V
/*     */     //   134: ldc2_w 98
/*     */     //   137: invokestatic 100	java/lang/Thread:sleep	(J)V
/*     */     //   140: wide
/*     */     //   146: goto -65 -> 81
/*     */     //   149: astore_3
/*     */     //   150: aload_3
/*     */     //   151: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   154: aload_2
/*     */     //   155: invokevirtual 106	java/net/Socket:isClosed	()Z
/*     */     //   158: ifne -129 -> 29
/*     */     //   161: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   164: ldc 110
/*     */     //   166: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   169: aload_2
/*     */     //   170: invokevirtual 78	java/net/Socket:close	()V
/*     */     //   173: goto -144 -> 29
/*     */     //   176: astore 7
/*     */     //   178: aload 7
/*     */     //   180: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   183: goto -154 -> 29
/*     */     //   186: astore_3
/*     */     //   187: aload_3
/*     */     //   188: invokevirtual 112	java/lang/Exception:printStackTrace	()V
/*     */     //   191: aload_2
/*     */     //   192: invokevirtual 106	java/net/Socket:isClosed	()Z
/*     */     //   195: ifne -166 -> 29
/*     */     //   198: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   201: ldc 110
/*     */     //   203: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   206: aload_2
/*     */     //   207: invokevirtual 78	java/net/Socket:close	()V
/*     */     //   210: goto -181 -> 29
/*     */     //   213: astore 7
/*     */     //   215: aload 7
/*     */     //   217: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   220: goto -191 -> 29
/*     */     //   223: astore 6
/*     */     //   225: aload_2
/*     */     //   226: invokevirtual 106	java/net/Socket:isClosed	()Z
/*     */     //   229: ifne +25 -> 254
/*     */     //   232: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   235: ldc 110
/*     */     //   237: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   240: aload_2
/*     */     //   241: invokevirtual 78	java/net/Socket:close	()V
/*     */     //   244: goto +10 -> 254
/*     */     //   247: astore 7
/*     */     //   249: aload 7
/*     */     //   251: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   254: aload 6
/*     */     //   256: athrow
/*     */     //   257: aload_2
/*     */     //   258: invokevirtual 106	java/net/Socket:isClosed	()Z
/*     */     //   261: ifne -232 -> 29
/*     */     //   264: getstatic 22	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   267: ldc 110
/*     */     //   269: invokevirtual 30	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   272: aload_2
/*     */     //   273: invokevirtual 78	java/net/Socket:close	()V
/*     */     //   276: goto -247 -> 29
/*     */     //   279: astore 7
/*     */     //   281: aload 7
/*     */     //   283: invokevirtual 41	java/io/IOException:printStackTrace	()V
/*     */     //   286: goto -257 -> 29
/*     */     // Line number table:
/*     */     //   Java source line #22	-> byte code offset #0
/*     */     //   Java source line #26	-> byte code offset #8
/*     */     //   Java source line #29	-> byte code offset #10
/*     */     //   Java source line #30	-> byte code offset #21
/*     */     //   Java source line #31	-> byte code offset #24
/*     */     //   Java source line #34	-> byte code offset #25
/*     */     //   Java source line #41	-> byte code offset #29
/*     */     //   Java source line #42	-> byte code offset #37
/*     */     //   Java source line #45	-> byte code offset #39
/*     */     //   Java source line #49	-> byte code offset #44
/*     */     //   Java source line #51	-> byte code offset #52
/*     */     //   Java source line #52	-> byte code offset #64
/*     */     //   Java source line #55	-> byte code offset #78
/*     */     //   Java source line #58	-> byte code offset #81
/*     */     //   Java source line #60	-> byte code offset #89
/*     */     //   Java source line #61	-> byte code offset #97
/*     */     //   Java source line #62	-> byte code offset #101
/*     */     //   Java source line #65	-> byte code offset #104
/*     */     //   Java source line #66	-> byte code offset #110
/*     */     //   Java source line #68	-> byte code offset #118
/*     */     //   Java source line #69	-> byte code offset #121
/*     */     //   Java source line #70	-> byte code offset #129
/*     */     //   Java source line #73	-> byte code offset #134
/*     */     //   Java source line #74	-> byte code offset #140
/*     */     //   Java source line #56	-> byte code offset #146
/*     */     //   Java source line #77	-> byte code offset #149
/*     */     //   Java source line #80	-> byte code offset #150
/*     */     //   Java source line #89	-> byte code offset #154
/*     */     //   Java source line #91	-> byte code offset #161
/*     */     //   Java source line #94	-> byte code offset #169
/*     */     //   Java source line #95	-> byte code offset #173
/*     */     //   Java source line #96	-> byte code offset #176
/*     */     //   Java source line #99	-> byte code offset #178
/*     */     //   Java source line #82	-> byte code offset #186
/*     */     //   Java source line #85	-> byte code offset #187
/*     */     //   Java source line #89	-> byte code offset #191
/*     */     //   Java source line #91	-> byte code offset #198
/*     */     //   Java source line #94	-> byte code offset #206
/*     */     //   Java source line #95	-> byte code offset #210
/*     */     //   Java source line #96	-> byte code offset #213
/*     */     //   Java source line #99	-> byte code offset #215
/*     */     //   Java source line #88	-> byte code offset #223
/*     */     //   Java source line #89	-> byte code offset #225
/*     */     //   Java source line #91	-> byte code offset #232
/*     */     //   Java source line #94	-> byte code offset #240
/*     */     //   Java source line #95	-> byte code offset #244
/*     */     //   Java source line #96	-> byte code offset #247
/*     */     //   Java source line #99	-> byte code offset #249
/*     */     //   Java source line #104	-> byte code offset #254
/*     */     //   Java source line #89	-> byte code offset #257
/*     */     //   Java source line #91	-> byte code offset #264
/*     */     //   Java source line #94	-> byte code offset #272
/*     */     //   Java source line #95	-> byte code offset #276
/*     */     //   Java source line #96	-> byte code offset #279
/*     */     //   Java source line #99	-> byte code offset #281
/*     */     //   Java source line #38	-> byte code offset #286
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	289	0	args	String[]
/*     */     //   9	31	1	welcomeSocket	java.net.ServerSocket
/*     */     //   24	2	2	e	IOException
/*     */     //   38	235	2	connectionSocket	java.net.Socket
/*     */     //   63	42	3	inFromClient	java.io.DataInputStream
/*     */     //   149	2	3	e	IOException
/*     */     //   186	2	3	e	Exception
/*     */     //   79	62	4	timerSocket	int
/*     */     //   108	22	5	clientSentence	String
/*     */     //   223	32	6	localObject	Object
/*     */     //   176	3	7	e	IOException
/*     */     //   213	3	7	e	IOException
/*     */     //   247	3	7	e	IOException
/*     */     //   279	3	7	e	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   10	21	24	java/io/IOException
/*     */     //   39	149	149	java/io/IOException
/*     */     //   169	173	176	java/io/IOException
/*     */     //   39	149	186	java/lang/Exception
/*     */     //   206	210	213	java/io/IOException
/*     */     //   39	154	223	finally
/*     */     //   186	191	223	finally
/*     */     //   240	244	247	java/io/IOException
/*     */     //   272	276	279	java/io/IOException
/*     */   }
/*     */   
/*     */   private static void parseMessage(String messaggio)
/*     */     throws Exception
/*     */   {
/* 112 */     String[] parts = messaggio.split("\\|");
/* 113 */     if (parts.length >= 2) {
/*     */       String str;
/* 115 */       switch ((str = parts[0]).hashCode()) {
					case -1117715757:  
						if (str.equals("DOWNLOAD_FILE")) 
							break;  
					case 66230918:  
						if (str.equals("ERASE")) {
/* 117 */           System.out.println("Richiesta cancellazione file " + parts[1]);
/* 118 */           FileOutputStream writer = new FileOutputStream(parts[1]);
/* 119 */           writer.write(new String().getBytes());
/* 120 */           writer.close();
/* 121 */           MethodsForSocket.write(outToClient, "OK");
/* 122 */           return;

/*     */         }
/* 136 */         break;
/*     */       }
/* 138 */       System.out.println("Comando sconosciuto " + parts[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 144 */     MethodsForSocket.write(outToClient, "FAIL");
/*     */   }
/*     */   
/*     */ 
/*     */   private static void downloadFile(File file)
/*     */     throws IOException
/*     */   {
/* 151 */     long fileSize = file.length();
/* 152 */     long completed = 0L;
/*     */     
/*     */ 
/* 155 */     FileInputStream fileStream = new FileInputStream(file);
/*     */     
/*     */ 
/* 158 */     MethodsForSocket.write(outToClient, "DOWNLOAD_FILE|" + fileSize);
/*     */     int buffSize;
/* 161 */     if (fileSize > 10240L) {
/* 162 */       buffSize = 10240;
/*     */     } else {
/* 164 */       buffSize = (int)fileSize;
/*     */     }
/* 166 */     while (completed < fileSize)
/*     */     {
/* 168 */       byte[] buffer = new byte[buffSize];
/* 169 */       int realSize = fileStream.read(buffer);
/* 170 */       outToClient.write(buffer, 0, realSize);
/*     */       
/* 172 */       completed += realSize;
/* 173 */       System.out.println("Byte totali inviati: " + completed + " (" + fileSize + ")");
/*     */     }
/* 175 */     fileStream.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\function_tester\socket\SocketServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */