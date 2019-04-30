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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@Api(tags = { "/country" })
@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private CountryService countryService = new CountryService();

    public CountryResource() {
    }

    @ApiOperation(value = "List all countries", notes = "保持している全ての国情報のリストを取得します", response = Country.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "国情報（国コード、国名）のリスト", responseContainer = "List", response = Country.class),
            @ApiResponse(code = 401, message = "認証に失敗しました"), })
    @GET
    @Path("/")
    public Country[] getCountries() {
        return countryService.getCountries();
    }

    @ApiOperation(value = "Find country by country code", notes = "国コードから国情報を検索します", response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "国際電話の国番号 - US=1, JP=81, etc.", response = Country.class),
            @ApiResponse(code = 401, message = "認証に失敗しました"),
            @ApiResponse(code = 404, message = "指定した国コードから国情報が見つかりませんでした") })
    @GET
    @Path("/{countryId}")
    public Country getCountry(
            @ApiParam(value = "国際電話の国番号 - US=1, JP=81, etc.", required = true) @PathParam("countryId") int countryId) {
        return countryService.getCountry(countryId);
    }

    public static class CountryService {
        private static Map<Integer, String> countries;// = new HashMap<Integer, String>();
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
