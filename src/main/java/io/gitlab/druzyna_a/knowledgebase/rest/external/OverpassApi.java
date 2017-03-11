package io.gitlab.druzyna_a.knowledgebase.rest.external;

import io.gitlab.druzyna_a.knowledgebase.model.external.OverpassFisheryData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * @author Damian Terlecki
 */
public interface OverpassApi {

    String BASE_URL = "http://overpass-api.de/api/";

    @GET("interpreter")
    Call<OverpassFisheryData> getLocations(@Query(value = "data") String data);

}
