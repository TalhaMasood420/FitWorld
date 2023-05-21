package com.example.i190417_i190468_i190260;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.i190417_i190468_i190260.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button sign_in , sign_up;
    FirebaseDatabase database;
    List<Users> usersList;
    List<Users> localList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_up=findViewById(R.id.sign_up);
        sign_in=findViewById(R.id.sign_in);
        database = FirebaseDatabase.getInstance();
        usersList = new ArrayList<>();
        localList = new ArrayList<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null){
            MyDBHelper myDBHelper = new MyDBHelper(MainActivity.this);
            SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
            String[] projection = {MyContract.Users._ID, MyContract.Users._EMAIL, MyContract.Users._PASS, MyContract.Users._NAME};
            Cursor cursor = sqLiteDatabase.query(MyContract.Users.TABLE_NAME, projection, null, null, null, null, null);
            database.getReference().child("Users").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        Users user = snapshot.getValue(Users.class);
                        usersList.add(user);
                    }
                }
            });
            while (cursor.moveToNext()){
                String email = cursor.getString(cursor.getColumnIndexOrThrow(MyContract.Users._EMAIL));
                String pass = cursor.getString(cursor.getColumnIndexOrThrow(MyContract.Users._PASS));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MyContract.Users._NAME));
                Users tempUser = new Users(name, email, pass);
                if (!usersList.contains(tempUser)){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            String score = "0";
                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            database.getReference().child("Users").child(userid).setValue(tempUser);
                            database.getReference().child("Scores").child(userid).child("Score").setValue(score);
                            database.getReference().child("Scores").child(userid).child("Name").setValue(name);
                        }
                    });
                }
                localList.add(tempUser);
            }

            for (Users user : usersList){
                if (!localList.contains(user)){
                    ContentValues values = new ContentValues();
                    values.put(MyContract.Users._EMAIL, user.getEmail());
                    values.put(MyContract.Users._PASS, user.getPassword());
                    values.put(MyContract.Users._NAME, user.getName());
                    sqLiteDatabase.insert(MyContract.Users.TABLE_NAME, null, values);
                }
            }
        }

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignIn.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
    }
}