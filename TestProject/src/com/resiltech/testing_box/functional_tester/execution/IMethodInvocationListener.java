package com.resiltech.testing_box.functional_tester.execution;

import java.net.URL;

public abstract interface IMethodInvocationListener
{
  public abstract void startWorkload();
  
  public abstract void endWorkload();
  
  public abstract void startInvocation(URL paramURL, String paramString);
  
  public abstract void endInvocation(URL paramURL, int paramInt, String paramString);
}


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\execution\IMethodInvocationListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */