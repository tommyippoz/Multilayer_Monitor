/*     */ package com.resiltech.testing_box.functional_tester;
/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class ConfigurationFileHandler extends org.xml.sax.helpers.DefaultHandler
/*     */ {
/*     */   private java.util.ArrayList<XmlObjectConstructorWithPattern> _handlersCollection;
/*     */   private Stack<XmlObjectConstructor> _handlers;
/*     */   private Stack<StringBuilder> _contentHandler;
/*     */   private String _xmlPath;
/*     */   private Object _configuration;
/*     */   
/*     */   private static class XmlObjectConstructorWithPattern
/*     */   {
/*     */     private java.util.regex.Pattern _pattern;
/*     */     private XmlObjectConstructor _constructor;
/*     */     
/*     */     public XmlObjectConstructorWithPattern(XmlObjectConstructor constructor)
/*     */     {
/*  22 */       String rawPattern = constructor.getElementNameHandled().replaceAll("\\*/", "(\\\\w+/)*");
/*  23 */       this._constructor = constructor;
/*     */       
/*  25 */       this._pattern = java.util.regex.Pattern.compile(rawPattern);
/*     */     }
/*     */     
/*     */     public String match(String text) {
/*  29 */       java.util.regex.Matcher m = this._pattern.matcher(text);
/*  30 */       String match = null;
/*  31 */       if (m.find()) {
/*  32 */         match = m.group();
/*     */       }
/*     */       
/*  35 */       return match;
/*     */     }
/*     */     
/*     */     public XmlObjectConstructor getConstructor() {
/*  39 */       return this._constructor;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String stripLastPieceFromPath()
/*     */   {
/*  50 */     int slashIndex = this._xmlPath.lastIndexOf('/');
/*  51 */     String strippedText = "";
/*  52 */     if (slashIndex > 0) {
/*  53 */       strippedText = this._xmlPath.substring(slashIndex + 1);
/*  54 */       this._xmlPath = this._xmlPath.substring(0, slashIndex);
/*     */     }
/*     */     
/*  57 */     return strippedText;
/*     */   }
/*     */   
/*     */   private void enqueueToPath(String name) {
/*  61 */     if (this._xmlPath.length() == 1) {
/*  62 */       this._xmlPath += name;
/*     */     } else {
/*  64 */       this._xmlPath = (this._xmlPath + "/" + name);
/*     */     }
/*     */   }
/*     */   
/*     */   public ConfigurationFileHandler(java.util.ArrayList<XmlObjectConstructor> handlers) {
/*  69 */     this._handlersCollection = new java.util.ArrayList();
/*  70 */     this._handlers = new Stack();
/*  71 */     this._contentHandler = new Stack();
/*  72 */     this._xmlPath = "/";
/*     */     
/*  74 */     for (XmlObjectConstructor xmlObjectConstructor : handlers) {
/*  75 */       this._handlersCollection.add(new XmlObjectConstructorWithPattern(xmlObjectConstructor));
/*     */     }
/*     */   }
/*     */   
/*     */   private XmlObjectConstructor lookupForSuitableHandler() {
/*  80 */     XmlObjectConstructor bestSolution = null;
/*  81 */     String bestMatch = null;
/*  82 */     for (XmlObjectConstructorWithPattern constructor : this._handlersCollection) {
/*  83 */       String match = constructor.match(this._xmlPath);
/*  84 */       if ((match != null) && (
/*  85 */         (bestMatch == null) || (bestMatch.length() < match.length()))) {
/*  86 */         bestSolution = constructor.getConstructor();
/*  87 */         bestMatch = match;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  92 */     return bestSolution;
/*     */   }
/*     */   
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
/*     */     throws SAXException
/*     */   {
/*  99 */     enqueueToPath(localName);
/* 100 */     XmlObjectConstructor handler = lookupForSuitableHandler();
/* 101 */     if (handler == null) {
/* 102 */       handler = (XmlObjectConstructor)this._handlers.peek();
/*     */     }
/* 104 */     this._handlers.push(handler);
/* 105 */     this._contentHandler.push(new StringBuilder());
/*     */     
/* 107 */     handler.startElement(uri, localName, qName, attributes);
/*     */   }
/*     */   
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 113 */     if (this._handlers.size() > 0) {
/* 114 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.pop();
/* 115 */       StringBuilder tagContentHandler = (StringBuilder)this._contentHandler.pop();
/* 116 */       char[] contentAsCharArray = tagContentHandler.toString().toCharArray();
/* 117 */       stripLastPieceFromPath();
/* 118 */       handler.characters(contentAsCharArray, 0, contentAsCharArray.length);
/* 119 */       handler.endElement(uri, localName, qName);
/* 120 */       if (this._handlers.size() > 0) {
/* 121 */         if (handler != this._handlers.peek()) {
/* 122 */           ((XmlObjectConstructor)this._handlers.peek()).processedElement(localName, handler.getProducedObject());
/*     */         }
/*     */       } else {
/* 125 */         this._configuration = handler.getProducedObject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 132 */     if (this._handlers.size() > 0) {
/* 133 */       StringBuilder tagContentHandler = (StringBuilder)this._contentHandler.peek();
/* 134 */       tagContentHandler.append(new String(ch, start, length));
/*     */     }
/*     */   }
/*     */   
/*     */   public void endDocument() throws SAXException
/*     */   {
/* 140 */     if (this._handlers.size() > 0) {
/* 141 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 142 */       handler.endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   public void endPrefixMapping(String prefix) throws SAXException
/*     */   {
/* 148 */     if (this._handlers.size() > 0) {
/* 149 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 150 */       handler.endPrefixMapping(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 157 */     if (this._handlers.size() > 0) {
/* 158 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 159 */       handler.ignorableWhitespace(ch, start, length);
/*     */     }
/*     */   }
/*     */   
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 166 */     if (this._handlers.size() > 0) {
/* 167 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 168 */       handler.processingInstruction(target, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(org.xml.sax.Locator locator)
/*     */   {
/* 174 */     if (this._handlers.size() > 0) {
/* 175 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 176 */       handler.setDocumentLocator(locator);
/*     */     }
/*     */   }
/*     */   
/*     */   public void skippedEntity(String name) throws SAXException
/*     */   {
/* 182 */     if (this._handlers.size() > 0) {
/* 183 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 184 */       handler.skippedEntity(name);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startDocument() throws SAXException
/*     */   {
/* 190 */     if (this._handlers.size() > 0) {
/* 191 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 192 */       handler.startDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 199 */     if (this._handlers.size() > 0) {
/* 200 */       XmlObjectConstructor handler = (XmlObjectConstructor)this._handlers.peek();
/* 201 */       handler.startPrefixMapping(prefix, uri);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getConfiguration()
/*     */   {
/* 207 */     return this._configuration;
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\ConfigurationFileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */