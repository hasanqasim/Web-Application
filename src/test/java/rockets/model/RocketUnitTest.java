package rockets.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class RocketUnitTest {
    private Rocket target;
    @BeforeEach
    public void setUp() {
        target = new Rocket("name", "country", new LaunchServiceProvider("manufacturer", 1980, "USA"));
    }

    @AfterEach
    public void tearDown() {
    }

    @DisplayName("should create rocket successfully when given right parameters to constructor")
    @Test
    public void shouldConstructRocketObject() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertNotNull(bfr);
    }

    @DisplayName("should throw exception when given null manufacturer to constructor")
    @Test
    public void shouldThrowExceptionWhenNoManufacturerGiven() {
        String name = "BFR";
        String country = "USA";
        assertThrows(NullPointerException.class, () -> new Rocket(name, country, null));
    }

    @DisplayName("should set rocket massToLEO value")
    @ValueSource(strings = {"10000", "15000"})
    public void shouldSetMassToLEOWhenGivenCorrectValue(String massToLEO) {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");

        Rocket bfr = new Rocket(name, country, manufacturer);

        bfr.setMassToLEO(massToLEO);
        assertEquals(massToLEO, bfr.getMassToLEO());
    }

    @DisplayName("should throw exception when set massToLEO to null")
    @Test
    public void shouldThrowExceptionWhenSetMassToLEOToNull() {
        String name = "BFR";
        String country = "USA";
        LaunchServiceProvider manufacturer = new LaunchServiceProvider("SpaceX", 2002, "USA");
        Rocket bfr = new Rocket(name, country, manufacturer);
        assertThrows(NullPointerException.class, () -> bfr.setMassToLEO(null));
    }

    //<----- Other Groups Test Cases ------->

    //country name can only be letters
    @DisplayName("should throw exception when pass an invalid country address to constructor")
    @ParameterizedTest
    @ValueSource(strings = {"count123", "afs@@"})
    public void shouldReturnFalseWhenSetInvalidCountry(String country) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket("name", country, new LaunchServiceProvider("manufacturer", 1980, "USA")));
        assertEquals("country name can only contain letters", exception.getMessage());
    }

    //empty country
    @DisplayName("should throw exception when pass null or empty country when creating a rocket")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenPassNullOrEmptyRocketCountry(String country){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket("name", country, new LaunchServiceProvider("manufacturer", 1980, "USA")));
        assertEquals("The validated character sequence is blank", exception.getMessage());
    }

    //null country
    @DisplayName("should throw exception when pass null country to the rocket")
    @Test
    public void shouldThrowExceptionWhenPassNullCountryToRocket(){
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new Rocket("name", null, new LaunchServiceProvider("manufacturer", 1980, "USA")));
        assertEquals("The validated character sequence is blank", exception.getMessage());
    }

    //ass2 null launchServiceProvider
    @DisplayName("should throw exception when pass null launchServiceProvider to the rocket")
    @Test
    public void shouldThrowExceptionWhenPassNullLaunchServiceProviderToRocket(){
        LaunchServiceProvider launchServiceProvider = null;
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new Rocket("name", "country", launchServiceProvider));
        assertEquals("The validated object is null", exception.getMessage());
    }

    //ass2 empty rocket name
    @DisplayName("should throw exception when pass empty name when creating a rocket")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenPassNullOrEmptyRocketName(String name){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Rocket(name, "USA", new LaunchServiceProvider("manufacturer", 1980, "USA")));
        assertEquals("The validated character sequence is blank", exception.getMessage());
    }

    //ass2 null rocket name
    @DisplayName("should throw exception when pass null name to the rocket")
    @Test
    public void shouldThrowExceptionWhenPassNullNameToRocket(){
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new Rocket(null, "country", new LaunchServiceProvider("manufacturer", 1980, "USA")));
        assertEquals("The validated character sequence is blank", exception.getMessage());
    }

    //pass valid country names
    @DisplayName("should return true when pass a valid name")
    @ParameterizedTest
    @ValueSource(strings = {"USA", "Australia", "China"})
    public void shouldReturnTrueWhenGetPassValidNames(String countryName){
        Rocket rocket = new Rocket("name", countryName, new LaunchServiceProvider("manufacturer", 1980, "USA"));
        assertTrue(rocket.getCountry().equals(countryName));
    }

    //pass valid rocket names
    @DisplayName("should return true when pass valid rocket names")
    @ParameterizedTest
    @ValueSource(strings = {"Delta IV Heavy", "Ariane 4"})
    public void shouldReturnTrueWhenGetCountryFunctionWorks(String name){
        Rocket rocket = new Rocket(name, "country", new LaunchServiceProvider("manufacturer", 1980, "USA"));
        assertTrue(rocket.getName().equals(name));
    }

    //pass valid manufacturer
    @DisplayName("should return true when pass valid Manufacturer")
    @Test
    public void shouldReturnTrueWhenPassValidManufacturer(){
        LaunchServiceProvider launchServiceProvider = new LaunchServiceProvider("manufacturer", 1980, "USA");
        assertTrue(target.getManufacturer().equals(launchServiceProvider));
    }

    @DisplayName("should return true when pass valid MassToGTO")
    @Test
    public void shouldReturnTrueWhenPassValidMassToGTO(){
        String string = "123";
        target.setMassToGTO(string);
        assertTrue(target.getMassToGTO().equals(string));
    }

    @DisplayName("should return true when pass valid MassToOther")
    @Test
    public void shouldReturnTrueWhenPassValidMassToOther(){
        String string = "123";
        target.setMassToOther(string);
        assertTrue(target.getMassToOther().equals(string));
    }

    @DisplayName("should throw exception when pass a empty massToLEO to setMassToLEO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToLEOToEmpty(String massToLEO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToLEO(massToLEO));
        assertEquals("massToLEO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty massToGTO to setMassToGTO function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToGTOToEmpty(String massToGTO) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToGTO(massToGTO));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setMassToGTO function")
    @Test
    public void shouldThrowExceptionWhenSetMassToGTOToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToGTO(null));
        assertEquals("massToGTO cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass a empty massToOther to setMassToOther function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetMassToOtherEmpty(String massToOther) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setMassToOther(massToOther));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setMassToOther function")
    @Test
    public void shouldThrowExceptionWhenSetMassToOtherToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setMassToOther(null));
        assertEquals("massToOther cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should return true when two rockets are the same")
    @Test
    public void shouldReturnTrueWhenTwoRocketsAreTheSame(){
        Rocket rocket = new Rocket(target.getName(), target.getCountry(), target.getManufacturer());
        assertTrue(target.equals(rocket));
    }

    //test the hash code function
    @DisplayName("should return true when two rockets have been hashed")
    @Test
    public void shouldReturnTrueWhenTwoRocketsHaveBeenHashed(){
        Rocket rocket= new Rocket("name", "country", new LaunchServiceProvider("manufacturer", 1980, "USA"));
        target.hashCode();
        rocket.hashCode();
        assertTrue(target.getName().equals(rocket.getName()) && target.getCountry().equals(rocket.getCountry()) &&
                target.getManufacturer().equals(rocket.getManufacturer()));
    }

}