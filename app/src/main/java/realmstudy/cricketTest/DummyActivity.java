package realmstudy.cricketTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import realmstudy.MainFragmentActivity;
import realmstudy.R;
import realmstudy.SplashAct;
import realmstudy.data.DetailedScoreData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.ScoreBoardData;
import realmstudy.data.UpdateDetailScoreData;
import realmstudy.extras.Utils;

/**
 * Created by Admin on 07-04-2018.
 */

public class DummyActivity extends AppCompatActivity {
    private String matchID = "";


    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName );
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.blockEntry).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i=new Intent(DummyActivity.this, MainFragmentActivity.class);
                i.putExtra("fragmentToLoad","AddNewTeam");
                startActivity(i);
                finish();
                return false;
            }
        });
        findViewById(R.id.blockEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("InningsDetailData/" + matchID);
                DetailedScoreData detailedScoreData = new DetailedScoreData();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth != null) {
                    try {
                        UpdateDetailScoreData jsonObject = new UpdateDetailScoreData();
                        jsonObject.uid = String.valueOf(mAuth.getCurrentUser().getUid());
                        jsonObject.match_id = String.valueOf(matchID);


                        try {
                            JSONObject obj = new JSONObject(loadJSONFromAsset("overdata.json"));
                            JSONArray jsonArray = obj.getJSONArray("overAdapterData");

                            detailedScoreData.setOverAdapterData(Arrays.asList(Utils.fromJson(jsonArray.toString(), OverAdapterData[].class)));


                            JSONObject objs = new JSONObject(loadJSONFromAsset("scoreboard.json"));


                            JSONObject jsonArrays = objs.getJSONObject("scoreBoardData");

                            detailedScoreData.setScoreBoardData((Utils.fromJson(jsonArrays.toString(), ScoreBoardData.class)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new Test().mains(detailedScoreData);
                       // jsonObject.data = lastInningsDataItem.getDetailedScoreBoardData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
