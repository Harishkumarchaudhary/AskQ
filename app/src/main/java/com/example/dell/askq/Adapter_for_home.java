package com.example.dell.askq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dell on 21-07-2019.
 */

public class Adapter_for_home extends RecyclerView.Adapter<Adapter_for_home.myViewHolder> {
    Context context;
    ArrayList<QuestionClass> arrayList;

    public Adapter_for_home(Context context, ArrayList<QuestionClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.homelayout_adapter,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
           holder.q_txt.setText(arrayList.get(position).getQuestion_text());
           holder.q_txt.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //Start a fragment showing all answers to that question
                   Bundle bundle=new Bundle();
                   bundle.putSerializable(StringNames.q_info,arrayList.get(position));
                   MainActivity mainActivity= (MainActivity) context;
                   //Show the answers to users on click of the question in home by starting AnswerShowingFragy
                   AnswerShowingFragy answerShowingFragy=new AnswerShowingFragy();
                   FragmentManager fragmentManager=mainActivity.getSupportFragmentManager();
                   FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                   answerShowingFragy.setArguments(bundle);
                   fragmentTransaction.replace(R.id.container,answerShowingFragy).addToBackStack(StringNames.stacky).commit();
               }
           });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder{
       TextView q_txt;
        public myViewHolder(View itemView) {
            super(itemView);
            q_txt=itemView.findViewById(R.id.q_text_in_home_layout);
        }
    }
}
