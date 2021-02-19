/*package PetsManagementSystem.api;

import PetsManagementSystem.api.owner.Owner;
import PetsManagementSystem.api.owner.OwnerService;
import PetsManagementSystem.api.pet.Pet;
import PetsManagementSystem.api.pet.PetService;
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
        String o_Id = UserUtility.currentUserId(authentication);

        return petService.addPet(o_Id, petObj).map(o -> {
            return HttpResponse.created(o);
        });
    }

    @Get("/pet")
    public Single<HttpResponse<Object[]>> allPet(Authentication authentication) {
        String o_Id = UserUtility.currentUserId(authentication);

        return Single.just(petService.pets(o_Id)).map(o -> {
            return HttpResponse.ok(o);
        });
    }

    @Get("/pet/{petId}")
    public Single<Object> getPet(@QueryValue String petId, Authentication authentication) {
        String ownerId = UserUtility.currentUserId(authentication);
        //get Pet from Database
        Pet pet;
        try {
            pet = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (Exception e) {
            return Single.just(HttpResponse.unauthorized().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
        }
        try {
            return Single.just(pet);
        } catch (Exception e) {
            return Single.just(HttpResponse.badRequest().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
        }
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


    @Put("/pet/{petId}")
    public Single<Object> updatePet(@Body Pet petObj, @QueryValue String petId, Authentication authentication) {
        if (petObj.name.equals("") || petObj.species.equals("") || petObj.breed.equals("") || petObj.color.equals("")) {
            return Single.just(HttpResponse.badRequest().body("Four fields require to update the pet: [name, species, breed, color]"));
        }
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);

        // Store the Old Pet Data
        Pet oldPetData;
        try {
            oldPetData = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (NullPointerException e) {
            return Single.just(HttpResponse.badRequest().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
        }
        // Store updated pet data
        HashMap<String, Pet> updates = new HashMap<>();
        updates.put(petObj.id, petObj);
        Pet updatedPetData = (Pet) updates.get(petObj.id);
        // id and owner_id must be same
        try {
            updatedPetData.id = oldPetData.id;
            updatedPetData.o_id = oldPetData.o_id;
        } catch (Exception e) {
            return Single.just(HttpResponse.unauthorized().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
        }
        //update the other four field of pet
        updatedPetData.name = petObj.name;
        updatedPetData.species = petObj.species;
        updatedPetData.breed = petObj.breed;
        updatedPetData.color = petObj.color;
        //update the pet in database
        if (petService.hasPet(oldPetData.id)) {
            return petService.updatePet(oldPetData.id, updatedPetData).map(HttpResponse::created);
        }
        return Single.just(HttpResponse.badRequest("Error!!"));
    }

    @Delete("/pet/{petId}")
    public Single<Object> deletePet(@QueryValue String petId, Authentication authentication) {
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);
        Pet pet;
        try {
            pet = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (Exception e) {
            return Single.just(HttpResponse.unauthorized().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
        }
        if (pet != null && pet.o_id.equals(ownerId)) {
            return Single.just(petService.popPet(petId));
        }
        return Single.just(HttpResponse.unauthorized().body("pet id: " + petId + " does not exist for user id " + ownerId + " or username " + authentication.getName()));
    }

}
*/
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
/*.body("Login First using
  http://localhost:8080/login")*/
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
/*
@Get("/pet/{id}")
    public Single<Optional<Object>> getPet(@QueryValue String id) {
        return Single.defer(() -> {
            return Single.just(Optional.ofNullable(petService.pet(id)));
        });
    }
 */