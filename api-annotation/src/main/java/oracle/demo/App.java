package oracle.demo;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import oracle.demo.mapper.CountryNotFoundExceptionMapper;

@ApplicationPath("/api/v1")
public class App extends Application {

    public Set<Class<?>> getClasses() {
        return getRestClasses();
    }

    private Set<Class<?>> getRestClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(CountryResource.class);
        resources.add(CountryNotFoundExceptionMapper.class);

        return resources;
    }
}
