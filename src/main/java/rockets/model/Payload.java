package rockets.model;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;

public class Payload extends Entity {

    private String name;

    private String category;

    private int weight;

    private boolean returnable;

    public Payload() {
        name = null;
        category = null;
        weight = 0;

    }

    public Payload(String name, String category, int weight, boolean returnable) {
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.returnable = returnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notBlank(name, "Name cannot be null or empty");
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        notBlank(category, "Category cannot be null or empty");
        this.category = category;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws IllegalArgumentException{
        if (weight < 0)
            throw new IllegalArgumentException("Weight can not be negative");
        this.weight = weight;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload payload = (Payload) o;
        return returnable == payload.returnable &&
                name.equals(payload.name) &&
                category.equals(payload.category) &&
                weight == payload.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, weight, returnable);
    }

    @Override
    public String toString() {
        return "Payload{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", weight=" + weight +
                ", returnable=" + returnable +
                '}';
    }
}
