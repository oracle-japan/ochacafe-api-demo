/*
cd ..\ * Open API アノテーション サンプル
 * コード・ファーストでAPIを実装し、アノテーションを付加してOpen API仕様のドキュメントを出力するサンプルです.
 *
 * OpenAPI spec version: 1.0
 * Contact: tadahisa.kotegawa@oracle.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.Country;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CountryApi
 */
// @Ignore
public class CountryApiTest {

    private final CountryApi api = new CountryApi();

    /**
     * List all countries
     *
     * 保持している全ての国情報のリストを取得します
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getCountriesTest() throws ApiException {
        List<Country> response = api.getCountries();

        // TODO: test validations
        System.out.println(">>> Test api.getCountries()");
        // response.stream().forEach(c ->
        // System.out.println(String.format("getCountries: %s\n", c)));
        int i = 0;
        for (Country c : response) {
            System.out.println(String.format("[%d] %s", i++, c));
        }
        org.junit.Assert.assertEquals(2, response.size());

    }

    /**
     * Find country by country code
     *
     * 国コードから国情報を検索します
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getCountryTest() throws ApiException {
        Integer countryId = new Integer(1);
        Country response = api.getCountry(countryId);

        // TODO: test validations
        System.out.println(">>> Test api.getCountry(1)");
        System.out.println(String.format("%s", response));
        org.junit.Assert.assertEquals("USA", response.getCountryName());
    }
}
