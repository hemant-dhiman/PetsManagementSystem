package PetsManagementSystem.api;

import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@ToString
public class PetService {

    private ConcurrentHashMap<String, Pet> pets = new ConcurrentHashMap<>();

    public Single<Object> addPet(Pet obj) {
        pets.put(obj.id, obj);
        return Single.just(obj);
    }
}
