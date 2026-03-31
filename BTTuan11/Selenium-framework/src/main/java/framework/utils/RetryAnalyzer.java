package framework.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Tự động chạy lại test bị FAIL tối đa MAX_RETRY lần.
 *
 * Cách dùng:
 *   @Test(retryAnalyzer = RetryAnalyzer.class)
 *   public void testSomething() { ... }
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println("↩ Retry lần " + retryCount + "/" + MAX_RETRY
                    + " cho test: " + result.getName());
            return true;
        }
        return false;
    }
}
