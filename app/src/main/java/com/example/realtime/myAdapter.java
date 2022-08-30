package com.example.realtime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    public Context context;
    public ArrayList<model> arrayList;

    ImageView edit;
    ImageView delete;

    public myAdapter(Context context, ArrayList<model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_row, parent, false);
        return new myViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        model ob = arrayList.get(position);
        holder.dname.setText(ob.getName());
        holder.dfather.setText(ob.getFather());
        holder.daddress.setText(ob.getAddress());
        holder.demail.setText(ob.getEmail());
        holder.dmobile.setText(ob.getMobile());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50, 0, 50, 0)
                        .setContentHolder(new ViewHolder(R.layout.activity_update_record))
                        .setExpanded(false)
                        .create();

                View view1 = (LinearLayout) dialogPlus.getHolderView();

                EditText Uname = view1.findViewById(R.id.Uname);
                EditText Ufather = view1.findViewById(R.id.Ufather);
                EditText Uaddress = view1.findViewById(R.id.Uaddress);
                EditText Uemail = view1.findViewById(R.id.Uemail);
                EditText Umobile = view1.findViewById(R.id.Umobile);


                model ob = arrayList.get(position);
                Uname.setText(ob.getName());
                Ufather.setText(ob.getFather());
                Uaddress.setText(ob.getAddress());
                Uemail.setText(ob.getEmail());
                Umobile.setText(ob.getMobile());
                Button update = view1.findViewById(R.id.update);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", Uname.getText().toString());
                        map.put("father", Ufather.getText().toString());
                        map.put("address", Uaddress.getText().toString());
                        map.put("email", Uemail.getText().toString());
                        map.put("mobile", Umobile.getText().toString());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Detail")
                                .child(ob.getKey())
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
                dialogPlus.show();

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this detail?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("Detail")
                                .child(arrayList.get(position).getKey())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView dname, dfather, daddress, demail, dmobile;

        ImageView edit;
        ImageView delete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            dname = (itemView).findViewById(R.id.namee);
            dfather = (itemView).findViewById(R.id.fatherr);
            daddress = (itemView).findViewById(R.id.addresss);
            demail = (itemView).findViewById(R.id.emaill);
            dmobile = (itemView).findViewById(R.id.mobilee);
            edit = (itemView).findViewById(R.id.edit);
            delete = (itemView).findViewById(R.id.delete);


        }


    }
}
