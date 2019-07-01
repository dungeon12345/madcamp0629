package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactViewAdapter extends AppCompatActivity {


    public ContactViewAdapter() {

    }

    private Toolbar toolbar1;
    private ImageView imageView4;
    private TextView textView2, textView3, editText, editText3;

    private String num;
    private String name;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);


        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        num = intent.getExtras().getString("num");

        initContactViews();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    public void initContactViews() {
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        editText = (TextView) findViewById(R.id.editText);
        editText3 = (TextView) findViewById(R.id.editText3);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        Button phone = (Button) findViewById(R.id.phone);

        editText.setText(name);
        editText3.setText(num);
        imageView4.setImageResource(R.drawable.akaist);

        toolbar1.setTitle("Contacts");
        toolbar1.setTitleTextColor(getResources().getColor(android.R.color.black));
        //setHasOptionsMenu(true);
        toolbar1.inflateMenu(R.menu.contact_detail_menu);
        toolbar1.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar1);

        /** enabling back button ***/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.item_list) {
                    // do something
                    gotoContactListActivity();
                } else {
                    // do something
                }

                return false;
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                try {
                    String mNum = num;
                    String tel = "tel" + num;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);}
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void gotoContactListActivity() {
        Intent intent = new Intent(this, Frag1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}


