package org.acme;

import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/hello")
public class GreetingResource {

    @POST
    public String hello(@Valid Person person) {
        return "Hello RESTEasy";
    }
}
