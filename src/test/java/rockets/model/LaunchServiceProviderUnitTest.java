package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.util.PreconditionViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchServiceProviderUnitTest {

    private LaunchServiceProvider target;

    @BeforeEach
    public void setUp()
    {
        target = new LaunchServiceProvider("ABC",1990,"Melbourne");
    }

    @DisplayName("should return true when LaunchServiceProviders are same")
    @Test
    public void shouldReturnTrueWhenLaunchServiceProvidersAreSame() {

        LaunchServiceProvider another = new LaunchServiceProvider("ABC",1990,"Melbourne");
        assertTrue(target.equals(another));
    }

    @DisplayName("should return false when LaunchServiceProviders are different")
    @Test
    public void shouldReturnFalseWhenLaunchServiceProvidersAreDifferent(){
        LaunchServiceProvider another = new LaunchServiceProvider("DEF",1900,"Sydeny");
        assertFalse(target.equals(another));
    }

    @DisplayName("should throw exception when pass null to setYearFounded function")
    @Test
    public void shouldThrowExceptionWhenSetYearFoundedToNull() {
        PreconditionViolationException exception = assertThrows(PreconditionViolationException.class, () -> target.setYearFounded(null));
        assertEquals("year founded cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when year founded less than 1900")
    @ParameterizedTest
    @ValueSource(ints = 1899)
    public void shouldThrowExceptionWhenYearFoundedLessThan1900(int year) {
        PreconditionViolationException exception = assertThrows(PreconditionViolationException.class, () -> target.setYearFounded(year));
        assertEquals("year founded should greater than 1900", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty name to setName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetNameToEmpty(String name) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setName(name));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setName function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setName(null));
        assertEquals("name cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty country to setCountry function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String country) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setCountry(country));
        assertEquals("country cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setCountry function")
    @Test
    public void shouldThrowExceptionWhenSetCountryToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setCountry(null));
        assertEquals("country cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass digital to setCountry function")
    @ParameterizedTest
    @ValueSource(strings = "Melbourne1")
    public void shouldThrowExceptionWhenSetCountryTodigital(String country)
    {
        PreconditionViolationException exception = assertThrows(PreconditionViolationException.class, ()-> target.setCountry(country));
        assertEquals("country cannot have number or special characters", exception.getMessage());

    }

    @DisplayName("should throw exception when pass a empty headquarter to setHeadquarter function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetHeadquartersToEmpty(String headquarters) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setHeadquarters(headquarters));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setHeadquarter function")
    @Test
    public void shouldThrowExceptionWhenSetHeadquartersToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setHeadquarters(null));
        assertEquals("headquarters cannot be null or empty", exception.getMessage());
    }



}