package realmstudy;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.fragments.DialogFragment.NewPlayerDialog;
import realmstudy.fragments.DialogFragment.NewTeamDialog;
import realmstudy.fragments.DialogFragment.OutDialogFragment;
import realmstudy.fragments.DialogFragment.SelectMultiPlayerDialog;
import realmstudy.fragments.DialogFragment.SelectPlayerDialog;
import realmstudy.fragments.DialogFragment.SelectTeamDialog;
import realmstudy.fragments.MatchInfo;
import realmstudy.fragments.MenuActivity;
import realmstudy.fragments.PlayerListFragment;
import realmstudy.fragments.SavedListFragment;
import realmstudy.fragments.ScheduleNewGame;
import realmstudy.fragments.TeamListFragment;
import realmstudy.fragments.TeamListSelectionFragment;
import realmstudy.fragments.ViewMatch;
import realmstudy.interfaces.DialogInterface;
import realmstudy.interfaces.ItemClickInterface;
import realmstudy.interfaces.MsgFromDialog;
import realmstudy.interfaces.MsgToFragment;
import realmstudy.matchList.MatchListMainFragment;

/**
 * Created by developer on 26/12/16.
 */
public class MainFragmentActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, MsgToFragment, MsgFromDialog,

        Toolbar.OnMenuItemClickListener, ItemClickInterface {

    private FrameLayout content_frame, shadow;
    private android.support.v4.widget.DrawerLayout drawer_layout;
    private android.support.v7.widget.Toolbar tool_bar;
    private ImageView toolbar_logo;
    //    private TextView toolbar_title;
//    private LinearLayout toolbar_titletm;
    //  private ImageView imageee;
    // private ImageButton left_icon;
    private ImageButton right_icon;
    private TextView cancel_b;
    private android.support.v7.widget.SwitchCompat switch_right_icon;
    private FrameLayout mainFrag;
    // private LinearLayout left_drawer;
    public Realm realm;
    private int dialogType;
    public static DialogInterface dialogInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setLocale();
        setContentView(realmstudy.R.layout.home_fragment);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .build();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            if (bundle.getString("fragmentToLoad") != null)
                setFragment(bundle.getString("fragmentToLoad"));

        realm = Realm.getInstance(config);
//        realm.beginTransaction();
//        realm.delete(InningsData.class);
//        realm.commitTransaction();
        content_frame = (FrameLayout) findViewById(realmstudy.R.id.content_frame);
        //   drawer_layout = (android.support.v4.widget.DrawerLayout) findViewById(realmstudy.R.id.drawer_layout);
//        tool_bar = (android.support.v7.widget.Toolbar) findViewById(realmstudy.R.id.tool_bar);
//        tool_bar.inflateMenu(R.menu.main_menu);
//        toolbar_logo = (ImageView) findViewById(realmstudy.R.id.toolbar_logo);


        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(tool_bar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool_bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // toolbar_title = (TextView) findViewById(realmstudy.R.id.toolbar_title);
        // toolbar_titletm = (LinearLayout) findViewById(realmstudy.R.id.toolbar_titletm);
        //  imageee = (ImageView) findViewById(realmstudy.R.id.imageee);
        // left_icon = (ImageButton) findViewById(realmstudy.R.id.left_icon);
        right_icon = (ImageButton) findViewById(realmstudy.R.id.right_icon);
        cancel_b = (TextView) findViewById(realmstudy.R.id.cancel_b);
        switch_right_icon = (android.support.v7.widget.SwitchCompat) findViewById(realmstudy.R.id.switch_right_icon);
        mainFrag = (FrameLayout) findViewById(realmstudy.R.id.mainFrag);
        shadow = (FrameLayout) findViewById(realmstudy.R.id.shadow);
        //  left_drawer = (LinearLayout) findViewById(realmstudy.R.id.left_drawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        System.out.println("happening:" + menuItem.getItemId() + "__" + android.R.id.home);
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Toast.makeText(MainFragmentActivity.this, "clicked", Toast.LENGTH_SHORT).show();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
        //  return super.onCreateOptionsMenu(menu);
    }


    public void setLocale() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void setFragment(String id) {
        switch (id) {
            case CommanData.AddNewTeam:
                getSupportFragmentManager().beginTransaction()
                        .add(realmstudy.R.id.mainFrag, new MatchListMainFragment())
                        .commit();
                break;
        }
    }


    public void homePage_title(String title) {
//        toolbar_title.setVisibility(View.VISIBLE);
//        toolbar_title.setText(title);
        setSupportActionBar(tool_bar);
        tool_bar.setTitle("ILC");
        tool_bar.hideOverflowMenu();
//        toolbar_titletm.setVisibility(View.VISIBLE);
//        toolbar_logo.setVisibility(View.GONE);


    }

    @Override
    public void onBackPressed() {

        Fragment fromFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (fromFragment != null)
            if (fromFragment instanceof MatchListMainFragment
                    || fromFragment instanceof MatchInfo
                    || fromFragment instanceof ScheduleNewGame

                    || fromFragment instanceof TeamListFragment
                    || fromFragment instanceof MatchDetailActivity) {
                finish();
            } else if (fromFragment instanceof MainActivity) {
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null).add(R.id.mainFrag, new MatchListMainFragment()).commit();
            } else {
                super.onBackPressed();

            }

    }

    public void onClick(View v) {
        System.out.println("________dottt1");
        Fragment f = getSupportFragmentManager().findFragmentById(realmstudy.R.id.mainFrag);
        if (f instanceof MainActivity) {
            System.out.println("________dottt2");
            ((MainActivity) f).onClick(v);
        } else if (f instanceof MenuActivity)
            ((MenuActivity) f).onClick(v);

    }

    public Realm getRealm() {
        return realm;
    }

    /**
     * create and show dialog for add team and players
     *
     * @param type 0-->TeamDialog
     *             1-->PlayerDialog
     */
    public void showNewTeamDialog(int type, DialogInterface dialogInterface, int teamID) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        this.dialogInterface = dialogInterface;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        System.out.println("_______DDDDDD__" + prev);
        if (prev != null) {
            System.out.println("_______DDDDDD__");
            ft.remove(prev);
            //  ((DialogFragment)prev).dismiss();
        }
        // ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment;
        if (type == 0)
            newFragment = NewTeamDialog.newInstance(0);
        else
            newFragment = NewPlayerDialog.newInstance(teamID);

        newFragment.show(ft, "dialog");


    }


    public void showMultiTeamSelect(int match_id, boolean ishomeTeam, int current_bowler) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        this.dialogInterface = dialogInterface;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = SelectTeamDialog.newInstance(match_id, ishomeTeam);
        //   ((SelectMultiPlayerDialog) newFragment).setDialogInterface(dialogInterface, match_id, ishomeTeam, current_bowler);
        System.out.println("______________cccc");

        newFragment.show(ft, "dialog");


    }

    public void closePrevSelectPlayer() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        System.out.println("_________________prev" + prev);
        if (prev != null) {
            System.out.println("_________________prevrr" + prev);
            ((DialogFragment) prev).dismiss();
            ft.remove(prev);

        }
    }

    public void showSelectplayer(int match_id, boolean ishomeTeam, Player current_bowler, String title, int assignTo) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        this.dialogInterface = dialogInterface;

        // ft.addToBackStack(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        closePrevSelectPlayer();
        int cb = -1;
        if (current_bowler != null)
            cb = current_bowler.getpID();
        // Create and show the dialog.
        DialogFragment newFragment = SelectPlayerDialog.newInstance(match_id, ishomeTeam, cb, title, assignTo);
        // ((SelectPlayerDialog) newFragment).setDialogInterface(dialogInterface, match_id, ishomeTeam, current_bowler,title);
//        if (type == 0)
//            newFragment = NewTeamDialog.newInstance();
//        else
//            newFragment = NewPlayerDialog.newInstance().setDialogInterface(dialogInterface);
        System.out.println("_________SHH");
        newFragment.show(ft, "dialog");


    }

    public void showOutDialog(int striker, int non_striker, int current_bowler, int matchDetails, float over) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        this.dialogInterface = dialogInterface;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = OutDialogFragment.newInstance(striker, non_striker, current_bowler, matchDetails, over);
        //   ((SelectMultiPlayerDialog) newFragment).setDialogInterface(dialogInterface, match_id, ishomeTeam, current_bowler);


        newFragment.show(ft, "dialog");


    }

    public void showMultiPlayerSelect(int match_id, boolean ishomeTeam, int current_bowler) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

        this.dialogInterface = dialogInterface;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = SelectMultiPlayerDialog.newInstance(match_id, ishomeTeam);
        //   ((SelectMultiPlayerDialog) newFragment).setDialogInterface(dialogInterface, match_id, ishomeTeam, current_bowler);


        newFragment.show(ft, "dialog");


    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment ff = getSupportFragmentManager().findFragmentByTag("dialog");
        System.out.println("_____PP" + 1);
        if (ff != null)
            ff.onActivityResult(requestCode, resultCode, data);
        else if (getSupportFragmentManager().findFragmentById(R.id.mainFrag) != null)
            getSupportFragmentManager().findFragmentById(R.id.mainFrag).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void msg(String s) {
        Fragment ff = getSupportFragmentManager().findFragmentById(realmstudy.R.id.mainFrag);
        if (ff instanceof MsgToFragment) {
            ((MsgToFragment) ff).msg(s);
        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, String data, String message, int assignTo) {
        Fragment ff = getSupportFragmentManager().findFragmentById(realmstudy.R.id.mainFrag);
        if (ff instanceof MsgFromDialog) {
            ((MsgFromDialog) ff).messageFromDialog(dialogType, success, data, message, assignTo);
        }
    }

    public void messageFromDialog(int dialogType, boolean success, String data, String message) {
        Fragment ff = getSupportFragmentManager().findFragmentById(realmstudy.R.id.mainFrag);
        if (ff instanceof MsgFromDialog) {
            ((MsgFromDialog) ff).messageFromDialog(dialogType, success, data, message);
        }
    }

    @Override
    public void messageFromDialog(int dialogType, boolean success, ArrayList<Integer> data, String message) {
        Fragment ff = getSupportFragmentManager().findFragmentById(realmstudy.R.id.mainFrag);
        if (ff instanceof MsgFromDialog) {
            ((MsgFromDialog) ff).messageFromDialog(dialogType, success, data, message);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_saved_game) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrag, new MatchListMainFragment()).commit();
        } else if (id == R.id.nav_viewer) {
            MatchListMainFragment f = new MatchListMainFragment();
            Bundle b = new Bundle();
            b.putBoolean("is_online", true);
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, f).commit();
        } else if (id == R.id.nav_schedule_game) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, new ScheduleNewGame()).commit();
        } else if (id == R.id.nav_add_player) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, new PlayerListFragment()).commit();
        } else if (id == R.id.nav_add_team) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, new TeamListFragment()).commit();
        } else if (id == R.id.nav_new_game) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.mainFrag, new MatchInfo()).commit();
        }

//
//        case R.id.new_game_menu_lay:
//        
//        break;
//        case R.id.saved_game_menu_lay:
//
//        
//        break;
//        case R.id.profile_menu_lay:
//        ViewMatch f=new ViewMatch();
////                Bundle b=new Bundle();
////                b.putInt("match_id",1492432485);
////                f.setArguments(b);
//       
//        break;
//        case R.id.schedule_game_menu_lay:
//        
//
//        break;
//        case R.id.add_player_lay:
//        
//        break;
//        case R.id.add_team_menu_lay:
//        
//        break;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void itemPicked(int id, String message) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrag);
        if (f != null)
            if (f instanceof ItemClickInterface)
                ((ItemClickInterface) f).itemPicked(id, message);
    }

}
