package com.example.dell.askq;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    ArrayList<String> qids;
    ArrayList<String> questions;
    ArrayList<QuestionClass> questionClassArrayList;
    ArrayList<String> uids;
    ArrayList<String> usernames;
    ArrayList<String> answers;
    Adapter_for_home adapter_for_home;
    TextView answer;
    public home() {
        // Required empty public constructor
    }
     FloatingActionButton floatingActionButton;
     Spinner home_spinner;
     RecyclerView homerecyclerView;
     ProgressBar progressy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle=getArguments();
        final MyUser myUser= (MyUser) bundle.getSerializable(StringNames.user);
        Toast.makeText(getContext(), "Tap on question to see all the answers", Toast.LENGTH_SHORT).show();
        //Show the questions having answers in that category to user
        home_spinner=view.findViewById(R.id.home_spinner);
        homerecyclerView=view.findViewById(R.id.home_recycler_view);
        progressy=view.findViewById(R.id.home_progressy);
        progressy.setVisibility(View.VISIBLE);
        home_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category= (String) parent.getItemAtPosition(position);
                qids=new ArrayList<>();
                questionClassArrayList=new ArrayList<>();
                questions=new ArrayList<>();
                adapter_for_home=new Adapter_for_home(getContext(),questionClassArrayList);
                homerecyclerView.setNestedScrollingEnabled(false);
                homerecyclerView.setAdapter(adapter_for_home);
                homerecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                homerecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                fetching_work_from_firebase(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "Nothing selected from categories", Toast.LENGTH_SHORT).show();
            }
        });
        floatingActionButton=view.findViewById(R.id.fab_addquestion);
        answer=view.findViewById(R.id.answer);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer_click_fragy answerClickFragy=new Answer_click_fragy();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(StringNames.stacky);
                fragmentTransaction.replace(R.id.container,answerClickFragy).commit();
            }
            });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question question=new question();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                sending_object(question,myUser);
                fragmentTransaction.addToBackStack(StringNames.stacky);
                fragmentTransaction.replace(R.id.container,question).commit();
            }
        });
        return view;
    }
    public void sending_object(question question,MyUser myUser){
        Bundle bundle =new Bundle();
        bundle.putSerializable(StringNames.user,myUser);
        question.setArguments(bundle);
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
                   // progressBar.setVisibility(View.INVISIBLE);
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
        for(int i=0;i<qids.size();i++){
            DatabaseReference myref2 = firebaseDatabase.getReference();
            myref2=myref2.child("questions").child(qids.get(i));
            myref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //No need of iterating on children because each time the for loop executes i am on the child i want to be
                    QuestionClass questionClass=dataSnapshot.getValue(QuestionClass.class);
                    questions.add(questionClass.getQuestion_text());
                    questionClassArrayList.add(questionClass);
                    if(questions.size()==qids.size()){
                        //Notify adapter
                        adapter_for_home.notifyDataSetChanged();
                        progressy.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
