package PetsManagementSystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationUserDetailsAdapter;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;
import javax.inject.Inject;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;

@ToString
@Controller("/pms")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AppController {

    @Inject
    private OwnerService ownerService;

    @Inject
    private PetService petService;

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Single<HttpResponse<Object>> addUser(@Valid @Body Owner ownerObj) {
        if(!ownerService.hasUser(ownerObj.user_name)){
            return ownerService.addOwner(ownerObj).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("User Already Exist!"));
    }

    @Post("/pet")
    //@Secured("OWNER")
    public Single<HttpResponse<Object>> addPets(@Valid @Body Pet petObj) {
        return petService.addPet(petObj).map(o -> {
            return HttpResponse.created(o);
        });
    }

    @Get("/pet")
    public Single<HttpResponse<Object[]>> allPet() {
        return Single.just(petService.pets()).map(o -> {
            return HttpResponse.ok(o);
        });
    }

    @Get("/pet/{id}")
    // multiple paramtr query paramtr!
    // single paramtr path variable!
    public Single<Optional<Object>> getPet(@QueryValue String id) {
        return Single.defer(() -> {
            return Single.just(Optional.ofNullable(petService.pets(id)));
        });
    }

    @Get("/details")
    public Single<Object> getDetails(Authentication authentication){
        if(ownerService.getOwner(authentication.getName()) != null)
            return Single.just(ownerService.getOwner(authentication.getName()));
        else
            return Single.just(HttpResponse.unauthorized().body("Unauthorized"));
    }

    @Put("/details")
    public Single<Object> updateDetails(@Valid @Body Owner ownerObj){

        return Single.just("");
    }

}

/*
    // pet entry
    @Inject
    private PetService ownerService;

    @Post
    public Single<HttpResponse<Object>> add(@Body Pet obj){
        System.out.println(obj);
        return ownerService.addPet(obj).map(HttpResponse::created);
    }*/

/*HashMap<String, Object> data = new HashMap<>(authentication.getAttributes());
    JSONObject ja = new JSONObject();
        ja.merge(data.get(authentication.getName()));
                return Single.just(ja.get("id"));*/