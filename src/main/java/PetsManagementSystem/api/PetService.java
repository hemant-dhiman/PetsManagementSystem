package PetsManagementSystem.api;

import io.reactivex.Single;
import lombok.ToString;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@ToString
public class PetService {

    private ConcurrentHashMap<String, Pet> pets = new ConcurrentHashMap<>();

    public Single<Object> addPet(Pet petObj) {
        String generateID = String.valueOf(pets.size()+1);
        petObj.setId(generateID);

        pets.put(generateID, petObj);
        System.out.println("Pet added: "+pets);
        return Single.just(petObj);

    }

    public Object[] pets(){
        //Object[] obj =
        return Collections.list(pets.elements()).toArray();
    }

    public Object pets(String id){
        return pets.get(id);
    }
}
