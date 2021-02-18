package PetsManagementSystem.api;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

@Singleton
public class AuthenticationUser implements AuthenticationProvider {

    @Inject
    private OwnerService ownerService;


    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String userName = authenticationRequest.getIdentity().toString();
        if (ownerService.hasUser(userName, authenticationRequest.getSecret().toString())) {
            UserDetails details = new UserDetails(userName, Collections.singletonList("ROLE_OWNER"), ownerService.getOwner(userName));
            //UserDetails d = new UserDetails();
            return Flowable.just(details);
        }
        return Flowable.just(new AuthenticationFailed("Credentials are Not Valid"));
    }
}
