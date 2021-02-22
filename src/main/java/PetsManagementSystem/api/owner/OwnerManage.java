package PetsManagementSystem.api.owner;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Introspected
@ToString
@Getter
@Setter
@AllArgsConstructor
public class OwnerManage {
    @NotBlank
    @Size(min = 5, max = 15)
    String username;

    @NotBlank
    @Size(min = 5, max = 20)
    String password;
}
