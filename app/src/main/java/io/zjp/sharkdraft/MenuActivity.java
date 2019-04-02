package io.zjp.sharkdraft;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView listLeagues;
    private RecyclerView.Adapter leaguesAdapter;
    private RecyclerView.LayoutManager leaguesManager;

    private ArrayList<LeagueInfo> leagueArray;

    private ProgressBar progressBar;

    public class LeagueInfo {
        public String leagueID;
        public String leagueName;
        public String ownerName;
        public boolean own;
        public boolean member;
        public boolean frozen;

        public LeagueInfo(String leagueID,
                          String leagueName,
                          String ownerName,
                          boolean own,
                          boolean member,
                          boolean frozen) {
            this.leagueID = leagueID;
            this.leagueName = leagueName;
            this.ownerName = ownerName;
            this.own = own;
            this.member = member;
            this.frozen = frozen;
        }
    }

    public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.ViewHolder> {
        private ArrayList<LeagueInfo> data;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvLeagueName, tvLeagueOwner;
            public CheckBox checkOwn, checkMember, checkFrozen;

            public ViewHolder(View v) {
                super(v);
                tvLeagueName = v.findViewById(R.id.tvLeagueName);
                tvLeagueOwner = v.findViewById(R.id.tvLeagueOwner);
                checkOwn = v.findViewById(R.id.checkOwn);
                checkMember = v.findViewById(R.id.checkMember);
                checkFrozen = v.findViewById(R.id.checkFrozen);
            }
        }

        public LeaguesAdapter(ArrayList<LeagueInfo> dataset) {
            data = dataset;
        }

        @Override
        public LeaguesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(
                    parent.getContext()
            ).inflate(R.layout.league_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LeagueInfo info = data.get(position);
            holder.tvLeagueName.setText(info.leagueName);
            holder.tvLeagueOwner.setText(info.ownerName);
            holder.checkOwn.setChecked(info.own);
            holder.checkMember.setChecked(info.member);
            holder.checkFrozen.setChecked(info.frozen);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Leagues");

        progressBar = findViewById(R.id.pbMenu);

        leagueArray = new ArrayList<>();
        listLeagues = findViewById(R.id.listLeagues);
        listLeagues.setHasFixedSize(true);
        leaguesManager = new LinearLayoutManager(this);
        listLeagues.setLayoutManager(leaguesManager);
        leaguesAdapter = new LeaguesAdapter(leagueArray);
        listLeagues.setAdapter(leaguesAdapter);
        refreshLeaguesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dbmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(
                        new Intent(this, Login.class)
                );
                break;
            case R.id.menuRefresh:
                refreshLeaguesList();
                break;
        }

        return true;
    }

//    public void testAuth(View view) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//        Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
//    }
//
//    public void testAuth1(View view) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//        Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
//    }
//
//    public void testDB(View view) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("leagues").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    String out = "List of Leagues:\n";
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        out += document.getId() + ": " + document.getString("Name") + "\n";
//                    }
//                    dbInfo.setText(out);
//                } else {
//                    Toast.makeText(MenuActivity.this, "Error =(", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    public void refreshLeaguesList() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leagues").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String userUID = user.getUid();
                    leagueArray.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String leagueName = "<No Name>";
                        String leagueOwner = "<No Owner>";
                        HashMap<String, String> ownerInfo;
                        ArrayList<HashMap<String, String>> memberArray;
                        boolean own = false;
                        boolean member = false;
                        boolean frozen = false;
                        try {
                            if (data.containsKey("Name"))
                                leagueName = (String) data.get("Name");
                            if (data.containsKey("Owner")) {
                                ownerInfo = (HashMap<String, String>) data.get("Owner");
                                System.out.println("<<<GETTING OWNER INFO>>>");
                                for (String k : ownerInfo.keySet())
                                    System.out.println(k + ":::" + ownerInfo.get(k));
                                if (ownerInfo.containsKey("Name"))
                                    leagueOwner = ownerInfo.get("Name");
                                if (ownerInfo.containsKey("UID") && ownerInfo.get("UID").equals(userUID))
                                    own = true;
                            }
                            if (data.containsKey("Members")) {
                                memberArray = (ArrayList<HashMap<String, String>>) data.get("Members");
                                for (HashMap<String, String> m : memberArray) {
                                    if (m.containsKey("UID") && m.get("UID").equals(userUID)) {
                                        member = true;
                                        break;
                                    }
                                }
                            }
                            if (data.containsKey("DraftOrder"))
                                frozen = true;
                        } catch (ClassCastException e) {
                            System.out.println("ClassCastException:::" + e.toString());
                        } catch (NullPointerException e) {
                            System.out.println("NullPointerException:::" + e.toString());
                        }
                        leagueArray.add(new LeagueInfo(
                                document.getId(), leagueName, leagueOwner,
                                own, member, frozen
                        ));
                        leaguesAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(
                            MenuActivity.this,
                            "Error Fetching Leagues: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    public void joinLeague(View view) {

    }

    public void createLeague(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.create_league_dialog, null);
        builder.setTitle("Create a New League")
                .setView(dialogView)
                .setPositiveButton("Create League", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lname = ((EditText) dialogView.findViewById(R.id.txtDialogNewLeagueName)).getText().toString();
                        String lown = ((EditText) dialogView.findViewById(R.id.txtDialogNewLeagueOwner)).getText().toString();
                        String ldiv = ((EditText) dialogView.findViewById(R.id.txtDialogNewLeagueDivisions)).getText().toString();
                        HashMap<String, Object> league = new HashMap<>();
                        league.put("Name", lname);
                        HashMap<String, Object> ownerInfo = new HashMap<>();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        ownerInfo.put("Name", lown);
                        ownerInfo.put("UID", user.getUid());
                        league.put("Owner", ownerInfo);
                        int ndiv;
                        try {
                            ndiv = Integer.parseInt(ldiv);
                            league.put("Divisions", ndiv);
                            league.put("Members", new ArrayList<>());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            progressBar.setVisibility(View.VISIBLE);
                            db.collection("leagues").add(league).addOnSuccessListener(
                                new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(
                                                MenuActivity.this,
                                                "League Created!",
                                                Toast.LENGTH_SHORT).show();
                                        refreshLeaguesList();
                                    }
                                }).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(
                                                MenuActivity.this,
                                                e.getLocalizedMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                            });
                        } catch (NumberFormatException nfe) {
                            Toast.makeText(MenuActivity.this, "# Leagues must be a number", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void navigateManualDraft(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
