package oracle.demo.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import oracle.demo.CountryResource.CountryNotFoundException;

@Provider
public class CountryNotFoundExceptionMapper implements ExceptionMapper<CountryNotFoundException> {

	@Override
	public Response toResponse(CountryNotFoundException e) {
        return Response.status(Status.NOT_FOUND).build();
	}
	
}
