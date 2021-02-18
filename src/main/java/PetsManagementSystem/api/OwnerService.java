package PetsManagementSystem.api;

import io.micronaut.security.authentication.Authentication;
import io.reactivex.Single;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OwnerService {

    private ConcurrentHashMap<String, Owner> owners = new ConcurrentHashMap<>();

    public Single<Object> addOwner(Owner ownerObj) {
        Random rand = new Random();
        String generateId = String.valueOf(owners.size() + rand.nextInt(1000));
        ownerObj.setId(generateId);
        owners.put(ownerObj.user_name, ownerObj);
        System.out.println("Owner added: -----> " + owners + "\n");
        return Single.just(ownerObj);
    }

    public Single<Object> updateOwner(Authentication auth, String old_key, Owner ownerObj) {
        owners.remove(old_key);
        owners.put(ownerObj.user_name, ownerObj);
        System.out.println("Owner updated: -----> " + owners + "\n");
        return Single.just(ownerObj);
    }

    public Boolean hasUser(String usr, String psw) {
        boolean has = false;
        for (Map.Entry<String, Owner> i : owners.entrySet()) {
            Owner obj = i.getValue();
            if (obj.user_name.equals(usr) && obj.password.equals(psw)) {
                has = true;
                break;
            }
        }
        return has;
    }

    public HashMap<String, Object> getOwner(String usr) {
        HashMap<String, Object> obj = new HashMap<>();
        if (!String.valueOf(owners.get(usr)).equals("null")) {
            obj.put(usr, owners.get(usr));
            System.out.println("user attributes: ----> " + obj + "\n");
            return obj;
        }
        return null;
    }

    public Boolean hasUser(String obj) {
        return owners.containsKey(obj);
    }

}
