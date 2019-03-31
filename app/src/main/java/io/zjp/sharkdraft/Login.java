package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText txtEmail, txtPass;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        txtEmail=findViewById(R.id.txtEmail);
        txtPass=findViewById(R.id.txtPass);
        progressBar =findViewById(R.id.progressbar);

        findViewById(R.id.btnSignup).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
    }
    private void userLogin(){
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
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Intent intent = new Intent(Login.this, MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnSignup:

                startActivity(new Intent(this,signup.class));

                break;
            case R.id.btnLogin:

                userLogin();

                break;
        }

    }
}
