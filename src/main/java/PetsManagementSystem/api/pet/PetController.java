package PetsManagementSystem.api.pet;

import PetsManagementSystem.api.utility.UserUtility;
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
@Controller("/pms/pet")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PetController {
    @Inject
    private PetService petService;


    @Post("/")
    //@Secured("OWNER")
    public Single<HttpResponse<Object>> addPets(@Valid @Body Pet petObj, Authentication authentication) {
        String o_Id = UserUtility.currentUserId(authentication);

        return petService.addPet(o_Id, petObj).map(o -> {
            return HttpResponse.created(o);
        });
    }

    @Get("/")
    public Single<HttpResponse<Object[]>> allPet(Authentication authentication) {
        String o_Id = UserUtility.currentUserId(authentication);

        return Single.just(petService.pets(o_Id)).map(o -> {
            return HttpResponse.ok(o);
        });
    }

    @Get("/{petId}")
    public Single<Object> getPet(@QueryValue String petId, Authentication authentication) {
        String ownerId = UserUtility.currentUserId(authentication);
        //get Pet from Database
        Pet pet;
        try {
            pet = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (Exception e) {
            return Single.just(HttpResponse.notFound());
        }
        try {
            return Single.just(pet);
        } catch (Exception e) {
            return Single.just(HttpResponse.notFound());
        }
    }

    @Put("/{petId}")
    public Single<Object> updatePet(@Body Pet petObj, @QueryValue String petId, Authentication authentication) {
        if (petObj.name.equals("") || petObj.species.equals("") || petObj.breed.equals("") || petObj.color.equals("")) {
            return Single.just(HttpResponse.badRequest());
        }
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);

        // Store the Old Pet Data
        Pet oldPetData;
        try {
            oldPetData = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (NullPointerException e) {
            return Single.just(HttpResponse.notFound());
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
            return Single.just(HttpResponse.notFound());
        }
        //update the other four field of pet
        updatedPetData.name = petObj.name;
        updatedPetData.species = petObj.species;
        updatedPetData.breed = petObj.breed;
        updatedPetData.color = petObj.color;
        //update the pet in database
        if (petService.hasPet(oldPetData.id)) {
            return petService.updatePet(oldPetData.id, updatedPetData).map(HttpResponse::ok);
        }
        return Single.just(HttpResponse.badRequest("Error!!"));
    }

    @Delete("/{petId}")
    public Single<Object> deletePet(@QueryValue String petId, Authentication authentication) {
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);
        Pet pet;
        try {
            pet = (Pet) petService.getPet(petId, ownerId).get(petId);
        } catch (Exception e) {
            return Single.just(HttpResponse.notFound());
        }
        if (pet != null && pet.o_id.equals(ownerId)) {
            return Single.just(petService.popPet(petId));
        }
        return Single.just(HttpResponse.notFound());
    }
}
