package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MenuActivity extends AppCompatActivity {

    TextView dbInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbInfo = findViewById(R.id.txtDBResults);
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

    public void testDB(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leagues").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String out = "List of Leagues:\n";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        out += document.getId() + ": " + document.getString("Name") + "\n";
                    }
                    dbInfo.setText(out);
                } else {
                    Toast.makeText(MenuActivity.this, "Error =(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void navigateManualDraft(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
