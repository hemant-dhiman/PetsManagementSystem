package PetsManagementSystem.api;

import io.reactivex.Single;
import lombok.ToString;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@ToString
public class OwnerService {

    private ConcurrentHashMap<String, Owner> owners = new ConcurrentHashMap<>();

    public Single<Object> addOwner(Owner obj){
        owners.put(obj.id, obj);
        System.out.println(obj);
        return Single.just(obj);
    }


}
