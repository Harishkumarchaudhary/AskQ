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
public class AnswerShowingFragy extends Fragment {


    public AnswerShowingFragy() {
        // Required empty public constructor
    }

    ArrayList<Q_answer_class> arrayListtemp;
    ArrayList<Q_answer_class> req_array_list;//Because i need name of username who has written that answer
    Adapter_for_showing_ans adapter_for_showing_ans;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_answer_showing_fragy, container, false);
        Bundle bundle=getArguments();
        QuestionClass questionClass= (QuestionClass) bundle.getSerializable(StringNames.q_info);
        //Now retreive all the answers for the given qid
        arrayListtemp=new ArrayList<>();
        req_array_list=new ArrayList<>();
        adapter_for_showing_ans=new Adapter_for_showing_ans(getContext(),req_array_list);
        recyclerView=view.findViewById(R.id.answer_showing_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter_for_showing_ans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference();
        myref=myref.child("questions").child(questionClass.getQid()).child("answers");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child:dataSnapshot.getChildren()){
                  Q_answer_class qAnswerClass=child.getValue(Q_answer_class.class);
                    arrayListtemp.add(qAnswerClass);
                }
                for(int i=0;i<arrayListtemp.size();i++){
                  String uid=arrayListtemp.get(i).getThe_answerer();
                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference myref2=database.getReference();
                   myref2=myref2.child("users").child(uid).child("username");
                    final String ans=arrayListtemp.get(i).getAnswer();
                    myref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            req_array_list.add(new Q_answer_class(ans,(String)dataSnapshot.getValue()));
                            if(req_array_list.size()==arrayListtemp.size()){
                                adapter_for_showing_ans.notifyDataSetChanged();
                            }
                            Log.d("gh",ans);
                            Log.d("ty",(String)dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "No answer for this question right now", Toast.LENGTH_SHORT).show();
            }
        });
        if(req_array_list.size()==0){
            Toast.makeText(getContext(), "No answer for this question right now", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
