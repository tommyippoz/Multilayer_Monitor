package com.resiltech.testing_box.functional_tester.execution;

import java.util.Set;

public abstract interface IScope
{
  public abstract Set<String> getVariableNames();
  
  public abstract String getValue(String paramString);
  
  public abstract boolean hasVariable(String paramString);
  
  public abstract void setValue(String paramString1, String paramString2);
  
  public abstract void declare(String paramString);
}


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\execution\IScope.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */