/*     */ package com.resiltech.testing_box.functional_tester.configuration;
/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.WorkloadConfiguration;
/*     */ import com.resiltech.testing_box.functional_tester.WorkloadConfiguration.SUTConfiguration;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
/*     */ import com.resiltech.testing_box.functional_tester.configuration.instructions.Declare;
/*     */ import com.resiltech.testing_box.functional_tester.exception.FunctionalTestExecutionException;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IMethodInvocationListener;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IResponse;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IScope;
/*     */ import com.resiltech.testing_box.functional_tester.execution.InvocationException;
/*     */ import com.resiltech.testing_box.functional_tester.soapui.SoapUIInvocationException;
/*     */ import com.resiltech.testing_box.functional_tester.soapui.SoapUIInvoker;
/*     */ import com.resiltech.testing_box.functional_tester.soapui.SoapUIInvoker.HTTPResponse;
/*     */ import com.resiltech.testing_box.functional_tester.soapui.SoapUISetupExcetion;

import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
import java.util.Collection;
/*     */ import java.util.HashMap;
import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.MatchResult;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;

/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.xpath.XPathExpressionException;

/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionalTest
/*     */ {
/*     */   private HashMap<String, XmlWorkload> _workloads;
/*     */   private RequestInvokerImpl _requestInvoker;
/*     */   private ArrayList<String> _sequenceExecution;
/*     */   private SoapUIInvoker _invokerBackend;
/*     */   private ArrayList<Declare> _declarations;
/*     */   
/*     */   private static class ResponseImpl
/*     */     implements IResponse
/*     */   {
/*     */     private String _httpCode;
/*     */     private String _xmlCode;
/*     */     private Document _document;
/*     */     private Request _request;
/*     */     private static DocumentBuilder _documentBuilder;
/*     */     
/*     */     private Document getDocument()
/*     */       throws SAXException, IOException
/*     */     {
/*  61 */       if (this._document == null) {
/*  62 */         this._document = _documentBuilder.parse(new ByteArrayInputStream(this._xmlCode.getBytes()));
/*     */       }
/*  64 */       return this._document;
/*     */     }
/*     */     
/*     */     public ResponseImpl(String httpCode, String xmlCode, Request request)
/*     */     {
/*  48 */       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  49 */       factory.setValidating(false);
/*  50 */       factory.setNamespaceAware(true);
/*     */       try {
/*  52 */         _documentBuilder = factory.newDocumentBuilder();
/*     */       } catch (ParserConfigurationException e) {
/*  54 */         System.out.println("Response can't be parsed. " + e.getMessage());
/*  55 */         e.printStackTrace(System.err);
/*  56 */         System.exit(-1);
/*     */       }
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
/*  68 */       this._httpCode = httpCode;
/*  69 */       this._request = request;
/*  70 */       this._xmlCode = xmlCode;
/*     */     }
/*     */     
/*     */     public String getResponseCode()
/*     */     {
/*  75 */       return this._httpCode;
/*     */     }
/*     */     
/*     */     public String getResult(String name)
/*     */     {
/*  80 */       String value = null;
/*     */       try {
/*  82 */         value = this._request.getResultValue(name, getDocument());
/*     */       }
/*     */       catch (XPathExpressionException|FunctionalTestExecutionException|SAXException|IOException e)
/*     */       {
/*  86 */         System.out.println("Most likely the Portlet definition of \"" + this._request.getUrl() + "\" is not well formed. Pleaese have a look into the file which define " + this._request.getPortlet() + ":" + this._request.getMethod() + " on parameter " + name + ". " + e.getMessage());
/*  87 */         e.printStackTrace(System.err);
/*  88 */         System.exit(-1);
/*     */       }
/*     */       
/*  91 */       return value;
/*     */     }
/*     */   }
/*     */   
/*     */   private class RequestInvokerImpl implements IRequestInvoker
/*     */   {
/*     */     private HashMap<String, Request> _requestCollection;
/*     */     
/*     */     public RequestInvokerImpl()
/*     */     {
/* 101 */       this._requestCollection = new HashMap();
/*     */     }
/*     */     
/*     */     public void registerRequest(Request request) {
/* 105 */       this._requestCollection.put(request.getPortlet() + "?" + request.getMethod(), request);
/*     */     }
/*     */     
/*     */     public IResponse invoke(String portlet, String methodName, Map<String, String> parameters) throws InvocationException
/*     */     {
/* 110 */       Request request = (Request)this._requestCollection.get(portlet + "?" + methodName);
/* 111 */       if (request == null) {
/* 112 */         throw new InvocationException(portlet + ":" + methodName + " can't be called because it hasn't been defined.");
/*     */       }
/* 114 */       String content = request.getContent();
/* 115 */       SoapUIInvoker.HTTPResponse result = null;
/* 116 */       Matcher matcher = FunctionalTest.variableSchema.matcher(content);
/*     */       
/* 118 */       while (matcher.find()) {
/* 119 */         MatchResult matchResult = matcher.toMatchResult();
/* 120 */         String match = matchResult.group();
/* 121 */         String replacement = (String)parameters.get(match.substring(2, match.length() - 1));
/* 122 */         if (replacement == null) {
/* 123 */           throw new InvocationException("The parameter '" + match.substring(2, match.length() - 1) + "' hasn't been assigned.");
/*     */         }
/*     */         
/*     */ 
/* 127 */         content = content.replaceAll(Pattern.quote(match), replacement);
/* 128 */         matcher.reset(content);
/*     */       }
/*     */       try
/*     */       {
/* 132 */         result = FunctionalTest.this._invokerBackend.invokeRequest(new URL(request.getUrl(), portlet), methodName, content);
/*     */       } catch (MalformedURLException e) {
/* 134 */         throw new InvocationException("Internal state corruption", e);
/*     */       } catch (SoapUISetupExcetion e) {
/* 136 */         throw new InvocationException("Invocation of " + portlet + ":" + methodName + " can't be executed due to setup exception", e);
/*     */       } catch (SoapUIInvocationException e) {
/* 138 */         throw new InvocationException("Invocation of " + portlet + ":" + methodName + " throws an exception", e);
/*     */       }
/*     */       
/* 141 */       if (result.getCode().equals("500"))
/*     */       {
/* 143 */         throw new InvocationException("Internal exception" + result.getXML(), new InvocationException("Erroneous Invocation: " + content, null));
/*     */       }
/*     */       
/*     */ 
/* 147 */       return new FunctionalTest.ResponseImpl(result.getCode(), result.getXML(), request);
/*     */     }
/*     */   }
/*     */   
/*     */   
/*     */   private static class Scope implements IScope
/*     */   {
/*     */     private static final String VARIABLE_INITIALIZATION_VALUE = "";
/*     */     private HashMap<String, String> _variables;
/*     */     
/*     */     Scope() {
/* 162 */       this._variables = new HashMap();
/*     */     }
/*     */     
/*     */     Scope(HashMap<String, String> arguments)
/*     */     {
/* 167 */       this._variables = new HashMap(arguments);
/*     */     }
/*     */     
/*     */     public Set<String> getVariableNames()
/*     */     {
/* 172 */       return this._variables.keySet();
/*     */     }
/*     */     
/*     */     public String getValue(String variableName)
/*     */     {
/* 177 */       String value = null;
/* 178 */       if (this._variables.containsKey(variableName)) {
/* 179 */         value = (String)this._variables.get(variableName);
/*     */       }
/* 181 */       return value;
/*     */     }
/*     */     
/*     */     public boolean hasVariable(String variableName)
/*     */     {
/* 186 */       return this._variables.containsKey(variableName);
/*     */     }
/*     */     
/*     */     public void declare(String variableName)
/*     */     {
/* 191 */       this._variables.put(variableName, "");
/*     */     }
/*     */     
/*     */     public void setValue(String variableName, String value)
/*     */     {
/* 196 */       if (this._variables.containsKey(variableName)) {
/* 197 */         this._variables.put(variableName, value);
/* 198 */         System.out.println(variableName + "=" + value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */   private static final Pattern variableSchema = Pattern.compile("\\{\\$\\w+\\}");

			private String userId;
			private String screenName;
			private String password;
			private String host;
			private LinkedList<HashMap<String, String>> invocations;
/*     */   
/*     */   public FunctionalTest(String userId, String screenName, String password, String host) {
	/* 211 */     this._invokerBackend = new SoapUIInvoker(userId, screenName, password, host);
					this.userId = userId;
					this.screenName = screenName;
					this.password = password;
					this.host = host;
	/* 212 */     this._workloads = new HashMap();
	/* 213 */     this._requestInvoker = new RequestInvokerImpl();
	/* 214 */     this._sequenceExecution = new ArrayList();
	/* 215 */     this._declarations = new ArrayList();
					this.invocations = new LinkedList<HashMap<String, String>>();
			}
			
			public HashMap<String, XmlWorkload> getWorkloads(){
				return _workloads;
			}
/*     */   
/*     */   public void run(HashMap<String, String> _options) {
			  try {
/* 220 */       IScope scope = null;
/* 221 */       if (_options.size() > 0) {
/* 222 */         scope = new Scope(_options);
/*     */       } else {
/* 224 */         scope = new Scope();
/*     */       }
/* 226 */       this._invokerBackend.openWorkspace();
/*     */       
/* 228 */       for (Declare declaration : this._declarations) {
/* 229 */         declaration.execute(this._requestInvoker, scope);
/*     */       }
/* 231 */       for (String workloadName : this._sequenceExecution) {
/* 232 */         XmlWorkload wl = (XmlWorkload)this._workloads.get(workloadName);
				  LinkedList<HashMap<String, String>> exeResult = wl.execute(this._requestInvoker, scope); 
				  if(exeResult != null && exeResult.size() > 0){
					  for(HashMap<String, String> call : exeResult){
						  call.put("SERVICE_NAME", wl.getName());
						  invocations.add(call);
					  }  
				  }
				  Thread.sleep(1000);
/*     */       }
/* 235 */       this._invokerBackend.closeWorkspace();
/*     */     } catch (SoapUISetupExcetion e) {
/* 237 */       System.out.println("This really shouldn't have happened. " + e.getMessage());
/* 238 */       e.printStackTrace(System.err);
/*     */     } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }   
			}
/*     */   
/*     */   public static XmlObjectConstructor getXmlConstructor()
/*     */   {
/* 244 */     return new XmlObjectConstructor("/FunctionalTest", new XmlObjectConstructor.DefaultHandlerProducingObject()
/*     */     {
				private String userId;
				private String screenName;
				private String password;
				private String host;
/*     */       private FunctionalTest _functionalTest;
/*     */       
/*     */       public void startFunctionaltest(Attributes attribute) throws SAXException {
/* 249 */         	readPreferences(new File("workloadGenerator.properties"));
					this._functionalTest = new FunctionalTest(userId, screenName, password, host);
/*     */       }

				private void readPreferences(File prefFile){
					String readed, tag, value;
					BufferedReader reader;
					HashMap<String, String> map = new HashMap<String, String>();
					try {
						if(prefFile.exists()){
							reader = new BufferedReader(new FileReader(prefFile));
							while(reader.ready()){
								readed = reader.readLine();
								if(readed.length() > 0) {
									if(readed.contains("=")){
										tag = readed.split("=")[0];
										value = readed.split("=")[1];
										if(tag.equals("SUT.host")){
											host = value;
										} else if(tag.equals("SUT.liferay.screenName")){
											screenName = value;
										} else if(tag.equals("SUT.liferay.password")){
											password = value;
										} else if(tag.equals("SUT.liferay.userId")){
											userId = value;
										}
									}
								}
							}
							reader.close();
						}
					} catch(Exception ex){}
				} 
/*     */       
/*     */       public void processedWorkload(Object obj) {
/* 253 */         XmlWorkload w = (XmlWorkload)obj;
/* 254 */         this._functionalTest._workloads.put(w.getName(), w);
/*     */       }
/*     */       
/*     */       public void processedRequest(Object obj) {
/* 258 */         _functionalTest._requestInvoker.registerRequest((Request)obj);
/*     */       }
/*     */       
/*     */       public void processedWorkloadreference(Object obj) {
/* 262 */         this._functionalTest._sequenceExecution.add(((WorkloadReference)obj).getName());
/*     */       }
/*     */       
/*     */       public void processedDeclare(Object obj) throws SAXException {
/* 266 */         if ((obj instanceof Declare)) {
/* 267 */           Declare d = (Declare)obj;
/* 268 */           this._functionalTest._declarations.add(d);
/*     */         } else {
/* 270 */           throw new SAXException(obj.toString() + " is an invalid object for Declare element");
/*     */         }
/*     */       }
/*     */       
/*     */       public Object getObject()
/*     */       {
/* 276 */         return this._functionalTest;
/*     */       }
/*     */     });
/*     */   }
/*     */
			public LinkedList<HashMap<String, String>> getInvocations() {
				return invocations;
			}
				

}


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\FunctionalTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */