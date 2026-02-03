package fpoly.junit;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class ErrorCollectorExampleTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    // TC01: Test addError() thu thập nhiều lỗi
    @Test
    public void example() {
        collector.addError(new Throwable("There is an error in first line"));

        collector.addError(new Throwable("There is an error in second line"));

        System.out.println("Hello");
        try {
            Assert.assertTrue("A " == "B");
        } catch (Throwable t) {
            collector.addError(t);
        }
        System.out.println("World!!!");
    }

    // TC02: Test checkThat() với giá trị đúng - Không có lỗi
    @Test
    public void testCheckThatCorrectValue() {
        collector.checkThat("Value should be 10", 10, equalTo(10));
        collector.checkThat("String should match", "Hello", equalTo("Hello"));
        System.out.println("TC02 - All values correct, no errors collected");
    }

    // TC03: Test checkThat() với giá trị sai - Thu thập lỗi
    @Test
    public void testCheckThatWrongValue() {
        collector.checkThat("Value should be 10 but got 5", 5, equalTo(10));
        System.out.println("TC03 - Wrong value, error collected but test continues");
    }

    // TC04: Test nhiều checkThat() với một số sai
    @Test
    public void testMultipleCheckThat() {
        collector.checkThat("First check - correct", 10, equalTo(10));
        collector.checkThat("Second check - wrong", 5, equalTo(10));
        collector.checkThat("Third check - correct", 20, equalTo(20));
        System.out.println("TC04 - Multiple checks, one error collected");
    }

    // TC05: Test không có lỗi nào - Test pass hoàn toàn
    @Test
    public void testNoErrors() {
        collector.checkThat(100, equalTo(100));
        collector.checkThat("test", equalTo("test"));
        collector.checkThat(true, equalTo(true));
        System.out.println("TC05 - No errors, test passed completely");
    }
}
