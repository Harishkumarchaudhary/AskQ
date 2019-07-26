package com.example.dell.askq;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class logindetails extends Fragment {


    Button login;
    Button sigup;
    private FirebaseAuth mAuth;
    EditText email,password,username;
    String email_s,password_s,username_s;
    public logindetails() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_logindetails, container, false);
        mAuth = FirebaseAuth.getInstance();
        email=view.findViewById(R.id.enter_email);
        password=view.findViewById(R.id.enter_password);
        username=view.findViewById(R.id.username);
        login = view.findViewById(R.id.login);
        sigup=view.findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_s=email.getText().toString();
                password_s=password.getText().toString();
                username_s=username.getText().toString();
                mAuth.signInWithEmailAndPassword(email_s,password_s).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            home home=new home();

                          //  FragmentManager fragmentManager=home.getFragmentManager();--->>>Do not use this now somehow it do not works now
                            FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                            sending_objects(home,currentUser,username_s);
                            fragmentTransaction.addToBackStack(StringNames.stacky);
                            fragmentTransaction.replace(R.id.container,home).commit();

                            //Toast.makeText(getContext(), currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Signin failed,try again",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
            }
        });
        sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_s=email.getText().toString();
                password_s=password.getText().toString();
                username_s=username.getText().toString();
                mAuth.createUserWithEmailAndPassword(email_s,password_s).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            home home=new home();
                            //FragmentManager fragmentManager=home.getFragmentManager();
                            FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                            sending_objects(home,currentUser,username_s);
                            fragmentTransaction.addToBackStack(StringNames.stacky);
                            fragmentTransaction.replace(R.id.container,home).commit();
                           // Toast.makeText(getContext(), currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                            updatedatabase_for_user(username_s,email_s);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "SignUp failed,try again",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
            }
        });
        return view;
    }

    private void updatedatabase_for_user(String username_s,String email_s) {
     //   It is a one time thing and must be put when the user first sign up otherwise repeated entries will be made for the same user
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference();
        DatabaseReference myref2=firebaseDatabase.getReference();
        myref=myref.child("users");
        Map<String,Object> usermap=new HashMap<>();
        String uid=myref.push().getKey();
        MyUser myUser=new MyUser(username_s,email_s,uid);
        usermap.put("username",myUser.getDisplayname());
        usermap.put("email",myUser.getEmail());
        usermap.put("uid",myUser.getUid());
        myref=myref.child(uid);
        myref.updateChildren(usermap);
        String x="";
        //Firebase cannot store paths containing #, . , [ etc
        for(int i=0;i<email_s.length();i++){
            if(email_s.charAt(i)=='@'){
                break;
            }
            else{
                x=x+email_s.charAt(i);
            }
        }
        myref2.child(x).setValue(uid);
    }

    public void sending_objects(home home,FirebaseUser currentuser,String username_s){
        Bundle bundle =new Bundle();
        MyUser myUser=new MyUser(username_s,currentuser.getEmail());
        bundle.putSerializable(StringNames.user,myUser);
        home.setArguments(bundle);
    }

}
