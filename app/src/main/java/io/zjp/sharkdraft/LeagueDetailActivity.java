package io.zjp.sharkdraft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LeagueDetailActivity extends AppCompatActivity {

    private MenuActivity.LeagueInfo info;
    private ArrayList<String> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_detail);

        // TODO: get teams list

        RecyclerView rv = findViewById(R.id.listDetailLeague);
        rv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        MainActivity.TeamsAdapter adapter = new MainActivity.TeamsAdapter(teams);
        rv.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.tbLeagueDetail);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (info.own && info.member)
            inflater.inflate(R.menu.menu_detail_own_member, menu);
        else if (info.own)
            inflater.inflate(R.menu.menu_detail_own, menu);
        else if (info.member)
            inflater.inflate(R.menu.menu_detail_member, menu);
        else
            inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.menuDetailChangeMemberName:
                // TODO
                break;
            case R.id.menuDetailLeave:
                // TODO
                break;
            case R.id.menuDetailChangeLeagueName:
                // TODO
                break;
            case R.id.menuDetailChangeOwnerName:
                // TODO
                break;
            case R.id.menuDetailSetDraftOrder:
                // TODO
                break;
            case R.id.menuDetailDeleteLeague:
                // TODO
                break;
        }
        return true;
    }
}
