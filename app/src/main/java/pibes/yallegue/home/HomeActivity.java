package pibes.yallegue.home;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import pibes.yallegue.receive.PushNotificationApp;
import pibes.yallegue.utils.ConstantsPlayer;

public class HomeActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {


    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    @Bind(R.id.bottom_sheet_home)
    FrameLayout mHomeBottomSheet;

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

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectGoogleApiClient();
    }

    private void connectGoogleApiClient() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
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

        if (myMarker == null) {
            myMarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(ConstantsPlayer.TYPE_PLAYER == 1 ? BitmapDescriptorFactory.HUE_GREEN :
                                    BitmapDescriptorFactory.HUE_RED)));
        } else {
            myMarker.setPosition(latLng);
        }

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

}
