/*     */ package com.resiltech.testing_box.functional_tester.configuration;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlObjectConstructor
/*     */   implements ContentHandler
/*     */ {
/*     */   private String _xmlElementName;
/*     */   private DefaultHandlerProducingObject _saxHandler;
/*     */   private Stack<String> _currentElement;
/*     */   
/*     */   private static String capitalizeString(String name)
/*     */   {
/*  24 */     return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
/*     */   }
/*     */   
/*     */   public XmlObjectConstructor(String xmlElementName, DefaultHandlerProducingObject saxHandler) {
/*  28 */     this._xmlElementName = xmlElementName;
/*  29 */     this._saxHandler = saxHandler;
/*  30 */     this._currentElement = new Stack();
/*     */   }
/*     */   
/*     */   public Object getProducedObject() {
/*  34 */     return this._saxHandler.getObject();
/*     */   }
/*     */   
/*     */   public String getElementNameHandled() {
/*  38 */     return this._xmlElementName;
/*     */   }
/*     */   
/*     */   public void processedElement(String localName, Object object) {
/*     */     try {
/*  43 */       Method elementprocessedHandler = this._saxHandler.getClass().getMethod("processed" + capitalizeString(localName), new Class[] { Object.class });
/*  44 */       elementprocessedHandler.invoke(this._saxHandler, new Object[] { object });
/*     */     }
/*     */     catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException localNoSuchMethodException) {}catch (InvocationTargetException e) {
/*  47 */       e.getCause().printStackTrace();
/*  48 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/*  54 */     String localName = (String)this._currentElement.peek();
/*     */     try {
/*  56 */       Method elementContentHandler = this._saxHandler.getClass().getMethod("content" + capitalizeString(localName), new Class[] { String.class });
/*  57 */       elementContentHandler.setAccessible(true);
/*  58 */       elementContentHandler.invoke(this._saxHandler, new Object[] { new String(ch, start, length) });
/*     */     } catch (NoSuchMethodException|SecurityException e) {
/*  60 */       this._saxHandler.characters(ch, start, length);
/*     */     } catch (IllegalAccessException|IllegalArgumentException e) {
/*  62 */       e.getCause().printStackTrace();
/*  63 */       System.exit(-1);
/*     */     } catch (InvocationTargetException e) {
/*  65 */       e.getCause().printStackTrace();
/*  66 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException
/*     */   {
/*  72 */     this._saxHandler.endDocument();
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*  78 */     this._currentElement.pop();
/*     */     try {
/*  80 */       Method elementEndHandler = this._saxHandler.getClass().getMethod("end" + capitalizeString(localName), new Class[0]);
/*  81 */       elementEndHandler.invoke(this._saxHandler, new Object[0]);
/*     */     } catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException e) {
/*  83 */       this._saxHandler.endElement(uri, localName, qName);
/*     */     } catch (InvocationTargetException e) {
/*  85 */       e.getCause().printStackTrace();
/*  86 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*  93 */     this._saxHandler.endPrefixMapping(prefix);
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*  99 */     this._saxHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */   
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 105 */     this._saxHandler.processingInstruction(target, data);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 110 */     this._saxHandler.setDocumentLocator(locator);
/*     */   }
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException
/*     */   {
/* 115 */     this._saxHandler.skippedEntity(name);
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException
/*     */   {
/* 120 */     this._saxHandler.startDocument();
/*     */   }
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */     throws SAXException
/*     */   {
/* 126 */     this._currentElement.push(localName);
/*     */     try {
/* 128 */       Method elementStartHandler = this._saxHandler.getClass().getMethod("start" + capitalizeString(localName), new Class[] { Attributes.class });
/* 129 */       elementStartHandler.setAccessible(true);
/* 130 */       elementStartHandler.invoke(this._saxHandler, new Object[] { attributes });
/*     */     } catch (NoSuchMethodException|SecurityException e) {
/* 132 */       this._saxHandler.startElement(uri, localName, qName, attributes);
/*     */     } catch (IllegalAccessException|IllegalArgumentException e) {
/* 134 */       e.getCause().printStackTrace();
/* 135 */       System.exit(-1);
/*     */     } catch (InvocationTargetException e) {
/* 137 */       e.getCause().printStackTrace();
/* 138 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 145 */     this._saxHandler.startPrefixMapping(prefix, uri);
/*     */   }
/*     */   
/*     */   public static abstract class DefaultHandlerProducingObject
/*     */     extends DefaultHandler
/*     */   {
/*     */     public abstract Object getObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\XmlObjectConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */