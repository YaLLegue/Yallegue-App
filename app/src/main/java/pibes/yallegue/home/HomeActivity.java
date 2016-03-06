package pibes.yallegue.home;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.Bind;
import pibes.yallegue.R;
import pibes.yallegue.common.BaseActivity;
import pibes.yallegue.party.PartyDialogFragment;

public class HomeActivity extends BaseActivity implements HomeContract.View {


    @Bind(R.id.bottom_sheet_home)
    FrameLayout mBottomSheetHome;

    @Bind(R.id.bottom_sheet_content)
    LinearLayout mBottomSheetContent;

    @Bind(R.id.button_home)
    FloatingActionButton mButtonHome;

    @Bind(R.id.button_party)
    Button mButtonParty;

    private BottomSheetBehavior mBottomSheetBehavior;
    private HomePresenter mHomePresenter;


    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }


    @Override
    public void initView() {
        super.initView();

        if (mHomePresenter == null)
            mHomePresenter = new HomePresenter(this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetHome);
        setBottomSheetCallback(mBottomSheetBehavior);

    }

    @Override
    protected void onResume() {
        super.onResume();


        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.startGame();
            }
        });

        mButtonParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.play();
            }
        });

    }

    private void setBottomSheetCallback(final BottomSheetBehavior bottomSheetBehavior) {

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        hideBottomSheetContent();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        showBottomSheetContent();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


    }

    @Override
    public void showBottomSheetContent() {
        mBottomSheetContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomSheetContent() {
        mBottomSheetContent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void bottomSheetBehaviorExpanded() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void bottomSheetBehaviorCollapsed() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public int getState() {
        return mBottomSheetBehavior.getState();
    }

    @Override
    public void draggableBottomSheet() {
        if (getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            showBottomSheetContent();
            bottomSheetBehaviorExpanded();
        } else
            bottomSheetBehaviorCollapsed();

    }

    @Override
    public void showDialogParty() {
        PartyDialogFragment partyDialogFragment = PartyDialogFragment.newInstance();
        partyDialogFragment.show(getSupportFragmentManager(),"");
    }

}
