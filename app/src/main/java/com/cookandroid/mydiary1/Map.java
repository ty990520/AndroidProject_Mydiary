package com.cookandroid.mydiary1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cookandroid.mydiary1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    MapFragment mapFragment;
    GroundOverlayOptions videoMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("공공체육시설");
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //String strUrl = "https://openapi.gg.go.kr/PubPhstFtM?KEY=81a4c5828ce747a4bacb0f42bc889e4e";
        String strUrl = "https://openapi.gg.go.kr/SynthesizePhysicalTraining?KEY=6a86a481d4064b91ba9a37331f566c7c";

        new DownloadWebpageTask().execute(strUrl);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256, 126.897240), 15));
        gMap.getUiSettings().setZoomControlsEnabled(true);
        /*gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                videoMark = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(
                        R.drawable.presence_video_busy)).position(latLng, 100f, 100f);
                gMap.addGroundOverlay(videoMark);
            }
        });*/
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            displayBuspos(result);
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            BufferedReader br = null;
            String page = "";
            try {
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();

                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String line = null;

                while ((line = br.readLine()) != null) {
                    page += line;
                }
                return page;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return page; // xml page 리턴
        }

        private void displayBuspos(String result) {
            String plainNo = "";
            String gpsX = "";
            String gpsY = "";
            //String road = "";
            String call = "";
            //String vehId   = "";
            boolean bSet_plainNo = false;
            boolean bSet_gpsX = false;
            boolean bSet_gpsY = false;
            //boolean bSet_road = false;
            boolean bSet_call = false;
            //boolean bSet_vehId   = false;


            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("BIZPLC_NM"))
                            bSet_plainNo = true;
                        if (tag_name.equals("REFINE_WGS84_LOGT"))
                            bSet_gpsX = true;
                        if (tag_name.equals("REFINE_WGS84_LAT"))
                            bSet_gpsY = true;
//                        if (tag_name.equals("REFINE_ROADNM_ADDR"))
//                            bSet_road = true;
                        if (tag_name.equals("LOCPLC_FACLT_TELNO"))
                            bSet_call = true;

                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet_plainNo) {
                            plainNo = xpp.getText();
                            bSet_plainNo = false;
                        }
                        if (bSet_call) {
                            call = xpp.getText();
                            bSet_call = false;
                        }
                        if (bSet_gpsX) {
                            gpsX = xpp.getText();
                            bSet_gpsX = false;
                        }
//                        if (bSet_road) {
//                            road = xpp.getText();
//                            bSet_road = false;
//                        }
                        if (bSet_gpsY) {
                            gpsY = xpp.getText();
                            bSet_gpsY = false;

                            displayMap(gpsX, gpsY, plainNo, call);
                        }


                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next(); // 다음 이벤트 찾음
                }

            } catch (Exception e) {
                ;
            }
        }

        private void displayMap(String gpsX, String gpsY, String plainNo, String call) {
            double latitude = Double.parseDouble(gpsY);
            double longitude = Double.parseDouble(gpsX);
            final LatLng LOC = new LatLng(latitude, longitude);

//            ArrayList<String> addr = new ArrayList<>();
//            ArrayList<String> phone = new ArrayList<>();

            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOC, 15));
            Marker mk = gMap.addMarker(new MarkerOptions()
                    .position(LOC)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.spot))
                    .snippet(call)
                    .title(plainNo));
            mk.showInfoWindow();

//            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//
//                    Toast.makeText(getApplicationContext(), mk.getTitle(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });

//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(LOC);
//            markerOptions.title(plainNo);
//            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
//            Marker mk = gMap.addMarker(markerOptions);
//            mk.showInfoWindow();

//            addr.add(Integer.parseInt(mk.getId()), road);
//            phone.add(Integer.parseInt(mk.getId()), call);
//
            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    //Toast.makeText(getApplicationContext(), marker.getSnippet(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
                    builder.setTitle("공공체육시설");
                    builder.setMessage(
                            "이름 : " + marker.getTitle() +
                            "\n전화번호 : " + marker.getSnippet()
                    );
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("전화걸기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri=Uri.parse("tel:"+marker.getSnippet());
                            Intent intent=new Intent(Intent.ACTION_DIAL, uri);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }

    }
}