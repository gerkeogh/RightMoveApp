
package ucd.android.rightmove.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ucd.android.rightmove.R;


/**
 * Created by Kate on 04/06/2016.
 */
public class GoogleMapFragment extends Fragment {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if ( savedInstanceState != null ){

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        View layout = inflater.inflate(R.layout.fragment_gmaps, container, false);
        //runTimer(layout);

        Button qbutton = (Button) layout.findViewById(R.id.search_button);
        qbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "You click on the google map button", Toast.LENGTH_LONG);
            }
        });

        return layout;
    }
}
