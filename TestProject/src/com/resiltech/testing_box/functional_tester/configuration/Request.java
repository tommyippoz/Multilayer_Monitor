/*     */ package com.resiltech.testing_box.functional_tester.configuration;
/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.exception.FunctionalTestExecutionException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Request
/*     */ {
/*     */   private URL _url;
/*     */   private String _content;
/*     */   private HashMap<String, XPathExpression> _results;
/*     */   private String _method;
/*     */   private String _portlet;
/*     */   private static XPathFactory _factory;
/*     */   private static XPath _engine;
/*     */   
/*     */   private static class SoapAwareNamespaceContext implements NamespaceContext
/*     */   {
/*     */     private HashMap<String, String> _namespaces;
/*     */     
/*     */     public SoapAwareNamespaceContext()
/*     */     {
/*  42 */       this._namespaces = new HashMap();
/*  43 */       this._namespaces.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
/*  44 */       this._namespaces.put("urn", "urn:http.service.portal.liferay.com");
/*  45 */       this._namespaces.put("soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
/*     */     }
/*     */     
/*     */     public String getNamespaceURI(String prefix) {
/*  49 */       String namespace = null;
/*  50 */       if (this._namespaces.containsKey(prefix)) {
/*  51 */         namespace = (String)this._namespaces.get(prefix);
/*     */       }
/*  53 */       return namespace;
/*     */     }
/*     */     
/*     */     public String getPrefix(String namespaceURI) {
/*  57 */       return null;
/*     */     }
/*     */     
/*     */     public Iterator getPrefixes(String namespaceURI)
/*     */     {
/*  62 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private Request(String portlet, String method)
/*     */   {
/*  68 */     _factory = XPathFactory.newInstance();
/*  69 */     _engine = _factory.newXPath();
/*  70 */     _engine.setNamespaceContext(new SoapAwareNamespaceContext());
/*     */     
/*     */ 
/*     */ 
/*  74 */     this._results = new HashMap();
/*  75 */     this._portlet = portlet;
/*  76 */     this._method = method;
/*     */   }

			private Request(String portlet, String method, Object obj)
/*     */   {
/*  68 */     this(portlet, method);
/*     */   }
/*     */   
/*     */   private void registerResult(String resultName, String xPathExpression) throws XPathExpressionException {
/*  80 */     XPathExpression expression = _engine.compile(xPathExpression);
/*  81 */     this._results.put(resultName, expression);
/*     */   }
/*     */   
/*     */   public URL getUrl() {
/*  85 */     return this._url;
/*     */   }
/*     */   
/*     */   public String getContent() {
/*  89 */     return this._content;
/*     */   }
/*     */   
/*     */   public String[] getResultsName() {
/*  93 */     Set<String> keys = this._results.keySet();
/*  94 */     return (String[])keys.toArray(new String[keys.size()]);
/*     */   }
/*     */   
/*     */   public boolean isAValidResultName(String resultName) {
/*  98 */     return this._results.containsKey(resultName);
/*     */   }
/*     */   
/*     */   public String getResultValue(String resultName, Document contentResponse) throws FunctionalTestExecutionException, XPathExpressionException {
/* 102 */     if (this._results.containsKey(resultName)) {
/* 103 */       XPathExpression _expr = (XPathExpression)this._results.get(resultName);
/* 104 */       Node nl = (Node)_expr.evaluate(contentResponse, XPathConstants.NODE);
/* 105 */       String textContent = "";
/* 106 */       if (nl != null) {
/* 107 */         NamedNodeMap attributes = nl.getAttributes();
/* 108 */         Node href = attributes.getNamedItem("href");
/* 109 */         if (href != null) {
/* 110 */           String hrefString = href.getTextContent();
/*     */           
/* 112 */           XPathExpression _exprLookUpForId = _engine.compile("//multiRef[@id=\"" + hrefString.substring(1) + "\"]");
/* 113 */           nl = (Node)_exprLookUpForId.evaluate(contentResponse, XPathConstants.NODE);
/*     */         }
/*     */         
/* 116 */         textContent = nl.getTextContent();
/*     */       }
/*     */       
/* 119 */       return textContent;
/*     */     }
/* 121 */     throw new FunctionalTestExecutionException("A result named " + resultName + " doens't exists for [Portlet]:[Method] " + this._portlet + ":" + this._method);
/*     */   }
/*     */   
/*     */   public String getMethod()
/*     */   {
/* 126 */     return this._method;
/*     */   }
/*     */   
/*     */   public String getPortlet() {
/* 130 */     return this._portlet;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 135 */     return this._portlet + "." + this._method;
/*     */   }
/*     */   
/*     */   public static XmlObjectConstructor getXmlConstructor()
/*     */   {
/* 140 */     return new XmlObjectConstructor("/FunctionalTest/Configuration/Request", new XmlObjectConstructor.DefaultHandlerProducingObject()
/*     */     {
/*     */       private Request _request;
/*     */       private String _resultName;
/* 144 */       private boolean _skipContentContent = false;
/*     */       
/*     */       private String getContentDescription() {
/* 147 */         return this._request.getPortlet() + ":" + this._request.getMethod();
/*     */       }
/*     */       
/*     */       public void startRequest(Attributes attribute) throws SAXException {
/* 151 */         String portlet = attribute.getValue("portlet");
/* 152 */         String method = attribute.getValue("method");
/* 153 */         this._request = new Request(portlet, method, null);
/*     */       }
/*     */       
/*     */       public void contentUrl(String content) throws SAXException {
/*     */         try {
/* 158 */           this._request._url = new URL(content);
/*     */         } catch (MalformedURLException e) {
/* 160 */           throw new SAXException(getContentDescription() + " specifies a malformed URL: " + content, e);
/*     */         }
/*     */       }
/*     */       
/*     */       public void startContent(Attributes attributes) throws SAXException {
/* 165 */         int indexOfFileAttribute = attributes.getIndex("file");
/* 166 */         if (indexOfFileAttribute != -1) {
/* 167 */           String filePath = attributes.getValue(indexOfFileAttribute);
/*     */           try {
/* 169 */             BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
/* 170 */             StringBuilder fileContent = new StringBuilder();
/* 171 */             String aLineFromFile = br.readLine();
/* 172 */             while (aLineFromFile != null) {
/* 173 */               fileContent.append(aLineFromFile);
/* 174 */               aLineFromFile = br.readLine();
/*     */             }
/* 176 */             br.close();
/* 177 */             this._request._content = fileContent.toString();
/*     */           } catch (IOException e) {
/* 179 */             throw new SAXException(getContentDescription() + " - Content specifies a file from which hasn't been possible reading: " + filePath, e);
/*     */           }
/* 181 */           this._skipContentContent = true;
/*     */         } else {
/* 183 */           this._request._content = "";
/* 184 */           this._skipContentContent = false;
/*     */         }
/*     */       }
/*     */       
/*     */       public void contentContent(String content) throws SAXException {
/* 189 */         if (!this._skipContentContent) {
/* 190 */           this._request._content += content;
/*     */         }
/* 192 */         else if (content.trim().length() > 0) {
/* 193 */           System.out.println("WARNING: " + getContentDescription() + " - Content: the content has been skipped because a file has been previously specified");
/*     */         }
/*     */       }
/*     */       
/*     */       public void startResult(Attributes attributes) throws SAXException
/*     */       {
/* 199 */         this._resultName = attributes.getValue("name");
/*     */       }
/*     */       
/*     */       public void contentPath(String content) throws SAXException {
/*     */         try {
/* 204 */           this._request.registerResult(this._resultName, content);
/*     */         } catch (XPathExpressionException e) {
/* 206 */           throw new SAXException(getContentDescription() + " - Result[name=\"" + this._resultName + "\"] doesn't specify a valid XPath expression", e);
/*     */         }
/*     */       }
/*     */       
/*     */       public Object getObject()
/*     */       {
/* 212 */         return this._request;
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\Request.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */