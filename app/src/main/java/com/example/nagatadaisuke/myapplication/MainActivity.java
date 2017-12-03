package br.android.cericatto.phonecallback;

/**
 * Created by nagatadaisuke on 2017/11/25.
 */

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


class MainActivity extends AppCompatActivity {

    public static int OVERLAY_PERMISSION_REQ_CODE = 1000;
    static String phoneNumber;
    static String simCountryIso;
    //ListViewの設定seltutei
    private ListView lstNames;
    ContentResolver mcResolver = null;

    //パーミッションの設定
    private  static  final  int PREMISSIONS_REQUEST_READ_CONTASTS = 100;


    //--------------------------------------------------
    // Constants
    //--------------------------------------------------

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE};
    private static final int PERMISSION_REQUEST = 100;
    private ListView listView;


    //--------------------------------------------------
    // Activity Life Cycle
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LIstViewの設定
        this.listView = (ListView) findViewById(R.id.listView);

        // API 23 以上であればPermission chekを行う
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        Intent intent = new Intent(getApplication(), MainActivity.class);
        startService(intent);


    }

    //Andorid6パーミッションの設定
    private  void  showCOntants()
    {
        //chehkパーミッション
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_CONTACTS}, PREMISSIONS_REQUEST_READ_CONTASTS);

        }else{
            //Android6.0の対応
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_expandable_list_item_1,contacts);
            listView.setAdapter(adapter);
        }
    }

    private  List<String>  getContactNames()
    {
        List<String> contacts = new ArrayList<>();
        Cursor cursor;

        //Get the CobtevtResolver
        ContentResolver cr = getContentResolver();

        //Get the Currsor of all the contacts
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        //Move the cursor to first
        while (cursor.moveToNext()) {

                //Get thecontacts name
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(id);
        }
        //Close the curosor
        cursor.close();
        return contacts;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission() {

        requestPermissions(PERMISSIONS, PERMISSION_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PREMISSIONS_REQUEST_READ_CONTASTS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
                showCOntants();
            } else {
                Toast.makeText(this,"permission is Name",Toast.LENGTH_SHORT).show();
            }
        }
    }
}