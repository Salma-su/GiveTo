package com.example.giveit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giveit.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Button signup, logout;
    //View binding
    private ActivityLoginBinding binding;
    FirebaseUser user;

    RecyclerView recview;
    DatabaseReference database; // new
    myadapter adapter;// Create Object of the Adapter class
    ArrayList<ObjectDto> objectsList;

    Button btn;

    public home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static home newInstance(String param1, String param2) {
        home fragment = new home();
        Bundle args = new Bundle();
        //args.setString()
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            startActivity(new Intent(this.getActivity(), LoginActivity.class));
        }
        else{
            Toast.makeText(getActivity(), user.getEmail().toString(), Toast.LENGTH_SHORT).show();
        }

/*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ObjectListActivity.this, addObjectActivity.class));            }
        });
*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn = (Button) view.findViewById(R.id.add) ;

        recview=(RecyclerView) view.findViewById(R.id.recview);
        database = FirebaseDatabase.getInstance().getReference("objects");
        // To display the Recycler view linearly
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        objectsList = new ArrayList<>();



        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter=new myadapter(getContext(), objectsList);

        // Connecting Adapter class with the Recycler view*/
        recview.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    ObjectDto objectDto = dataSnapshot.getValue(ObjectDto.class);
                    objectsList.add(objectDto);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), addObjectActivity.class));            }
        });


        //TextView text = view.findViewById(R.id.homeFragment);
        //text.setText("hello");

        //Button button = view.findViewById(R.id.logoutBtn);
        //button.setText("button logout");


    }
}