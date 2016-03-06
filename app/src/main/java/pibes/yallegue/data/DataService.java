package pibes.yallegue.data;

import pibes.yallegue.model.Subway;
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
}
