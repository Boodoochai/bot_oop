import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Request Class Tests")
class RequestTest {

    @Nested
    @DisplayName("Constructor & Initialization")
    class ConstructorTests {

        @Test
        @DisplayName("Should store non-null owner and text")
        void constructorShouldStoreOwnerAndText() {
            @NonNull Client client = mock(Client.class);
            Request request = new Request(client, "Hello");

            assertEquals(client, request.getRequestOwner());
            assertEquals("Hello", request.getText());
            assertSame(client, request.getRequestOwner());
        }

        @Test
        @DisplayName("Should throw NullPointerException if owner is null")
        void constructorShouldThrowIfOwnerIsNull() {
            assertThrows(NullPointerException.class, () -> new Request(null, "Hello"));
        }

        @Test
        @DisplayName("Should throw NullPointerException if text is null")
        void constructorShouldThrowIfTextIsNull() {
            @NonNull Client client = mock(Client.class);
            assertThrows(NullPointerException.class, () -> new Request(client, null));
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getRequestOwner() should return the same instance")
        void getRequestOwnerShouldReturnSameInstance() {
            @NonNull Client client = mock(Client.class);
            Request request = new Request(client, "Hi");
            assertSame(client, request.getRequestOwner());
        }

        @Test
        @DisplayName("getText() should return exact string")
        void getTextShouldReturnText() {
            Request request = new Request(mock(Client.class), "Hello");
            assertEquals("Hello", request.getText());
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Request should be immutable - no setters exist")
        void shouldHaveNoSetters() {
            try {
                Request.class.getDeclaredMethod("setRequestOwner", Client.class);
                fail("Setter setRequestOwner() should not exist");
            } catch (NoSuchMethodException e) {
                // OK
            }

            try {
                Request.class.getDeclaredMethod("setText", String.class);
                fail("Setter setText() should not exist");
            } catch (NoSuchMethodException e) {
                // OK
            }
        }

        @Test
        @DisplayName("Fields are private and final")
        void fieldsShouldBePrivateAndFinal() {
            assertFieldIsPrivateFinal("requestOwner");
            assertFieldIsPrivateFinal("text");
        }

        private void assertFieldIsPrivateFinal(String fieldName) {
            try {
                var field = Request.class.getDeclaredField(fieldName);
                int modifiers = field.getModifiers();
                assertTrue(java.lang.reflect.Modifier.isPrivate(modifiers));
                assertTrue(java.lang.reflect.Modifier.isFinal(modifiers));
            } catch (NoSuchFieldException e) {
                fail("Field " + fieldName + " not found");
            }
        }
    }

    @Nested
    @DisplayName("Equality & HashCode Contract")
    class EqualsAndHashCodeTests {

        private final Client client1 = mock(Client.class);
        private final Client client2 = mock(Client.class);
        private final String text1 = "Hello";
        private final String text2 = "World";

        @Test
        @DisplayName("Two identical requests should be equal")
        void twoRequestsWithSameDataShouldBeEqual() {
            Request r1 = new Request(client1, text1);
            Request r2 = new Request(client1, text1);

            assertEquals(r1, r2);
            assertEquals(r1.hashCode(), r2.hashCode());
        }

        @Test
        @DisplayName("Requests with different owners should not be equal")
        void requestsWithDifferentOwnersShouldNotBeEqual() {
            Request r1 = new Request(client1, text1);
            Request r2 = new Request(client2, text1);

            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("Requests with different texts should not be equal")
        void requestsWithDifferentTextsShouldNotBeEqual() {
            Request r1 = new Request(client1, text1);
            Request r2 = new Request(client1, text2);

            assertNotEquals(r1, r2);
        }

        @Test
        @DisplayName("equals() should be symmetric")
        void equalsShouldBeSymmetric() {
            Request a = new Request(client1, text1);
            Request b = new Request(client1, text1);
            assertTrue(a.equals(b) && b.equals(a));
        }

        @Test
        @DisplayName("equals() should be transitive")
        void equalsShouldBeTransitive() {
            Request a = new Request(client1, text1);
            Request b = new Request(client1, text1);
            Request c = new Request(client1, text1);

            assertEquals(a, b);
            assertEquals(b, c);
            assertEquals(a, c);
        }

        @Test
        @DisplayName("equals() should be consistent on repeated calls")
        void equalsShouldBeConsistent() {
            Request a = new Request(client1, text1);
            Request b = new Request(client1, text1);

            assertEquals(a.equals(b), a.equals(b));
        }

        @Test
        @DisplayName("equals() should return false when comparing to null")
        void equalsShouldReturnFalseForNull() {
            Request a = new Request(client1, text1);
            assertNotEquals(null, a);
        }

        @Test
        @DisplayName("hashCode() should be consistent across calls")
        void hashCodeShouldBeConsistent() {
            Request r = new Request(client1, text1);
            int first = r.hashCode();
            int second = r.hashCode();
            assertEquals(first, second);
        }

        @Test
        @DisplayName("Different requests should have different hashCodes when data differs")
        void differentRequestsShouldHaveDifferentHashCodes() {
            Request r1 = new Request(client1, text1);
            Request r2 = new Request(client2, text1);
            Request r3 = new Request(client1, text2);

            assertNotEquals(r1.hashCode(), r2.hashCode());
            assertNotEquals(r1.hashCode(), r3.hashCode());
            assertNotEquals(r2.hashCode(), r3.hashCode());
        }

        @Test
        @DisplayName("Request should not equal to instance of another class")
        void shouldNotEqualOtherClass() {
            Request request = new Request(client1, text1);
            Object other = new Object();
            assertNotEquals(request, other);
        }

        @Test
        @DisplayName("Request should not equal to String")
        void shouldNotEqualString() {
            Request request = new Request(client1, text1);
            assertNotEquals("Some string", request);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() should not be null")
        void toStringShouldNotBeNull() {
            Request request = new Request(mock(Client.class), "Hi");
            assertNotNull(request.toString());
        }
    }

    @Nested
    @DisplayName("Contract Tests")
    class ContractTests {

        @Test
        @DisplayName("Multiple requests should store independent values")
        void multipleRequestsShouldStoreIndependentValues() {
            Client client1 = mock(Client.class);
            Client client2 = mock(Client.class);

            Request r1 = new Request(client1, "A");
            Request r2 = new Request(client2, "B");

            assertEquals(client1, r1.getRequestOwner());
            assertEquals("A", r1.getText());

            assertEquals(client2, r2.getRequestOwner());
            assertEquals("B", r2.getText());
        }
    }
}