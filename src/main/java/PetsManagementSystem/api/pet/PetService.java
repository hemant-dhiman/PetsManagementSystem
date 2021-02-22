package PetsManagementSystem.api.pet;

import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@ToString
public class PetService {

    private ConcurrentHashMap<String, Pet> pets = new ConcurrentHashMap<>();

    public Single<Pet> addPet(String o_Id, Pet petObj) {
        String generateID = String.valueOf(pets.size() + 1);
        petObj.setId(generateID);
        petObj.setO_id(o_Id);
        pets.put(generateID, petObj);
        //System.out.println("Pet added: " + pets);
        return Single.just(petObj);

    }

    public List<Pet> pets(String o_Id) {
        ConcurrentHashMap<String, Pet> ownersPet = new ConcurrentHashMap<>();
        for (Map.Entry<String, Pet> map : pets.entrySet()) {
            String petId = map.getKey();
            Pet pet = map.getValue();
            if (pet.o_id.equals(o_Id)) {
                ownersPet.put(petId, pet);
            }
        }
        return new ArrayList<>(ownersPet.values());
    }


    public Pet getPet(String petId, String oId) {
        HashMap<String, Pet> obj = new HashMap<>();
        if (pets.get(petId) != null) {
            for (Map.Entry<String, Pet> map : pets.entrySet()) {
                String id = map.getKey();
                Pet pet = map.getValue();
                if (pet.id.equals(petId) && pet.o_id.equals(oId)) {
                    obj.put(id, pet);
                }
            }
            return obj.get(petId);
        }
        return null;
    }

    public Pet updatePet(String oId, String pet_id, Pet petObj) {
        if (getPet(pet_id, oId) != null) {
            petObj.setId(pet_id);
            petObj.setO_id(oId);
            pets.put(pet_id, petObj);
            //System.out.println("pet updated: -----> " + pets + "\n");
            return petObj;
        }
        return null;
    }

    public Pet deletePet(String oId, String pet_id) {
        if (getPet(pet_id, oId) != null) {
            return pets.remove(pet_id);
        }
        return null;
    }

}
