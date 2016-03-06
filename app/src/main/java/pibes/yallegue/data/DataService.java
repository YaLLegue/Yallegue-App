package pibes.yallegue.data;

import pibes.yallegue.model.Subway;
import pibes.yallegue.model.TrailResponse;
import pibes.yallegue.model.UserResponse;
import pibes.yallegue.model.Winner;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by Jhordan on 06/03/16.
 */
public interface DataService {

    @GET(DataConstants.ENDPOINT_STATION)
    Observable<Subway> getStation();

    @GET(DataConstants.ENDPOINT_WINNER)
    Observable<Winner> getWinner(@Path("id") String id);

    @GET(DataConstants.ENDPOINT_TRAILS)
    Observable<TrailResponse> getTrails();

    @GET(DataConstants.ENDPOINT_USERS)
    Observable<UserResponse> getUsers();
}
