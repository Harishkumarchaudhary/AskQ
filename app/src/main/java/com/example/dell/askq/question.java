package com.example.dell.askq;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class question extends Fragment {


    public question() {
        // Required empty public constructor
    }

    EditText editText;
    FloatingActionButton floatingActionButton;
    Spinner spinner;
    String user_email;
    String user_email_actual;
    String item;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        Bundle bundle = getArguments();
        final MyUser myUser = (MyUser) bundle.getSerializable(StringNames.user);
        user_email=myUser.getEmail();
        user_email_actual="";
        uid="";
        for(int kp=0;kp<user_email.length();kp++){
            if(user_email.charAt(kp)=='@'){
                break;
            }
            else{
                user_email_actual=user_email_actual+user_email.charAt(kp);
            }
        }
        item=null;
        editText = view.findViewById(R.id.question);
        spinner=view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        floatingActionButton = view.findViewById(R.id.fab_questionadded);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editText.getText().toString();
                if (question.isEmpty()) {
                    Toast.makeText(getContext(), "Question not added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "We are here", Toast.LENGTH_SHORT).show();
                    if(item.isEmpty()){
                        Toast.makeText(getContext(), "Select a category", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        getUidandUpdate(question, item);
                    }
                }
            }
        });

        return view;
    }

    public void getUidandUpdate(final String question, final String question_category) {
             final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
             DatabaseReference myref = firebaseDatabase.getReference();
             myref=myref.child(user_email_actual);
             myref.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Toast.makeText(getContext(), dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                     uid=dataSnapshot.getValue().toString();
                     FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                     DatabaseReference myref = firebaseDatabase.getReference();
                     DatabaseReference myref2=firebaseDatabase.getReference();
                     DatabaseReference myref3=firebaseDatabase.getReference();
                     myref = myref.child("questions");
                     Map<String, Object> mymap = new HashMap<>();
                     mymap.put("question text", question);
                     mymap.put("asked by", uid);
                     String key=myref.push().getKey();
                    mymap.put("qid",key);

                     myref.child(key).updateChildren(mymap);
                     //Updating same time in users
                     myref2.child("users").child(uid).child("questions_asked").push().setValue(key);
                     //Updating in categories at same time
                     myref3.child("categories").child(question_category).push().setValue(key);
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {
                     Toast.makeText(getContext(), "OOPS,WHILE LOOP GONE WRONG", Toast.LENGTH_SHORT).show();
                 }
             });
    }

}
