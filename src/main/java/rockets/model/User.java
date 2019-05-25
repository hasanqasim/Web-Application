package rockets.model;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;

@NodeEntity
public class User extends Entity {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        notBlank(firstName, "firstName cannot be null or empty");
        char[] chars = firstName.trim().toCharArray();
        for(char c: chars){
            if (!Character.isLetter(c)){
                throw new IllegalArgumentException("firstName can only contain letters");
            }
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        notBlank(lastName, "lastName cannot be null or empty");
        char[] chars = lastName.trim().toCharArray();
        for(char c: chars){
            if (!Character.isLetter(c)){
                throw new IllegalArgumentException("lastName can only contain letters");
            }
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    /*the email is a valid email that start with a group of letters or numbers, follow by a dot and an optional
    a group of letters or numbers, and must follow by an @
    the domain part could have 1 to three part with a dot in the middle and the final domain name must greater than 2 letters
     */
    public void setEmail(String email) {
        notBlank(email, "email cannot be null or empty");
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(email);
        if (m.matches())
            this.email = email;
        else
            throw new IllegalArgumentException("Email is not a valid email");
    }

    public String getPassword() {
        return password;
    }

    /*
    password should have at least one digit, one lower case letter, one upper case latter and a special character
    there are not whitespace allowed in the entire password, the password must have at least 8 characters
     */
    public void setPassword(String password) {
        notBlank(password, "password cannot be null or empty");
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(password);
        if (m.matches())
            this.password = password;
        else
            throw new IllegalArgumentException("Password is not a valid password");
    }

    // match the given password against user's password and return the result
    public boolean isPasswordMatch(String password) {
        return this.password.equals(password.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}