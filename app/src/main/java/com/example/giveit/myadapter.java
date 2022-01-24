package com.example.giveit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//In order to show the data from ObjectDto.java in activity_object_list.xml
// we have to create an Adapter class :MyAdapter.java


// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class myadapter extends RecyclerView.Adapter<myadapter.myviewholder>
{

    Context context;
    ArrayList<ObjectDto> objectsList ;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public myadapter(Context context, ArrayList<ObjectDto> objectsList) {

        this.context = context;
        this.objectsList = objectsList;
    }


    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position ) {
        ObjectDto objects = objectsList.get(position);

        holder.name.setText(objects.getName());
        holder.description.setText(objects.getDescription());
        holder.adresse.setText(objects.getAdresse());
        Glide.with(holder.img.getContext()).load(objects.getImage()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, MessageActivity.class);
                intent.putExtra("userid", user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }





    // Function to tell the class about the Card view in activity_object_list.xml
    // which the data will be shown
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent, false);
         return new myviewholder(view);
    }


    // Sub Class to create references of the views
    // view activity_object_list.xml
    class myviewholder  extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView name, description, adresse;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img=(CircleImageView) itemView.findViewById(R.id.img1);
            name=(TextView) itemView.findViewById(R.id.nametext);
            description=(TextView) itemView.findViewById(R.id.descriptiontext);
            adresse = (TextView) itemView.findViewById(R.id.adressetext);

        }
    }
}
