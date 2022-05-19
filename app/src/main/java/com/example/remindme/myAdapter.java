package com.example.remindme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remindme.MainActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> {
    public ViewGroup p;
    ArrayList<Model> dataholder = new ArrayList<Model>();
    private onItemClickListener mListener;
    public myAdapter(ArrayList<Model> dataholder) {
        this.dataholder = dataholder;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);  //inflates the xml file in recyclerview
        myviewholder myviewholder = new myviewholder(view, mListener);
        return myviewholder;
    }
//

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
        Model model = dataholder.get(position);
        holder.mTitle.setText(dataholder.get(position).getTitle());                                 //Binds the single reminder objects to recycler view
        holder.mDate.setText(dataholder.get(position).getDate());
        holder.mTime.setText(dataholder.get(position).getTime());
        holder.position = position;
        holder.model = model;
//        holder.mMarkdone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "button clicked",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    class myviewholder extends RecyclerView.ViewHolder {

        int position;
        Model model;

        TextView mTitle, mDate, mTime;

        CheckBox checkBox;

        public myviewholder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);                               //holds the reference of the materials to show data in recyclerview
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);

            checkBox = (CheckBox) itemView.findViewById(R.id.checkdone);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                            dbManager dbManager = new dbManager(p.getContext());
                            dbManager.update_complete(model);
                            Toast.makeText(view.getContext(),"Task Completed!",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo", "onclick" + position + " user " + model.getTitle());
                }
            });

        }

    }
}
