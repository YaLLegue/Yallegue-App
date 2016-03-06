package pibes.yallegue.home;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

    private BottomSheetBehavior mBottomSheetBehavior;
    private HomePresenter mHomePresenter;

    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = true;
    private GoogleMap map;
    private Marker myMarker;
    private Polyline myPolyline;
    private List<LatLng> myPoints;

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
            mHomePresenter = new HomePresenter(this);

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


    private void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                mHomePresenter.play();
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
                            .defaultMarker(ConstantsPlayer.TYPE_PLAYER == 1 ? BitmapDescriptorFactory.HUE_GREEN :
                                    BitmapDescriptorFactory.HUE_RED)));
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
}
