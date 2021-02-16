package PetsManagementSystem.api;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
@Getter
@Setter
@ToString
public class Pet{
    /**
     * Pet *
     * Name
     * Species
     * Breed
     * Color
     */
    @NotBlank
    String id;
    @NotBlank
    String name;
    @NotBlank
    String species;
    @NotBlank
    String breed;
    @NotBlank
    String color;

    public Pet(@NotNull String id, @NotNull String name, @NotNull String species, @NotNull String breed, @NotNull String color) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.color = color;
    }

}
