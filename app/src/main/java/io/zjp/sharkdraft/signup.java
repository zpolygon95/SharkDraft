package io.zjp.sharkdraft;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity implements View.OnClickListener{

    EditText txtEmail, txtPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtEmail=findViewById(R.id.txtEmail);
        txtPass=findViewById(R.id.txtPass);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignup2).setOnClickListener(this);
    }

    private void registerUser() {
        String email=txtEmail.getText().toString().trim();
        String password=txtPass.getText().toString().trim();

        //Validation block
        if(email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Enter Valid Email");
            txtEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtPass.setError("Password is required");
            txtPass.requestFocus();
            return;
        }
        if(password.length()<6){
            txtPass.setError("Minimum length of 6");
            txtPass.requestFocus();
            return;

        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User Registered Successful",Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignup2:
                registerUser();
                break;

            case R.id.btnBacklogin:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}
