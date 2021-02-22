package PetsManagementSystem.api.owner;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ToString
@Controller("/pms/user")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class OwnerController {
    @Inject
    private OwnerService ownerService;

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Single<Owner> addUser(@NotNull @Valid @RequestBean Owner ownerObj) {
        if (!ownerService.hasUser(ownerObj.user_name)) {
            return Single.just(ownerService.addOwner(ownerObj));
        }
        return Single.error(new HttpStatusException(HttpStatus.BAD_REQUEST, "User Already Exist"));
    }

    @Get("/details")
    public Single<Owner> getDetails(@NotNull Authentication authentication) {
        if (ownerService.getOwner(authentication.getName()) != null)
            return Single.just(ownerService.getOwner(authentication.getName()));
        else
            return Single.error(new HttpStatusException(HttpStatus.BAD_REQUEST, "User Doesn't Exist"));
    }


    @Patch("/details")
    public Single<Owner> updateDetails(@NotNull @Valid @RequestBean OwnerManage ownerManage, Authentication authentication) {
        return Single.just(ownerService.updateOwner(authentication, ownerManage));
    }
}
