package com.example.i190417_i190468_i190260;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

public class SignIn extends AppCompatActivity {
    TextView sign_up;
    Button sign_in;
    EditText email, pass;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_up=findViewById(R.id.sign_up);
        sign_in=findViewById(R.id.sign_in);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please wait while we log you in");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SignIn.this.finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();
                if (email1.equals("") || pass1.equals("")){
                    Toast.makeText(SignIn.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    MyDBHelper myDBHelper = new MyDBHelper(SignIn.this);
                    SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
                    String[] projection = {MyContract.Users._ID, MyContract.Users._EMAIL, MyContract.Users._PASS, MyContract.Users._NAME};
                    Cursor cursor = sqLiteDatabase.query(MyContract.Users.TABLE_NAME, projection, null, null, null, null, null);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(SignIn.this, "No user found", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        return;
                    }
                    while (cursor.moveToNext()) {
                        String email2 = cursor.getString(1);
                        String pass2 = cursor.getString(2);
                        if (email1.equals(email2) && pass1.equals(pass2)) {
                            String name = cursor.getString(3);
                            Intent intent = new Intent(SignIn.this, ExerciseMainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("name", name);
                            startActivity(intent);
                            SignIn.this.finish();
                            progressDialog.dismiss();
                            return;
                        }
                    }
                }
                else {
                    mAuth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                String deviceIDStr = OneSignal.getDeviceState().getUserId();
                                firebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("deviceID").setValue(deviceIDStr);

                                Intent intent = new Intent(SignIn.this, ExerciseMainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                SignIn.this.finish();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }


}