import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Response Class Tests")
class ResponseTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should store non-null text")
        void constructorShouldStoreText() {
            Response response = new Response("Hello");
            assertEquals("Hello", response.getText());
        }

        @Test
        @DisplayName("Should accept empty string")
        void constructorShouldAllowEmptyString() {
            Response response = new Response("");
            assertEquals("", response.getText());
        }

        @Test
        @DisplayName("Should throw NullPointerException if text is null")
        void constructorShouldThrowIfTextIsNull() {
            Exception exception = assertThrows(NullPointerException.class, () -> {
                new Response(null);
            });
            assertTrue(exception.getMessage().contains("text"));
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getText() should return the stored text")
        void getTextShouldReturnSameInstance() {
            @NonNull String source = "data";
            Response response = new Response(source);
            assertSame(source, response.getText());
        }

        @Test
        @DisplayName("getText() should not return null")
        void getTextShouldNeverReturnNull() {
            Response response = new Response("Hello");
            assertNotNull(response.getText());
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Response should be immutable - no setters exist")
        void shouldHaveNoSetters() {
            try {
                Response.class.getDeclaredMethod("setText", String.class);
                fail("Setter setText() should not exist");
            } catch (NoSuchMethodException e) {
                // OK
            }
        }

        @Test
        @DisplayName("Field is private and final")
        void fieldShouldBePrivateAndFinal() {
            try {
                var field = Response.class.getDeclaredField("text");
                int modifiers = field.getModifiers();
                assertTrue(java.lang.reflect.Modifier.isPrivate(modifiers));
                assertTrue(java.lang.reflect.Modifier.isFinal(modifiers));
            } catch (NoSuchFieldException e) {
                fail("Field 'text' not found");
            }
        }
    }

    @Nested
    @DisplayName("Equality & HashCode Contract")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Two responses with same text should be equal")
        void twoResponsesWithSameTextShouldBeEqual() {
            Response r1 = new Response("Hello");
            Response r2 = new Response("Hello");

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("Responses with different text should not be equal")
        void responsesWithDifferentTextShouldNotBeEqual() {
            Response r1 = new Response("Hello");
            Response r2 = new Response("World");

            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals() should be symmetric")
        void equalsShouldBeSymmetric() {
            Response a = new Response("Hi");
            Response b = new Response("Hi");
            assertTrue(a.equals(b) && b.equals(a));
        }

        @Test
        @DisplayName("equals() should be transitive")
        void equalsShouldBeTransitive() {
            Response a = new Response("Hi");
            Response b = new Response("Hi");
            Response c = new Response("Hi");

            assertTrue(a.equals(b));
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));
        }

        @Test
        @DisplayName("equals() should be consistent on repeated calls")
        void equalsShouldBeConsistent() {
            Response a = new Response("Hi");
            Response b = new Response("Hi");

            assertEquals(a.equals(b), a.equals(b));
        }

        @Test
        @DisplayName("equals() should return false when comparing to null")
        void equalsShouldReturnFalseForNull() {
            Response a = new Response("Hi");
            assertFalse(a.equals(null));
        }

        @Test
        @DisplayName("hashCode() should be consistent across calls")
        void hashCodeShouldBeConsistent() {
            Response r = new Response("Hi");
            int first = r.hashCode();
            int second = r.hashCode();
            assertEquals(first, second);
        }

        @Test
        @DisplayName("Different responses should have different hashCodes")
        void differentResponsesShouldHaveDifferentHashCodes() {
            Response r1 = new Response("A");
            Response r2 = new Response("B");

            assertNotEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("Should not equal to instance of another class")
        void shouldNotEqualOtherClass() {
            Response response = new Response("Hello");
            assertFalse(response.equals(new Object()));
        }

        @Test
        @DisplayName("Should not equal to String")
        void shouldNotEqualString() {
            Response response = new Response("Hello");
            assertFalse(response.equals("Hello"));
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString() should contain text")
        void toStringShouldIncludeText() {
            Response response = new Response("Help");
            String str = response.toString();

            assertNotNull(str);
            assertTrue(str.contains("Response{"));
            assertTrue(str.contains("text='Help'"));
            assertTrue(str.contains("}"));
        }

        @Test
        @DisplayName("toString() should not be null")
        void toStringShouldNotBeNull() {
            Response response = new Response("Hi");
            assertNotNull(response.toString());
        }
    }

    @Nested
    @DisplayName("Contract Tests")
    class ContractTests {

        @Test
        @DisplayName("Multiple instances should store independent values")
        void multipleInstancesShouldStoreIndependentValues() {
            Response r1 = new Response("A");
            Response r2 = new Response("B");

            assertEquals("A", r1.getText());
            assertEquals("B", r2.getText());
        }
    }
}