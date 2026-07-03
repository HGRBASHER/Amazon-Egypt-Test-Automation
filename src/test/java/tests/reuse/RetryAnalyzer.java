package tests.reuse;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxCount = 3;

    @Override
    public boolean retry(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE){
            if (retryCount < maxCount){
                retryCount++;
                return true;
            }
            return false;
        }
        return false;
    }
}