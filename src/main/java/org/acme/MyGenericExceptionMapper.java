package org.acme;

import jakarta.annotation.Priority;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Objects;

@Provider
@Priority(1)
public class MyGenericExceptionMapper implements ExceptionMapper<Exception> {

    /**
     * I hoped that this class would catch all the exceptions,
     * and then I would judge the type of exception separately and handle it differently,
     * but it turned out that I couldn't catch any that had been caught by other similar classes.
     *
     * {@link io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyViolationExceptionMapper}
     * This class is still in effect.
     */
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ValidationException)
            return Response.status(Status.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .entity(new Result(exception.getLocalizedMessage())).build();
        return Response.status(Status.SERVICE_UNAVAILABLE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(new Result(exception.getLocalizedMessage())).build();
    }

    static class Result {
        private String message;

        public Result(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return Objects.equals(message, result.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
