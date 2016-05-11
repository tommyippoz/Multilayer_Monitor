package com.resiltech.testing_box.functional_tester.execution;

import java.util.HashMap;
import java.util.LinkedList;

public abstract interface IStatement
{
  public abstract LinkedList<HashMap<String, String>> execute(IRequestInvoker paramIRequestInvoker, IScope paramIScope);
}


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\execution\IStatement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */