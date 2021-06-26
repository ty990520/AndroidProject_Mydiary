package com.cookandroid.mydiary1;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowActivity extends AppCompatActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add markers to the map and do other map setup.
        // ...
        // Set a listener for info window events.
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(Map.class);
//        builder.setTitle("병원정보");
//        builder.setMessage(
//                "이름 : "
////                + clinics.get(marker_ID_number-1).getName() +
////                        "\n주소 : " + clinics.get(marker_ID_number-1).getAddress() +
////                        "\n병원전화번호 : " + clinics.get(marker_ID_number-1).getPhoneNumber() +
////                        "\n검체채취가능여부 : " + clinics.get(marker_ID_number-1).getSample()
//        );
//        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setNegativeButton("전화걸기", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel" + clinics.get(Integer.parseInt(marker_ID_number)).getPhoneNumber())));
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }
}
