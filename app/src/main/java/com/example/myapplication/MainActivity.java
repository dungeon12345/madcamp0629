package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||                // 권한이 없는게 있다면
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                    }, 1052);

        }

        else{                     // 모든 권한이 있다면
            tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
            viewPager = (ViewPager) findViewById(R.id.viewpager_id);

            adapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);

            // Add Fragment Here

            adapter.AddFragment(new Frag1(), "Contacts");
            adapter.AddFragment(new Frag2(), "Voice");
            adapter.AddFragment(new com.example.myapplication.Frag3(), "Gallery");

            viewPager.setAdapter(adapter);add .
            viewPager.setOffscreenPageLimit(2);
            tabLayout.setupWithViewPager(viewPager);

        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1052: {
                // If request is cancelled, the result
                // arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED
                ) {

                    tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
                    viewPager = (ViewPager) findViewById(R.id.viewpager_id);

                    adapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);

                    // Add Fragment Here

                    adapter.AddFragment(new Frag1(), "Contacts");
                    adapter.AddFragment(new Frag2(), "Gallery");
                    adapter.AddFragment(new com.example.myapplication.Frag3(), "Voice");

                    viewPager.setAdapter(adapter);
                    viewPager.setOffscreenPageLimit(2);
                    tabLayout.setupWithViewPager(viewPager);

                } else {
                    // Permission denied - Show a message
                    // to inform the user that this app only works
                    // with these permissions granted
                }

            }

        }

    }
}