package com.example.mycontactapp;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_number);
        editAge = findViewById(R.id.editText_age);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instanstaiated DatabaseHelper");
    }

    public void addData(View view){
        Log.d("MyContactApp", "MainActivity: adding data");
        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(),editAge.getText().toString() );

        if(isInserted == true){
            Toast.makeText(MainActivity.this, "Success - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Failed - contact not inserted", Toast.LENGTH_LONG).show();
        }

    }

    public void viewData(View view){
        Log.d("MyContactApp", "MainActivity: viewing data");
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            showMessage("Error", "No data found in database");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //append res column 0, ... to the buffer - see StringBuffer and cursor api's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone Number: " + res.getString(2)+ "\n");
            buffer.append("Age: " + res.getString(3)+ "\n");
        }
        showMessage("Data", buffer.toString());
    }

    public void searchData(View view){
        Log.d("MyContactApp","MainActivity: searching for data");
        Cursor res = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        if(editName.getText().toString().isEmpty() && editPhone.getText().toString().isEmpty() && editAge.getText().toString().isEmpty()){
            showMessage("No result found", "You contact does not seem to be in the database");
            return;
        }
        while(res.moveToNext()){
            if(editName.getText().toString().isEmpty() || editName.getText().toString().equals((res.getString(1)))&&
                    editPhone.getText().toString().isEmpty() || editPhone.getText().toString().equals((res.getString(2)))&&
                    editAge.getText().toString().isEmpty() || editAge.getText().toString().equals((res.getString(3)))){
                buffer.append("ID: " + res.getString(0) + "\n" +
                        "Name: " + res.getString(1) + "\n" + "Phone Number: " + res.getString(2) + "\n"
                        + "Age: " + res.getString(3));
            }
        }
        if(buffer.toString().isEmpty()){
            showMessage("Error","no matches were found");
            return;
        }
        showMessage("Search results", buffer.toString());
    }

    public void showMessage(String title, String message){
        Log.d("MyContactApp", "MainActivity: showing message");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
