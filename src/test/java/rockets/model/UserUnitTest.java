package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {
    private User target;

    //set up the target
    @BeforeEach
    public void setUp() {
        target = new User();
    }


    //tests about the email
    //cannot be empty
    @DisplayName("should throw exception when pass an empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    //cannot be null value
    @DisplayName("should throw exception when pass null to setEmail function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    //the email address must be valid
    @DisplayName("should throw exception when pass an invalid email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"asldkfj@@@&**("})
    public void shouldReturnFalseWhenSetInvalidEmailAddress(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("Email is not a valid email", exception.getMessage());
    }

    //the password cannot be null or empty
    @DisplayName("should throw exceptions when pass a null password to setPassword function")
    @Test
    public void shouldThrowExceptionWhenSetPasswordToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setPassword(null));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty password to setPassword function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetPasswordToEmpty(String password) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPassword(password));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    //password must be valid
    @DisplayName("should throw exception when pass an invalid password")
    @ParameterizedTest
    @ValueSource(strings = {"password", "Password", "Passwrod123", "Password@@", "Pass12&"})
    public void shouldThrowExceptionWhenPassInvalidPassword(String password){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPassword(password));
        assertEquals("Password is not a valid password", exception.getMessage());
    }

    //two users cannot have same email
    @DisplayName("should return true when two users have the same email")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameEmail() {
        String email = "abc@example.com";
        target.setEmail(email);
        User anotherUser = new User();
        anotherUser.setEmail(email);
        assertTrue(target.equals(anotherUser));
    }

    //two users can have different email
    @DisplayName("should return false when two users have different emails")
    @Test
    public void shouldReturnFalseWhenUsersHaveDifferentEmails() {
        target.setEmail("abc@example.com");
        User anotherUser = new User();
        anotherUser.setEmail("def@example.com");
        assertFalse(target.equals(anotherUser));
    }

    //firstName cannot be null or empty
    @DisplayName("should throw exception when pass a null first name to setFirstName function")
    @Test
    public void shouldThrowExceptionWhenSetFirstNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setFirstName(null));
        assertEquals("firstName cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty firstName to setFirstName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetFistNameToEmpty(String firstName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFirstName(firstName));
        assertEquals("firstName cannot be null or empty", exception.getMessage());
    }

    //firstName must be valid
    @DisplayName("should throw exception when pass an invalid firstName to setFirstName function")
    @ParameterizedTest
    @ValueSource(strings = {"name123", "name@@!"})
    public void shouldThrowExceptionWhenPassInvalidFirstName(String firstName){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setFirstName(firstName));
        assertEquals("firstName can only contain letters", exception.getMessage());
    }


    //lastName cannot be null or empty
    @DisplayName("should throw exceptions when pass a null lastName to setLastName function")
    @Test
    public void shouldThrowExceptionWhenSetLastNameToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> target.setLastName(null));
        assertEquals("lastName cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass an empty lastName to setLastName function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetLastNameToEmpty(String lastName) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastName));
        assertEquals("lastName cannot be null or empty", exception.getMessage());
    }

    //lastName must be valid
    @DisplayName("should throw exception when pass an invalid lastName to setLastName function")
    @ParameterizedTest
    @ValueSource(strings = {"name123", "name@@!"})
    public void shouldThrowExceptionWhenPassInvalidLastName(String lastName){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastName));
        assertEquals("lastName can only contain letters", exception.getMessage());
    }

    //test the isPasswordMatch function
    @DisplayName("should return true when the password matches")
    @Test
    public void shouldReturnTrueWhenPasswordsMatch(){
        target.setPassword("Password!123");
        assertTrue(target.isPasswordMatch("Password!123"));
    }

    @DisplayName("should return true when two users are the same")
    @Test
    public void shouldReturnTrueWhenTwoUsersAreTheSame(){
        assertTrue(target.equals(target));
    }

    //test hash code method
    @DisplayName("should return true when hashCode method works")
    @Test
    public void shouldReturnTrueWhenHashCodeMethodWorks(){
        User user = new User();
        String email = "abc@cde.def";
        user.setEmail(email);
        target.setEmail(email);
        assertTrue(!(target.getEmail().equals(target.hashCode())) && target.hashCode() == user.hashCode());
    }

    //pass valid firstName
    @DisplayName("should return true when pass valid firstName")
    @ParameterizedTest
    @ValueSource(strings = {"firstName"})
    public void shouldReturnTrueWhenSetFirstNameAndGetFirstNameWork(String firstName){
        target.setFirstName(firstName);
        assertTrue(firstName.equals(target.getFirstName()));
    }

    //pass valid lastName
    @DisplayName("should return true when pass valid lastName")
    @ParameterizedTest
    @ValueSource(strings = {"lastName"})
    public void shouldReturnTrueWhenSetLastNameAndGetLastNameWork(String lastName){
        target.setLastName(lastName);
        assertTrue(lastName.equals(target.getLastName()));
    }

    //pass valid email address
    @DisplayName("should return true when pass valid email address")
    @ParameterizedTest
    @ValueSource(strings = {"user@domail.com", "user.100@domain.com.au"})
    public void shouldReturnTrueWhenSetEmailAndGetEmailWork(String email){
        target.setEmail(email);
        assertTrue(email.equals(target.getEmail()));
    }

    //pass valid password
    @DisplayName("should return true when pass valid password")
    @ParameterizedTest
    @ValueSource(strings = {"Password@1"})
    public void shouldReturnTrueWhenPassValidPassword(String password){
        target.setPassword(password);
        assertTrue(password.equals(target.getPassword()));
    }
}