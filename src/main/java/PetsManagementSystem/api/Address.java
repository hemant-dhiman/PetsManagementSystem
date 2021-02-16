package PetsManagementSystem.api;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public Address(String line1, String line2, String district, String state, String pin_code) {
        this.line1 = line1;
        this.line2 = line2;
        this.district = district;
        this.state = state;
        this.pin_code = pin_code;
    }


}
