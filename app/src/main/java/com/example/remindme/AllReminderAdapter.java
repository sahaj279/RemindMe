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

public class AllReminderAdapter extends RecyclerView.Adapter<AllReminderAdapter.myAllviewholder> {
    public ViewGroup p;
    ArrayList<Model> dataholder = new ArrayList<Model>();
    private onItemClickListener mListener;
    public AllReminderAdapter(ArrayList<Model> dataholder) {
        this.dataholder = dataholder;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }



    @NonNull
    @Override
    public myAllviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_all_reminders, parent, false);  //inflates the xml file in recyclerview
        myAllviewholder myviewholder = new myAllviewholder(view, mListener);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull myAllviewholder holder, @SuppressLint("RecyclerView") int position) {
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

    class myAllviewholder extends RecyclerView.ViewHolder {

        int position;
        Model model;

        TextView mTitle, mDate, mTime;

        CheckBox checkBox;

        public myAllviewholder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txtTitledel);                               //holds the reference of the materials to show data in recyclerview
            mDate = (TextView) itemView.findViewById(R.id.txtDatedel);
            mTime = (TextView) itemView.findViewById(R.id.txtTimedel);

            checkBox = (CheckBox) itemView.findViewById(R.id.delete);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                            dbManager dbManager = new dbManager(p.getContext());
                            dbManager.deleteone(model);
                            Toast.makeText(view.getContext(),"Task Deleted Successfully!",Toast.LENGTH_SHORT).show();
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
