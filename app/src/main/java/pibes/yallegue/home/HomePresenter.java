package pibes.yallegue.home;

import android.support.annotation.NonNull;

/**
 * Created by Jhordan on 06/03/16.
 */
public class HomePresenter implements HomeContract.UserActionListener {

    private HomeContract.View mView;

    public HomePresenter(@NonNull HomeContract.View view){
        mView = view;
    }

    @Override
    public void startGame() {
        mView.draggableBottomSheet();
    }
}
