package com.example.dell.askq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {

  /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            home home=new home();
            //  FragmentManager fragmentManager=home.getFragmentManager();--->>>Do not use this now somehow it do not works now
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            sending_objects(home,currentUser);
            fragmentTransaction.addToBackStack(StringNames.stacky);
            fragmentTransaction.replace(R.id.container,home).commit();
        }
       //--->>>>>> updateUI(currentUser);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //This place is much more suitable for adding fields. in firebase.
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference();
        //Try not to use setValue() method ...It's too risky.
        myref.child("users");
        myref.child("questions");
        myref.child("categories");
        logindetails logindetails=new logindetails();
        //FragmentManager fragmentManager=logindetails.getFragmentManager();--->>>>This do not works now
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(StringNames.stacky);
        fragmentTransaction.replace(R.id.container,logindetails).commit();

    }
    /*public void sending_objects(home home,FirebaseUser currentuser){
        Bundle bundle =new Bundle();
        MyUser myUser=new MyUser(currentuser.getDisplayName(),currentuser.getEmail());
        bundle.putSerializable(StringNames.user,myUser);
        home.setArguments(bundle);
    }*/
}