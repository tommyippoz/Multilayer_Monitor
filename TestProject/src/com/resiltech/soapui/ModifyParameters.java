/*     */ package com.resiltech.soapui;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ class ModifyParameters
/*     */ {
/*     */   private DocumentBuilder _builder;
/*     */   private XPath _xpathEngine;
/*     */   private Transformer _transformer;
/*     */   private XPathExpression _exprAllNamespaces;
/*     */   
/*     */   public ModifyParameters()
/*     */     throws ParserConfigurationException, TransformerConfigurationException
/*     */   {
/*  39 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  40 */     XPathFactory xPathfactory = XPathFactory.newInstance();
/*  41 */     TransformerFactory tFactory = TransformerFactory.newInstance();
/*     */     
/*  43 */     this._builder = factory.newDocumentBuilder();
/*  44 */     this._xpathEngine = xPathfactory.newXPath();
/*     */     try {
/*  46 */       this._exprAllNamespaces = this._xpathEngine.compile("//namespace::*");
/*     */     }
/*     */     catch (XPathExpressionException localXPathExpressionException) {}
/*  49 */     this._transformer = tFactory.newTransformer();
/*     */   }
/*     */   
/*     */   private HashMap<String, String> getNamespaces(Document doc)
/*     */   {
/*  54 */     HashMap<String, String> namespaceMapper = new HashMap();
/*  55 */     Element root = doc.getDocumentElement();
/*  56 */     NamedNodeMap attributes = root.getAttributes();
/*     */     
/*  58 */     for (int i = 0; i < attributes.getLength(); i++)
/*     */     {
/*  60 */       Node node = attributes.item(i);
/*  61 */       if (node.getNodeType() == 2)
/*     */       {
/*  63 */         String name = node.getNodeName();
/*  64 */         if (name.startsWith("xmlns:")) {
/*  65 */           name = name.substring("xmlns:".length());
/*  66 */           namespaceMapper.put(name, node.getTextContent());
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*  72 */       NodeList namespaces = (NodeList)this._exprAllNamespaces.evaluate(doc, XPathConstants.NODESET);
/*  73 */       for (int i = 0; i < namespaces.getLength(); i++) {
/*  74 */         Node node = namespaces.item(i);
/*  75 */         String name = node.getNodeName();
/*  76 */         if (name.startsWith("xmlns:")) {
/*  77 */           name = name.substring("xmlns:".length());
/*  78 */           namespaceMapper.put(name, node.getTextContent());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (XPathExpressionException localXPathExpressionException) {}
/*     */     
/*  84 */     return namespaceMapper;
/*     */   }
/*     */   
/*     */   public String populateWsdlRequest(String originalRequest) throws SAXException, IOException, XPathExpressionException, TransformerException {
/*  88 */     Document doc = this._builder.parse(new ByteArrayInputStream(originalRequest.getBytes()));
/*     */     
/*  90 */     this._xpathEngine.setNamespaceContext(new Main.NamespaceContextByHashMap(getNamespaces(doc)));
/*  91 */     XPathExpression expr = this._xpathEngine.compile("/Envelope/Body/addLayoutBranch/name");
/*  92 */     Node nl = (Node)expr.evaluate(doc, XPathConstants.NODE);
/*     */     
/*  94 */     nl.setTextContent("fabio");
/*     */     
/*     */ 
/*  97 */     DOMSource source = new DOMSource(doc);
/*  98 */     ByteArrayOutputStream resultContainer = new ByteArrayOutputStream();
/*  99 */     StreamResult result = new StreamResult(resultContainer);
/* 100 */     this._transformer.transform(source, result);
/*     */     
/* 102 */     return new String(resultContainer.toByteArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\soapui\ModifyParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */