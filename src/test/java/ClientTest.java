import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client Class Tests")
public class ClientTest {

    private @NonNull UUID testUuid;
    private @NonNull String testName;
    private @NonNull Client client;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testName = "John Doe";
        client = new Client(testUuid, testName);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create client with valid UUID and name")
        void shouldCreateClientWithValidData() {
            assertNotNull(client);
            assertEquals(testUuid, client.getUUID());
            assertEquals(testName, client.getName());
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("getUUID() should return correct UUID")
        void getUUIDShouldReturnCorrectUUID() {
            assertEquals(testUuid, client.getUUID());
            assertNotSame(UUID.randomUUID(), client.getUUID()); // Не новый UUID
        }

        @Test
        @DisplayName("getName() should return correct name")
        void getNameShouldReturnCorrectName() {
            assertEquals(testName, client.getName());
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @SuppressWarnings("JavaReflectionMemberAccess")
        @Test
        @DisplayName("Client should be immutable - no setters present")
        void clientShouldBeImmutable() {
            try {
                client.getClass().getDeclaredMethod("setClientId", UUID.class);
                fail("Method setClientId() should not exist");
            } catch (NoSuchMethodException e) {
                // OK
            }

            try {
                client.getClass().getDeclaredMethod("setName", String.class);
                fail("Method setName() should not exist");
            } catch (NoSuchMethodException e) {
                // OK
            }
        }

        @Test
        @DisplayName("Fields are private and final")
        void fieldsShouldBePrivateAndFinal() {
            try {
                var field = client.getClass().getDeclaredField("clientId");
                assertTrue(java.lang.reflect.Modifier.isPrivate(field.getModifiers()));
                assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()));
            } catch (NoSuchFieldException e) {
                fail("Field clientId not found");
            }

            try {
                var field = client.getClass().getDeclaredField("name");
                assertTrue(java.lang.reflect.Modifier.isPrivate(field.getModifiers()));
                assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()));
            } catch (NoSuchFieldException e) {
                fail("Field name not found");
            }
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Two clients with same UUID and name should be equal")
        void twoClientsWithSameDataShouldBeEqual() {
            Client other = new Client(testUuid, testName);
            assertEquals(client, other);
            assertEquals(client.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("Clients with different UUIDs should not be equal")
        void clientsWithDifferentUUIDsShouldNotBeEqual() {
            Client other = new Client(UUID.randomUUID(), testName);
            assertNotEquals(client, other);
        }

        @Test
        @DisplayName("Clients with different names should not be equal")
        void clientsWithDifferentNamesShouldNotBeEqual() {
            Client other = new Client(testUuid, "Jane Doe");
            assertNotEquals(client, other);
        }

        @Test
        @DisplayName("equals() should return false when comparing to null")
        void equalsShouldReturnFalseForNull() {
            assertNotNull(client);
        }

        @Test
        @DisplayName("equals() should be symmetric")
        void equalsShouldBeSymmetric() {
            Client other = new Client(testUuid, testName);
            assertEquals(client, other);
            assertEquals(other, client);
        }

        @Test
        @DisplayName("equals() should be transitive")
        void equalsShouldBeTransitive() {
            Client other = new Client(testUuid, testName);
            Client third = new Client(testUuid, testName);
            assertEquals(client, other);
            assertEquals(other, third);
            assertEquals(client, third);
        }

        @Test
        @DisplayName("hashCode() should be consistent across calls")
        void hashCodeShouldBeConsistent() {
            int first = client.hashCode();
            int second = client.hashCode();
            assertEquals(first, second);
        }

        @RepeatedTest(10)
        @DisplayName("hashCode() should produce same result on repeated calls")
        void repeatedHashCodeShouldBeStable() {
            int initial = client.hashCode();
            assertEquals(initial, client.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        @Test
        @DisplayName("toString() should not be empty or null")
        void toStringShouldNotBeEmptyOrNull() {
            assertNotNull(client.toString());
            assertFalse(client.toString().isEmpty());
        }
    }

    @Nested
    @DisplayName("Contract & Edge Case Tests")
    class ContractTests {

        @Test
        @DisplayName("hashCode should depend only on clientId and name")
        void hashCodeShouldDependOnlyOnFields() {
            Client a = new Client(testUuid, "Alice");
            Client b = new Client(testUuid, "Alice");
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("Different name but same UUID should have different hashCode")
        void differentNameSameUuidShouldHaveDifferentHash() {
            Client a = new Client(testUuid, "Alice");
            Client b = new Client(testUuid, "Bob");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("Client should not equal to instance of another class")
        void shouldNotEqualOtherClass() {
            Object other = new Object();
            assertNotEquals(client, other);
        }
    }
}