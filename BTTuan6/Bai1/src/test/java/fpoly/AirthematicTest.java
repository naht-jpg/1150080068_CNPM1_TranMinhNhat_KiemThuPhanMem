package fpoly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AirthematicTest {

    public String message = "fpoly exception";

    JunitMessage junitMessage = new JunitMessage(message);

    // TC01: Gọi printMessage() - Kiểm tra exception chia cho 0
    @Test(expected = ArithmeticException.class)
    public void testJUnitMessage() throws Exception {
        System.out.println("fpoly JUnit Message exception is printing ");
        junitMessage.printMessage();
    }

    // TC02: Gọi printHiMessage() - Kiểm tra chuỗi trả về
    @Test
    public void testJUnitHiMessage() {
        message = "Hi!" + message;
        System.out.println("fpoly JUnit Message is printing ");
        assertEquals(message, junitMessage.printHiMessage());
    }

    // TC03: Gọi printHiMessage() với message rỗng - Giá trị biên Xmin
    @Test
    public void testEmptyMessage() {
        JunitMessage emptyMsg = new JunitMessage("");
        String result = emptyMsg.printHiMessage();
        assertEquals("Hi!", result);
    }

    // TC04: Gọi printHiMessage() với 1 ký tự - Giá trị biên Xmin+1
    @Test
    public void testSingleCharMessage() {
        JunitMessage singleCharMsg = new JunitMessage("A");
        String result = singleCharMsg.printHiMessage();
        assertEquals("Hi!A", result);
    }

    // TC05: Gọi printHiMessage() với ký tự đặc biệt
    @Test
    public void testSpecialCharMessage() {
        JunitMessage specialMsg = new JunitMessage("@#$%");
        String result = specialMsg.printHiMessage();
        assertEquals("Hi!@#$%", result);
    }

    // TC06: Kiểm tra kết quả không null và bắt đầu bằng "Hi!"
    @Test
    public void testResultNotNull() {
        JunitMessage testMsg = new JunitMessage("test");
        String result = testMsg.printHiMessage();
        assertNotNull(result);
        assertTrue(result.startsWith("Hi!"));
    }
}
