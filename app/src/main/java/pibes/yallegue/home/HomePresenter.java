package pibes.yallegue.home;

import android.content.Context;
import android.database.Observable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import pibes.yallegue.YaLlegueApplication;
import pibes.yallegue.data.DataService;
import pibes.yallegue.model.Station;
import pibes.yallegue.model.Subway;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Jhordan on 06/03/16.
 */
public class HomePresenter implements HomeContract.UserActionListener {

    private HomeContract.View mView;
    private Context mContext;
    private YaLlegueApplication llegueApplication;

    public HomePresenter(@NonNull HomeContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        llegueApplication = YaLlegueApplication.create(mContext);
    }

    @Override
    public void startGame() {
        mView.draggableBottomSheet();
    }

    @Override
    public void play() {
        mView.showDialogParty();
    }

    @Override
    public void loadStation() {

        try {
            mView.showProgressIndicator(true);

            DataService dataService = llegueApplication.getDataService();
            dataService.getStation()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<Subway>() {
                @Override
                public void call(Subway subway) {
                    mView.showStationOnEditText(subway.getName());
                    mView.showTextLabel("Jugar");
                    mView.showProgressIndicator(false);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();

                    mView.showProgressIndicator(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void loadOrangeStation() {

        DataService dataService = llegueApplication.getDataService();
        dataService.getOrangeStation().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<List<Station>>() {
            @Override
            public void call(List<Station> stations) {

                final List<String> mStations = new ArrayList<>();
                for (Station stationOrange : stations) {
                    mStations.add(stationOrange.getName());
                }

                mView.showOrangeStation(mStations);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }


}
