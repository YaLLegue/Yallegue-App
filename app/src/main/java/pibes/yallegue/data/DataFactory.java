package pibes.yallegue.data;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Jhordan on 06/03/16.
 */
public class DataFactory {

    private final static String BASE_URL = "http://82.196.13.137:8888/v1";

    public static DataFactory create(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(DataFactory.class);
    }
}
