package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.SortedList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telecom.Call;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private TextView weather1,latitude,longitude,adress,time,weather;
    private EditText et1,et2,et3,et4,et5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION );
        }else {
            getLoationAndWeather();
        }

    }
    private void getLoationAndWeather(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && 
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, this::onSuccess);
        
    }

    private void fetchWeatherData(double latitude, double longitude) {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call call = service.getCurrentWeather(latitude,longitude,"8b6bfa710ba6b7d95b02657cb1174696");
        call.equals(new Callback<WeatherRes>(){

            public void onResponse(SortedList.Callback<WeatherRes> call, Response<WeatherRes> response) throws InterruptedException {

                if (!response.setResult()) {
                    return;
                } else {
                    WeatherRes Weather = response.wait();
                    TextView et5 = findViewById(R.id.et5);
                    et5.setText(String.format("Temperature: %.2f°C, Humidity: %d%%, Description: %s" ,Weather.getMain().getTemp() - 273.15 , Weather.getMain().getHumidity(), Weather.getWeather().get(0).getDescription()));

                }


            }
            public void onFailure(Call call1, Throwable T ){
                T.printStackTrace();
                Toast.makeText(MainActivity.this, "failed to retrieve meteorological information", Toast.LENGTH_SHORT).show();
            }
        });
        


    }
    public void onRequestPremissionResult(int requestcode , String[] permission,  int[] grantResults){
        super.onRequestPermissionsResult(requestcode,permission,grantResults);
        if (requestcode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.length > 0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                getLoationAndWeather();
            }
            else {
                Toast.makeText(this, "Location authorization is refused", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void onSuccess(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = latitude.getLongitude();
            TextView et1 = findViewById(R.id.et01);
            et1.setText(String.format("Latitude: %.6f , Longitude: %.6f ", latitude, longitude));

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                TextView et3 = findViewById(R.id.et3);
                et3.setText(address);

            } catch (IOException E) {
                E.printStackTrace();
            }
            long currentTime = System.currentTimeMillis();
            SimpleDateFormat SDF = new SimpleDateFormat("DD-MM-YYYY HH:mm:ss", Locale.getDefault());
            String formattedTime = SDF.format(new Date(currentTime));
            TextView et4 = findViewById(R.id.et4);
            et4.setText(String.format("Current System Time : %s", formattedTime));
            fetchWeatherData(latitude, longitude);

        }


    }
}