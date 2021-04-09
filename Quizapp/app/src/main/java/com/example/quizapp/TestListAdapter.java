package com.example.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestListAdapter extends BaseAdapter {

    private int num_of_tests;





    public TestListAdapter(int num_of_tests) {
        this.num_of_tests = num_of_tests;
    }


    @Override
    public int getCount() {
        return num_of_tests;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent,false);
        }
        else{
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( parent.getContext(), MCQuestionActivity2.class);
                intent.putExtra("info",1);
                parent.getContext().startActivity(intent);


            }
        });

        ((TextView)view.findViewById(R.id.list_num_textview)).setText("Test " + String.valueOf(position + 1));

        return view;
    }
}
