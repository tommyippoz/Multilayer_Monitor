/*     */ package com.resiltech.testing_box.functional_tester.soapui;
/*     */ 
/*     */ import com.eviware.soapui.impl.WorkspaceImpl;
/*     */ import com.eviware.soapui.impl.wsdl.WsdlProject;
/*     */ import com.eviware.soapui.impl.wsdl.WsdlRequest;
/*     */ import com.eviware.soapui.model.iface.Interface;
/*     */ import com.eviware.soapui.model.iface.Operation;
/*     */ import com.eviware.soapui.model.iface.Submit;
/*     */ import com.eviware.soapui.support.types.StringToObjectMap;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IMethodInvocationListener;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SoapUIInvoker
/*     */ {
/*     */   private String _userId;
/*     */   private String _password;
/*     */   private String _username;
/*     */   private String _hostName;
/*     */   private WorkspaceImpl _workspace;
/*     */   private IMethodInvocationListener _eventliListener;
/*     */   private HashMap<String, SoapUIProject> _projects;
/*     */   
/*     */   private static class SubmitContextImpl implements com.eviware.soapui.model.iface.SubmitContext
/*     */   {
/*  28 */     private StringToObjectMap _properties = new StringToObjectMap();
/*     */     
/*     */     public void setProperty(String arg0, Object arg1)
/*     */     {
/*  32 */       this._properties.put(arg0, arg1);
/*     */     }
/*     */     
/*     */     public Object removeProperty(String arg0)
/*     */     {
/*  37 */       return this._properties.remove(arg0);
/*     */     }
/*     */     
/*     */     public boolean hasProperty(String arg0)
/*     */     {
/*  42 */       return this._properties.containsKey(arg0);
/*     */     }
/*     */     
/*     */     public String[] getPropertyNames()
/*     */     {
/*  47 */       return (String[])this._properties.keySet().toArray(new String[this._properties.keySet().size()]);
/*     */     }
/*     */     
/*     */     public Object getProperty(String arg0)
/*     */     {
/*  52 */       return this._properties.get(arg0);
/*     */     }
/*     */     
/*     */     public StringToObjectMap getProperties()
/*     */     {
/*  57 */       return this._properties;
/*     */     }
/*     */     
/*     */     public com.eviware.soapui.model.ModelItem getModelItem()
/*     */     {
/*  62 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public String expand(String arg0)
/*     */     {
/*  68 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HTTPResponse
/*     */   {
/*     */     private String _code;
/*     */     private String _xml;
/*     */     
/*     */     public HTTPResponse(String code, String xml) {
/*  78 */       this._code = code;
/*  79 */       this._xml = xml;
/*     */     }
/*     */     
/*     */     public String getCode() {
/*  83 */       return this._code;
/*     */     }
/*     */     
/*     */     public String getXML() {
/*  87 */       return this._xml;
/*     */     }
/*     */   }
/*     */   
/*     */   private class SoapUIProject {
/*     */     private WsdlProject _project;
/*     */     private HashMap<String, WsdlRequest> _requests;
/*     */     private com.eviware.soapui.model.iface.SubmitContext _submitContext;
/*     */     private String _securePath;
/*     */     private String _unsecurePath;
/*     */     
/*     */     public SoapUIProject(URL url) throws SoapUISetupExcetion {
/*  99 */       this._unsecurePath = _convertUrlToUnsecure(url);
/* 100 */       this._securePath = _convertUrlToSecure(url);
/* 101 */       this._project = _createProject();
/* 102 */       this._requests = new HashMap();
/* 103 */       this._submitContext = new SoapUIInvoker.SubmitContextImpl();
/* 104 */       _harvestAvaialableOperations();
/*     */     }
/*     */     
/*     */     private String _extractCode(String operationName, List<String> status) throws SoapUIInvocationException {
/* 108 */       if (status == null) {
/* 109 */         throw new SoapUIInvocationException("Request toward operation '" + operationName + "' can't be fulfilled due to the inability to fetch server response", null);
/*     */       }
/* 111 */       String[] tokens = ((String)status.get(0)).split(" ");
/* 112 */       return tokens[1];
/*     */     }
/*     */     
/*     */     public SoapUIInvoker.HTTPResponse invokeOperation(String operationName, String content) throws SoapUIInvocationException {
/* 116 */       WsdlRequest wsdlReq = (WsdlRequest)this._requests.get(operationName);
/* 117 */       Submit result = null;
/* 118 */       String code = null;
/* 119 */       if (wsdlReq != null) {
/* 120 */         wsdlReq.setEndpoint(this._securePath);
/* 121 */         wsdlReq.setAuthType("Global HTTP Settings");
/* 122 */         wsdlReq.setUsername(SoapUIInvoker.this._username);
/* 123 */         wsdlReq.setPassword(SoapUIInvoker.this._password);
/* 124 */         wsdlReq.setRequestContent(content);
/*     */         try {
/* 126 */           result = wsdlReq.submit(this._submitContext, false);
/*     */         } catch (RuntimeException|com.eviware.soapui.model.iface.Request.SubmitException e) {
/* 128 */           throw new SoapUIInvocationException("Request toward operation '" + operationName + "' can't be fulfilled", e);
/*     */         }
/*     */       } else {
/* 131 */         throw new RuntimeException("Operation named '" + operationName + "' doesn't exist");
/*     */       }
/*     */       
/* 134 */       code = _extractCode(operationName, (List)result.getResponse().getResponseHeaders().get("#status#"));
/* 135 */       return new SoapUIInvoker.HTTPResponse(code, result.getResponse().getContentAsXml());
/*     */     }
/*     */     
/*     */     private WsdlProject _createProject() throws SoapUISetupExcetion {
/* 139 */       WsdlProject project = new WsdlProject(SoapUIInvoker.this._workspace);
/* 140 */       ((com.eviware.soapui.config.ProjectConfig)project.getConfig()).setSoapuiVersion("1.1");
/*     */       try {
/* 142 */         com.eviware.soapui.impl.WsdlInterfaceFactory.importWsdl(project, this._unsecurePath + "?wsdl", true);
/*     */       } catch (com.eviware.soapui.support.SoapUIException e) {
/* 144 */         throw new SoapUISetupExcetion("Unable to fetch wsdl from: '" + this._unsecurePath + "?wsdl'", e);
/*     */       }
/* 146 */       return project;
/*     */     }
/*     */     
/*     */     private String _convertUrlToUnsecure(URL url) {
/* 150 */       return url.getProtocol() + "://" + SoapUIInvoker.this._userId + ":" + SoapUIInvoker.this._password + "@" + url.getHost() + ":" + url.getPort() + url.getFile().replace("/secure/", "/");
/*     */     }
/*     */     
/*     */ 
/* 154 */     private String _convertUrlToSecure(URL url) { return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + url.getFile().replace("/api/axis/", "/api/secure/axis/"); }
/*     */     
/*     */     private void _harvestAvaialableOperations() { 
				int j;
/*     */       int i;
/* 158 */       for (Iterator localIterator = this._project.getInterfaceList().iterator(); localIterator.hasNext(); )
/*     */       {
/* 158 */         Interface inter = (Interface)localIterator.next();
/* 159 */         String lastOperationName = "";
/* 160 */         int counter = 0;
/* 161 */         	Operation[] arrayOfOperation; 
					j = (arrayOfOperation = inter.getAllOperations()).length;
					i = 0; 
					while(i<j){
						Operation op = arrayOfOperation[i];
		/* 162 */         if (lastOperationName.equals(op.getName())) {
		/* 163 */           counter++;
		/*     */         } else {
		/* 165 */           counter = 1;
		/*     */         }
		/* 167 */         lastOperationName = op.getName();
		/* 168 */         com.eviware.soapui.model.iface.Request req = (com.eviware.soapui.model.iface.Request)op.getRequestList().get(0);
		/* 169 */         if ((req != null) && 
		/* 170 */           ((req instanceof WsdlRequest))) {
		/* 171 */           WsdlRequest wsdlReq = (WsdlRequest)req;
		/* 172 */           this._requests.put(op.getName() + "/" + counter, wsdlReq);
		/*     */         }
		/* 161 */         i++;
						}
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public SoapUIInvoker(String userId, String username, String password, String hostName)
/*     */   {
/* 189 */     this._userId = userId;
/* 190 */     this._username = username;
/* 191 */     this._password = password;
/* 192 */     this._hostName = hostName;
/*     */   }

/*     */   
/*     */   public void openWorkspace() throws SoapUISetupExcetion {
/* 201 */     com.eviware.soapui.support.types.StringToStringMap options = new com.eviware.soapui.support.types.StringToStringMap();
/* 202 */     String workspacePath = "/tmp/prova.xml";
/*     */     try {
/* 204 */       this._workspace = new WorkspaceImpl("/tmp/prova.xml", options);
/*     */     } catch (org.apache.xmlbeans.XmlException|java.io.IOException e) {
/* 206 */       throw new SoapUISetupExcetion("An exception has been raised during workspace creation in " + workspacePath, e);
/*     */     }
/* 208 */     this._projects = new HashMap();
/*     */   }
/*     */   
/*     */   public HTTPResponse invokeRequest(URL url, String methodName, String content) throws SoapUISetupExcetion, SoapUIInvocationException {
/* 212 */     SoapUIProject project = null;
/* 213 */     URL normalizedUrl = convertUrl(url);
/*     */     System.out.println(normalizedUrl.toString());
/* 215 */     if (!this._projects.containsKey(normalizedUrl.toString())) {
/* 216 */       this._projects.put(normalizedUrl.toString(), new SoapUIProject(normalizedUrl));
/*     */     }
/* 218 */     project = (SoapUIProject)this._projects.get(normalizedUrl.toString());
/* 219 */     //this._eventliListener.startInvocation(normalizedUrl, content);
/* 220 */     HTTPResponse response = project.invokeOperation(methodName, content);
/* 221 */     //this._eventliListener.endInvocation(normalizedUrl, Integer.parseInt(response._code), response._xml);
/*     */     
System.out.println(response.getCode());

/* 223 */     return response;
/*     */   }
/*     */   
/*     */   public void closeWorkspace() {
/* 227 */     this._workspace.save(false);
/*     */   }
/*     */   
/*     */   private URL convertUrl(URL url) {
/* 231 */     String s = url.toString();
/* 232 */     if ((this._hostName != null) && (this._hostName.length() > 0)) {
/* 233 */       s = s.replace(url.getHost(), this._hostName);
/*     */     }
/*     */     try
/*     */     {
/* 237 */       return new URL(s);
/*     */     } catch (java.net.MalformedURLException e) {}
/* 239 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\soapui\SoapUIInvoker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */