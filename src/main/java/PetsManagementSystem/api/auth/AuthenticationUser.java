package PetsManagementSystem.api.auth;

import PetsManagementSystem.api.owner.OwnerService;
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
        String passWord = authenticationRequest.getSecret().toString();
        if (!ownerService.hasUser(userName)) {
            return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND));
        } else if (ownerService.hasUser(userName, passWord)) {
            UserDetails details = new UserDetails(userName, Collections.singletonList("ROLE_OWNER"), ownerService.getOwnerAttributes(userName));
            return Flowable.just(details);
        } else {
            return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
        }
    }
}
