package rockets.model;

import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.junit.platform.commons.util.Preconditions.condition;
import static org.junit.platform.commons.util.Preconditions.notNull;

public class LaunchServiceProvider extends Entity {
    private String name;

    private int yearFounded;

    private String country;

    private String headquarters;

    private Set<Rocket> rockets;

    public LaunchServiceProvider(String name, int yearFounded, String country) {
        this.name = name;
        this.yearFounded = yearFounded;
        this.country = country;
        rockets = Sets.newLinkedHashSet();
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setHeadquarters(String headquarters) {
        notBlank(headquarters, "headquarters cannot be null or empty");
        this.headquarters = headquarters;
    }

    public void setRockets(Set<Rocket> rockets) {
        this.rockets = rockets;
    }

    public void setName(String name) {
        notBlank(name, "name cannot be null or empty");
        this.name = name;
    }

    public void setCountry(String country) {
        notBlank(country, "country cannot be null or empty");
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(country);
        condition(m.matches(), "country cannot have number or special characters");
        this.country = country;
    }

    public void setYearFounded(Integer yearFounded) {
        notNull(yearFounded, "year founded cannot be null or empty");
        condition(yearFounded > 1900, "year founded should greater than 1900");
        this.yearFounded = yearFounded;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaunchServiceProvider that = (LaunchServiceProvider) o;
        return yearFounded == that.yearFounded &&
                Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, yearFounded, country);
    }
}
