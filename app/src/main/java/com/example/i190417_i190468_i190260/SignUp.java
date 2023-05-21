package com.example.i190417_i190468_i190260;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import java.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.i190417_i190468_i190260.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SignUp extends AppCompatActivity {
    TextView sign_in;
    Button sign_up;
    ImageView profilePic;
    EditText name, email, pass, confirm_pass;
    CheckBox checkbox;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    Uri imageURI;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_up=findViewById(R.id.sign_up);
        sign_in=findViewById(R.id.sign_in);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confirm_pass = findViewById(R.id.confirm_pass);
        checkbox = findViewById(R.id.checkbox);
        profilePic = findViewById(R.id.profilePic);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Please wait while we create your account");

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 80);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                SignUp.this.finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkbox.isChecked()){
                    Toast.makeText(SignUp.this, "Please accept terms and conditions", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }
                String name1 = name.getText().toString();
                String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();
                String confirm_pass1 = confirm_pass.getText().toString();
                if (name1.equals("") || email1.equals("") || pass1.equals("") || confirm_pass1.equals("")){
                    Toast.makeText(SignUp.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    MyDBHelper helper = new MyDBHelper(SignUp.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(MyContract.Users._NAME, name1);
                    cv.put(MyContract.Users._EMAIL, email1);
                    cv.put(MyContract.Users._PASS, pass1);
                    db.insert(MyContract.Users.TABLE_NAME, null, cv);
                    helper.close();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                progressDialog.show();
                Bitmap bmp = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();
                final String image1 = Base64.getEncoder().encodeToString(byteArray);






                mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String score = "0";
                        Users user = new Users(name1, email1, pass1);
                        firebaseDatabase.getReference().child("Scores").child(mAuth.getCurrentUser().getUid()).child("Score").setValue(score);
                        firebaseDatabase.getReference().child("Scores").child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name1);
                        firebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = new FormBody.Builder().add("email", email1).add("name", name1).add("image", image1).build();
                        Request request = new Request.Builder().url("http://10.0.2.2:5000/postImage").post(body).build();
                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                                if (response.isSuccessful()) {
                                    String resp = response.body().string();
                                    if (resp.contains("success")) {
                                        Log.d("Response", resp);
                                        progressDialog.dismiss();
                                        response.close();
                                        Intent intent = new Intent(SignUp.this, SignIn.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Log.d("Response", resp);
                                        progressDialog.dismiss();
                                        response.close();
                                        runOnUiThread(() -> Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_LONG).show());
                                    }
                                }
                            }
                        });
                    }else {
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 80 && resultCode == RESULT_OK) {
            profilePic.setImageURI(data.getData());
            imageURI = data.getData();
        }
    }
}