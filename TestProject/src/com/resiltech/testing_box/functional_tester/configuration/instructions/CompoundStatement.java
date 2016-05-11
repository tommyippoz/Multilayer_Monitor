/*    */ package com.resiltech.testing_box.functional_tester.configuration.instructions;
/*    */ 
/*    */ import com.resiltech.testing_box.functional_tester.execution.IRequestInvoker;
/*    */ import com.resiltech.testing_box.functional_tester.execution.IStatement;
/*    */ import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class CompoundStatement implements IStatement
/*    */ {
/*    */   private ArrayList<IStatement> _statements;
/*    */   
/*    */   public static abstract class CompoundStatementHandler extends com.resiltech.testing_box.functional_tester.configuration.XmlObjectConstructor.DefaultHandlerProducingObject
/*    */   {
/*    */     protected abstract CompoundStatement getCompoundStatement();
/*    */     
/*    */     public void processedDeclare(Object obj) throws SAXException
/*    */     {
/* 18 */       if ((obj instanceof Declare)) {
/* 19 */         Declare d = (Declare)obj;
/* 20 */         getCompoundStatement()._statements.add(d);
/*    */       } else {
/* 22 */         throw new SAXException(obj.toString() + " is an invalid object for Declare element");
/*    */       }
/*    */     }
/*    */     
/*    */     public void processedCall(Object obj) throws SAXException {
/* 27 */       if ((obj instanceof Call)) {
/* 28 */         Call c = (Call)obj;
/* 29 */         getCompoundStatement()._statements.add(c);
/*    */       } else {
/* 31 */         throw new SAXException(obj.toString() + " is an invalid object for Call element");
/*    */       }
/*    */     }
/*    */     
/*    */     public void processedCycle(Object obj) throws SAXException {
/* 36 */       if ((obj instanceof Cycle)) {
/* 37 */         Cycle c = (Cycle)obj;
/* 38 */         getCompoundStatement()._statements.add(c);
/*    */       } else {
/* 40 */         throw new SAXException(obj.toString() + " is an invalid object for Cycle element");
/*    */       }
/*    */     }
/*    */     
/*    */     public void processedCondition(Object obj) throws SAXException {
/* 45 */       if ((obj instanceof Condition)) {
/* 46 */         Condition c = (Condition)obj;
/* 47 */         getCompoundStatement()._statements.add(c);
/*    */       } else {
/* 49 */         throw new SAXException(obj.toString() + " is an invalid object for Condition element");
/*    */       }
/*    */     }
/*    */     
/*    */     public void processedSleep(Object obj) throws SAXException {
/* 54 */       if ((obj instanceof Sleep)) {
/* 55 */         Sleep s = (Sleep)obj;
/* 56 */         getCompoundStatement()._statements.add(s);
/*    */       } else {
/* 58 */         throw new SAXException(obj.toString() + " is an invalid object for Sleep element");
/*    */       }
/*    */     }
/*    */     
/*    */     public void processedParallel(Object obj) throws SAXException {
/* 63 */       if ((obj instanceof Parallel)) {
/* 64 */         Parallel p = (Parallel)obj;
/* 65 */         getCompoundStatement()._statements.add(p);
/*    */       } else {
/* 67 */         throw new SAXException(obj.toString() + " is an invalid object for Parallel element");
/*    */       }
/*    */     }
/*    */     
/*    */     public Object getObject()
/*    */     {
/* 73 */       return getCompoundStatement();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected CompoundStatement()
/*    */   {
/* 80 */     this._statements = new ArrayList();
/*    */   }


			public ArrayList<IStatement> get_statements() {
				return _statements;
			}
/*    */   
/*    */   public LinkedList<HashMap<String, String>> executeStatements(IRequestInvoker invoker, com.resiltech.testing_box.functional_tester.execution.IScope scope) {
/* 84 */     Scope compoundScope = new Scope(scope);
			 LinkedList<HashMap<String, String>> list = new LinkedList<HashMap<String, String>>();
			 LinkedList<HashMap<String, String>> partial;
			 String output = "";
/* 85 */     for (IStatement statement : this._statements) {
/*    */       try {
				 partial = statement.execute(invoker, compoundScope);
				 if(partial != null)
					 list.addAll(partial);
/*    */       }
/*    */       catch (RuntimeException localRuntimeException) {
				localRuntimeException.printStackTrace();
				}
/*    */     }
			 return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\configuration\instructions\CompoundStatement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */