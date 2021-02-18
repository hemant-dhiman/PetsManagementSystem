package PetsManagementSystem.api;

import com.nimbusds.jose.shaded.json.JSONObject;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;
import javax.inject.Inject;
import javax.validation.Valid;
import java.util.*;

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
        if (!ownerService.hasUser(ownerObj.user_name)) {
            return ownerService.addOwner(ownerObj).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("User Already Exist!"));
    }

    @Post("/pet")
    //@Secured("OWNER")
    public Single<HttpResponse<Object>> addPets(@Valid @Body Pet petObj, Authentication authentication) {

        HashMap<String, Object> data = new HashMap<>(authentication.getAttributes());
        JSONObject ja = new JSONObject();
        ja.merge(data.get(authentication.getName()));
        String o_Id = String.valueOf(ja.get("id"));

        return petService.addPet(o_Id, petObj).map(o -> {
            return HttpResponse.created(o);
        });
    }

    @Get("/pet")
    public Single<HttpResponse<Object[]>> allPet(Authentication authentication) {
        HashMap<String, Object> data = new HashMap<>(authentication.getAttributes());
        JSONObject ja = new JSONObject();
        ja.merge(data.get(authentication.getName()));
        String o_Id = String.valueOf(ja.get("id"));

        return Single.just(petService.pets(o_Id)).map(o -> {
            return HttpResponse.ok(o);
        });
    }

    @Get("/pet/{id}")
    // multiple paramtr query paramtr!
    // single paramtr path variable!
    public Single<Optional<Object>> getPet(@QueryValue String id) {
        return Single.defer(() -> {
            return Single.just(Optional.ofNullable(petService.pet(id)));
        });
    }

    @Get("/details")
    public Single<Object> getDetails(Authentication authentication) {
        if (ownerService.getOwner(authentication.getName()) != null)
            return Single.just(ownerService.getOwner(authentication.getName()));
        else
            return Single.just(HttpResponse.unauthorized().body("Unauthorized"));
    }

    @Put("/details")
    public Single<Object> updateDetails(@Body Owner ownerObj, Authentication auth) {
        System.out.println();
        Owner temp;
        try {
            temp = (Owner) ownerService.getOwner(auth.getName()).get(auth.getName());
        } catch (NullPointerException e) {
            return Single.just(HttpResponse.unauthorized()/*.body("Login First using  http://localhost:8080/login")*/);
        }

        HashMap<String, Owner> updated = new HashMap<>();
        updated.put(ownerObj.user_name, ownerObj);
        Owner temp1 = (Owner) updated.get(ownerObj.user_name);

        temp1.id = temp.id;
        temp1.password = ownerObj.password;
        temp1.user_name = ownerObj.user_name;
        temp1.address = temp.address;
        temp1.email = temp.email;
        temp1.full_name = temp.full_name;

        if (ownerService.hasUser(temp.user_name)) {
            return ownerService.updateOwner(auth, temp.user_name, temp1).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("Error!!"));
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


/*
Owner temp;
        if (!String.valueOf(ownerService.getOwner(auth.getName()).get(auth.getName())).equals("null")) {
            temp = (Owner) ownerService.getOwner(auth.getName()).get(auth.getName());
        } else {
            return Single.just(HttpResponse.badRequest("Error!!"));
        }


        //HashMap<String, Object> old = new HashMap<>();
        //old.put(String.valueOf(ownerService.getOwner(auth.getName()).get("id")), ownerService.getOwner(auth.getName()));

        HashMap<String, Owner> updated = new HashMap<>();
        updated.put(ownerObj.user_name, ownerObj);
        Owner temp1 = (Owner) updated.get(ownerObj.user_name);


        temp1.id = temp.id;
        temp1.password = ownerObj.password;
        temp1.user_name = ownerObj.user_name;
        //if(temp.address != null && temp.email != null && temp.full_name != null) {
        temp1.address = temp.address;
        temp1.email = temp.email;
        temp1.full_name = temp.full_name;
        //}
        //System.out.println(temp);
        //System.out.println("UPDATED DATA: " + temp1 + "\n\n");
        //return Single.just(updated);
        */