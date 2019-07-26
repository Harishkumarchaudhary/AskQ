package com.example.dell.askq;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Answer_click_fragy extends Fragment {


    public Answer_click_fragy() {
        // Required empty public constructor
    }

    AdapterForAnswerClass adapterForAnswerClass;
    ArrayList<AnswerClass> answerClassArrayList;
    ArrayList<String> qids;
    ArrayList<String> questions;
    ArrayList<String> uids;
    ArrayList<String> usernames;
    RecyclerView answerrecyclerview;
    Spinner spinner2;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_answer_click_fragy, container, false);
        spinner2=view.findViewById(R.id.spinner2);
        answerrecyclerview=view.findViewById(R.id.recyclerview);
        progressBar=view.findViewById(R.id.answer_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        //Fetching work
        //The spinner onItemSelected gets automatically fired without clicking or anything ->i just used this fact to fetch education
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("how","how");
                progressBar.setVisibility(View.VISIBLE);
                qids=new ArrayList<>();
                uids=new ArrayList<>();
                questions=new ArrayList<>();
                usernames=new ArrayList<>();
                answerClassArrayList=new ArrayList<>();
                adapterForAnswerClass=new AdapterForAnswerClass(getContext(),answerClassArrayList);
                answerrecyclerview.setNestedScrollingEnabled(false);
                answerrecyclerview.setAdapter(adapterForAnswerClass);
                answerrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                answerrecyclerview.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                String category= (String) parent.getItemAtPosition(position);
                fetching_work_from_firebase(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("nothing","nothing");
            }
        });
        return view;
    }
    public void fetching_work_from_firebase(String category){
        //Firebase Reference
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference();
        myref = myref.child("categories").child(category);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    //That means no question has been added to this category till yet, so we won't run a loop
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "No question have been added under this category", Toast.LENGTH_LONG).show();
                }
                else {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        qids.add(dataSnapshot1.getValue().toString());
                        Log.d("L1", dataSnapshot1.getValue().toString());
                    }
                    doit(firebaseDatabase);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doit(final FirebaseDatabase firebaseDatabase) {
        Log.d("L2",qids.size()+"");
        for(int i=0;i<qids.size();i++){
            DatabaseReference myref2 = firebaseDatabase.getReference();
            myref2=myref2.child("questions").child(qids.get(i));
            myref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //No need of iterating on children because each time the for loop executes i am on the child i want to be
                    QuestionClass questionClass=dataSnapshot.getValue(QuestionClass.class);
                    uids.add(questionClass.getUid());
                    Log.d("id",questionClass.getUid());
                    questions.add(questionClass.getQuestion_text());
                    Log.d("texty",questionClass.getQuestion_text());
                    DatabaseReference myref3 = firebaseDatabase.getReference();
                    myref3.child("users").child(questionClass.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            MyUser myUser1=dataSnapshot.getValue(MyUser.class);
                            usernames.add(myUser1.getDisplayname());
                            Log.d("username",myUser1.getDisplayname());
                            if(usernames.size()==qids.size()){
                                Log.d("hey","howdy");
                                for(int k=0;k<usernames.size();k++){
                                    answerClassArrayList.add(new AnswerClass(questions.get(k),usernames.get(k),qids.get(k),uids.get(k)));
                                }
                                adapterForAnswerClass.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
