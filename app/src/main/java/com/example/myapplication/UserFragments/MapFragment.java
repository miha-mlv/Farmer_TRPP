package com.example.myapplication.UserFragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.myapplication.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.*;;

public class MapFragment extends Fragment {
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private Double current_latitude, current_longitude;
    private Double point_latitude = 0.0, point_longitude = 0.0;
    private ImageButton btnMapBack;
    private Boolean flag;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map, container, false);
        btnMapBack = view.findViewById(R.id.btnMapBack);
        Configuration.getInstance().load(getActivity().getApplicationContext(),getActivity().getSharedPreferences("osmdroid", MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue("user-agent-string");
        mapView = view.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        flag = getArguments().getBoolean("flag");

        if(flag)
        {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                initializeMap();
                point_latitude = getArguments().getDouble("latitude");
                point_longitude = getArguments().getDouble("longitude");
                setMarker(55.731414, 37.633587);
                setMarker(point_latitude, point_longitude);
                buildRoad();
            }
        }else {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                initializeMap();
            }
        }

        btnMapBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void buildRoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RoadManager roadManager = new OSRMRoadManager(getActivity().getApplicationContext(), Configuration.getInstance().getUserAgentValue());
                    GeoPoint endPoint = new GeoPoint(point_latitude, point_longitude);
                    GeoPoint startPoint = new GeoPoint(55.731414, 37.633587);

                    ArrayList<GeoPoint> arrayGeoPoint = new ArrayList<>();
                    arrayGeoPoint.add(startPoint);
                    arrayGeoPoint.add(endPoint);

                    Road road = roadManager.getRoad(arrayGeoPoint);
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

                    // Обновление карты должно быть выполнено в основном UI потоке
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mapView.getOverlays().add(roadOverlay);
                            mapView.invalidate();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setMarker(Double point_latitude, Double point_longitude)
    {
        Marker marker = new Marker(mapView);
        GeoPoint geoPoint = new GeoPoint(point_latitude, point_longitude);
        marker.setPosition(geoPoint);
        marker.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.mappointmax));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                return true;
            }
        });
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void initializeMap() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mapView);
        myLocationOverlay.enableMyLocation();
        IGeoPoint myLocation = myLocationOverlay.getMyLocation();

        if (myLocation != null) {
            mapView.getController().animateTo(myLocation);
            mapView.getController().setZoom(18.0);
            current_latitude = myLocation.getLatitude();
            current_longitude = myLocation.getLongitude();
        }
        mapView.getOverlays().add(myLocationOverlay);
    }


}