package com.example.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Creator_GradeTestListAdpater extends BaseAdapter {

    private ArrayList<String> List;

    public Creator_GradeTestListAdpater(ArrayList<String> testList) {
        this.List = testList;
    }

    @Override
    public int getCount() {
        return List.size();
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
                Intent intent = new Intent( parent.getContext(), Test_WorksheetListActivity.class);
                intent.putExtra("info",1);
                intent.putExtra("NAME", position );
                parent.getContext().startActivity(intent);


            }
        });

        ((TextView)view.findViewById(R.id.list_num_textview)).setText(List.get(position));

        return view;
    }
}
