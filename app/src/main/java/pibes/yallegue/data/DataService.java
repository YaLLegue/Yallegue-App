package pibes.yallegue.data;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import pibes.yallegue.model.Avance;
import pibes.yallegue.model.Reference;
import pibes.yallegue.model.Station;
import pibes.yallegue.model.Subway;
import pibes.yallegue.model.TrailResponse;
import pibes.yallegue.model.UserResponse;
import pibes.yallegue.model.Winner;
import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Jhordan on 06/03/16.
 */
public interface DataService {

    @GET(DataConstants.ENDPOINT_STATION)
    Observable<Subway> getStation();

    @GET(DataConstants.ENDPOINT_METRO_NARANJA)
    Observable<List<Station>> getOrangeStation();

    @GET(DataConstants.ENDPOINT_WINNER)
    Observable<Winner> getWinner(@Path("id") String id);

    @GET(DataConstants.ENDPOINT_TRAILS)
    Observable<TrailResponse> getTrails();

    @GET(DataConstants.ENDPOINT_USERS)
    Observable<UserResponse> getUsers(@Path("user_name") String userName);

    @GET(DataConstants.ENDPOINT_PARTYS_AVANCE)
    Observable<List<Avance>> getAvanceUser(@Path("id") String id);

    @POST(DataConstants.ENDPOINT_PARTYS_AVANCE)
    Observable<Response<ResponseBody>> sendAvanceUser(@Path("id") String id, @Body Reference reference);
}
