package oracle.demo;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import oracle.demo.mapper.CountryNotFoundExceptionMapper;

@OpenAPIDefinition(
    info = @Info(
                title = "Open API アノテーション サンプル", 
                version = "1.0", 
                description = "コード・ファーストでAPIを実装し、アノテーションを付加してOpen API仕様のドキュメントを出力するサンプルです", 
                license = @License(
                            name = "Oracle Japan", 
                            url = "http://www.oracle.com"), 
                            contact = @Contact(name = "Tad Kotegawa"
                            )
                ), 
    servers = { 
        @Server(url = "http://jcs.ochacafe.kotegawa.com/api-ochacafe/api/v1") 
        }
    )
@ApplicationPath("/")
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
