package com.example.chat_room.LoginAndRegisterActivity;

import static com.example.chat_room.LoginAndRegisterActivity.Splash_Image.editor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_room.databinding.ActivityLoginWithEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginWithEmail extends AppCompatActivity {

    ActivityLoginWithEmailBinding binding;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWithEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email1.getText().toString();
                String password = binding.password1.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginWithEmail.this, "Enetr Valid Data", Toast.LENGTH_SHORT).show();
                }
                else if (!email.matches(emailPattern)){
                    Toast.makeText(LoginWithEmail.this, "Email Not Valid Format", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {
                    binding.password1.setError("Password is't below 6 charcter");
                    Toast.makeText(LoginWithEmail.this, "Passwoed Not Valid", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                editor.putInt("login",1);
                                editor.commit();
                                Intent intent = new Intent(LoginWithEmail.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginWithEmail.this, "Error in login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.notregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginWithEmail.this, LoginWithEmail1.class);
                startActivity(intent);
            }
        });

        Log.d("TTT", "onClick: Who logged in?= "+auth.getCurrentUser());
    }

}