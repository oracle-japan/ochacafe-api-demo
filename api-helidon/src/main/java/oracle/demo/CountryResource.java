package oracle.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import oracle.demo.filter.Auth;
import oracle.demo.filter.CORS;

@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private CountryService countryService = new CountryService();

    public CountryResource() {
    }

    @GET
    @Path("/{countryId}")
    public Country getCountry(@PathParam("countryId") int countryId) {
        return countryService.getCountry(countryId);
    }

    @GET
    @Path("/cors/{countryId}")
    @CORS
    public Country getCountryWithCORS(@PathParam("countryId") int countryId) {
        return countryService.getCountry(countryId);
    }

    @GET
    @Path("/auth/{countryId}")
    @CORS
    @Auth
    public Country getCountryWithAuth(@PathParam("countryId") int countryId) {
        return countryService.getCountry(countryId);
    }

    @GET
    @Path("/")
    public Country[] getCountries() throws Exception {
        return countryService.getCountries();
    }

    public static class CountryService {
        private static Map<Integer, String> countries;
        static {
            countries = new HashMap<Integer, String>();
            countries.put(1, "United States of America");
            countries.put(81, "Japan");
        }

        public Country getCountry(int countryId) {
            String countryName = countries.get(countryId);
            Optional.ofNullable(countryName)
                    .orElseThrow(() -> new CountryNotFoundException(String.format("Bad countryId: %d", countryId)));
            return new Country(countryId, countryName);
        }

        public Country[] getCountries() {
            ArrayList<Country> cList = new ArrayList<Country>();
            countries.forEach((id, name) -> {
                cList.add(new Country(id, name));
            });
            return cList.toArray(new Country[countries.size()]);
        }

    }

    @XmlRootElement
    public static class Country {

        public int countryId;
        public String countryName;

        public Country() {
        } // need empty constructor

        public Country(int countryId, String countryName) {
            this.countryId = countryId;
            this.countryName = countryName;
        }
    }

    @SuppressWarnings("serial")
    public static class CountryNotFoundException extends RuntimeException {
        public CountryNotFoundException() {
            super();
        }

        public CountryNotFoundException(String message) {
            super(message);
        }
    }

}
