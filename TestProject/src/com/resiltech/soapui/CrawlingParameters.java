/*     */ package com.resiltech.soapui;
/*     */ 
/*     */ import com.eviware.soapui.config.WsaConfigConfig;
/*     */ import com.eviware.soapui.config.WsdlRequestConfig;
/*     */ import com.eviware.soapui.impl.wsdl.WsdlRequest;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class CrawlingParameters
/*     */ {
/*     */   private DocumentBuilder _builder;
/*     */   private Transformer _transformer;
/*     */   private XPathExpression _lookupParameters;
/*     */   
/*     */   public CrawlingParameters()
/*     */     throws ParserConfigurationException, TransformerConfigurationException
/*     */   {
/*  39 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  40 */     XPathFactory xPathfactory = XPathFactory.newInstance();
/*  41 */     TransformerFactory tFactory = TransformerFactory.newInstance();
/*  42 */     XPath xpathEngine = xPathfactory.newXPath();
/*     */     
/*  44 */     this._builder = factory.newDocumentBuilder();
/*  45 */     this._transformer = tFactory.newTransformer();
/*     */     try
/*     */     {
/*  48 */       this._lookupParameters = xpathEngine.compile("/Envelope/Body/*");
/*     */     }
/*     */     catch (XPathExpressionException localXPathExpressionException) {}
/*     */   }
/*     */   
/*     */   private static Node getParameterByName(String parameterName, Node node)
/*     */   {
/*  55 */     NamedNodeMap attributes = node.getAttributes();
/*     */     
/*  57 */     for (int i = 0; i < attributes.getLength(); i++)
/*     */     {
/*  59 */       Node attributeItem = attributes.item(i);
/*  60 */       if ((attributeItem.getNodeType() == 2) && (attributeItem.getLocalName() == parameterName))
/*     */       {
/*  62 */         return attributeItem;
/*     */       }
/*     */     }
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   private String getNodeContent(Node node) throws TransformerException {
/*  69 */     if (getContentType(node) == ParameterInstrumentation.ContentType.TEXT) {
/*  70 */       return node.getTextContent();
/*     */     }
/*  72 */     DOMSource source = new DOMSource(node);
/*  73 */     ByteArrayOutputStream resultContainer = new ByteArrayOutputStream();
/*  74 */     StreamResult result = new StreamResult(resultContainer);
/*  75 */     this._transformer.transform(source, result);
/*     */     
/*  77 */     return new String(resultContainer.toByteArray());
/*     */   }
/*     */   
/*     */   private ParameterInstrumentation.ContentType getContentType(Node node)
/*     */   {
/*  82 */     if ((node.getChildNodes().getLength() == 1) && (node.getChildNodes().item(0).getNodeType() == 3)) {
/*  83 */       return ParameterInstrumentation.ContentType.TEXT;
/*     */     }
/*  85 */     return ParameterInstrumentation.ContentType.XML;
/*     */   }
/*     */   
/*     */   public MethodInstrumentation getMethod(WsdlRequest request)
/*     */     throws SAXException, IOException, XPathExpressionException, TransformerException
/*     */   {
/*  91 */     String originalRequest = request.getRequestContent();
/*  92 */     Document doc = this._builder.parse(new ByteArrayInputStream(originalRequest.getBytes()));
/*  93 */     ArrayList<ParameterInstrumentation> parameters = new ArrayList();
/*     */     
/*  95 */     NodeList nl = (NodeList)this._lookupParameters.evaluate(doc, XPathConstants.NODESET);
/*  96 */     if (nl.getLength() == 1) {
/*  97 */       Node method = nl.item(0);
/*  98 */       NodeList listOfChild = method.getChildNodes();
/*  99 */       for (int i = 0; i < listOfChild.getLength(); i++) {
/* 100 */         Node parameter = listOfChild.item(i);
/* 101 */         if (parameter.getNodeType() == 1) {
/* 102 */           Node typeAttribute = getParameterByName("xsi:type", parameter);
/* 103 */           ParameterInstrumentation pi = new ParameterInstrumentation(parameter.getLocalName(), getNodeContent(parameter), getContentType(parameter), typeAttribute.getTextContent());
/* 104 */           parameters.add(pi);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 109 */     return new MethodInstrumentation(((WsdlRequestConfig)request.getConfig()).getWsaConfig().getAction(), parameters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\CrawlingParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */