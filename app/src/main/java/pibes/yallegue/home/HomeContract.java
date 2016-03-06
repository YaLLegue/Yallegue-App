package pibes.yallegue.home;

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
    }

    interface UserActionListener {

        void startGame();

        void play();

    }
}