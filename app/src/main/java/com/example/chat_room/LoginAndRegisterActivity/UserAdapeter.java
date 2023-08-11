package com.example.chat_room.LoginAndRegisterActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_room.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapeter extends RecyclerView.Adapter<UserAdapeter.viewholder> {

    MainActivity mainActivity;
    ArrayList<Users> modalUserArrayList;
    Users modalUser;
    private FirebaseDatabase database;

    public UserAdapeter(MainActivity mainActivity, ArrayList<Users> modalUserArrayList) {
        this.mainActivity = mainActivity;
        this.modalUserArrayList = modalUserArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapeter.viewholder holder, int position) {
        modalUser = modalUserArrayList.get(position);

        holder.txt1.setText(modalUser.name);
        holder.txt2.setText(modalUser.status);

        Picasso.get()
                .load(modalUser.imageUri)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return modalUserArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView txt1,txt2;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_image);
            txt1 = itemView.findViewById(R.id.username);
            txt2 = itemView.findViewById(R.id.userstatus);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("TTT", "onLongClick: ");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("User").orderByChild("uid").equalTo(modalUser.uid);

                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("TTT", "onCancelled", databaseError.toException());
                        }
                    });


                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                    Query applesQuery = ref.child("User").orderByChild("uid").equalTo(modalUser.uid);
                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("User").push();
                    String id = ref.getKey();
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                Users users=new Users(modalUser.uid,"GGG","GGG@gmail.com","hfgd.jpg","hhh");
                                appleSnapshot.getRef().setValue(users);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("TTT", "onCancelled", databaseError.toException());
                        }
                    });
                }
            });
        }
    }
}
