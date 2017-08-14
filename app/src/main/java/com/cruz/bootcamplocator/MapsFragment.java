package com.cruz.bootcamplocator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, TextView.OnEditorActionListener {

    private GoogleMap mMap;
    private Criteria criteria;
    private Looper looper;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private View rootView;
    private MapView mapView;
    private ArrayList<Bootcamp> bootcamps;
    private ImageButton searchBtn;
    private EditText zipCodeTxt;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        relativeLayout = rootView.findViewById(R.id.relativeLayout);
        zipCodeTxt = rootView.findViewById(R.id.zipCodeTxt);
        zipCodeTxt.setOnEditorActionListener(this);
        zipCodeTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Fragment fragment = getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag("bootCampFragment");
                if(fragment!=null){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBtn = rootView.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        getUserLocation();

        return rootView;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));

        CameraPosition currentPosition = CameraPosition.builder().target(currentLoc).zoom(15).bearing(0).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
    }

    public void getUserLocation(){

        //start of location
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Log.d("tyler",location.getLatitude() + " " + location.getLongitude());
                currentLocation = location;
                mapView = (MapView) rootView.findViewById(R.id.mapview);
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(MapsFragment.this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MapsFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsFragment.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
        }else {
            locationManager.requestSingleUpdate(criteria, locationListener, looper);
        }

        looper = null;
        //end of location
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestSingleUpdate(criteria, locationListener, looper);
                }
                break;
        }
    }

    public void getBootcamps(){
        bootcamps = new ArrayList<>();
        Bootcamp bootcamp1 = new Bootcamp("Usjr TESDA Building", "Basak Pardo, Cebu City, Cebu", "https://upload.wikimedia.org/wikipedia/en/3/32/St._Ezekiel_Moreno_building%2C_University_of_San_Jose%E2%80%93Recoletos%2C_Basak_campus.jpg", new LatLng(10.290830, 123.861865));
        Bootcamp bootcamp2 = new Bootcamp("Don Bosco Technological Center","Punta Princesa, Cebu City, Cebu", "http://i340.photobucket.com/albums/o359/jakeventura/untitled.jpg", new LatLng(10.297955, 123.863183));
        Bootcamp bootcamp3 = new Bootcamp("Archdiocesan Shrine of Our Lady of Lourdes", "Punta Princesa, Cebu City, Cebu", "http://lourdeskoa.weebly.com/uploads/1/2/8/1/1281954/5994909_orig.jpg", new LatLng(10.293386, 123.869857) );
        bootcamps.add(bootcamp1);
        bootcamps.add(bootcamp2);
        bootcamps.add(bootcamp3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchBtn:
                if(!(zipCodeTxt.getText().toString().equals(""))){
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                    getBootcamps();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.framey, new BootcampListFragment().newInstance(bootcamps),"bootCampFragment" )
                            .commit();
                    for(int i = 0; i<bootcamps.size();i++){
                        addMarkers(bootcamps.get(i));
                    }
                }else{
                    Toast.makeText(MapsFragment.this.getContext(), "Input a zip code to search", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void addMarkers(final Bootcamp bootCamp){
        mMap.addMarker(new MarkerOptions()
                .position(bootCamp.getLatLng())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin))
                .title(bootCamp.getName())
                .snippet(bootCamp.getAddress()));
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            if (!(zipCodeTxt.getText().toString().equals(""))) {
                getBootcamps();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.framey, new BootcampListFragment().newInstance(bootcamps), "bootCampFragment")
                        .commit();
                for (int i2 = 0; i < bootcamps.size(); i2++) {
                    addMarkers(bootcamps.get(i2));
                }
                return false;
            }else{
                Toast.makeText(MapsFragment.this.getContext(), "Input a zip code to search", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
