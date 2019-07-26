package com.example.dell.askq;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
public class Answer_for_question_fragy extends Fragment {

    Button button;
    EditText editText;
    public Answer_for_question_fragy() {
        // Required empty public constructor
    }
    String user_email_actual,uid;
    Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_answer_for_question_fragy, container, false);
        Bundle bundle=getArguments();
        final AnswerClass answerClass= (AnswerClass) bundle.getSerializable(StringNames.q_info);
        button=view.findViewById(R.id.submit_answer);
        editText=view.findViewById(R.id.answer_of_the_question);
        //Actually the one who is using the app is the person that will answer here .So, get uid from email again
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=mAuth.getCurrentUser();
        String email=currentuser.getEmail();
        user_email_actual="";
        uid="";
        for(int kp=0;kp<email.length();kp++){
            if(email.charAt(kp)=='@'){
                break;
            }
            else{
                user_email_actual=user_email_actual+email.charAt(kp);
            }
        }
      button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              final String answer=editText.getText().toString();
              FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
              final DatabaseReference myref=firebaseDatabase.getReference();
              myref.child(user_email_actual).addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      uid=dataSnapshot.getValue().toString();
                      String qid=answerClass.getQid();
                      FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                      DatabaseReference myref2=firebaseDatabase.getReference();
                      myref2.child("users").child(uid).child("questions_answered").push().setValue(qid);

                      Map<String,Object> mymap=new HashMap<>();
                      mymap.put("the_answerer",uid);
                      mymap.put("answer",answer);
                      myref2.child("questions").child(qid).child("answers").push().updateChildren(mymap);
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });
          }
      });
        return view;
    }

}
