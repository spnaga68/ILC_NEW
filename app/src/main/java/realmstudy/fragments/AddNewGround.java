package realmstudy.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Ground;

public class AddNewGround extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private Bundle savedInstanceState;
    GoogleApiClient apiclient;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    Marker mCurrLocationMarker;
    Location mLastLocation;
    Marker long_marker;
    EditText latlong;
    int intervalTime = 10000;
    private LocationRequest locationRequest;
    private String locale;
    private Spinner country;
    private EditText ground_name, region;
    private AppCompatButton save;
    private LatLng groundLatLng;
    private String countryEdit = "";
    @Inject
    Realm realm;
    private int groundId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MyApplication) (getActivity()).getApplication()).getComponent().inject(this);
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        latlong = (EditText) view.findViewById(R.id.latlng);
        country = (Spinner) view.findViewById(R.id.country);
        ground_name = (EditText) view.findViewById(R.id.ground_name);
        region = (EditText) view.findViewById(R.id.region);
        save = (AppCompatButton) view.findViewById(R.id.save);
        if (getArguments() != null) {
            groundId = getArguments().getInt("id");
            Ground g = realm.where(Ground.class).equalTo("id", groundId).findFirst();
            if (g != null) {
                ground_name.setText(g.getGroundName());
                region.setText(g.getRegionName());
                groundLatLng = new LatLng(g.getLat(), g.getLng());
                countryEdit = g.getCountryName();
            }
        }
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groundLatLng != null) {
                    if (ground_name.getText().toString().length() > 5) {
                        if (region.getText().toString().length() > 3) {
                            long tsLong;
                            tsLong = System.currentTimeMillis() / 1000;

                            realm.beginTransaction();
                            Ground ground;
                            if (groundId == 0)
                                ground = realm.createObject(Ground.class, (int) tsLong);
                            else
                                ground = realm.where(Ground.class).equalTo("id", groundId).findFirst();
                            ground.setCountryName(country.getSelectedItem().toString());
                            ground.setGroundName(ground_name.getText().toString());
                            ground.setRegionName(region.getText().toString());
                            ground.setLat(groundLatLng.latitude);
                            ground.setLng(groundLatLng.longitude);
                            realm.commitTransaction();
                            getActivity().onBackPressed();

                        } else
                            Toast.makeText(getActivity(), getString(R.string.region_valid), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), getString(R.string.ground_name_valid), Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity(), getString(R.string.select_location), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("ONMAPrr");
        if (groundLatLng != null) {
            long_marker = googleMap.addMarker(new MarkerOptions()
                    .position(groundLatLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(groundLatLng, 16f));
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLastLocation == null) {
                    stopLocationUpdate();
                    locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                    startLocUpdate();
                }
            }
        }, intervalTime);

        if (apiclient == null) {
            System.out.println("ONMAP");
            apiclient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            apiclient.connect();
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(apiclient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                status.startResolutionForResult(getActivity(), 1000);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(final LatLng latLng) {

                if (long_marker != null) {
                    long_marker.remove();
                }
                groundLatLng = latLng;
                if (!latlong.equals("")) {
                    latlong.setText("");
                }

                System.out.println("latlong_____" + latLng);

                long_marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng));
                latlong.setText(latLng.toString());

            }

        });

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        apiclient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(AddNewGround.this)
                .addApi(LocationServices.API)
                .build();
        apiclient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        startLocUpdate();

    }


    void startLocUpdate() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            if (apiclient != null && apiclient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(apiclient, mLocationRequest, this);
        }
    }

    void stopLocationUpdate() {


        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            if (apiclient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(apiclient, this);
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        if (groundLatLng == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

        stopLocationUpdate();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (countryEdit.trim().equals(""))
            countryEdit = getResources().getConfiguration().locale.getDisplayCountry();
        for (int i = 0; i < country.getAdapter().getCount(); i++) {
            String countrystring = (String) country.getAdapter().getItem(i);
            if (countrystring.trim().equalsIgnoreCase(countryEdit.trim())) {
                country.setSelection(i);
                break;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

        }
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        System.out.println("locale" + locale);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        if (apiclient == null) {
                            buildGoogleApiClient();
                        } else
                            startLocUpdate();
                    }


                }


            }
        }
    }


}
