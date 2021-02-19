package PetsManagementSystem.api.owner;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;

@ToString
@Controller("/pms/user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OwnerController {
    @Inject
    private OwnerService ownerService;

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Single<HttpResponse<Object>> addUser(@Valid @Body Owner ownerObj) {
        if (!ownerService.hasUser(ownerObj.user_name)) {
            return ownerService.addOwner(ownerObj).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("User Already Exist!"));
    }

    @Get("/details")
    public Single<Object> getDetails(Authentication authentication) {
        if (ownerService.getOwner(authentication.getName()) != null)
            return Single.just(ownerService.getOwner(authentication.getName()));
        else
            return Single.just(HttpResponse.unauthorized());
    }

    @Put("/details")
    public Single<Object> updateDetails(@Body Owner ownerObj, Authentication auth) {
        if (ownerObj.user_name.equals("") || ownerObj.password.equals("")) {
            return Single.just(HttpResponse.badRequest().body("User_name or password must not blank!"));
        }
        //Storing old Owner data which needs to be updated
        Owner oldOwnerData;
        try {
            oldOwnerData = (Owner) ownerService.getOwner(auth.getName()).get(auth.getName());
        } catch (NullPointerException e) {
            return Single.just(HttpResponse.unauthorized());
        }
        //Storing updated Owner data
        HashMap<String, Owner> updates = new HashMap<>();
        updates.put(ownerObj.user_name, ownerObj);
        Owner updatedOwnerData = (Owner) updates.get(ownerObj.user_name);
        //password and user_name should be updated
        updatedOwnerData.password = ownerObj.password;
        updatedOwnerData.user_name = ownerObj.user_name;
        //rest fields should be same
        updatedOwnerData.id = oldOwnerData.id;
        updatedOwnerData.address = oldOwnerData.address;
        updatedOwnerData.email = oldOwnerData.email;
        updatedOwnerData.full_name = oldOwnerData.full_name;
        //updating the owner in database
        if (ownerService.hasUser(oldOwnerData.user_name)) {
            return ownerService.updateOwner(auth, oldOwnerData.user_name, updatedOwnerData).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("Error!!"));
    }
}
