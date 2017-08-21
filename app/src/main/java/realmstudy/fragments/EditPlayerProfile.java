package realmstudy.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
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

import static com.facebook.GraphRequest.TAG;

/**
 * Created by developer on 15/6/17.
 */

public class EditPlayerProfile extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 10;//10
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 430;
    private static final int GALLERY_PICK = 321;
    private static final int CAMERA_PICK = 123;

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

    TextView name_center, textview_title;
    @Inject
    Realm realm;
    private Uri imageUri;
    private ImageView profile_image;
    private String destinationFileName = "profileImage";

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
        name_center = (TextView) v.findViewById(R.id.name_center);
        textview_title = (TextView) v.findViewById(R.id.textview_title);
        profile_image = (ImageView) v.findViewById(R.id.profile_image);
        save = (LinearLayout) v.findViewById(R.id.saveLay);
        mAppBarLayout.addOnOffsetChangedListener(this);
        if (getArguments() != null) {
            System.out.println("iddddddd" + getArguments().getInt("id"));
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

        } else
            System.out.println("idddddddnull");

        //  mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmDB.editPlayer(realm, playerName.getText().toString(), editdob.getText().toString(), playerEmail.getText().toString(), playerPhno.getText().toString(), playerBatStyle.getSelectedItemPosition(), playerBowlStyle.getSelectedItemPosition(), playerRole.getSelectedItemPosition(), pID);
                getActivity().onBackPressed();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
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
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((MainFragmentActivity) getActivity()).removeNaviHome();
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

                                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE' , 'dd' 'MMM yyyy");
                                String ss = sdf.format(calendar.getTime());
                                editdob.setText(ss);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }

    void getImage() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                return;
            } else
                getCamera();
        } catch (Exception e) {

            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void getCamera() {
        android.support.v7.app.AlertDialog.Builder dialogb = new android.support.v7.app.AlertDialog.Builder(getActivity()).setMessage("" + getResources().getString(R.string.choose_an_image)).setTitle("" + getResources().getString(R.string.profile_image)).setCancelable(true).setNegativeButton("" + getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                System.gc();
                final Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                getActivity().startActivityForResult(intent, GALLERY_PICK);
                dialog.cancel();
            }
        }).setPositiveButton("" + getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
                if (!photo.exists())
                    photo.mkdirs();
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                imageUri = Uri.fromFile(mediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                getActivity().startActivityForResult(intent, CAMERA_PICK);
            }
        });
        final android.support.v7.app.AlertDialog dialog = dialogb.create();
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface dialogs) {
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
        });
        dialog.show();
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
        try {
            //System.out.println("CametoEdit"+requestcode);
            if (requestcode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (resultcode == getActivity().RESULT_OK) {
                System.gc();
                switch (requestcode) {
                    case GALLERY_PICK:
                        try {
                            UCrop uCrop = UCrop.of(Uri.fromFile(new File(getRealPathFromURI(data.getDataString()))), Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)))
                                    .withAspectRatio(4, 4)
                                    .withMaxResultSize(100, 100);
                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.appbg));
                            options.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                            options.setToolbarWidgetColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                            uCrop.withOptions(options);
                            uCrop.start(getActivity());
                            //new ImageCompressionAsyncTask().execute(data.getDataString());
//                            CropImage.activity( Uri.parse(data.getDataString()))
//                                    .start(getContext(),this);
                        } catch (final Exception e) {
                        }
                        break;
                    case CAMERA_PICK:
                        try {
                            //  new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//                            CropImage.activity(imageUri)
//                                    .start(getContext(),this);

                            UCrop.of(imageUri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)))
                                    .withAspectRatio(4, 4)
                                    .withMaxResultSize(100, 100)
                                    .start(getActivity());
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        } catch (final Exception e) {
        }
    }

    private void handleCropResult(Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
            System.out.println("Hellow" + resultUri);
            Picasso.with(getActivity()).load(resultUri).error(R.drawable.calendar_new).into(profile_image);
            // new ImageCompressionAsyncTask().execute(resultUri.toString());
        } else {
            // Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(final String contentURI) {

        final Uri contentUri = Uri.parse(contentURI);
        final Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
