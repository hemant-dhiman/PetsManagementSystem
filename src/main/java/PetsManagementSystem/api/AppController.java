package PetsManagementSystem.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;
import lombok.ToString;

import javax.inject.Inject;

@ToString
@Controller("/pms/pets")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AppController {

    //@Inject
    //private OwnerService ownerService;

    //@Post
    //public Single<HttpResponse<Object>> add(@Body Owner obj){
    //    return ownerService.addOwner(obj).map(HttpResponse::created);
    //}


    @Inject
    private PetService ownerService;

    @Post
    public Single<HttpResponse<Object>> add(@Body Pet obj){
        System.out.println(obj);
        return ownerService.addPet(obj).map(HttpResponse::created);
    }

}
