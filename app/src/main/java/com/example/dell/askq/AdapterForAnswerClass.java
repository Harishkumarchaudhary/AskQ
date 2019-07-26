package com.example.dell.askq;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 13-06-2019.
 */

public class AdapterForAnswerClass extends RecyclerView.Adapter<AdapterForAnswerClass.UserHolder> {
    Context context;
    ArrayList<AnswerClass> answerClassArrayList;
    Activity activity;
    public AdapterForAnswerClass(Context context, ArrayList<AnswerClass> listy) {
          this.context=context;
          this.answerClassArrayList=listy;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.for_answertab,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
          String question=answerClassArrayList.get(position).getQuestion();
          String user=answerClassArrayList.get(position).getUser();
          holder.t1.setText(question);
          holder.t1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Bundle bundle=new Bundle();
                  bundle.putSerializable(StringNames.q_info, (Serializable) answerClassArrayList.get(position));
                  Answer_for_question_fragy answerClickFragy=new Answer_for_question_fragy();
                  MainActivity mainActivity= (MainActivity) context;
                  android.support.v4.app.FragmentTransaction fragmentTransaction=mainActivity.getSupportFragmentManager().beginTransaction();
                  answerClickFragy.setArguments(bundle);
                  fragmentTransaction.replace(R.id.container,answerClickFragy).addToBackStack(StringNames.stacky).commit();
              }
          });
          holder.t2.setText(user);
    }

    @Override
    public int getItemCount() {
        return answerClassArrayList.size();
    }
    public class UserHolder extends RecyclerView.ViewHolder {
        TextView t1,t2;
        public UserHolder(View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.question_in_answertab);
            t2=itemView.findViewById(R.id.user_in_answertab);
        }
    }
}
