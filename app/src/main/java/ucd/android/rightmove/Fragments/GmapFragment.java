package ucd.android.rightmove.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.os.Parcel;
import android.os.Parcelable;

import android.os.Handler;
import android.os.ResultReceiver;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.location.Address;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ucd.android.rightmove.Constants;
import ucd.android.rightmove.FetchAddressIntentService;
import ucd.android.rightmove.R;

public class GmapFragment extends Fragment
        implements
            GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener,
                    OnMapReadyCallback {


    private static final String TAG = "GmapFragment";
    private GoogleApiClient client;
    private Location mlocation;
    private GoogleMap gMap;
    private LatLng marker;
    private Marker m;
    private Intent intent;

    //Receiver registered with this activity to get the response from FetchAddressIntentService.

    private AddressResultReceiver mResultReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // keep the fragment and all its data across screen rotation

        setRetainInstance(true);

        return inflater.inflate(R.layout.fragment_gmaps, container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        buildGoogleApiClient();

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
         gMap = googleMap;

    }

    public void mapPosition(){

        // try - catch hack to get around Permissions ...

        try {
            mlocation = LocationServices.FusedLocationApi.getLastLocation( client );

            if (mlocation != null ){
                LatLng marker = new LatLng(mlocation.getLatitude(), mlocation.getLongitude());

                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
                //m = gMap.addMarker(new MarkerOptions().title( getAddress() ).position(marker));
                m = gMap.addMarker(new MarkerOptions().position(marker));

                // get Address - using an IntentService to get the task of the main thread - as in the case of getAddress()

                if (client.isConnected() && mlocation != null) {
                    startIntentService();
                }
            }

        } catch (SecurityException e){}
    }


    // Will need to get this off the main thread ... hence the IntentService.

    public String getAddress() {

        StringBuilder sb = new StringBuilder();
        sb.append("");
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
            List<Address> addressList = geocoder.getFromLocation(mlocation.getLatitude(), mlocation.getLongitude(), 1);

            if (geocoder.isPresent()) {
                Address address = addressList.get(0);

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getCountryName());
            }

        }catch (IOException e) { }

        return( sb.toString());
    }

    protected synchronized void buildGoogleApiClient(){
        client = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public void onConnected(Bundle bundle){
        Log.d(TAG,"onConnected ... ");
        mapPosition();
    }

    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG,"onConnectionFailed ... . ");
    }

    public void onConnectionSuspended(int i ){
        Log.d(TAG,"onConnectionSuspended ...  ");
    }

    public void onStart() {
        super.onStart();
        client.connect();
    }


    protected void startIntentService() {

        // Create an intent for passing to the intent service responsible for fetching the address.

        intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.

        mResultReceiver = new AddressResultReceiver(new Handler());

        intent.putExtra(Constants.RECEIVER , mResultReceiver);

        // Pass the location data as an extra to the service.

        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mlocation);

        getActivity().startService(intent);
    }


    // Technically we should implement an address class that would implement Parcelable

    // see - http://alexzh.com/uncategorized/passing-object-by-intent/ - for a simple explanation.

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver  {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String address = resultData.getString(Constants.RESULT_DATA_KEY);

            if (resultCode == Constants.SUCCESS_RESULT) {
                m.setTitle(address);
            }
        }
    }
}