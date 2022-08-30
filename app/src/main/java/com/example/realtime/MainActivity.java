package com.example.realtime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText name, father, address, email, mobile;
    Button save, fetch;
    FirebaseDatabase mdatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        father = findViewById(R.id.father);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        save = findViewById(R.id.save);
        fetch = findViewById(R.id.fetch);

        mdatabase = FirebaseDatabase.getInstance();
        databaseReference = mdatabase.getReference();

//        Uri uri = getIntent().getData();
//        if (uri != null)
//        {
//            List<String> param = uri.getPathSegments();
//            String id = param.get(param.size() - 1);
//            Toast.makeText(this, "id "+id, Toast.LENGTH_SHORT).show();
//        }
        getDynamicLinkFromFirebase();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertData();

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FetchRecord.class));
            }
        });



    }
    boolean validateMobile(String mobile)
    {
        Pattern p = Pattern.compile("[6-9][0-9]{9}");
        Matcher m =p.matcher(mobile);
        return m.matches();
    }

    public void insertData()
    {
            /*  HashMap<String,Object> map = new HashMap<>();
                map.put("Name",name.getText().toString());
                map.put("Father",father.getText().toString());
                map.put("Address",address.getText().toString());
                map.put("Email",email.getText().toString());
                map.put("Mobile",mobile.getText().toString());

                name.setText("");
                father.setText("");
                address.setText("");
                email.setText("");
                mobile.setText("");
                FirebaseDatabase.getInstance().getReference().child("Post").push()
                        .setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.i("kj","OnComplete");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("dd","onFailure"+e.toString());
                            }
                        });*/

        String Name = name.getText().toString();
        String Father = father.getText().toString();
        String Address = address.getText().toString();
        String Email = email.getText().toString();
        String Mobile = mobile.getText().toString();
        if (TextUtils.isEmpty(Name) ) {
            name.setError("Please Enter Your Name");
        }
        else  if (TextUtils.isEmpty(Father) ) {
            father.setError("Please Enter Your Father Name");
        }
        else  if (TextUtils.isEmpty(Address) ) {
            address.setError("Please Enter Your Address");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Email Address is not validate");

        }
        else if (!validateMobile(mobile.getText().toString()))

        {
            validateMobile(Mobile);
            mobile.setError("Invalid Mobile Number");
        }

        else

        {
//                    String userId = databaseReference.push().getKey();
            model ob = new model();
            ob.setName(Name);
            ob.setFather(Father);
            ob.setAddress(Address);
            ob.setEmail(Email);
            ob.setMobile(Mobile);
            ob.setKey("");

            name.setText("");
            father.setText("");
            address.setText("");
            email.setText("");
            mobile.setText("");
            databaseReference.child("Detail").push().setValue(ob, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
        });
    }

    private void getDynamicLinkFromFirebase() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Log.i("MainActivity","We have a dynamic link");
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if(deepLink!=null)
                        {
                            Log.i("MainActivity","Here the Dynamic link \n"+deepLink.toString());

//                            String email = deepLink.getQueryParameter("email");
//                            String password = deepLink.getQueryParameter("password");


                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}