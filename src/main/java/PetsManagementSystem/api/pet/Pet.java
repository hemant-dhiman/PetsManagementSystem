package PetsManagementSystem.api.pet;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Introspected
@Getter
@Setter
@ToString
public class Pet {
    /**
     * Pet *
     * Name
     * Species
     * Breed
     * Color
     */
    String id;
    String o_id;
    @NotBlank
    //@Min(value = 5, message = "Name should be minimum of 5 character")
            //@Max(value = 15, message = "User Name should be maximum of 15 character")
            String name;
    @NotBlank
    String species;
    @NotBlank
    String breed;
    @NotBlank
    String color;

    public Pet(@NotBlank String name, @NotBlank String species, @NotBlank String breed, @NotBlank String color) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.color = color;
    }
}
