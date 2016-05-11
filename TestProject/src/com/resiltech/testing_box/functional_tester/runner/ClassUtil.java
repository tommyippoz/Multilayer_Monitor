/*    */ package com.resiltech.testing_box.functional_tester.runner;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Enumeration;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.jar.JarInputStream;
/*    */ 
/*    */ class ClassUtil
/*    */ {
/*    */   static Set<Class<?>> getClasses(String packageName) throws Exception
/*    */   {
/* 15 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 16 */     return getClasses(loader, packageName);
/*    */   }
/*    */   
/*    */   static Set<Class<?>> getClasses(ClassLoader loader, String packageName) throws ClassNotFoundException, IOException
/*    */   {
/* 21 */     Set<Class<?>> classes = new HashSet();
/* 22 */     String path = packageName.replace('.', '/');
/* 23 */     Enumeration<URL> resources = loader.getResources(path);
/* 24 */     if (resources != null) {
/* 25 */       while (resources.hasMoreElements()) {
/* 26 */         String filePath = ((URL)resources.nextElement()).getFile();
/*    */         
/* 28 */         if (filePath.indexOf("%20") > 0)
/* 29 */           filePath = filePath.replaceAll("%20", " ");
/* 30 */         if (filePath != null) {
/* 31 */           if (((filePath.indexOf("!") > 0 ? 1 : 0) & (filePath.indexOf(".jar") > 0 ? 1 : 0)) != 0) {
/* 32 */             String jarPath = filePath.substring(0, filePath.indexOf("!"))
/* 33 */               .substring(filePath.indexOf(":") + 1);
/*    */             
/* 35 */             if (jarPath.indexOf(":") >= 0) jarPath = jarPath.substring(1);
/* 36 */             classes.addAll(getFromJARFile(jarPath, path));
/*    */           } else {
/* 38 */             classes.addAll(
/* 39 */               getFromDirectory(new File(filePath), packageName));
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 44 */     return classes;
/*    */   }
/*    */   
/*    */   static Set<Class<?>> getFromDirectory(File directory, String packageName) throws ClassNotFoundException {
/* 48 */     Set<Class<?>> classes = new HashSet();
/* 49 */     if (directory.exists()) { String[] arrayOfString;
/* 50 */       int j = (arrayOfString = directory.list()).length; for (int i = 0; i < j; i++) { String file = arrayOfString[i];
/* 51 */         if (file.endsWith(".class")) {
/* 52 */           String name = packageName + '.' + stripFilenameExtension(file);
/* 53 */           Class<?> clazz = Class.forName(name);
/* 54 */           classes.add(clazz);
/*    */         }
/*    */       }
/*    */     }
/* 58 */     return classes;
/*    */   }
/*    */   
/*    */   static Set<Class<?>> getFromJARFile(String jar, String packageName) throws IOException, ClassNotFoundException {
/* 62 */     Set<Class<?>> classes = new HashSet();
/* 63 */     JarInputStream jarFile = new JarInputStream(new java.io.FileInputStream(jar));
/*    */     java.util.jar.JarEntry jarEntry;
/*    */     do {
/* 66 */       jarEntry = jarFile.getNextJarEntry();
/* 67 */       if (jarEntry != null) {
/* 68 */         String className = jarEntry.getName();
/* 69 */         if (className.endsWith(".class")) {
/* 70 */           className = stripFilenameExtension(className);
/* 71 */           if (className.startsWith(packageName))
/* 72 */             classes.add(Class.forName(className.replace('/', '.')));
/*    */         }
/*    */       }
/* 75 */     } while (jarEntry != null);
/* 76 */     jarFile.close();
/* 77 */     return classes;
/*    */   }
/*    */   
/*    */   private static String stripFilenameExtension(String filename)
/*    */   {
/* 82 */     int i = filename.lastIndexOf('.');
/* 83 */     if (i > 0) {
/* 84 */       return filename.substring(0, i);
/*    */     }
/* 86 */     return filename;
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\runner\ClassUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */