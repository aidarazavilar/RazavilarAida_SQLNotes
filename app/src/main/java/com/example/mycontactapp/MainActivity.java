package com.example.mycontactapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.mycontactapp.DatabaseHelper.COLUMN_NAME_CONTACT;
import static com.example.mycontactapp.DatabaseHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyContactApp", "MainActivity: setting up the layout" );
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editPhoneNumber);
        editAddress = findViewById(R.id.editAddress);


        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper" );

    }

    public boolean insertData(String name){
        Log.d("MyContactApp", "MainActivity: inserting data");
        SQLiteDatabase db = myDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CONTACT, name);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            Log.d("MyContactApp", "MainActivity: Contact Insert - FAILED");
            return false;
        }
        else {
            Log.d("MyContactApp", "MainActivity: Contact Insert - PASSED");
            return true;
        }
    }

    public void addData (View view){
        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());
        if (isInserted == true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Failed - contact inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData (View view){
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewing data");
        if (res.getCount() == 0){
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //append res columns to the buffer - see StringBugger and Cursor API's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone: " + res.getString(2) + "\n");
            buffer.append("Address: " + res.getString(3) + "\n");

        }

        showMessage("Contact List", buffer.toString());
    }

    public void showMessage(String title, String message){
        Log.d("MyContactApp", "MainActivity: show message");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void searchData(View view){
      Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: searching data");

        if (res.getCount() == 0){
            showMessage("Error: No contacts found ","No data found in database" );
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            boolean nameNull = false;
            boolean phoneNull = false;
            boolean addressNull = false;

            if(editName.getText().toString().equals("")) nameNull = true;
            if(editPhone.getText().toString().equals("")) phoneNull = true;
            if(editAddress.getText().toString().equals("")) addressNull = true;



            if (((res.getString(1).equals(editName.getText().toString())) || nameNull) &&
            ((res.getString(2).equals(editPhone.getText().toString())) || phoneNull) &&
                    ((res.getString(3).equals(editAddress.getText().toString())) || addressNull)) {

                //append res columns to the buffer - see StringBugger and Cursor API's
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("Name: " + res.getString(1) + "\n");
                buffer.append("Phone: " + res.getString(2) + "\n");
                buffer.append("Address: " + res.getString(3) + "\n");

            }


            Log.d("MyContactApp", "MainActivity: the address is" + editAddress.getText().toString());

        }

        if (buffer.length() == 0){
            showMessage("No search query", "Please enter a search query");
            return;

        }

        showMessage("Data", buffer.toString());
    }
}


