/*     */ package com.resiltech.soapui;
/*     */ 
/*     */ import com.eviware.soapui.config.WsaConfigConfig;
/*     */ import com.eviware.soapui.config.WsdlRequestConfig;
/*     */ import com.eviware.soapui.impl.WorkspaceImpl;
/*     */ import com.eviware.soapui.impl.WsdlInterfaceFactory;
/*     */ import com.eviware.soapui.impl.wsdl.WsdlProject;
/*     */ import com.eviware.soapui.impl.wsdl.WsdlRequest;
/*     */ import com.eviware.soapui.model.iface.Interface;
/*     */ import com.eviware.soapui.model.iface.Operation;
/*     */ import com.eviware.soapui.model.iface.Request;
/*     */ import com.eviware.soapui.model.iface.Response;
/*     */ import com.eviware.soapui.model.iface.Submit;
/*     */ import com.eviware.soapui.model.iface.SubmitContext;
/*     */ import com.eviware.soapui.support.types.StringToObjectMap;
/*     */ import com.eviware.soapui.support.types.StringToStringMap;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.xmlbeans.XmlException;
/*     */ 
/*     */ public class Main
/*     */ {
/*     */   static class NamespaceContextByHashMap implements javax.xml.namespace.NamespaceContext
/*     */   {
/*     */     private HashMap<String, String> _namespaceMapper;
/*     */     
/*     */     public NamespaceContextByHashMap(HashMap<String, String> namespaceMapper)
/*     */     {
/*  35 */       this._namespaceMapper = namespaceMapper;
/*     */     }
/*     */     
/*     */     public Iterator<?> getPrefixes(String arg0)
/*     */     {
/*  40 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public String getPrefix(String arg0)
/*     */     {
/*  45 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public String getNamespaceURI(String arg0)
/*     */     {
/*  50 */       return (String)this._namespaceMapper.get(arg0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws XmlException, IOException, com.eviware.soapui.support.SoapUIException
/*     */   {
/*  56 */     StringToStringMap options = new StringToStringMap();
/*  57 */     WorkspaceImpl workspace = new WorkspaceImpl("/tmp/prova.xml", options);
/*  58 */     WsdlProject project = new WsdlProject(workspace);
/*     */     
/*     */ 
/*  61 */     String username = "184801";
/*  62 */     String password = "resiltech";
/*  63 */     if ((System.getProperty("liferay.username") != null) && (System.getProperty("liferay.username").length() > 0)) {
/*  64 */       username = System.getProperty("liferay.username");
/*     */     }
/*  66 */     if ((System.getProperty("liferay.password") != null) && (System.getProperty("liferay.password").length() > 0)) {
/*  67 */       password = System.getProperty("liferay.password");
/*     */     }
/*  69 */     WsdlInterfaceFactory.importWsdl(project, "http://" + username + ":" + password + "@" + args[0].replace("/secure/", "/") + "?wsdl", true);
/*  70 */     SubmitContext submitContext = new SubmitContext() {
/*  71 */       private StringToObjectMap _properties = new StringToObjectMap();
/*     */       
/*     */       public void setProperty(String arg0, Object arg1)
/*     */       {
/*  75 */         this._properties.put(arg0, arg1);
/*     */       }
/*     */       
/*     */       public Object removeProperty(String arg0)
/*     */       {
/*  80 */         return this._properties.remove(arg0);
/*     */       }
/*     */       
/*     */       public boolean hasProperty(String arg0)
/*     */       {
/*  85 */         return this._properties.containsKey(arg0);
/*     */       }
/*     */       
/*     */       public String[] getPropertyNames()
/*     */       {
/*  90 */         return (String[])this._properties.keySet().toArray(new String[this._properties.keySet().size()]);
/*     */       }
/*     */       
/*     */       public Object getProperty(String arg0)
/*     */       {
/*  95 */         return this._properties.get(arg0);
/*     */       }
/*     */       
/*     */       public StringToObjectMap getProperties()
/*     */       {
/* 100 */         return this._properties;
/*     */       }
/*     */       
/*     */       public com.eviware.soapui.model.ModelItem getModelItem()
/*     */       {
/* 105 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 111 */       public String expand(String arg0) { return null; }
/*     */     };
/*     */     int j;
/*     */     int i;
/*      for (Iterator localIterator1 = project.getInterfaceList().iterator(); localIterator1.hasNext(); 
/* 116 *         i < j)
/*     *    {
/* 115 *       Interface inter = (Interface)localIterator1.next();
/* 116 *       Operation[] arrayOfOperation; j = (arrayOfOperation = inter.getAllOperations()).length;i = 0; continue;Operation op = arrayOfOperation[i];
/* 117 *       for (Request req : op.getRequestList()) {
/* 118 *         System.out.println(req.getRequestContent());
/* 119 *         if ((req instanceof WsdlRequest)) {
/* 120 *           WsdlRequest wsdlReq = (WsdlRequest)req;
/*     *           
/* 122 *           File parameterFile = new File(normalizeActionName(op.getName()));
/* 123 *           if (parameterFile.exists()) {
/*     *             try {
/* 125 *               if (!op.getName().equals("addUser"))
/*     *                 continue;
/* 127 *               wsdlReq.setEndpoint("http://" + args[0]);
/* 128 *               String modifiedRequest = FileUtils.readFileToString(parameterFile);
/* 129 *              // wsdlReq.setAuthType("Global HTTP Settings");
/* 130 *               wsdlReq.setUsername("184801");
/* 131 *               wsdlReq.setPassword("resiltech");
/* 132 *               wsdlReq.setRequestContent(modifiedRequest);
/*     *               
/* 134 *               System.out.println(req.getRequestContent());
/*     *               
/* 136 *               Submit result = req.submit(submitContext, false);
/* 137 *               System.out.println(result.getStatus());
/* 138 *               FileUtils.writeStringToFile(new File(normalizeActionName(op.getName()) + "_response"), result.getResponse().getContentAsXml());
/*     *             }
/*     *             catch (IOException|com.eviware.soapui.model.iface.Request.SubmitException e) {
/*     *               try {
/* 142 *                 e.printStackTrace(new PrintStream(new File(normalizeActionName(((WsdlRequestConfig)wsdlReq.getConfig()).getWsaConfig().getAction()) + "_exception")));
/*     *               } catch (FileNotFoundException e1) {
/* 144 *                 e1.printStackTrace(System.err);
/* 145 *                 System.exit(1);
/*     *               }
/*     *            }
/*     *           } else {
/*     *             try {
/* 150 *               FileUtils.writeStringToFile(parameterFile, wsdlReq.getRequestContent());
/*     *             } catch (IOException e) {
/*     *               try {
/* 153 *                 e.printStackTrace(new PrintStream(new File(normalizeActionName(((WsdlRequestConfig)wsdlReq.getConfig()).getWsaConfig().getAction()) + "_exception")));
/*     *               } catch (FileNotFoundException e1) {
/* 155 *                e1.printStackTrace(System.err);
/* 156 *                 System.exit(1);
/*     *               }
/*     *             }
/*     *           }
/*     *         }
/*     *       }
/* 116 *       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 *     workspace.save(false);
/*     *   }
/*     */   
/*     */   private static String normalizeActionName(String action) {
/* 170 */     return action.replace(":", "").replace('/', '_').replace("%", "");
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */