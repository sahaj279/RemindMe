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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewholder> {
    public ViewGroup p;
    ArrayList<Person> dataholder = new ArrayList<Person>();
    private PeopleAdapter.onItemClickListenerp mListener;
    public PeopleAdapter(ArrayList<Person> dataholder) {
        this.dataholder = dataholder;
    }

    public void setOnItemClickListener(PeopleAdapter.onItemClickListenerp listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_person, parent, false);  //inflates the xml file in recyclerview
        PeopleViewholder myviewholder = new PeopleAdapter.PeopleViewholder(view, mListener);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleViewholder holder, int position) {
        String mar=dataholder.get(position).getMarriage_date();
        if(mar.equals("Date of Marriage Anniversary")){
            mar="Unmarried";
        }
//
        Calendar calendar = Calendar.getInstance();
        int c_month = calendar.get(Calendar.MONTH)+1;
        int c_day = calendar.get(Calendar.DAY_OF_MONTH);
        int c_year = calendar.get(Calendar.YEAR);
        int b_year=c_year;

        String str =dataholder.get(position).getDob();
        String[] dd = str.split("-", 3);


            if(c_day>Integer.parseInt(dd[0]) && c_month>=Integer.parseInt(dd[1])){
                b_year++;
            }


        String b_date=dd[0]+"-" +dd[1]+"-" +b_year;
        String c_date=c_day+"-" +c_month+"-" +c_year;

        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.ENGLISH);
        Date firstDate = null;
        Date secondDate=null;
        try {
            firstDate = sdf.parse(b_date);

            secondDate = sdf.parse(c_date);
        } catch (ParseException e) {
            e.printStackTrace();
//            Toast.makeText(p.getContext()," "+firstDate.toString()+" no date "+curr,Toast.LENGTH_LONG).show();
        }


        long diff = Math.abs(secondDate.getTime() - firstDate.getTime());

        TimeUnit time = TimeUnit.DAYS;
        long a = time.convert(diff, TimeUnit.MILLISECONDS);


        Person person=dataholder.get(position);
        holder.name.setText(dataholder.get(position).getP_name());
        holder.contact.setText("Phone No. "+dataholder.get(position).getContact());
        holder.dob.setText("Birthdate "+dataholder.get(position).getDob());
        holder.daysleft.setText(a+" days left for birthday");
        holder.marriage.setText("Marriage Anniversary "+ mar);

        holder.position = position;
        holder.person = person;
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public interface onItemClickListenerp {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    class PeopleViewholder extends RecyclerView.ViewHolder {

        int position;
        Person person;

        TextView name,contact,dob,daysleft,marriage;

        CheckBox checkBox;

        public PeopleViewholder(@NonNull View itemView, PeopleAdapter.onItemClickListenerp listener) {
            super(itemView);

           name=(TextView)itemView.findViewById(R.id.txtTitlep);
            contact=(TextView)itemView.findViewById(R.id.txtContact);
            dob=(TextView)itemView.findViewById(R.id.dob);
            daysleft=(TextView)itemView.findViewById(R.id.daysLeftToBirthday);
            marriage=(TextView)itemView.findViewById(R.id.txtMarriagep);

            checkBox = (CheckBox) itemView.findViewById(R.id.del_person);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                            dbManager dbManager = new dbManager(p.getContext());
                            dbManager.delete_person(person);
                            Toast.makeText(view.getContext(),"Friend Removed!",Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });


        }

    }
}