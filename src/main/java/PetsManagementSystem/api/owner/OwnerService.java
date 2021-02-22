package PetsManagementSystem.api.owner;

import io.micronaut.security.authentication.Authentication;
import io.reactivex.Single;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OwnerService {

    private ConcurrentHashMap<String, Owner> owners = new ConcurrentHashMap<>();

    @PostConstruct
    void addTestUser() {
        owners.put("HD", new Owner("1", "Hemant Dhiman", "HD", "hemant@gmail.com",
                "12345", new Address("Rani tal", "Partap Bhawan", "Sirmaur",
                "Himachal Pradesh", "173001")));
    }

    public Single<Owner> addOwner(@NotNull Owner ownerObj) {
        String generateId = String.valueOf(owners.size() + 1);
        ownerObj.setId(generateId);
        owners.put(ownerObj.user_name, ownerObj);
        System.out.println("Owner added: -----> " + owners + "\n");
        return Single.just(ownerObj);
    }

    public Owner updateOwner(Authentication authentication, OwnerManage ownerManage) {
        Owner owner = owners.get(authentication.getName());
        owner.setUser_name(ownerManage.getUsername());
        owner.setPassword(ownerManage.getPassword());
        owners.remove(authentication.getName());
        return owners.put(ownerManage.getUsername(), owner);
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

    public Owner getOwner(String usr) {
        return owners.get(usr);
    }

    public HashMap<String, Object> getOwnerAttributes(String usr) {
        HashMap<String, Object> obj = new HashMap<>();
        if (!String.valueOf(owners.get(usr)).equals("null")) {
            obj.put(usr, owners.get(usr));
            System.out.println("user attributes: ----> " + obj + "\n");
            return obj;
        }
        return null;
    }

    public Boolean hasUser(String user) {
        return owners.containsKey(user);
    }

}
