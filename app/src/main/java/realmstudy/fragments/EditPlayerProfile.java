package realmstudy.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import io.realm.Realm;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.databaseFunctions.RealmDB;

/**
 * Created by developer on 15/6/17.
 */

public class EditPlayerProfile extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 10;//10

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ImageView DOBimg;
    private TextView editdob;

    private int mYear, mMonth, mDay, mHour, mMinute;
    int pID;
    Player player;
    private EditText playerPhno, playerEmail, playerName;
    private Spinner playerBatStyle, playerBowlStyle, playerRole;
    LinearLayout save;

    TextView name_center,textview_title;
    @Inject
    Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_player_profile, container, false);


        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        bindActivity(v);
        playerBatStyle = (Spinner) v.findViewById(R.id.player_bat_style);
        playerBowlStyle = (Spinner) v.findViewById(R.id.player_bowl_style);
        playerRole = (Spinner) v.findViewById(R.id.player_role);
        playerPhno = (EditText) v.findViewById(R.id.player_phno);
        playerName = (EditText) v.findViewById(R.id.player_name);
        playerEmail = (EditText) v.findViewById(R.id.player_email_id);
        name_center= (TextView) v.findViewById(R.id.name_center);
        textview_title= (TextView) v.findViewById(R.id.textview_title);
        save = (LinearLayout) v.findViewById(R.id.saveLay);
        mAppBarLayout.addOnOffsetChangedListener(this);
        if (getArguments() != null) {
            System.out.println("iddddddd"+getArguments().getInt("id"));
            pID = getArguments().getInt("id");
            player = RealmDB.getPlayer(realm, pID);
            name_center.setText(player.getName());
            textview_title.setText(player.getName());
            playerName.setText(player.getName());
            editdob.setText(player.getDob());
            playerPhno.setText(player.getPh_no());
            playerEmail.setText(player.getEmail());
            playerBatStyle.setSelection(player.getBattingSytle());
            playerBowlStyle.setSelection(player.getBowlingStyle());
            playerRole.setSelection(player.getRole());

        }else
            System.out.println("idddddddnull");

        //  mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmDB.editPlayer(realm, playerName.getText().toString(), editdob.getText().toString(),playerEmail.getText().toString(), playerPhno.getText().toString(), playerBatStyle.getSelectedItemPosition(), playerBowlStyle.getSelectedItemPosition(), playerRole.getSelectedItemPosition(), pID);
           getActivity().onBackPressed();
            }
        });


        return v;
    }

    @Override
    public void onStop() {
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((MainFragmentActivity)getActivity()).removeNaviHome();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private void bindActivity(View v) {
        mToolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        mTitle = (TextView) v.findViewById(R.id.textview_title);
        mTitleContainer = (LinearLayout) v.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) v.findViewById(R.id.main_appbar);


        editdob = (TextView) v.findViewById(R.id.set_date);
        DOBimg = (ImageView) v.findViewById(R.id.pick_date1);


        DOBimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar calendar=new GregorianCalendar(year,monthOfYear,dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE' , 'dd' 'MMM yyyy");
                                String ss = sdf.format(calendar.getTime());
                                editdob.setText(ss);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
