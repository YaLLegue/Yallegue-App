package pibes.yallegue.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import pibes.yallegue.R;
import pibes.yallegue.common.BaseActivity;
import pibes.yallegue.login.LoginActivity;
import pibes.yallegue.party.PartyDialogFragment;
import pibes.yallegue.preference.AppPreferences;
import pibes.yallegue.receive.PushNotificationApp;
import pibes.yallegue.utils.ConstantsPlayer;

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, HomeContract.View {


    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
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

    private int flag = 0;


    private BottomSheetBehavior mBottomSheetBehavior;
    private HomePresenter mHomePresenter;

    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = true;
    private GoogleMap map;
    private Marker myMarker;
    private Polyline myPolyline;
    private List<LatLng> myPoints;

    private ArrayAdapter<String> mAdapter;

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

                mHomePresenter.loadStation();

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
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        showMarker(latLng);
        showPolyline(latLng);
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
        mRequestingLocationUpdates = false;
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
            Toast.makeText(this, "Inicia Juego", Toast.LENGTH_SHORT).show();
        }
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

}
