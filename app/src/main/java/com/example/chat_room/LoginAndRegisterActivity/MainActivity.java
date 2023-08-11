package com.example.chat_room.LoginAndRegisterActivity;

import static com.example.chat_room.LoginAndRegisterActivity.Splash_Image.editor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_room.R;
import com.example.chat_room.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    UserAdapeter adapeter;
    ArrayList<Users> usersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert...!");
                builder.setMessage("Are you sure?"+"\n" +
                        "you want to logout");
                builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putInt("login",0);
                        editor.commit();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginWithEmail1.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        usersArrayList = new ArrayList<>();
        DatabaseReference reference = database.getReference().child("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapeter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.mainuserreecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapeter = new UserAdapeter(MainActivity.this, usersArrayList);
        recyclerView.setAdapter(adapeter);

        if(auth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, LoginWithEmail1.class));
        }
    }
}