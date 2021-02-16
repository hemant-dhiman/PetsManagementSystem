package PetsManagementSystem.api;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@Introspected
@Getter
@Setter
@ToString
public class Owner {
    /**
     * Owner’s attributes *
     * Full name
     * Username
     * Email
     * Password
     * Address
     */
    @NotBlank
    String id;
    @NotBlank
    String full_name;
    @NotBlank
    String user_name;
    @NotBlank
    String email;
    @NotBlank
    String password;
    @NotBlank
    Address address;

    public Owner(@NotBlank String id, @NotBlank String full_name, @NotBlank String user_name, @NotBlank String email, @NotBlank String password, @NotBlank Address address) {
        this.id = id;
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

}
