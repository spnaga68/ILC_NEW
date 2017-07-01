package realmstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.adapter.TeamListAdapter;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.interfaces.ItemClickInterface;

/**
 * Created by developer on 1/7/17.
 */

public class TeamPickerActivity extends AppCompatActivity implements ItemClickInterface {
    private RecyclerView
            list_view;
    private android.support.design.widget.FloatingActionButton add;
    private android.support.design.widget.FloatingActionButton next;
    TeamListAdapter adapter;
    @Inject
    Realm realm;
    TextView selected_teams;
    String pickerFor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_list_view);

        if (getIntent() != null && getIntent().getStringExtra("for") != null)
            pickerFor = getIntent().getStringExtra("for");
        ((MyApplication) getApplication()).getComponent().inject(this);
        list_view = (RecyclerView) findViewById(R.id.list_view);
        add = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add);
        next = (android.support.design.widget.FloatingActionButton) findViewById(R.id.next);
        selected_teams = (TextView) findViewById(R.id.selected_teams);
        selected_teams.setSelected(true);
        adapter = new TeamListAdapter(this, realm.where(Team.class).findAll(), false);
        list_view.setAdapter(adapter);
        list_view.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void itemPicked(int id, String message) {
        Bundle conData = new Bundle();
        conData.putInt("id", id);
        conData.putString("for", pickerFor);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
