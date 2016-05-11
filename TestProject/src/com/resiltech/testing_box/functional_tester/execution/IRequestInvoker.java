package com.resiltech.testing_box.functional_tester.execution;

import java.util.Map;

public abstract interface IRequestInvoker
{
  public abstract IResponse invoke(String paramString1, String paramString2, Map<String, String> paramMap)
    throws InvocationException;
}


/* Location:              C:\Users\Tommy\Desktop\TestingBox Sorgenti jar\WorkloadGenerator.jar!\com\resiltech\testing_box\functional_tester\execution\IRequestInvoker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */