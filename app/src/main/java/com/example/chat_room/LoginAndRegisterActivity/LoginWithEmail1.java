package com.example.chat_room.LoginAndRegisterActivity;

import static com.example.chat_room.LoginAndRegisterActivity.Splash_Image.editor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_room.databinding.ActivityLoginWithEmail1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class LoginWithEmail1 extends AppCompatActivity {

    ActivityLoginWithEmail1Binding binding;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    Uri imageUri;
    String imageURI;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWithEmail1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String cpassword = binding.cpassword.getText().toString();
                String status = "Hey There I'm using this application";


                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginWithEmail1.this, "Please enetr valid data", Toast.LENGTH_SHORT).show();
                }
                else if (!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    binding.email.setError("Please enter valid email");
                    Toast.makeText(LoginWithEmail1.this, "Email Not Valid Format", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    binding.password.setError("Password is't below 6 charcter");
                    Toast.makeText(LoginWithEmail1.this, "Passwoed Not Valid", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cpassword)) {
                    progressDialog.dismiss();
                    binding.cpassword.setError("Password dosn't match");
                    Toast.makeText(LoginWithEmail1.this, "Passwoed Not Valid", Toast.LENGTH_SHORT).show();

                } else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference reference = firebaseDatabase.getReference().child("User").child(auth.getUid());
                                StorageReference reference1 = firebaseStorage.getReference().child("Upload").child(auth.getUid());

                                if(imageUri!=null){
                                    reference1.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()){
                                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(),name,email,imageURI,status);
                                                        reference.setValue(users ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent(LoginWithEmail1.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                else {
                                                                    Toast.makeText(LoginWithEmail1.this, "Error in creating new user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                                else {
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/chat-room-c7950.appspot.com/o/profileuser.png?alt=media&token=106eaef6-d16c-4c89-8d55-5e021bc9f780";
                                    Users modalUser = new Users(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(modalUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                editor.putInt("login",1);
                                                editor.commit();
                                                Intent intent = new Intent(LoginWithEmail1.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(LoginWithEmail1.this, "Error in creating new user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginWithEmail1.this, "Error in register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20);
            }
        });

        binding.simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginWithEmail1.this, LoginWithEmail.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==20){
            if (data!=null){
                imageUri = data.getData();
                binding.profileImage.setImageURI(imageUri);
            }
        }
    }
}