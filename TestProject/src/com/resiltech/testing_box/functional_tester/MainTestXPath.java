/*    */ package com.resiltech.testing_box.functional_tester;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import javax.xml.namespace.NamespaceContext;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import javax.xml.xpath.XPath;
/*    */ import javax.xml.xpath.XPathConstants;
/*    */ import javax.xml.xpath.XPathExpression;
/*    */ import javax.xml.xpath.XPathExpressionException;
/*    */ import javax.xml.xpath.XPathFactory;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class MainTestXPath
/*    */ {
/*    */   private static class SoapAwareNamespaceContext implements NamespaceContext
/*    */   {
/*    */     private HashMap<String, String> _namespaces;
/*    */     
/*    */     public SoapAwareNamespaceContext()
/*    */     {
/* 29 */       this._namespaces = new HashMap();
/* 30 */       this._namespaces.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
/* 31 */       this._namespaces.put("urn", "urn:http.service.portal.liferay.com");
/* 32 */       this._namespaces.put("soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
/*    */     }
/*    */     
/*    */     public String getNamespaceURI(String prefix) {
/* 36 */       String namespace = null;
/* 37 */       if (this._namespaces.containsKey(prefix)) {
/* 38 */         namespace = (String)this._namespaces.get(prefix);
/*    */       }
/* 40 */       return namespace;
/*    */     }
/*    */     
/*    */     public String getPrefix(String namespaceURI) {
/* 44 */       return null;
/*    */     }
/*    */     
/*    */     public Iterator getPrefixes(String namespaceURI)
/*    */     {
/* 49 */       return null;
/*    */     }
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws XPathExpressionException, FileNotFoundException, SAXException, java.io.IOException, ParserConfigurationException
/*    */   {
/* 55 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 56 */     DocumentBuilder _documentBuilder = null;
/* 57 */     factory.setValidating(false);
/* 58 */     factory.setNamespaceAware(true);
/* 59 */     _documentBuilder = factory.newDocumentBuilder();
/*    */     
/* 61 */     XPathFactory _factory = XPathFactory.newInstance();
/* 62 */     XPath _engine = _factory.newXPath();
/* 63 */     _engine.setNamespaceContext(new SoapAwareNamespaceContext());
/* 64 */     XPathExpression expr = _engine.compile("//multiRef[@id=\"#\"]");
/* 65 */     Document _document = _documentBuilder.parse(new FileInputStream("/home/fabio/Documenti/Progetti/Secure/workspace/FunctionalTestConfigurationParser/test/response.xml"));
/*    */     
/* 67 */     Node n = (Node)expr.evaluate(_document, XPathConstants.NODE);
/*    */     
/* 69 */     System.out.println(n.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\MainTestXPath.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */