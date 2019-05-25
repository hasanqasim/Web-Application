package rockets.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class PayLoadTest {
    private Payload target;

    @BeforeEach
    public void setUp(){
        target = new Payload();
    }

    @DisplayName("Should throw exception when pass null to name")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetNameToEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setName(name));
        assertEquals("Name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("Should throw exception when pass null to category")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetCategoryToEmpty(String category) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setCategory(category));
        assertEquals("Category cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when pass negative integer to weight")
    public void shouldThrowExceptionWhenPassNegativeIntegerToWeight() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setWeight(-1));
        assertEquals("Weight can not be negative", exception.getMessage());
    }
}
