/*     */ package com.resiltech.testing_box.functional_tester.runner;
/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.ConfigurationFileHandler;
import com.resiltech.testing_box.functional_tester.MainTestXPath;
import com.resiltech.testing_box.functional_tester.WorkloadConfiguration;
/*     */ import com.resiltech.testing_box.functional_tester.configuration.FunctionalTest;
import com.resiltech.testing_box.functional_tester.configuration.Request;
import com.resiltech.testing_box.functional_tester.configuration.WorkloadReference;
import com.resiltech.testing_box.functional_tester.configuration.XmlWorkload;
/*     */ import com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Call;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Choreography;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Condition;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Cycle;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Declare;
import com.resiltech.testing_box.functional_tester.configuration.instructions.IfFalse;
import com.resiltech.testing_box.functional_tester.configuration.instructions.IfTrue;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Parallel;
import com.resiltech.testing_box.functional_tester.configuration.instructions.Sleep;
import com.resiltech.testing_box.functional_tester.configuration.instructions.CompoundStatement;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IMethodInvocationListener;
import com.resiltech.testing_box.monitored_system.automator.AutomatorController;

/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
import java.util.LinkedList;
/*     */ import java.util.Set;

/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import javax.xml.validation.SchemaFactory;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathExpression;
/*     */ import javax.xml.xpath.XPathExpressionException;
/*     */ import javax.xml.xpath.XPathFactory;

/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class FunctionalTestRunner
/*     */ {
			private FunctionalTest functionalTest;   
	
/*     */   private static class WorkloadSpecifierNamespaceContext implements NamespaceContext
/*     */   {
/*     */     private HashMap<String, String> _namespaces;
/*     */     
/*     */     public WorkloadSpecifierNamespaceContext()
/*     */     {
/*  52 */       this._namespaces = new HashMap();
/*  53 */       this._namespaces.put("tns", "http://www.example.org/configurationValidator");
/*     */     }
/*     */     
/*     */     public String getNamespaceURI(String prefix) {
/*  57 */       String namespace = null;
/*  58 */       if (this._namespaces.containsKey(prefix)) {
/*  59 */         namespace = (String)this._namespaces.get(prefix);
/*     */       }
/*  61 */       return namespace;
/*     */     }
/*     */     
/*     */     public String getPrefix(String namespaceURI) {
/*  65 */       return null;
/*     */     }
/*     */     
/*     */     public Iterator getPrefixes(String namespaceURI)
/*     */     {
/*  70 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static ArrayList<XmlObjectConstructor> getXmlBinders()
/*     */   {
/*  76 */     ArrayList<XmlObjectConstructor> constructors = new ArrayList();
/*     */     try {
/*  78 */       Class[] emptyArguments = new Class[0];
/*  79 */       Object[] emptyObjectArray = new Object[0];
/*  80 */       //Set<Class<?>> classes = ClassUtil.getClasses("com.resiltech.testing_box.functional_tester.configuration");
/*  81 */       //classes.addAll(ClassUtil.getClasses("com.resiltech.testing_box.functional_tester.configuration.instructions"));
/*  82 */       ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
				classes.add(Call.class);
				classes.add(Choreography.class);
				classes.add(Condition.class);
				classes.add(Cycle.class);
				classes.add(Declare.class);
				classes.add(IfFalse.class);
				classes.add(IfTrue.class);
				classes.add(Parallel.class);
				classes.add(Sleep.class);
				classes.add(FunctionalTest.class);
				classes.add(Request.class);
				classes.add(WorkloadReference.class);
				classes.add(XmlObjectConstructor.class);
				classes.add(XmlWorkload.class);
				for (Class<?> klass : classes) {
/*     */         try {
/*  84 */           Method m = klass.getMethod("getXmlConstructor", emptyArguments);
/*  85 */           if (Modifier.isStatic(m.getModifiers())) {
/*  86 */             XmlObjectConstructor c = (XmlObjectConstructor)m.invoke(null, emptyObjectArray);
/*  87 */             constructors.add(c);
/*     */           }
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */       }
/*     */     } catch (Exception e) {
/*  93 */       System.out.println("I wouldn't mind about this -> " + e.getMessage());
/*  94 */       e.printStackTrace(System.err);
/*     */     }
/*     */     
/*  97 */     return constructors;
/*     */   }
/*     */   
/*     */   private static File resolveIncludes(File inputFile) throws IOException, SAXException, ParserConfigurationException {
/* 101 */     File temp = File.createTempFile("processed" + inputFile.getName(), "");
/* 102 */     DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
/*     */     
/* 104 */     dbfac.setValidating(false);
/* 105 */     dbfac.setNamespaceAware(true);
/*     */     
/* 107 */     SchemaFactory schemaFactory = 
/* 108 */       SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
/* 109 */     dbfac.setSchema(schemaFactory.newSchema(
/* 110 */       new Source[] { new StreamSource("model/configurationValidator.xsd") }));
/*     */     
/* 112 */     DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
/* 113 */     XPathExpression configurationIncludesExpression = null;
/* 114 */     XPathExpression workloadsIncludesExpression = null;
/* 115 */     XPathExpression requestsExpression = null;
/* 116 */     XPathExpression workloadsExpression = null;
/* 117 */     Document doc = docBuilder.parse(inputFile);
/*     */     
/* 119 */     XPathFactory xPathfactory = XPathFactory.newInstance();
/* 120 */     XPath xpath = xPathfactory.newXPath();
/* 121 */     xpath.setNamespaceContext(new WorkloadSpecifierNamespaceContext());
/*     */     try {
/* 123 */       configurationIncludesExpression = xpath.compile("/tns:FunctionalTest/tns:Configuration/tns:Include");
/* 124 */       workloadsIncludesExpression = xpath.compile("/tns:FunctionalTest/tns:Workloads/tns:Include");
/* 125 */       requestsExpression = xpath.compile("/tns:FunctionalTest/tns:Configuration/tns:Request");
/* 126 */       workloadsExpression = xpath.compile("/tns:FunctionalTest/tns:Workloads/tns:Workload");
/* 127 */       NodeList configurationIncludes = (NodeList)configurationIncludesExpression.evaluate(doc, XPathConstants.NODESET);
/* 128 */       for (int i = 0; i < configurationIncludes.getLength(); i++) {
/* 129 */         Node n = configurationIncludes.item(i);
/* 130 */         if ((n != null) && (n.getNodeType() == 1)) {
/* 131 */           Node configurationNode = n.getParentNode();
/* 132 */           Element include = (Element)n;
/* 133 */           String filenameToBeIncluded = include.getAttribute("configuration-file");
/* 134 */           File fileToBeIncluded = new File(inputFile.getParentFile().getAbsolutePath(), filenameToBeIncluded);
/* 135 */           Document docToBeIncluded = docBuilder.parse(fileToBeIncluded);
/* 136 */           NodeList requests = (NodeList)requestsExpression.evaluate(docToBeIncluded, XPathConstants.NODESET);
/* 137 */           configurationNode.removeChild(n);
/* 138 */           for (int j = 0; j < requests.getLength(); j++) {
/* 139 */             Node rn = requests.item(j);
/* 140 */             if ((rn != null) && (rn.getNodeType() == 1)) {
/* 141 */               Node adaptedRequest = doc.importNode(rn, true);
/* 142 */               configurationNode.appendChild(adaptedRequest);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 149 */       NodeList workloadIncludes = (NodeList)workloadsIncludesExpression.evaluate(doc, XPathConstants.NODESET);
/* 150 */       for (int i = 0; i < workloadIncludes.getLength(); i++) {
/* 151 */         Node n = workloadIncludes.item(i);
/* 152 */         if ((n != null) && (n.getNodeType() == 1)) {
/* 153 */           Node workloadsNode = n.getParentNode();
/* 154 */           Element include = (Element)n;
/* 155 */           String filenameToBeIncluded = include.getAttribute("configuration-file");
/* 156 */           File fileToBeIncluded = new File(inputFile.getParentFile().getAbsolutePath(), filenameToBeIncluded);
/* 157 */           Document docToBeIncluded = docBuilder.parse(fileToBeIncluded);
/* 158 */           NodeList requests = (NodeList)workloadsExpression.evaluate(docToBeIncluded, XPathConstants.NODESET);
/* 159 */           workloadsNode.removeChild(n);
/* 160 */           for (int j = 0; j < requests.getLength(); j++) {
/* 161 */             Node rn = requests.item(j);
/* 162 */             if ((rn != null) && (rn.getNodeType() == 1)) {
/* 163 */               Node adaptedRequest = doc.importNode(rn, true);
/* 164 */               workloadsNode.appendChild(adaptedRequest);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 171 */       TransformerFactory tFactory = TransformerFactory.newInstance();
/* 172 */       Transformer transformer = tFactory.newTransformer();
/*     */       
/* 174 */       DOMSource source = new DOMSource(doc);
/* 175 */       StreamResult result = new StreamResult(new java.io.FileOutputStream(temp));
/* 176 */       transformer.transform(source, result);
/*     */     } catch (XPathExpressionException|TransformerException e) {
/* 178 */       System.out.println("This could be a serious issues, I have guess due to a \"System.exit(-1)\" that I'd written a couple of weeks ago. " + e.getMessage());
/* 179 */       e.printStackTrace(System.err);
/* 180 */       System.exit(-1);
/*     */     }
/*     */     
/* 183 */     return temp;
/*     */   }

			private File workloadFile;
			private boolean correctExecution;
			private AutomatorController automationController;
/*     */   
/*     */   public FunctionalTestRunner(File workloadFile) {
				this.workloadFile = workloadFile;
				correctExecution = false;
				setupTester();
/*     */   }

			private void setupTester(){
				 ArrayList<XmlObjectConstructor> constructors = getXmlBinders();
				 ConfigurationFileHandler configurationParser = new ConfigurationFileHandler(constructors);
				 /*     */     
				 SAXParserFactory factory = SAXParserFactory.newInstance();
				 /* 198 */     factory.setValidating(false);
				 /* 199 */     factory.setNamespaceAware(true);
				 /*     */     
				 /* 201 */     SchemaFactory schemaFactory = 
				 /* 202 */       SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
				 /*     */     try
				 /*     */     {
				 /* 205 */       factory.setSchema(schemaFactory.newSchema(
				 /* 206 */         new Source[] { new StreamSource("model/configurationValidator.xsd") }));
				 /*     */       
				 /* 208 */       SAXParser parser = factory.newSAXParser();
				 /*     */       
				 /* 210 */       File preprocessedFile = resolveIncludes(workloadFile);
				 /*     */       
				 /* 212 */       XMLReader reader = parser.getXMLReader();
				 /* 213 */       reader.setContentHandler(configurationParser);
				 /* 214 */       reader.parse(new org.xml.sax.InputSource(new java.io.FileInputStream(preprocessedFile)));
				 /*     */       
				 /* 216 */       functionalTest = (FunctionalTest)configurationParser.getConfiguration();
				 
				 /*     */     } catch (Exception e) {
				 /* 224 */       System.err.println("An error has been raised during the workload execution: " + workloadFile.getAbsolutePath());
				 /* 225 */       e.printStackTrace(System.err);
				 /*     */     }
			}
/*     */   
/*     */   public boolean execute() {
/* 195 */     correctExecution = false; 
/*     */     try
/*     */     {
				functionalTest.run(new HashMap<String, String>());
/* 222 */       correctExecution = true;
/*     */     } catch (Exception e) {
/* 224 */       System.out.println("An error has been raised during the workload execution: " + workloadFile.getAbsolutePath());
/* 225 */       e.printStackTrace(System.err);
				correctExecution = false;
/*     */     }
/* 228 */     return correctExecution;
/*     */   }

			public Collection<XmlWorkload> listWorkloads(){
				return functionalTest.getWorkloads().values();
			}
			
			public LinkedList<HashMap<String, String>> listInvocations(){
				if(correctExecution)
					return functionalTest.getInvocations();
				else return null;
			}
			
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\runner\FunctionalTestRunner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */