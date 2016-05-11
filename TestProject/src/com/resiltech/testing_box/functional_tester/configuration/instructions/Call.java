/*     */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*     */ 
/*     */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*     */ import com.resiltech.testing_box.functional_tester.execution.IScope;

/*     */ import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
/*     */ import java.util.Map.Entry;

/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ public class Call implements com.resiltech.testing_box.functional_tester.execution.IStatement
/*     */ {
/*     */   private String _method;
/*     */   private String _portlet;
/*     */   private HashMap<String, Parameter> _parameters;
/*     */   private HashMap<String, Result> _results;

			
public String get_method() {
	return _method;
}
public String get_portlet() {
	return _portlet;
}
public HashMap<String, Parameter> get_parameters() {
	return _parameters;
}
public HashMap<String, Result> get_results() {
	return _results;
}
/*     */   
/*     */   public static class Result
/*     */   {
/*     */     private String _name;
/*     */     private String _destinationVariable;
/*     */     
/*     */     public Result(String name, String destinationVariable)
/*     */     {
/*  23 */       this._destinationVariable = destinationVariable;
/*  24 */       this._name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  28 */       return this._name;
/*     */     }
/*     */     
/*     */     public String getDestinationVariable() {
/*  32 */       return this._destinationVariable;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Parameter {
/*     */     private String _name;
/*     */     private String _sourceVariable;
/*     */     
/*     */     public Parameter(String name, String sourceVariable) {
/*  41 */       this._name = name;
/*  42 */       this._sourceVariable = sourceVariable;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  46 */       return this._name;
/*     */     }
/*     */     
/*     */     public String getSourceVariable() {
/*  50 */       return this._sourceVariable;
/*     */     }
/*     */     
/*     */     public String getValue(IScope scope) {
/*  54 */       return scope.getValue(this._sourceVariable);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StaticParameter extends Call.Parameter {
/*     */     private String _value;
/*     */     
/*     */     public StaticParameter(String name, String value) {
/*  62 */       super(null, null);
/*  63 */       this._value = value;
/*     */     }
/*     */     
/*     */     public String getValue(IScope scope)
/*     */     {
/*  68 */       return this._value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Call(String portlet, String method)
/*     */   {
/*  79 */     this._parameters = new HashMap();
/*  80 */     this._results = new HashMap();
/*  81 */     this._portlet = portlet;
/*  82 */     this._method = method;
/*     */   }
/*     */   
/*     */   private java.util.Map<String, String> computeParameters(IScope scope) {
/*  86 */     HashMap<String, String> parameters = new HashMap();
/*  87 */     for (Map.Entry<String, Parameter> entry : this._parameters.entrySet()) {
/*  88 */       parameters.put((String)entry.getKey(), ((Parameter)entry.getValue()).getValue(scope));
/*     */     }
/*     */     
/*  91 */     return parameters;
/*     */   }
/*     */   
/*     */   public LinkedList<HashMap<String, String>> execute(IRequestInvoker invoker, IScope scope)
/*     */   {
/*     */ 		HashMap<String, String> callMap = new HashMap<String, String>();    
				try {
					callMap.put("METHOD_NAME", get_method());
					callMap.put("START_TIME", String.valueOf(System.currentTimeMillis()));
	/*  97 */       com.resiltech.testing_box.functional_tester.execution.IResponse response = invoker.invoke(this._portlet, this._method, computeParameters(scope));
	/*  98 */       Thread.sleep(1000);
					callMap.put("END_TIME", String.valueOf(System.currentTimeMillis()));
					if(response != null) {
						for (String resultName : this._results.keySet()) {
	
							System.out.println(this._results.get(resultName).toString());
		/*  99 */         scope.setValue(((Result)this._results.get(resultName)).getDestinationVariable(), response.getResult(resultName));
		/*     */       }
					}
					callMap.put("RESPONSE_CODE", response.getResponseCode());
/*     */     } catch (com.resiltech.testing_box.functional_tester.execution.InvocationException e) {
				callMap.put("END_TIME", String.valueOf(System.currentTimeMillis()));  
				callMap.put("RESPONSE_CODE", "NULL");
				e.printStackTrace();
/*     */     } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
			  LinkedList<HashMap<String, String>> retList = new LinkedList<HashMap<String, String>>();
			  retList.add(callMap);
			  return retList;
			  
/*     */   }
/*     */   
/*     */   public static com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor getXmlConstructor()
/*     */   {
/* 108 */     return new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor("/FunctionalTest/Workloads/Workload/Choreography/*/Call", new com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor.DefaultHandlerProducingObject() {
/*     */       private Call _call;
/*     */       private String _parameterName;
/*     */       
/*     */       public void startCall(Attributes attribute) throws org.xml.sax.SAXException {
/* 113 */         String portlet = attribute.getValue("portlet");
/* 114 */         String method = attribute.getValue("method");
/* 115 */         this._call = new Call(portlet, method);
/*     */       }
/*     */       
/*     */       public void startParameter(Attributes attribute) {
/* 119 */         int variableAttributeIndex = attribute.getIndex("variable");
/* 120 */         if (variableAttributeIndex != -1) {
/* 121 */           String parameterName = attribute.getValue("name");
/* 122 */           String parameterVariable = attribute.getValue(variableAttributeIndex);
/* 123 */           this._call._parameters.put(parameterName, new Call.Parameter(parameterName, parameterVariable));
/* 124 */           this._parameterName = null;
/*     */         } else {
/* 126 */           this._parameterName = attribute.getValue("name");
/*     */         }
/*     */       }
/*     */       
/*     */       public void contentParameter(String content) {
/* 131 */         if (this._parameterName != null) {
/* 132 */           this._call._parameters.put(this._parameterName, new Call.StaticParameter(this._parameterName, content));
/*     */         }
/*     */       }
/*     */       
/*     */       public void endParameter() {
/* 137 */         this._parameterName = null;
/*     */       }
/*     */       
/*     */       public void startStoreresult(Attributes attribute) {
/* 141 */         String name = attribute.getValue("name");
/* 142 */         String variable = attribute.getValue("variable");
/* 143 */         this._call._results.put(name, new Call.Result(name, variable));
/*     */       }
/*     */       
/*     */       public Object getObject()
/*     */       {
/* 148 */         return this._call;
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\Call.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */