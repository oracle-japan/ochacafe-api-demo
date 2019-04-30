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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
    info = @Info(
            title = "Open API アノテーション サンプル", 
            version = "1.0", 
            description = "コード・ファーストでAPIを実装し、アノテーションを付加してOpen API仕様のドキュメントを出力するサンプルです",
            license = @License(
                        name = "Oracle Japan", 
                        url = "http://www.oracle.com"), 
                        contact = @Contact(name = "Tad Kotegawa")
                ), 
            servers = {
                @Server(url = "http://jcs.ochacafe.kotegawa.com/api-ochacafe/api/v1") 
                }
        )
@Tag(name = "/country", description = "国情報の提供")
@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource {

    private CountryService countryService = new CountryService();

    public CountryResource() {
    }

    @Operation(
        summary = "List all countries", 
        description = "保持している全ての国情報のリストを取得します", 
        responses = {
            @ApiResponse(
                responseCode = "200", description = "国情報のリスト", 
                content = @Content(
                            mediaType = "application/json", 
                            array = @ArraySchema(schema = @Schema(implementation = Country.class))
                            )
                ),
            @ApiResponse(responseCode = "401", description = "認証に失敗しました")
            }
        )
    @GET
    @Path("/")
    public Country[] getCountries() throws Exception {
        return countryService.getCountries();
    }

    @Operation(
        summary = "Find country by country code", 
        description = "国コードから国情報を検索します", 
        responses = {
            @ApiResponse(
                responseCode = "200", description = "国情報", 
                content = @Content(
                            mediaType = "application/json", 
                            schema = @Schema(implementation = Country.class)
                            )
                ),
            @ApiResponse(responseCode = "401", description = "認証に失敗しました"),
            @ApiResponse(responseCode = "404", description = "指定した国コードから国情報が見つかりませんでした") 
            }
        )
    @GET
    @Path("/{countryId}")
    public Country getCountry(
                    @Parameter(description = "国際電話の国番号 - US=1, JP=81, etc.", required = true) 
                    @PathParam("countryId") 
                    int countryId) {
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
