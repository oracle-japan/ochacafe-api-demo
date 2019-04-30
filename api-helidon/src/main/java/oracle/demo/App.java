package oracle.demo;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import oracle.demo.filter.BasicAuthFilter;
import oracle.demo.filter.CORSFilter;
import oracle.demo.mapper.CountryNotFoundExceptionMapper;

@ApplicationScoped
@ApplicationPath("/api/v1")
public class App extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return getRestClasses();
    }

    private Set<Class<?>> getRestClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(CountryResource.class);
        resources.add(CountryNotFoundExceptionMapper.class);
        resources.add(BasicAuthFilter.class);
        resources.add(CORSFilter.class);

        return resources;
    }
}
