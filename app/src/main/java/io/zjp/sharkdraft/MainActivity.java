package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText txtTeamName;
    private EditText numDivisions;
    private RecyclerView listTeams;
    private RecyclerView.Adapter teamsAdapter;
    private RecyclerView.LayoutManager teamsManager;

    private ArrayList<String> teamArray;

    public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
        private ArrayList<String> data;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName;
            public ViewHolder(View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
            }
        }

        public TeamsAdapter(ArrayList<String> dataset) {
            data = dataset;
        }

        @Override
        public TeamsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(
                    parent.getContext()
            ).inflate(R.layout.team_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvName.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTeamName = findViewById(R.id.txtTeamName);
        numDivisions = findViewById(R.id.numDivisions);
        teamArray = new ArrayList<>();
        listTeams = findViewById(R.id.listTeams);
        listTeams.setHasFixedSize(true);
        teamsManager = new LinearLayoutManager(this);
        listTeams.setLayoutManager(teamsManager);
        teamsAdapter = new TeamsAdapter(teamArray);
        listTeams.setAdapter(teamsAdapter);
    }

    public void addTeam(View view) {
        String toAdd = txtTeamName.getText().toString();
        if (toAdd.length() == 0) {
            Toast.makeText(this, "Enter a team name", Toast.LENGTH_SHORT).show();
        } else if (teamArray.contains(toAdd)) {
            Toast.makeText(this, "Team names must be unique", Toast.LENGTH_SHORT).show();
        } else {
            teamArray.add(toAdd);
            teamsAdapter.notifyItemInserted(teamArray.size() - 1);
        }
    }

    public void createDraft(View view) {
        Intent draftIntent = new Intent(this, DraftViewActivity.class);
        startActivity(draftIntent);
    }
}
