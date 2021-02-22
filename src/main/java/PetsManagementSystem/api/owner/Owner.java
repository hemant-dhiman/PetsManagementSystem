package PetsManagementSystem.api.owner;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Introspected
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Owner {
    /**
     * Owner's attributes *
     * Full name
     * Username
     * Email
     * Password
     * Address
     */
    String id;
    @NotBlank
    @Min(value = 10, message = "Name should be minimum of 10 character")
    @Max(value = 30, message = "User Name should be maximum of 6 character")
    String full_name;
    @NotBlank
    @Min(value = 10, message = "User Name should be minimum of 6 character")
    @Max(value = 15, message = "User Name should be Maximum of 15 character")
    String user_name;
    @NotBlank
    @Email(message = "Please Enter a valid Email ID")
    String email;
    @NotBlank
    @Size(min = 5, max = 22)
    String password;
    @NotBlank
    Address address;
}
