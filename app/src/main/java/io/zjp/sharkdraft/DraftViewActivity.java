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

        String[] dataset = new String[14];
        dataset[0] = "A";
        dataset[1] = "B";
        dataset[2] = "C";
        dataset[3] = "D";
        dataset[4] = "E";
        dataset[5] = "F";
        dataset[6] = "G";
        dataset[7] = "H";
        dataset[8] = "I";
        dataset[9] = "J";
        dataset[10] = "K";
        dataset[11] = "L";
        dataset[12] = "M";
        dataset[13] = "N";

        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerListAdapter(dataset);
        recyclerView.setAdapter(adapter);
    }
}
