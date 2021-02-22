package PetsManagementSystem.api.owner;

import io.micronaut.security.authentication.Authentication;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class OwnerService {

    private ConcurrentHashMap<String, Owner> owners = new ConcurrentHashMap<>();


    private OwnerPasswordEncrypt ownerPasswordEncryptor;

    public OwnerService(OwnerPasswordEncrypt ownerPasswordEncryptor) {
        this.ownerPasswordEncryptor = ownerPasswordEncryptor;
    }


    @PostConstruct
    void addTestUser() {
        owners.put("HD", new Owner("1", "Hemant Dhiman", "HD", "hemant@gmail.com",
                ownerPasswordEncryptor.encrypt("12345"), new Address("Rani tal", "Partap Bhawan",
                "Sirmaur", "Himachal Pradesh", "173001")));
    }

    public Owner addOwner(@NotNull Owner ownerObj) {
        String generateId = String.valueOf(owners.size() + 1);
        ownerObj.setId(generateId);
        ownerObj.setPassword(ownerPasswordEncryptor.encrypt(ownerObj.password));
        owners.put(ownerObj.user_name, ownerObj);
        return owners.get(ownerObj.user_name);
    }

    public Owner updateOwner(Authentication authentication, OwnerManage ownerManage) {
        Owner owner = owners.get(authentication.getName());
        owner.setUser_name(ownerManage.getUsername());
        owner.setPassword(ownerPasswordEncryptor.encrypt(ownerManage.getPassword()));
        owners.remove(authentication.getName());
        //System.out.println(owner);
        owners.put(ownerManage.getUsername(), owner);
        return owners.get(ownerManage.username);
    }


    public Boolean hasUser(String usr, String psw) {
        boolean has = false;
        for (Map.Entry<String, Owner> i : owners.entrySet()) {
            Owner obj = i.getValue();
            if (obj.user_name.equals(usr) && ownerPasswordEncryptor.decrypt(obj.password).equals(psw)) {
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
            //System.out.println("user attributes: ----> " + obj + "\n");
            return obj;
        }
        return null;
    }

    public Boolean hasUser(String user) {
        return owners.containsKey(user);
    }

}
