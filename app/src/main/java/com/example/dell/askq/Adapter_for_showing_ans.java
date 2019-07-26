package com.example.dell.askq;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by Dell on 23-07-2019.
 */

public class Adapter_for_showing_ans extends RecyclerView.Adapter<Adapter_for_showing_ans.myAnsHolder> {
    Context context;
    ArrayList<Q_answer_class> answers_list;

    public Adapter_for_showing_ans(Context context, ArrayList<Q_answer_class> answers_list) {
        this.context = context;
        this.answers_list = answers_list;
    }

    @NonNull
    @Override
    public myAnsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.ans_show_layout,parent,false);
        return new myAnsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myAnsHolder holder, int position) {
           holder.t1.setText(answers_list.get(position).getAnswer());
           holder.t2.setText("answered by " + answers_list.get(position).getThe_answerer());
    }

    @Override
    public int getItemCount() {
        return answers_list.size();
    }
    public class myAnsHolder extends RecyclerView.ViewHolder{
        TextView t1,t2;
        public myAnsHolder(View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.ans);
            t2=itemView.findViewById(R.id.id_of_user_who_ans);
        }
    }
}
