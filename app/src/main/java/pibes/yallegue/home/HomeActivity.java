package pibes.yallegue.home;

import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.Bind;
import pibes.yallegue.R;
import pibes.yallegue.common.BaseActivity;

public class HomeActivity extends BaseActivity {


    @Bind(R.id.bottom_sheet_home)
    FrameLayout mHomeBottomSheet;


    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }


    @Override
    public void initView() {
        super.initView();

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mHomeBottomSheet);
        setBottomSheetCallback(bottomSheetBehavior);
    }

    private void setBottomSheetCallback(final BottomSheetBehavior bottomSheetBehavior){

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                String nuevoEstado = "";

                switch(newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        nuevoEstado = "STATE_COLLAPSED";
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        nuevoEstado = "STATE_EXPANDED";
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        nuevoEstado = "STATE_HIDDEN";
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        nuevoEstado = "STATE_DRAGGING";

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        nuevoEstado = "STATE_SETTLING";
                        break;
                }

                Log.i("BottomSheets", "Nuevo estado: " + nuevoEstado);
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

                Log.i("BottomSheets", "Offset: " + slideOffset);

            }
        });


    }
}
