package realmstudy;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import realmstudy.adapter.Pager;
import realmstudy.extras.ZoomOutPageTransformer;

/**
 * Created by developer on 2/5/17.
 */

public class MatchDetailActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
//   BottomSheetBehavior behavior;

    private ViewPager viewPager;
    private int match_id;
    private Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_detail_main);

        match_id = getIntent().getIntExtra("match_id", 0);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout1);

//        View bottomSheet = findViewById(R.id.bot);
//        behavior = BottomSheetBehavior.from(bottomSheet);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.info)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overs)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.score)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(), match_id);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tool_bar = (android.support.v7.widget.Toolbar) findViewById(realmstudy.R.id.tool_bar);
        tool_bar.setTitle(getString(R.string.score_board));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case menu_one:
//                Intent ii=new Intent(MatchDetails.this,second.class);
//                startActivity(ii);

            case R.id.menu_two:

                return true;
            case R.id.menu_three:

                return true;
            case R.id.menu_four:

                return true;

        }
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.activity_main, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
//        LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
//        LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,"welcome",Toast.LENGTH_LONG).show();
//            }
//        });

        return super.onOptionsItemSelected(item);

    }


}

// Load data on background


