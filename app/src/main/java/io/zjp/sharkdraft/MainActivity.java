package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private EditText txtTeamName;
    private EditText numDivisions;
    private RecyclerView listTeams;
    private RecyclerView.Adapter teamsAdapter;
    private RecyclerView.LayoutManager teamsManager;
//dsfdsf
    private ArrayList<String> teamArray;
    FirebaseAuth mAuth;

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
        //Toolbar initialize
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,Login.class));
                break;
        }

        return true;
    }

    public void addTeam(View view) {
        String toAdd = txtTeamName.getText().toString();
        if (toAdd.length() == 0) {
            Toast.makeText(this, "Enter a team name", Toast.LENGTH_SHORT).show();
        } else if (teamArray.contains(toAdd)) {
            Toast.makeText(this, "Team names must be unique", Toast.LENGTH_SHORT).show();
            // test
        } else {
            teamArray.add(toAdd);
            teamsAdapter.notifyItemInserted(teamArray.size() - 1);
        }
    }

    public void createDraft(View view) {
        int nDivisions;

        // validate input

        if (teamArray.size() < 1)
        {
            Toast.makeText(this, "Please enter at least one Team", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            nDivisions = Integer.parseInt(numDivisions.getText().toString());
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "# of Divisions must be between 1 and # of Teams", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nDivisions < 1 || nDivisions > teamArray.size())
        {
            Toast.makeText(this, "# of Divisions must be between 1 and # of Teams", Toast.LENGTH_SHORT).show();
            return;
        }

        // create intent

        Intent draftIntent = new Intent(this, DraftViewActivity.class);

        // generate draft order

        ArrayList<String> randDraftOrder = new ArrayList<>(teamArray);
        Collections.shuffle(randDraftOrder);
        String toPass = "";
        for (String s : randDraftOrder) {
            toPass += s + ";";
        }
        toPass = toPass.substring(0, toPass.length() - 1);
        draftIntent.putExtra("io.zjp.DRAFT_TEAMS", toPass);

        // put teams into divisions

        Collections.shuffle(randDraftOrder);
        String divisions[] = new String[nDivisions];
        int division = 0;
        for (String s : randDraftOrder) {
            if (divisions[division] == null)
                divisions[division] = s;
            else
                divisions[division] += "\n" + s;
            division = (division + 1) % nDivisions;
        }
        String toPassDivisions = null;
        for (String s : divisions) {
            if (toPassDivisions == null)
                toPassDivisions = s;
            else
                toPassDivisions += ";" + s;
        }
        draftIntent.putExtra("io.zjp.DRAFT_DIVISIONS", toPassDivisions);

        // go to Draft Order View

        startActivity(draftIntent);
    }
}
