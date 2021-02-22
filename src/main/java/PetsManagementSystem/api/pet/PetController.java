package PetsManagementSystem.api.pet;

import PetsManagementSystem.api.utility.UserUtility;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@ToString
@Controller("/pms/pet")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class PetController {
    @Inject
    private PetService petService;


    @Post("/")
    //@Secured("ROLE_OWNER")
    public Single<Pet> addPets(@Valid @NotNull @RequestBean Pet petObj, @NotNull Authentication authentication) {
        String o_Id = UserUtility.currentUserId(authentication);
        return petService.addPet(o_Id, petObj);
    }

    @Get("/")
    public Single<List<Pet>> allPet(@NotNull Authentication authentication) {
        String o_Id = UserUtility.currentUserId(authentication);
        return Single.just(petService.pets(o_Id));
    }

    @Get("/{petId}")
    public Single<Optional<Pet>> getPet(@PathVariable String petId, Authentication authentication) {
        String ownerId = UserUtility.currentUserId(authentication);
        return Single.just(Optional.ofNullable(petService.getPet(petId, ownerId)));
    }

    @Patch("/{petId}")
    public Single<Optional<Pet>> updatePet(@NotNull @Body Pet petObj, @PathVariable String petId, Authentication authentication) {
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);
        return Single.just(Optional.ofNullable(petService.updatePet(ownerId, petId, petObj)));

    }

    @Delete("/{petId}")
    public Single<Optional<Pet>> deletePet(@PathVariable String petId, Authentication authentication) {
        // To get the current Logged user id
        String ownerId = UserUtility.currentUserId(authentication);
        return Single.just(Optional.ofNullable(petService.deletePet(ownerId, petId)));
    }
}
