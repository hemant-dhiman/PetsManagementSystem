package PetsManagementSystem.api;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.*;

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
    String id;
    @NotBlank
    @Min(value = 10,message = "Name should be minimum of 10 character")
    @Max(30)
    String full_name;
    @NotBlank
    String user_name;
    @NotBlank
    @Email(message = "Please Enter a valid Email ID")
    String email;
    @NotBlank @Size(min = 5)
    String password;
    @NotBlank
    Address address;

    public Owner(String full_name,  String user_name,  String email,  String password,  Address address) {
        this.full_name = full_name;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
