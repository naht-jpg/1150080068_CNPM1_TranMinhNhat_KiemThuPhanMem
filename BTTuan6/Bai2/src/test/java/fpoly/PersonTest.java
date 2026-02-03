package fpoly;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PersonTest {
    
    // Cách 1: Sử dụng ExpectedException Rule
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testExpectedException() {
        exception.expect(IllegalArgumentException.class);
        new Person("Fpoly", -1);
    }

    // Cách 2: Sử dụng @Test annotation với expected parameter
    @Test(expected = IllegalArgumentException.class)
    public void testExpectedException2() {
        new Person("Fpoly", -1);
    }

    // Cách 3: Sử dụng try-catch để test exception
    @Test
    public void testExpectedException3() {
        try {
            new Person("Fpoly", -1);
            fail("Should have thrown an IllegalArgumentException because age is invalid!");
        } catch (IllegalArgumentException e) {
            // Test passed - exception was thrown as expected
        }
    }

    // TC04: Test age = 0 - Giá trị biên Xmin (không hợp lệ)
    @Test(expected = IllegalArgumentException.class)
    public void testAgeZero() {
        new Person("Fpoly", 0);
    }

    // TC05: Test age = 1 - Giá trị biên Xmin+1 (hợp lệ)
    @Test
    public void testAgeOne() {
        Person person = new Person("Fpoly", 1);
        assertNotNull(person);
    }

    // TC06: Test age = 25 - Giá trị bình thường
    @Test
    public void testAgeNormal() {
        Person person = new Person("Fpoly", 25);
        assertNotNull(person);
    }

    // TC07: Test age = 150 - Giá trị biên Xmax
    @Test
    public void testAgeMax() {
        Person person = new Person("Fpoly", 150);
        assertNotNull(person);
    }
}
