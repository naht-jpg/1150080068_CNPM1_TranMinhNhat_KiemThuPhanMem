package framework.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Tự động chạy lại test bị FAIL tối đa 2 lần.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX) {
            count++;
            System.out.println("↩ Retry " + count + "/" + MAX + ": " + result.getName());
            return true;
        }
        return false;
    }
}
