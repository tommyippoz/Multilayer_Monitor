/*    */ package com.resiltech.testing_box.function_tester.socket;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class MethodsForSocket
/*    */ {
/*    */   private static final byte ETX = 3;
/*    */   public static final int CHUNK = 1024;
/*    */   public static final int TIMEOUT = 15000;
/*    */   public static final int WAIT = 200;
/*    */   
/*    */   public static String read(DataInputStream stream) throws IOException
/*    */   {
/* 17 */     int count = 0;
/* 18 */     byte[] buffer = new byte['€'];
/* 19 */     while (stream.available() != 0)
/*    */     {
/* 21 */       byte b = stream.readByte();
/* 22 */       if (b == 3)
/*    */       {
/* 24 */         System.out.println("Arrivato terminatore messaggio");
/* 25 */         break;
/*    */       }
/* 27 */       buffer[count] = b;
/*    */       
/* 29 */       count++;
/* 30 */       if (count > 1024)
/*    */       {
/* 32 */         System.out.println("Messaggio NON coerente");
/* 33 */         return null;
/*    */       }
/*    */     }
/*    */     
/* 37 */     return new String(buffer, 0, count);
/*    */   }
/*    */   
/*    */   public static void write(DataOutputStream stream, String message) throws IOException
/*    */   {
/* 42 */     System.out.println("Scritto <" + message + ">");
/* 43 */     stream.writeBytes(message);
/* 44 */     stream.writeByte(3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\function_tester\socket\MethodsForSocket.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */