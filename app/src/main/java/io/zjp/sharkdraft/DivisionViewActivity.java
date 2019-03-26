package io.zjp.sharkdraft;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DivisionViewActivity extends AppCompatActivity {

    private RecyclerView listDivisions;
    private RecyclerView.Adapter divisionsAdapter;
    private RecyclerView.LayoutManager divisionsManager;

    public class DivisionsAdapter extends RecyclerView.Adapter<DivisionsAdapter.ViewHolder> {
        private String data[];

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName;
            public TextView tvMembers;
            public ViewHolder(View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
                tvMembers = v.findViewById(R.id.tvMembers);
            }
        }

        public DivisionsAdapter(String dataset[]) {
            data = dataset;
        }

        @Override
        public DivisionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(
                    parent.getContext()
            ).inflate(R.layout.division_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvName.setText("Division " + position);
            holder.tvMembers.setText(data[position]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_view);
        listDivisions = findViewById(R.id.listDivisions);

        String[] dataset = getIntent().getStringExtra("io.zjp.DRAFT_DIVISIONS").split(";");

        listDivisions.setHasFixedSize(true);
        divisionsManager = new LinearLayoutManager(this);
        listDivisions.setLayoutManager(divisionsManager);
        divisionsAdapter = new DivisionsAdapter(dataset);
        listDivisions.setAdapter(divisionsAdapter);
    }
}
