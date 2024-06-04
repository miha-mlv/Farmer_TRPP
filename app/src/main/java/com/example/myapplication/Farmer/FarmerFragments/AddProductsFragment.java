package com.example.myapplication.Farmer.FarmerFragments;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Model.Products;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;

public class AddProductsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    String categoryProduct = "";
    private Spinner spinner;
    EditText etNameProducts, etDescriptionProduct, etPriceProduct;
    Button btnAddProduct;
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private String latitude, longitude;
    private Double latitude_for_map, longitude_for_map;
    String phone;
    private ProgressDialog loadingBar;
    private ImageButton btnAddImageProduct;
    private Uri imageUri;
    private static final int GALLERYPICK = 1;
    private String downloadImageUrl;
    private StorageReference productImageRef;

    public AddProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_products, container, false);
        phone = getArguments().getString("phone");
        // конфига карты
        Configuration.getInstance().load(getActivity().getApplicationContext(), getActivity().getSharedPreferences("osmdroid", MODE_PRIVATE));
        //для рисования карты задаем юзера
        Configuration.getInstance().setUserAgentValue("user-agent-string");
        mapView = view.findViewById(R.id.map);

        mapView.setTileSource(TileSourceFactory.MAPNIK);  // источник плиток
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        loadingBar = new ProgressDialog(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initializeMap();
        }

        etNameProducts = view.findViewById(R.id.etNameProducts);
        etDescriptionProduct = view.findViewById(R.id.etDescriptionProduct);
        etPriceProduct = view.findViewById(R.id.etPriceProduct);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        spinner = view.findViewById(R.id.spinnerCategory);
        btnAddImageProduct = view.findViewById(R.id.addImageProduct);

        String[] categoryProducts = getResources().getStringArray(R.array.category_products);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryProducts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Overlay touchOverlay = new Overlay(getActivity()) {
            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;

            @Override
            public void draw(Canvas arg0, MapView arg1, boolean arg2) {
            }

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

                final Drawable marker = getActivity().getApplicationContext().getResources().getDrawable(R.drawable.mappointmax);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
                longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
                latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);
                Parcelable parcelable;
                Toast.makeText(getActivity(), "долгота: " + longitude + "; широта: " + latitude, Toast.LENGTH_LONG).show();
                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000)
                        , (((double) loc.getLongitudeE6()) / 1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);
                if (anotherItemizedIconOverlay == null) {
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity().getApplicationContext()
                            , overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                } else {
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity().getApplicationContext()
                            , overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                return true;
            }
        };
        mapView.getOverlays().add(touchOverlay);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNameProducts.getText().toString().equals("")
                        || etDescriptionProduct.getText().toString().equals("")
                        || etPriceProduct.getText().toString().equals("")
                        || latitude.equals("")) {
                    Toast.makeText(getActivity(), "Заполните форму", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Товар размещается");
                    loadingBar.setMessage("Пожалуйста подождите");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    Products newProduct = new Products(etNameProducts.getText().toString(),
                            categoryProduct, etDescriptionProduct.getText().toString(),
                            etPriceProduct.getText().toString(),
                            latitude, longitude);
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("name", etNameProducts.getText().toString());
                    productMap.put("category", categoryProduct);
                    productMap.put("description", etDescriptionProduct.getText().toString());
                    productMap.put("price", etPriceProduct.getText().toString());
                    productMap.put("latitude", latitude);
                    productMap.put("longitude", longitude);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins").child(phone);
                    ref.child("Products").child(etNameProducts.getText().toString())
                            .updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        etDescriptionProduct.getText().clear();
                                        etNameProducts.getText().clear();
                                        etPriceProduct.getText().clear();
                                        spinner.setSelection(0);
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Товар успешно размещен", Toast.LENGTH_SHORT).show();
                                    } else {
                                        etDescriptionProduct.getText().clear();
                                        etNameProducts.getText().clear();
                                        etPriceProduct.getText().clear();
                                        spinner.setSelection(0);
                                        loadingBar.dismiss();
                                        Toast.makeText(getActivity(), "Заполните форму повторно", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnAddImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinnerCategory)
        {
            categoryProduct = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void initializeMap() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity()), mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(() -> new Thread(() -> {
            IGeoPoint myLocation = myLocationOverlay.getMyLocation();
            if (myLocation != null) {
                mapView.getController().animateTo(myLocation);
                mapView.getController().setZoom(18.0);
                latitude_for_map = myLocation.getLatitude();
                longitude_for_map = myLocation.getLongitude();
            }
        }));
        mapView.getOverlays().add(myLocationOverlay);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        }
    }
}