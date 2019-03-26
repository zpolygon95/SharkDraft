package io.zjp.sharkdraft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class DraftViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_view);
        recyclerView = findViewById(R.id.my_recycler_view);

        String[] dataset = getIntent().getStringExtra("io.zjp.DRAFT_TEAMS").split(";");

        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerListAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }
}
