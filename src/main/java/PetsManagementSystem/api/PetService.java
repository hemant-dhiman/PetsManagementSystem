package PetsManagementSystem.api;

import io.micronaut.security.authentication.Authentication;
import io.reactivex.Single;
import lombok.ToString;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@ToString
public class PetService {

    private ConcurrentHashMap<String, Pet> pets = new ConcurrentHashMap<>();

    public Single<Object> addPet(String o_Id, Pet petObj) {
        String generateID = String.valueOf(pets.size() + 1);
        petObj.setId(generateID);
        petObj.setO_id(o_Id);
        pets.put(generateID, petObj);
        System.out.println("Pet added: " + pets);
        return Single.just(petObj);

    }

    public Object[] pets(String o_Id) {
        ConcurrentHashMap<String, Pet> ownersPet = new ConcurrentHashMap<>();
        for (Map.Entry<String, Pet> map : pets.entrySet()) {
            String petId = map.getKey();
            Pet pet = map.getValue();
            if (pet.o_id.equals(o_Id)) {
                //System.out.println(pet.o_id);
                ownersPet.put(petId, pet);
            }

        }
        return Collections.list(ownersPet.elements()).toArray();
    }

    public Object pet(String id) {
        return pets.get(id);
    }
}
