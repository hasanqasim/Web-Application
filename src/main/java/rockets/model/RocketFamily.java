package rockets.model;

import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.Validate.notBlank;

public class RocketFamily extends Entity {

    private String familyName;

    private Set<Rocket> rockets;

    public RocketFamily(String familyName) {
        this.familyName = familyName;
        this.rockets = new TreeSet<Rocket>();
    }

    public RocketFamily() {
        this.familyName = "Default";
        this.rockets = new TreeSet<Rocket>();
    }

    public RocketFamily(String familyName, Set<Rocket> rockets) {
        this.familyName = familyName;
        this.rockets = rockets;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) throws IllegalArgumentException{
        notBlank(familyName, "Family name cannot be null or empty");
        if(!familyName.matches("^[A-Za-z0-9]{4,40}$"))
            throw new IllegalArgumentException("Family name can only have characters and numbers");
        this.familyName = familyName;
    }

    public Set<Rocket> getRockets() {
        return rockets;
    }

    public void setRockets(Set<Rocket> rockets) {
        this.rockets = rockets;
    }

    public void addRocketToFamily(Rocket r) throws IllegalArgumentException{
        if(r == null)
            throw new IllegalArgumentException("Can not add null to rockets");
        if(this.rockets.contains(r))
            throw new IllegalArgumentException("Can not add duplicate rocket");
        this.rockets.add(r);
    }

}
