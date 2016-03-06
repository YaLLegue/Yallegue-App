package pibes.yallegue.home;

import java.util.List;

import pibes.yallegue.model.Station;

/**
 * Created by Jhordan on 06/03/16.
 */
public class HomeContract {

    interface View {

        void showBottomSheetContent();

        void hideBottomSheetContent();

        void bottomSheetBehaviorExpanded();

        void bottomSheetBehaviorCollapsed();

        int getState();

        void draggableBottomSheet();

        void showDialogParty();

        void showStationOnEditText(String txt);

        void showOrangeStation(List<String> stations);
    }

    interface UserActionListener {

        void startGame();

        void play();

        void loadStation();

        void loadOrangeStation();

    }
}
