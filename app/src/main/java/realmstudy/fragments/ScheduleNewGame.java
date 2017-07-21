package realmstudy.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import realmstudy.GroundPickerActivity;
import realmstudy.MainActivity;
import realmstudy.MainFragmentActivity;
import realmstudy.MyApplication;
import realmstudy.R;
import realmstudy.TeamPickerActivity;
import realmstudy.data.CommanData;
import realmstudy.data.RealmObjectData.Ground;
import realmstudy.data.RealmObjectData.MatchDetails;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.data.RealmObjectData.Team;
import realmstudy.databaseFunctions.RealmDB;
import realmstudy.interfaces.MsgFromDialog;


/**
 * Created by developer on 6/3/17.
 */
public class ScheduleNewGame extends Fragment {

    private static final int WHATSAPP = 1;
    private static final int FACEBOOK = 2;
    private static final int GMAIL = 3;
    private static final int OTHER = 4;
    private static final int WRITE_PERMISSION = 154;
    TextView home_team, away_team, venue, desc, players, time;
    AppCompatButton save;

    DatePicker datePicker;
    TimePicker timePicker;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
    private Button set1;
    private Button set;
    private boolean askByHome;
    private Team homeTeam;
    private Team awayTeam;
    private AppCompatImageButton fb_share, whatsapp_share, gmail_share, other_share;
    private LinearLayout share_lay;
    private LinearLayout detail_lay;
    String OUTPUT_PATH = "";
    @Inject
    Realm realm;
    RealmList<Player> players_array;
    private long match_time;
    LinearLayout logo_lay;
    private TextView desc_head;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_list, container, false);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);
        OUTPUT_PATH = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name) + File.separator;


        players_array = new RealmList<>();
        home_team = (android.support.v7.widget.AppCompatButton) v.findViewById(R.id.home_team);
        away_team = (android.support.v7.widget.AppCompatButton) v.findViewById(R.id.away_team);
        venue = (TextView) v.findViewById(R.id.venue);
        desc = (TextView) v.findViewById(R.id.descc);
        players = (TextView) v.findViewById(R.id.players);
        time = (TextView) v.findViewById(R.id.times);
        logo_lay = (LinearLayout) v.findViewById(R.id.logo_lay);
        desc_head = (TextView) v.findViewById(R.id.desc);
        save = (AppCompatButton) v.findViewById(R.id.save);
        share_lay = (LinearLayout) v.findViewById(R.id.share_lay);
        detail_lay = (LinearLayout) v.findViewById(R.id.detail_lay);
        fb_share = (AppCompatImageButton) v.findViewById(R.id.fb_share);
        whatsapp_share = (AppCompatImageButton) v.findViewById(R.id.whatsapp_share);
        gmail_share = (AppCompatImageButton) v.findViewById(R.id.gmail_share);
        other_share = (AppCompatImageButton) v.findViewById(R.id.other_share);
        venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), GroundPickerActivity.class);
                getActivity().startActivityForResult(i, 30);
            }
        });
        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("______________cccc1");
                ((MainFragmentActivity) getActivity()).showMultiPlayerSelect(-1, false, -1);
            }
        });
        away_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TeamPickerActivity.class);
                Bundle b = new Bundle();
                i.putExtra("for", "away");
                getActivity().startActivityForResult(i, 20);
            }
        });
        home_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), TeamPickerActivity.class);
                Bundle b = new Bundle();
                i.putExtra("for", "home");
                getActivity().startActivityForResult(i, 20);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calDatePicker();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeTeam != null && awayTeam != null) {
                    if (match_time != 0) {
                        if (!venue.getText().toString().trim().equals("")) {
                            MatchDetails md = RealmDB.createNewMatch(getActivity(), realm, homeTeam, awayTeam, "", null, 0, venue.getText().toString(), 0, match_time);
                            OUTPUT_PATH += md.getMatch_id() + ".png";
                            RealmDB.addPlayerToMatch(players_array, getActivity(), realm, md);
                            share_lay.setVisibility(View.VISIBLE);
                            save.setVisibility(View.GONE);
                            logo_lay.setVisibility(View.VISIBLE);
                            desc_head.setText(desc.getText());
                            desc_head.setVisibility(View.GONE);
                            desc.setVisibility(View.GONE);
                            venue.clearFocus();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                venue.setBackgroundColor(getActivity().getColor(R.color.transperant));
                            } else
                                venue.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transperant));
                            venue.setCursorVisible(false);
                            realm.beginTransaction();
                            md.setDescription(desc.getText().toString());
                            realm.commitTransaction();
                            AppCompatImageButton buttons[] = {whatsapp_share, gmail_share, fb_share, other_share};

                            int i = 1;

                            for (AppCompatImageButton viewId : buttons) {
                                // Button imageButton = (Button) findViewById(viewId);
                                Animation fadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_anim);
                                fadeAnimation.setStartOffset(i * 200);
                                AppCompatImageButton butt = buttons[i - 1];
                                butt.startAnimation(fadeAnimation);

                                i++;
                            }


                        } else
                            Toast.makeText(getActivity(), getString(R.string.select_venue), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), getString(R.string.select_match_time), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), getString(R.string.select_valid_home_away_team), Toast.LENGTH_SHORT).show();
            }
        });
        fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(OTHER);
            }
        });
        whatsapp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                shareBitmap(WHATSAPP);
            }
        });


        gmail_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                shareBitmap(GMAIL);


            }
        });


        other_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                shareBitmap(OTHER);

            }
        });
        return v;
    }

    public Bitmap viewToBitmap(View view) {
        final boolean cachePreviousState = view.isDrawingCacheEnabled();
        final int backgroundPreviousColor = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheBackgroundColor(0xfffafafa);
        final Bitmap bitmap = view.getDrawingCache();
        view.setDrawingCacheBackgroundColor(backgroundPreviousColor);
        return bitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.schedule_game));
    }

    void shareBitmap(int type) {
        // File file = new File(getActivity().getCacheDir(), "fileImage" + ".png");
//            FileOutputStream fOut = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            fOut.flush();


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            }
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            Bitmap bitmap = viewToBitmap(detail_lay);

            try {
                FileOutputStream output = new FileOutputStream(OUTPUT_PATH);


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                output.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                File output = new File(OUTPUT_PATH);
                Uri outputUri = Uri.fromFile(output);
                System.out.println("_______PPP" + outputUri);
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, outputUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT, desc.getText() + "\n" + "www.Android.com/search?q=android");

                if (type == WHATSAPP) {
                    intent.setPackage("com.whatsapp");
                } else if (type == FACEBOOK) {
                    intent.setPackage("com.example.developer.fb");
                } else if (type == GMAIL) {
                    intent.setPackage("com.google.android.gm");
                }


                //  intent.putExtra(Intent.EXTRA_TEXT, "Hai");
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setType("image/jpeg");
                startActivity(Intent.createChooser(intent, "Share image using"));
            }
        }
    }

    void calDatePicker() {
        final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

        set = (Button) dialogView.findViewById(R.id.date_time_set);
        set1 = (Button) dialogView.findViewById(R.id.date_time_set1);
        System.out.println("Millisec___" + System.currentTimeMillis());
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
//        Date
        Calendar calendar = Calendar.getInstance();
        Calendar cc = new GregorianCalendar(calendar.get(Calendar.YEAR) + 1, 12, 31);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        datePicker.setMaxDate(cc.getTimeInMillis());
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dialogView.findViewById(R.id.date_time_set1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.VISIBLE);
                datePicker.setVisibility(View.GONE);
                set.setVisibility(View.VISIBLE);
                set1.setVisibility(View.GONE);

            }
        });
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            public Date sdate;

            @Override
            public void onClick(View view) {
                Calendar calendar;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    // only for gingerbread and newer versions
                    calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute());
                } else {
                    calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
                // Calendar calendar = Calendar.getInstance();
                match_time = calendar.getTimeInMillis() / 1000;
                System.out.println("SelectedTime---" + match_time);
                time.setText(CommanData.getDateCurrentTimeZone(match_time));
                // time.setText(dateFormat.format(calendar.getTimeInMillis()));
                alertDialog.dismiss();
            }
        });


        alertDialog.setView(dialogView);
        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 20) {
                final int teamID = data.getIntExtra("id", 0);
                home_team.post(new Runnable() {
                    @Override
                    public void run() {


                        if (data.getStringExtra("for").equals("home")) {
                            if (awayTeam != null && teamID == awayTeam.team_id) {
                                awayTeam = null;
                                away_team.setText("");
                            }
                            homeTeam = RealmDB.getTeam(realm, teamID);
                            home_team.setText(homeTeam.nick_name);
                        } else {
                            if (homeTeam != null && teamID == homeTeam.team_id) {
                                homeTeam = null;
                                home_team.setText("");
                            }
                            awayTeam = RealmDB.getTeam(realm, teamID);
                            away_team.setText(awayTeam.nick_name);
                        }
                    }
                });

            } else {
                final int groundId = data.getIntExtra("id", 0);
                Ground ground = RealmDB.getGround(realm, groundId);
                if (ground != null)
                    venue.setText(ground.getGroundName());
            }
        }
    }


//    @Override
//    public void messageFromDialog(int dialogType, boolean success, String data, String message) {
//        System.out.println("______vv" + data + "___" + dialogType);
//        if (dialogType == CommanData.DIALOG_SELECT_TEAM) {
//            System.out.println("______vv" + data);
//            int id = Integer.parseInt(data);
//            if (askByHome) {
//
//                if (away_team != null && id == away_team.getId()) {
//                    awayTeam = null;
//                    away_team.setText("");
//                }
//                homeTeam = RealmDB.getTeam(realm, id);
//                home_team.setText(homeTeam.nick_name);
//
//
//            } else {
//                if (homeTeam != null && id == home_team.getId()) {
//                    homeTeam = null;
//                    home_team.setText("");
//                }
//                awayTeam = RealmDB.getTeam(realm, id);
//                away_team.setText(awayTeam.nick_name);
//            }
//        }
//    }
//
//    @Override
//    public void messageFromDialog(int dialogType, boolean success, ArrayList<Integer> data, String message) {
//        //System.out.println("_________sss" + data);
//        if (dialogType == CommanData.DIALOG_SELECT_MULTI_PLAYER) {
//            String ss = "";
//            players_array.clear();
//            for (int i = 0; i < data.size(); i++) {
//                Player p = RealmDB.getPlayer(realm, data.get(i));
//                ss = ss + p.getName() + (i == (data.size() - 1) ? "" : ",");
//
//                players_array.add(p);
//                players.setText(ss);
//            }
//
//        }
//    }
//
//    @Override
//    public void messageFromDialog(int dialogType, boolean success, String data, String message, int assignTo) {
//
    //}
}
