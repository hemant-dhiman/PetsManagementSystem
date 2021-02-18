package PetsManagementSystem.api;

import com.nimbusds.jose.shaded.json.JSONObject;
import io.micronaut.security.authentication.Authentication;

import java.util.HashMap;

public class UserUtility {
    public static String currentUserId(Authentication authentication) {
        HashMap<String, Object> data = new HashMap<>(authentication.getAttributes());
        JSONObject ja = new JSONObject();
        ja.merge(data.get(authentication.getName()));
        return String.valueOf(ja.get("id"));
    }

}
