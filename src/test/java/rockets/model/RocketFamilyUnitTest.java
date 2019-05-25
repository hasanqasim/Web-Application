package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class RocketFamilyUnitTest {
    private RocketFamily target;

    @BeforeEach
    public void setUp(){
        target = new RocketFamily();
    }

    @DisplayName("Should throw exception when pass null to family name")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetNameToEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFamilyName(name));
        assertEquals("Family name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("Should throw exception when add null to rockets")
    @Test
    public void shouldThrowExceptionWhenAddNullToRockets() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.addRocketToFamily(null));
        assertEquals("Can not add null to rockets", exception.getMessage());
    }

    @DisplayName("Should throw exception when add duplicate rocket")
    @Test
    public void shouldThrowExceptionWhenAddDuplicateRockets() {
        Rocket r = new Rocket("rocket1","China", new LaunchServiceProvider("manufacturer", 1980, "USA"));
        try {
            target.addRocketToFamily(r);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Exception exception = assertThrows(IllegalArgumentException.class, () -> target.addRocketToFamily(r));
        assertEquals("Can not add duplicate rocket", exception.getMessage());
    }

    @DisplayName("Should throw exception when pass illegal family name")
    @Test
    public void shouldThrowExceptionWhenPassIllegalFamilyName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFamilyName("/@ad"));
        assertEquals("Family name can only have characters and numbers", exception.getMessage());
    }
}
