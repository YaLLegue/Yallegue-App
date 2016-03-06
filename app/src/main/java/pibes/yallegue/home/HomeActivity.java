package pibes.yallegue.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pibes.yallegue.R;
import pibes.yallegue.YaLlegueApplication;
import pibes.yallegue.common.BaseActivity;
import pibes.yallegue.data.DataService;
import pibes.yallegue.login.LoginActivity;
import pibes.yallegue.model.Avance;
import pibes.yallegue.model.Reference;
import pibes.yallegue.model.Winner;
import pibes.yallegue.party.PartyDialogFragment;
import pibes.yallegue.preference.AppPreferences;
import pibes.yallegue.receive.PushNotificationApp;
import pibes.yallegue.utils.ConstantsPlayer;
import retrofit.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, HomeContract.View {


    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static final String ID_PARTY = "56dca765c1c3fa2807000004";
    @Bind(R.id.bottom_sheet_home)
    FrameLayout mBottomSheetHome;

    @Bind(R.id.bottom_sheet_content)
    LinearLayout mBottomSheetContent;

    @Bind(R.id.button_home)
    FloatingActionButton mButtonHome;

    @Bind(R.id.button_party)
    Button mButtonParty;

    @Bind(R.id.auto_text)
    EditText mSearchInput;

    @Bind(R.id.start_station)
    Spinner mStateStationSpinner;

    @Bind(R.id.destine_start_station)
    Spinner mDestineSpinner;

    @Bind(R.id.end_station)
    Spinner mEndSpinner;

    @Bind(R.id.destine_end_station)
    Spinner mDestineEndSpinner;

    @Bind(R.id.button_win)
    Button buttonWin;

    private ProgressDialog mProgressDialogHome;

    private int flag = 0;


    private BottomSheetBehavior mBottomSheetBehavior;
    private HomePresenter mHomePresenter;

    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private GoogleMap map;
    private Marker myMarker;
    private Polyline myPolyline;
    private List<LatLng> myPoints;

    private ArrayAdapter<String> mAdapter;
    private boolean obtainAvance = false;
    private Polyline myPolylineRiver;
    private Marker myMarkerRiver;


    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }


    @Override
    public void initView() {
        super.initView();
        setupBottomSheet();
        buildGoogleApiClient();
        setupMap();
        checkNotification(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(LOG_TAG, "onNewIntent");
        checkNotification(intent);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupBottomSheet() {
        if (mHomePresenter == null)
            mHomePresenter = new HomePresenter(this, this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetHome);
        setBottomSheetCallback(mBottomSheetBehavior);

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
        partyDialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void showStationOnEditText(String txt) {
        mSearchInput.setText(txt);
    }

    @Override
    public void showOrangeStation(List<String> stations) {
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stations);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEndSpinner.setAdapter(mAdapter);


    }

    @Override
    public void showProgressIndicator(Boolean state) {
        if (state)
            setProgressState(true);
        else
            mProgressDialogHome.dismiss();

    }

    @Override
    public void showTextLabel(String play) {
        mButtonParty.setText(play);
    }


    private void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeStations();
        connectGoogleApiClient();

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.startGame();
            }
        });

        mButtonParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mButtonParty.getText().toString().equals("Jugar")){
                   mHomePresenter.play();
                }else{
                    mHomePresenter.loadStation();
                }


            }
        });

    }


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    protected void onStop() {
        super.onStop();
        disconnectGoogleApiClient();
    }

    private void disconnectGoogleApiClient() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        disconnectGoogleApiClient();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected", "true");

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("onConnectionSuspended", "true");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", "true");

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", location.getLatitude() + "," + location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        showMarker(latLng);
        showPolyline(latLng);
        senPosition(latLng);
    }


    private void showPolyline(LatLng latLng) {
        if (myPolyline == null) {
            myPoints = new ArrayList<LatLng>();
            myPoints.add(latLng);
            myPolyline = map.addPolyline(new PolylineOptions()
                    .add(latLng)
                    .color(ConstantsPlayer.TYPE_PLAYER == 1 ? getResources().getColor(R.color.player_1) : getResources().getColor(R.color.player_2)));
        } else {
            myPoints.add(latLng);
            myPolyline.setPoints(myPoints);
        }
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void showMarker(LatLng latLng) {
        if (myMarker == null) {
            myMarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(getMarkerBitmapFromView(ConstantsPlayer.TYPE_PLAYER == 1?R.drawable.player_1:R.drawable.player_2))));


        } else {
            myMarker.setPosition(latLng);
        }
    }


    private void startLocationUpdates() {
        mRequestingLocationUpdates = true;
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, makeLocationRequest(), this);
    }

    protected LocationRequest makeLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    protected void stopLocationUpdates() {
//        mRequestingLocationUpdates = false;
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng googleMexico = new LatLng(19.428284, -99.206594);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMexico, 17));
    }

    private void checkNotification(Intent intent) {
        if (intent.hasExtra(PushNotificationApp.EXTRA_START)) {
            mRequestingLocationUpdates = true;
            obtainAvance = true;
            connectGoogleApiClient();
            startRequest();
            hideBottomSheetContent();
            hideButton();

            showButtonWin();
        }
    }

    private void showButtonWin() {
        buttonWin.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        mButtonHome.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initializeStations() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.metro_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateStationSpinner.setAdapter(adapter);
        mDestineSpinner.setAdapter(adapter);

        mStateStationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Linea 7")) {
                    mHomePresenter.loadOrangeStation();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDestineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Linea 7")) {
                    // mHomePresenter.loadOrangeStation();
                    mDestineEndSpinner.setAdapter(mAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setProgressState(Boolean state) {
        mProgressDialogHome = ProgressDialog.show(HomeActivity.this, null, getString(R.string.text_dialog_home), state);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            AppPreferences.getInstance(getApplicationContext()).removePreference();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        mMarkerImageView.setImageResource(resId);
        mCustomMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mCustomMarkerView.layout(0, 0, mCustomMarkerView.getMeasuredWidth(), mCustomMarkerView.getMeasuredHeight());
        mCustomMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(mCustomMarkerView.getMeasuredWidth(), mCustomMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = mCustomMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        mCustomMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void startRequest() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (obtainAvance) {
                    Log.d("get dates", "true");
                    obtainAvance();
                    startRequest();
                }
            }
        }, 5000);
    }

    private void obtainAvance() {
        YaLlegueApplication llegueApplication = YaLlegueApplication.create(this);

        DataService dataService = llegueApplication.getDataService();


        dataService.getAvanceUser(ID_PARTY).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<List<Avance>>() {

            @Override
            public void call(List<Avance> avances) {

                Log.d("get dates", "avances: " + avances.size());


                if (avances.isEmpty()) {
                    return;
                }

                List<LatLng> latLngs = new ArrayList<LatLng>();
                for (Avance avance : avances) {
                    if (!avance.getUsername().equals(ConstantsPlayer.TYPE_PLAYER == 1 ? "amet" : "deadpool")) {
                        double latitude = Double.parseDouble(avance.getLatitude());
                        double longitude = Double.parseDouble(avance.getLongitud());
                        latLngs.add(new LatLng(latitude, longitude));
                    } else {
                        Log.d("no enter", "false");
                    }
                }

                if (myMarkerRiver == null) {
                    myMarkerRiver = map.addMarker(new MarkerOptions()
                            .position(latLngs.get(latLngs.size() - 1))
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(getMarkerBitmapFromView(ConstantsPlayer.TYPE_PLAYER == 1 ? R.drawable.player_2 : R.drawable.player_1))));
                } else {
                    myMarkerRiver.setPosition(latLngs.get(latLngs.size() - 1));
                }

                if (myPolylineRiver == null) {
                    myPolylineRiver = map.addPolyline(new PolylineOptions()
                            .addAll(latLngs)
                            .color(ConstantsPlayer.TYPE_PLAYER == 1 ? getResources().getColor(R.color.player_2) : getResources().getColor(R.color.player_1)));
                } else {
                    myPolylineRiver.setPoints(latLngs);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("sendAvanceUser", "error");
                throwable.printStackTrace();
            }
        });

    }


    private void senPosition(LatLng latLng) {
        YaLlegueApplication llegueApplication = YaLlegueApplication.create(this);

        Avance avance = new Avance(ConstantsPlayer.TYPE_PLAYER == 1 ? "amet" : "deadpool", latLng.latitude + "", latLng.longitude + "");

        Reference reference = new Reference(avance);

        DataService dataService = llegueApplication.getDataService();
        dataService.sendAvanceUser(ID_PARTY, reference).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<Response<ResponseBody>>() {
            @Override
            public void call(Response response) {
                Log.d("sendAvanceUser", "success");

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("sendAvanceUser", "error");
                throwable.printStackTrace();
            }
        });
    }


    @OnClick(R.id.button_win)
    public void buttonWin() {
        YaLlegueApplication llegueApplication = YaLlegueApplication.create(this);
        DataService dataService = llegueApplication.getDataService();
        dataService.getWinner(ID_PARTY).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<Winner>() {
            @Override
            public void call(Winner winner) {

                WinnnerDialog winnnerDialog;

                if (winner.isSuccces()) {
                    winnnerDialog = WinnnerDialog.newIntancer("Ganaste!! :)");
                } else {
                    winnnerDialog = WinnnerDialog.newIntancer("Perdiste!! :(");
                }

                winnnerDialog.show(getFragmentManager(), "");

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("sendAvanceUser", "error");
                throwable.printStackTrace();
            }
        });
    }


}





