package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void testAuth(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    public void testAuth1(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
    }

    public void navigateManualDraft(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
