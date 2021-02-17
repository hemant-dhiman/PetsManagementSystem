package PetsManagementSystem.api;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
@ToString
@Getter
@Setter
public class Address {
    /**
     * Address *
     * Line 1
     * Line 2
     * District
     * State
     * Pin_code
     */
    String line1,line2,district,state, pin_code;

    public Address(@NotBlank String line1, @NotBlank String line2, @NotBlank String district, @NotBlank String state, @NotBlank String pin_code) {
        this.line1 = line1;
        this.line2 = line2;
        this.district = district;
        this.state = state;
        this.pin_code = pin_code;
    }


}
