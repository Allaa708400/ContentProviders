package com.example.contentproviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DirectAction;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.contentproviders.databinding.ActivityMainBinding;
import com.example.contentproviders.databinding.CustomContactItemBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final int REQ_PERMISSION_RED_CONTACT = 1;
    ActivityMainBinding binding;
    ArrayList<ContactItem> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
               != PackageManager.PERMISSION_GRANTED){


           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.
                   READ_CONTACTS}, REQ_PERMISSION_RED_CONTACT);

       }
       else {

           new MyTask().execute();



       }
    }

    @SuppressLint("Range")
    private void  readContacts() {

        contacts = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

       Cursor cursor = getContentResolver().query(uri, null, null, null,
               ContactsContract.Contacts.DISPLAY_NAME+" ASC");

        if (cursor.moveToFirst()) {

            do {

                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex("_ID"));
                Uri cUri = ContactsContract.Data.CONTENT_URI;



                Cursor contactCursor = getContentResolver().query(cUri, null,
                        ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(id)}, null);

                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String photoPath = String.valueOf(R.drawable.baseline_account_circle_24);
                byte[] photoByte = null;
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";
                String contactNumbers = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";


                if (contactCursor.moveToFirst()) {

                    displayName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract
                           .Contacts.DISPLAY_NAME));

                    do {

                        if (contactCursor.getString(contactCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {

                            nickName = contactCursor.getString(contactCursor.getColumnIndex("data1"));
                            contactOtherDetails += nickName + "\n";

                        }


                        if (contactCursor.getString(contactCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            switch (contactCursor.getInt(contactCursor.getColumnIndex("data2"))) {

                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = contactCursor.getString(contactCursor.getColumnIndex("data1"));

                                    contactNumbers += "Home Phone : " + homePhone + " \n";

                                    break;


                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = contactCursor.getString(contactCursor.getColumnIndex("data1"));

                                    contactNumbers += "Work Phone : " + workPhone + " \n";

                                    break;


                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = contactCursor.getString(contactCursor.getColumnIndex("data1"));



                                    contactNumbers += "Mobile Phone : " + mobilePhone + " \n";

                                    break;


                            }
                        }


                        if (contactCursor.getString(contactCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            switch (contactCursor.getInt(contactCursor.getColumnIndex("data2"))) {

                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = contactCursor.getString(contactCursor.getColumnIndex("data1"));

                                    contactEmailAddresses += "Home Email : " + homeEmail + " \n";

                                    break;


                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = contactCursor.getString(contactCursor.getColumnIndex("data1"));

                                    contactEmailAddresses += "Work Email : " + workEmail + " \n";

                                    break;

                            }
                        }

                        if (contactCursor.getString(contactCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {


                            companyName = contactCursor.getString(contactCursor.getColumnIndex("data1"));

                            contactOtherDetails += "Company Name : " + companyName + " \n";


                            title = contactCursor.getString(contactCursor.getColumnIndex("data4"));

                            contactOtherDetails += "Title : " + title + " \n";


                        }
                        CustomContactItemBinding binding = null;

                        if (contactCursor.getString(contactCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {


                            photoByte = contactCursor.getBlob(contactCursor.getColumnIndex("data15"));
                            Log.d("Z", "p1");
                            if (photoByte !=null){

                                Bitmap bitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

                                File cacheDirectory = getBaseContext().getCacheDir();

                                File  tmp = new File(cacheDirectory.getPath()
                                + "/_androhub" + id + ".png");


                                try{

                                    FileOutputStream fileOutputStream = new FileOutputStream(tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();


                                }catch (Exception e){

                                    e.printStackTrace();

                                }

                                photoPath = tmp.getPath();

                            }
                        }

                    } while (contactCursor.moveToNext());

                    contacts.add(new ContactItem(Long.toString(id), displayName, contactNumbers
                            , contactEmailAddresses, photoPath, contactOtherDetails));

                }



            } while (cursor.moveToNext());

        }

    }


    class MyTask extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            readContacts();

         return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            binding.pb.setVisibility(View.GONE);

            populateDataIntRecyclerView(contacts);

        }
    }





    private void populateDataIntRecyclerView(ArrayList<ContactItem> items){

        ContactsAdapter adapter = new ContactsAdapter(items);

        binding.nameRv.setAdapter(adapter);

        binding.nameRv.setLayoutManager(new LinearLayoutManager(this));

        binding.nameRv.setHasFixedSize(true);




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_PERMISSION_RED_CONTACT && grantResults.length > 0){

            new MyTask().execute();


        }
    }
}