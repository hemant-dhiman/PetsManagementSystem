package PetsManagementSystem.api;

import io.micronaut.security.authentication.Authentication;
import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
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

    public Object pet(String o_Id, String id) {
        return pets.get(id);
    }

    public HashMap<String, Object> getPet(String petId, String o_id) {
        HashMap<String, Object> obj = new HashMap<>();
        if (!String.valueOf(pets.get(petId)).equals("null")) {
            for (Map.Entry<String, Pet> map : pets.entrySet()) {
                String id = map.getKey();
                Pet pet = map.getValue();
                if (pet.id.equals(petId) && pet.o_id.equals(o_id)) {
                    obj.put(id, pet);
                }
            }
            System.out.println("pet attributes: ----> " + obj + "\n");
            return obj;
        }
        return null;
    }

    public Single<Object> updatePet(String old_key, Pet petObj) {
        pets.remove(old_key);
        pets.put(petObj.id, petObj);
        System.out.println("pet updated: -----> " + pets + "\n");
        return Single.just(petObj);
    }

    public Boolean hasPet(String obj) {
        return pets.containsKey(obj);
    }

    public Single<Object> popPet(String key) {
        Pet p = pets.remove(key);
        System.out.println("Delete Pet: " + p);
        return Single.just(p);
    }

}
