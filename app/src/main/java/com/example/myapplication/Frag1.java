package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class Frag1 extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<ContactItem> lstContact;

//    private OnFragmentInteractionListener mListener;

    public Frag1() {
    }

    private int PERMISSION_CODE = 100;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_frag1,container, false);

        myrecyclerview = (RecyclerView) v.findViewById(R.id.contact_recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getContext(), lstContact);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);


        ArrayList<ContactItem> contactItems = getContractList();
        JSONObject json = JSON.getJsonObject((contactItems));

        return v;
    }


    public ArrayList<ContactItem> getContractList(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,selectionArgs,sortOrder);

        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();

        if(cursor.moveToFirst()){        //hashlist 작성문
            do{
                ContactItem contactItem = new ContactItem(cursor.getString(0), cursor.getString(1));
                contactItem.setUser_phNumber(cursor.getString(0));
                contactItem.setUser_Name(cursor.getString(1));
                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);

        return contactItems;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ArrayList<ContactItem> array = getContractList();
        int len = array.size();

        lstContact = new ArrayList<>();


        for(int i=0; i<len; i++) {
            ContactItem tmp = array.get(i);

            lstContact.add(new ContactItem(tmp.getUser_Name(), tmp.getUser_phNumber()));
        }
    }
}
    /*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObject json = getJsonObject(getContractList());
        lstContact = new ArrayList<>();
        int i =0;
        while(i<10) {
            try {
                String name = json.getString("User_name");
                lstContact.add(new ContactItem(name, "(111) 251236211"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i+=1;

        }
    }

}*/

