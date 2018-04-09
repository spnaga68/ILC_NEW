package realmstudy.extras;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Allocation.MipmapControl;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import realmstudy.R;

public class Utils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;




    public static String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:mm a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
    public static String getTimeOnly(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            Date netDate = (new Date(timeStamp*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
    public static String getDateOnly(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date netDate = (new Date(timeStamp*1000L));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    private static char[] f17c = new char[]{'K', 'M', 'G'};

    static class C05572 implements OnClickListener {
        C05572() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public static Date getDateFromMillis(long date, String dateFormatte) {
        ParseException e;
        try {
            Date value = new Date(1000 * date);
            Date date2;
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatte);
                dateFormatter.setTimeZone(TimeZone.getDefault());
                String dt = dateFormatter.format(value);
                date2 = value;
                return value;
            } catch (ParseException e2) {
                e = e2;
                date2 = value;
                e.printStackTrace();
                return null;
            }
        } catch (ParseException e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String data, Class<T> classn) {

        return new Gson().fromJson(data, classn);
    }

    public static String toString(Object s) {
        return new Gson().toJson(s);
    }

    public static void findTextViewTitle(String title, ActionBar ab, AppCompatActivity activity) {
        try {
            View decor = activity.getWindow().getDecorView();
            ArrayList<View> views = new ArrayList();
            decor.findViewsWithText(views, title, 1);
            AppCompatTextView tvTitle = null;
            Iterator it = views.iterator();
            while (it.hasNext()) {
                View view = (View) it.next();
                Log.d("ACTION BAR", "view " + view.toString());
                if (view instanceof AppCompatTextView) {
                    tvTitle = (AppCompatTextView) view;
                }
            }
            if (tvTitle != null) {
                tvTitle.setEllipsize(TruncateAt.MARQUEE);
                tvTitle.setFocusable(true);
                tvTitle.setFocusableInTouchMode(true);
                tvTitle.requestFocus();
                tvTitle.setSingleLine(true);
                tvTitle.setSelected(true);
                tvTitle.setMarqueeRepeatLimit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static Dialog showProgress(Context activity, boolean show) {
//        Exception e;
//        Dialog dialog = null;
//        try {
//            Dialog dialog2 = new Dialog(activity, R.style.ProgressAppTheme);
//            try {
//                dialog2.requestWindowFeature(1);
//                dialog2.setContentView(R.layout.custome_progress_dialog);
//               // dialog2.getWindow().setBackgroundDrawableResource(17170445);
//                ProgressBar progressBar = (ProgressBar) dialog2.findViewById(R.id.dialogProgressBar);
//                if (!show) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                }
//                dialog2.show();
//                dialog2.setCancelable(false);
//                return dialog2;
//            } catch (Exception e2) {
//                e = e2;
//                dialog = dialog2;
//                e.printStackTrace();
//                return dialog;
//            }
//        } catch (Exception e3) {
//            e = e3;
//            e.printStackTrace();
//            return dialog;
//        }
//    }

    public static ProgressDialog showProgress(Activity activity, String message, boolean cancelable) {
        if (activity == null) {
            return null;
        }
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(0);
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideProgress(Dialog dialog) {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Uri getImageUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    public static void showToast(Context context, String string, int type, boolean isLong) {
        int i = 1;
        if (context != null) {
            View layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            TextView textIcon = (TextView) layout.findViewById(R.id.toast_image);
            CardView cardView = (CardView) layout.findViewById(R.id.card_view);
            ((TextView) layout.findViewById(R.id.toast_text)).setText(string);
            if (type == 1) {
                textIcon.setText(context.getString(R.string.error_icon));
                cardView.setCardBackgroundColor(Color.parseColor("#e64a19"));
            } else if (type == 2) {
                textIcon.setText(context.getString(R.string.right_icon));
                cardView.setCardBackgroundColor(Color.parseColor("#26BE9B"));
            } else if (type == 3) {
                textIcon.setText(context.getString(R.string.warning_icon));
                cardView.setCardBackgroundColor(Color.parseColor("#ffa000"));
            }
            Toast toast = Toast.makeText(context, "", 0);
            if (!isLong) {
                i = 0;
            }
            toast.setDuration(i);
            toast.setView(layout);
            toast.show();
        }
    }

    public static float getScreenDensity(Context context) {
        float _density = context.getResources().getDisplayMetrics().density;
        if (_density == 0.0f) {
            return 1.0f;
        }
        return _density;
    }

    public static final boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        boolean connected;
        NetworkInfo nf = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (nf == null || !(nf.isConnected() || nf.isConnectedOrConnecting())) {
            connected = false;
        } else {
            connected = true;
        }
        return connected;
    }

    public static String getFormatedValue(long value) {
        return new DecimalFormat("###,##,##,##0").format(value);
    }

    public static boolean isEmpty(EditText edt) {
        if (edt.getText().toString().trim().length() > 0) {
            return true;
        }
        return false;
    }

//    public static boolean isEmailValid(String email) {
//        if (Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$", 2).matcher(email).matches()) {
//            return true;
//        }
//        return false;
//    }
//
//    public static String numberFormat(double number, int iteration) {
//        if (number <= 999.0d) {
//            return "" + ((int) number);
//        }
//        double d = ((double) (((long) number) / 100)) / 10.0d;
//        boolean isRound = (d * 10.0d) % 10.0d == 0.0d;
//        if (d >= 1000.0d) {
//            return numberFormat(d, iteration + 1);
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        Object valueOf = (d > 99.9d || isRound || (!isRound && d > 9.99d)) ? Integer.valueOf((((int) d) * 10) / 10) : d + "";
//        return stringBuilder.append(valueOf).append("").append(f17c[iteration]).toString();
//    }
//
//    public static SpannableString makeSpannableText(String teamASummary) {
//        int start;
//        SpannableString string = new SpannableString(teamASummary);
//        if (teamASummary.contains("&")) {
//            start = teamASummary.lastIndexOf("&") + 1;
//        } else {
//            start = 0;
//        }
//        string.setSpan(new ForegroundColorSpan(Color.parseColor("#41AA85")), start, teamASummary.length(), 0);
//        return string;
//    }
//
//    public static void hideSoftKeyboard(Activity context) {
//        if (context.getCurrentFocus() != null) {
//            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
//        }
//    }
//
//    public static void hideKeyboard(Context context, View v) {
//        if (v != null) {
//            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 2);
//        }
//    }
//
//    public static void showKeyboard(Context context, View v) {
//        if (v != null) {
//            ((InputMethodManager) context.getSystemService("input_method")).showSoftInput(v, 1);
//            System.out.println("sho");
//        }
//    }
//
//    public static void showKeyboardForce(Context context, View v) {
//        if (v != null) {
//            ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInputFromWindow(v.getApplicationWindowToken(), 2, 0);
//        }
//    }
//
//    public static boolean validatePassword(String password) {
//        return password.length() > 6;
//    }
//
//    public static String getUniqueImageFilename() {
//        return "file1";
//    }
//
//    public static boolean checkPermission(final Context context, String title, String message, final String permission, final int Permission_code) {
//        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, permission) == 0) {
//            return true;
//        }
//        Builder alertBuilder = new Builder(context);
//        alertBuilder.setCancelable(true);
//        alertBuilder.setTitle((CharSequence) title);
//        alertBuilder.setMessage((CharSequence) message);
//        alertBuilder.setPositiveButton((CharSequence) "ALLOW", new OnClickListener() {
//            @TargetApi(16)
//            public void onClick(DialogInterface dialog, int which) {
//                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, Permission_code);
//            }
//        });
//        alertBuilder.setNegativeButton((CharSequence) "DENY", new C05572());
//        alertBuilder.create().show();
//        return false;
//    }
//
//    public static String getCurrentLocality(Context context, Location location) {
//        try {
//            List<Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                return ((Address) addresses.get(0)).getLocality();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//        return "";
//    }
//
//    public static void showAlert(Context context, String title, String message, String positiveButtonTitle, String nagativeButtonTitle, OnClickListener dialogClickListener, boolean isCancelable) {
//        if (!((Activity) context).isFinishing()) {
//            try {
//                Builder ab = new Builder(context, R.style.CustomAlertDialogStyle);
//                View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.raw_alert_dialog_custom, null);
//                ab.setView(dialogView);
//                ab.setCancelable(isCancelable);
//                TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
//                TextView tvDescription = (TextView) dialogView.findViewById(R.id.tvMsg);
//                if (title.length() == 0) {
//                    tvTitle.setVisibility(8);
//                } else {
//                    tvTitle.setText(title);
//                }
//                if (nagativeButtonTitle.length() != 0) {
//                    ab.setNegativeButton((CharSequence) nagativeButtonTitle, dialogClickListener);
//                }
//                System.out.println("message ...d"+message.replace("\\n","<br>"));
//                tvDescription.setText(Html.fromHtml(message.replace("\\n","<br>")));
//                ab.setPositiveButton((CharSequence) positiveButtonTitle, dialogClickListener);
//                ab.create().show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static Bitmap takeScreenShot(Activity activity) {
//        View view = activity.getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        Bitmap b1 = view.getDrawingCache();
//        Rect frame = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, size.x, size.y - statusBarHeight);
//        view.destroyDrawingCache();
//        return b;
//    }
//
////    public static String md5(String s) {
////        System.out.println("PIN " + s);
////        if (isEmptyString(s)) {
////            return "";
////        }
////        String md5 = "";
////        try {
////            MessageDigest digest = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE);
////            digest.update(s.getBytes());
////            byte[] messageDigest = digest.digest();
////            StringBuffer hexString = new StringBuffer();
////            for (byte b : messageDigest) {
////                String h = Integer.toHexString(b & 255);
////                while (h.length() < 2) {
////                    h = "0" + h;
////                }
////                hexString.append(h);
////            }
////            md5 = hexString.toString();
////            System.out.println("MD 5 " + md5);
////            return md5;
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////            System.out.println("MD 5 " + md5);
////            return md5;
////        }
////    }
//
    public static void showAlert(Context context, String title, CharSequence message, String positiveButtonTitle, String nagativeButtonTitle, OnClickListener dialogClickListener, boolean isCancelable, DialogInterface.OnCancelListener onCancelListener) {
        if (context != null) {
            Builder ab = new Builder(context, R.style.CustomAlertDialogStyle);
            View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.raw_alert_dialog_custom, null);
            ab.setView(dialogView);
            ab.setCancelable(isCancelable);
            TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
            TextView tvDescription = (TextView) dialogView.findViewById(R.id.tvMsg);
            if (title.length() == 0) {
                tvTitle.setVisibility(8);
            } else {
                tvTitle.setText(title);
            }
            if (nagativeButtonTitle.length() != 0) {
                ab.setNegativeButton((CharSequence) nagativeButtonTitle, dialogClickListener);
            }
            tvDescription.setText(Html.fromHtml(message.toString()));
            ab.setPositiveButton((CharSequence) positiveButtonTitle, dialogClickListener);
            AlertDialog dialog = ab.create();
            dialog.setOnCancelListener(onCancelListener);
            dialog.show();
        }
    }
//
    public static boolean isEmptyString(String myString) {
        if (myString == null || myString.equals("") || myString.toString().trim().length() <= 0) {
            return true;
        }
        return false;
    }
//
////    public static String udid(Context c) {
////        return Secure.getString(CricHeroes.getApp().getContentResolver(), "android_id");
////    }
//
////    public static boolean isEmulator() {
////        return Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains(CommonUtils.GOOGLE_SDK) || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || CommonUtils.GOOGLE_SDK.equals(Build.PRODUCT));
////    }
//
//    public static String getCurrentDateTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return sdf.format(new Date());
//    }
//
//    public static String getMyCurrentDateTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a");
//        sdf.setTimeZone(TimeZone.getDefault());
//        return sdf.format(new Date());
//    }
//
//    public static String getCurrentDateTimeCurrentTimeZone() {
//        SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM dd yyyy hh:mm a");
//        sdf.setTimeZone(TimeZone.getDefault());
//        Date date = new Date();
//        date.setTime(date.getTime() + 600000);
//        return sdf.format(date);
//    }
//
//    public static String getUTCTimeZoneDate(String date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("EE, MMM dd yyyy hh:mm a");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        try {
//            Date date1 = sdf.parse(date);
//            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
//            date = sdf1.format(date1);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    public static String getCurrentTimeZoneDate(String date) {
//        System.out.println("time date  " + date);
//        SimpleDateFormat sdf1 = new SimpleDateFormat("EE, MMM dd yyyy hh:mm a");
//        try {
//            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date);
//            sdf1.setTimeZone(TimeZone.getDefault());
//            String newDate = sdf1.format(date1);
//            System.out.println("time date new " + newDate);
//            return newDate;
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return date;
//        }
//    }
//
//    public static int getResourceFromName(Context context, String resourceName) {
//        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
//    }
//
//    public static int convertDp2Pixels(Context context, int dp) {
//        return (int) TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
//    }
//
//    public static String changeDateformate(String strDate) {
//        if (strDate == null || strDate.length() == 0) {
//            return "";
//        }
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String str = "";
//        try {
//            return new SimpleDateFormat("dd MMM, yyyy").format(inputFormat.parse(strDate));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return str;
//        }
//    }
//
//    public static String changeDateformateForDatePicker(String strDate) {
//        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
//        String str = "";
//        try {
//            str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inputFormat.parse(strDate));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    public static String findTextWithRegex(String text, String regexPattern) {
//        Matcher m = Pattern.compile(regexPattern).matcher(text);
//        if (m.find()) {
//            return m.group();
//        }
//        return null;
//    }
//
//    public static String getMimeTypeFromUri(Context context, Uri uri) {
//        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri.toString()).toLowerCase());
//    }
//
////    public static boolean backupDatabaseToSdcard(Context context, String packageName, String databaseName) {
////        try {
////            File sd = Environment.getExternalStorageDirectory();
////            File data = Environment.getDataDirectory();
////            if (sd.canWrite()) {
////                File currentDB = new File(data, "//data//" + packageName + "//databases//" + databaseName);
////                Logger.m177d("current database path: %s %nbackup database path: %s", currentDB, new File(sd, databaseName));
////                if (currentDB.exists()) {
////                    FileChannel src = new FileInputStream(currentDB).getChannel();
////                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
////                    dst.transferFrom(src, 0, src.size());
////                    src.close();
////                    dst.close();
////                }
////                Logger.m176d("database backed up...");
////                return true;
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return false;
////    }
//
//    public static String changeDateformate(String strDate, String inputPattern, String outputPattern) {
//        if (isEmptyString(strDate)) {
//            return "";
//        }
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
//        outputFormat.setTimeZone(TimeZone.getDefault());
//        String str = "";
//        try {
//            return outputFormat.format(inputFormat.parse(strDate));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return str;
//        }
//    }
//
//    public static String formatToToday(String strDate, String inputPattern, String outputPattern) {
//        if (isEmptyString(strDate)) {
//            return "";
//        }
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        try {
//            Date dateTime = inputFormat.parse(strDate);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(dateTime);
//            Calendar today = Calendar.getInstance();
//            Calendar.getInstance().add(5, -1);
//            DateFormat timeFormatter = new SimpleDateFormat("HH:mm a");
//            DateFormat timeFormatter1 = new SimpleDateFormat(outputPattern);
//            if (calendar.get(1) == today.get(1) && calendar.get(6) == today.get(6)) {
//                return "Today, " + timeFormatter.format(dateTime);
//            }
//            return timeFormatter1.format(dateTime);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    public static String changeDateformateWithSuffix(String strDate, String inputPattern, String outputPattern) {
//        if (isEmptyString(strDate)) {
//            return "";
//        }
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String str = "";
//        try {
//            str = new SimpleDateFormat(outputPattern).format(inputFormat.parse(strDate));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return getFormatedDate(strDate, outputPattern);
//    }
//
//    private static String getFormatedDate(String strDate, String inputPattern) {
//        if (isEmptyString(strDate)) {
//            return "";
//        }
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("d").parse(strDate);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return new SimpleDateFormat(" d'" + getDayNumberSuffix(cal.get(5)) + "' " + inputPattern).format(date);
//    }
//
//    private static String getDayNumberSuffix(int day) {
//        if (day >= 11 && day <= 13) {
//            return "th";
//        }
//        switch (day % 10) {
//            case 1:
//                return "st";
//            case 2:
//                return "nd";
//            case 3:
//                return "rd";
//            default:
//                return "th";
//        }
//    }
//
//    public static float getEconomy(int run, String overPlayer) {
//        float balls;
//        if (overPlayer.contains(".")) {
//            balls = ((float) (Integer.parseInt(overPlayer.split("\\.")[0]) * 6)) + ((float) Integer.parseInt(overPlayer.split("\\.")[1]));
//        } else {
//            balls = (float) (Integer.parseInt(overPlayer) * 6);
//        }
//        return (((float) run) / balls) * 6.0f;
//    }
//
//    public static Date getDateFromString(String matchDateTime, String dateFormat) {
//        Logger.i("matchDateTime  " + matchDateTime);
//        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
//        try {
//            if (isEmptyString(matchDateTime)) {
//                return new Date();
//            }
//            return format.parse(matchDateTime);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static void setResizedImage(String imagePath, ImageView imgVTeamProfilePicture) {
//        Options options = new Options();
//        options.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        int height = bitmap.getHeight();
//        int width = bitmap.getWidth();
//        if (height <= 1280 || width <= 960) {
//            imgVTeamProfilePicture.setImageBitmap(bitmap);
//            System.out.println("WORKS");
//            return;
//        }
//        imgVTeamProfilePicture.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
//        System.out.println("Need to resize");
//    }
//
//    public static String getOver(String totalOver, boolean check) {
//        int over;
//        int ball;
//        Log.e("totalOver", "= " + totalOver);
//        if (totalOver.contains(".")) {
//            over = Integer.parseInt(totalOver.split("\\.")[0]);
//            ball = Integer.parseInt(totalOver.split("\\.")[1]);
//            if (!check) {
//                if (ball == 5) {
//                    over++;
//                    ball = 0;
//                } else {
//                    ball++;
//                }
//            }
//        } else {
//            over = Integer.valueOf(totalOver).intValue();
//            ball = 1;
//        }
//        return over + "." + ball;
//    }
//
    public static Bitmap fastblur(Bitmap sentBitmap, Context context) {
        try {
            if (VERSION.SDK_INT >= 17) {
                RenderScript rs = RenderScript.create(context);
                Bitmap blurredBitmap = sentBitmap.copy(Config.ARGB_8888, true);
                Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, MipmapControl.MIPMAP_FULL, 128);
                Allocation output = Allocation.createTyped(rs, input.getType());
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setInput(input);
                script.setRadius(20.0f);
                script.forEach(output);
                output.copyTo(blurredBitmap);
                return blurredBitmap;
            }
            sentBitmap = Bitmap.createScaledBitmap(sentBitmap, Math.round(((float) sentBitmap.getWidth()) * 1.0f), Math.round(((float) sentBitmap.getHeight()) * 1.0f), false);
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            if (20 < 1) {
                return null;
            }
            int i;
            int y;
            int bsum;
            int gsum;
            int rsum;
            int boutsum;
            int goutsum;
            int routsum;
            int binsum;
            int ginsum;
            int rinsum;
            int p;
            int[] sir;
            int rbs;
            int stackpointer;
            int x;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pix = new int[(w * h)];
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = 40 + 1;
            int[] r = new int[wh];
            int[] g = new int[wh];
            int[] b = new int[wh];
            int[] vmin = new int[Math.max(w, h)];
            int divsum = (42 >> 1) * 21;
            int[] dv = new int[112896];
            for (i = 0; i < 112896; i++) {
                dv[i] = i / divsum;
            }
            int yi = 0;
            int yw = 0;
            int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, new int[]{div, 3});
            int r1 = 20 + 1;
            for (y = 0; y < h; y++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                for (i = -20; i <= 20; i++) {
                    p = pix[Math.min(wm, Math.max(i, 0)) + yi];
                    sir = stack[i + 20];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & 255;
                    rbs = 21 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = 20;
                for (x = 0; x < w; x++) {
                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - 20) + 41) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (y == 0) {
                        vmin[x] = Math.min((x + 20) + 1, wm);
                    }
                    p = pix[vmin[x] + yw];
                    sir[0] = (16711680 & p) >> 16;
                    sir[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & p) >> 8;
                    sir[2] = p & 255;
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer % div];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                bsum = 0;
                gsum = 0;
                rsum = 0;
                boutsum = 0;
                goutsum = 0;
                routsum = 0;
                binsum = 0;
                ginsum = 0;
                rinsum = 0;
                int yp = (-20) * w;
                for (i = -20; i <= 20; i++) {
                    yi = Math.max(0, yp) + x;
                    sir = stack[i + 20];
                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];
                    rbs = 21 - Math.abs(i);
                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = 20;
                for (y = 0; y < h; y++) {
                    pix[yi] = (((ViewCompat.MEASURED_STATE_MASK & pix[yi]) | (dv[rsum] << 16)) | (dv[gsum] << 8)) | dv[bsum];
                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;
                    sir = stack[((stackpointer - 20) + 41) % div];
                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];
                    if (x == 0) {
                        vmin[y] = Math.min(y + 21, hm) * w;
                    }
                    p = x + vmin[y];
                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;
                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];
                    yi += w;
                }
            }
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return sentBitmap;
        } catch (Exception e2) {
            return sentBitmap;
        }
    }

////    public static void showFullImage(Context context, String imgUrl) {
////        if (context != null) {
////            Intent intent = new Intent(context, ImageDetailActivity.class);
////            if (isEmptyString(imgUrl)) {
////                intent.putExtra("userPicUrl", "");
////            } else {
////                intent.putExtra("userPicUrl", imgUrl);
////            }
////            context.startActivity(intent);
////        }
////    }
//
////    public static void selectImage(Context context, final OnPhotoSelect onPhotoSelect, boolean isVideoOption, String title) {
////        final Dialog dialog = new Dialog(context);
////        dialog.requestWindowFeature(1);
////        dialog.setContentView(R.layout.dialog_select_image);
////        ((TextView) dialog.findViewById(R.id.txt_title)).setText(title);
////        RelativeLayout rel_camera = (RelativeLayout) dialog.findViewById(R.id.rel_camera);
////        ((LinearLayout) dialog.findViewById(R.id.layVideo)).setVisibility(isVideoOption ? 0 : 8);
////        rel_camera.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                dialog.dismiss();
////                onPhotoSelect.onCameraClick();
////            }
////        });
////        ((RelativeLayout) dialog.findViewById(R.id.rel_gallery)).setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                dialog.dismiss();
////                onPhotoSelect.onGalleryClick();
////            }
////        });
////        ((RelativeLayout) dialog.findViewById(R.id.rel_video)).setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                dialog.dismiss();
////                onPhotoSelect.onVideoClick();
////            }
////        });
////        dialog.show();
////    }
//
//    public static int wordCount(String s) {
//        if (s == null) {
//            return 0;
//        }
//        return s.trim().split("\\s+").length;
//    }
//
//    public static String readableFileSize(long size) {
//        if (size <= 0) {
//            return size + " B";
//        }
//        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
//        return new DecimalFormat("#,##0.##").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)) + " " + new String[]{"B", "KB", "MB", "GB", "TB"}[digitGroups];
//    }
//
//    public static String fileSize(File file) {
//        return readableFileSize(file.length());
//    }
//
////    public static SpannableString getFirstInnSupTabText(String s) {
////        SpannableString string = new SpannableString(s);
////        int start = s.lastIndexOf(AppEventsConstants.EVENT_PARAM_VALUE_YES) + 1;
////        int end = start + 2;
////        System.out.println("start " + start);
////        System.out.println("end " + end);
////        string.setSpan(new SuperscriptSpan(), start, end, 0);
////        string.setSpan(new RelativeSizeSpan(0.8f), start, end, 0);
////        System.out.println("string " + string);
////        return string;
////    }
//
//    public static SpannableString getSecondInnSupTabText(String s) {
//        SpannableString string = new SpannableString(s);
//        int start = s.lastIndexOf("2") + 1;
//        int end = start + 2;
//        System.out.println("start " + start);
//        System.out.println("end " + end);
//        string.setSpan(new SuperscriptSpan(), start, end, 0);
//        string.setSpan(new RelativeSizeSpan(0.8f), start, end, 0);
//        System.out.println("string " + string);
//        return string;
//    }
//
//    public static SpannableString getInningText(int inning) {
//        String title = "";
//        switch (inning) {
//            case 1:
//            case 2:
//                title = "1st Innings";
//                break;
//            case 3:
//            case 4:
//                title = "2nd Innings";
//                break;
//        }
//        SpannableString string = new SpannableString(title);
//        int end = 1 + 2;
//        System.out.println("start " + 1);
//        System.out.println("end " + end);
//        string.setSpan(new SuperscriptSpan(), 1, end, 0);
//        string.setSpan(new RelativeSizeSpan(0.8f), 1, end, 0);
//        System.out.println("string " + string);
//        return string;
//    }
//
////    public static String getTeamName(Match match, MatchScore battingTeamMatchDetail) {
////        return battingTeamMatchDetail.getFkTeamId() == match.getFkATeamID() ? match.getTeamAName() : match.getTeamBName();
////    }
////
////    public static void ShowAlertCustom(Activity contex, String title, String message, String info, String positive, String negative, String natural, boolean cancelable, View.OnClickListener listener, boolean isDismiss) {
////        Builder dialogBuilder = new Builder(contex, R.style.CustomAlertDialogStyle);
////        View dialogView = contex.getLayoutInflater().inflate(R.layout.raw_dialog_fragment_sync_fail, null);
////        dialogBuilder.setView(dialogView);
////        final AlertDialog dialog = dialogBuilder.create();
////        dialog.setCancelable(cancelable);
////        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
////        TextView tvInfo = (TextView) dialogView.findViewById(R.id.tvInfo);
////        Button btnPositive = (Button) dialogView.findViewById(R.id.btnPositive);
////        Button btnNegative = (Button) dialogView.findViewById(R.id.btnNegative);
////        Button btnNatural = (Button) dialogView.findViewById(R.id.btnNatural);
////        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText(title);
////        tvMessage.setText(message);
////        tvMessage.setMovementMethod(new ScrollingMovementMethod());
////        if (isEmptyString(positive)) {
////            btnPositive.setVisibility(8);
////        } else {
////            btnPositive.setVisibility(0);
////            btnPositive.setText(positive);
////        }
////        if (isEmptyString(negative)) {
////            btnNegative.setVisibility(8);
////        } else {
////            btnNegative.setVisibility(0);
////            btnNegative.setText(negative);
////        }
////        if (isEmptyString(natural)) {
////            btnNatural.setVisibility(8);
////        } else {
////            btnNatural.setVisibility(0);
////            btnNatural.setText(natural);
////        }
////        if (isEmptyString(info)) {
////            tvInfo.setVisibility(8);
////        } else {
////            tvInfo.setVisibility(0);
////            tvInfo.setText(info);
////        }
////        final boolean z = isDismiss;
////        final View.OnClickListener onClickListener = listener;
////        btnPositive.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                if (z) {
////                    dialog.dismiss();
////                }
////                onClickListener.onClick(view);
////            }
////        });
////        final View.OnClickListener onClickListener2 = listener;
////        btnNegative.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                dialog.dismiss();
////                onClickListener2.onClick(view);
////            }
////        });
////        onClickListener2 = listener;
////        btnNatural.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View view) {
////                dialog.dismiss();
////                onClickListener2.onClick(view);
////            }
////        });
////        dialog.show();
////    }
////
////    public static String getTeamNameTossWon(Match match) {
////        String name = "";
////        if (match.getFkATeamID() == match.getFkTossWonTeamID()) {
////            return match.getTeamAName();
////        }
////        if (match.getFkBTeamID() == match.getFkTossWonTeamID()) {
////            return match.getTeamBName();
////        }
////        return name;
////    }
////
////    public static String getTossWonTeamDisision(Match match) {
////        if (match.getFkTossWonTeamID() == match.getFkBatFirstTeamID()) {
////            return "bat";
////        }
////        if (match.getFkTossWonTeamID() == match.getFkFieldFirstTeamID()) {
////            return "field";
////        }
////        return "";
////    }
////
////    public static String getInnSupText(String inning) {
////        if (inning.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_YES)) {
////            inning = inning + "st Inn";
////        } else if (inning.equalsIgnoreCase("2")) {
////            inning = inning + "nd Inn";
////        } else if (inning.equalsIgnoreCase("3")) {
////            inning = inning + "rd Inn";
////        } else if (inning.equalsIgnoreCase(RunType.BOUNDRY_4)) {
////            inning = inning + "th Inn";
////        }
////        SpannableString string = new SpannableString(inning);
////        string.setSpan(new SuperscriptSpan(), 1, 3, 0);
////        string.setSpan(new RelativeSizeSpan(0.8f), 1, 3, 0);
////        return string.toString();
////    }
////
////    public static void setTypeFace(Context context, TextView view, String string) {
////        context.getString(R.string.font_sourcesans_pro_semibold);
////        view.setTypeface(Typeface.createFromAsset(context.getAssets(), string));
////    }
////
    public static void activityChangeAnimationSlide(Activity activity, boolean isStart) {
        if (isStart) {
            activity.overridePendingTransition(R.anim.activity_start_in, R.anim.activity_start_out);
        } else {
            activity.overridePendingTransition(R.anim.activity_end_in, R.anim.activity_end_out);
        }
    }
//
////    public static void activityChangeAnimationBottom(Activity activity, boolean isStart) {
////        if (isStart) {
////            activity.overridePendingTransition(R.anim.slide_up, R.anim.hold);
////        } else {
////            activity.overridePendingTransition(R.anim.hold, R.anim.slide_down);
////        }
////    }
////
    public static void finishActivitySlide(Activity activity) {
        activity.finish();
        activityChangeAnimationSlide(activity, false);
    }
////
////    public static void finishActivityBottom(Activity activity) {
////        activity.finish();
////        activityChangeAnimationBottom(activity, false);
////    }
////
////    public static void openInAppBrowser(Activity activity, String url) {
////        new FinestWebView.Builder(activity).theme(R.style.FinestWebViewTheme).titleDefault("").showUrl(false).titleSize(24).titleFont(activity.getString(R.string.font_roboto_slab_regular)).statusBarColorRes(R.color.colorPrimaryDark).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.finestWhite).urlColorRes(R.color.colorAccentSelected).iconDefaultColorRes(R.color.finestWhite).progressBarColorRes(R.color.orange).stringResCopiedToClipboard(R.string.copied_to_clipboard).showSwipeRefreshLayout(true).swipeRefreshColorRes(R.color.colorPrimaryDark).menuSelector(R.drawable.selector_light_theme).titleSizeRes(R.dimen.sp_20).menuTextGravity(3).menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft).dividerHeight(2).gradientDivider(false).setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down).show(url);
////    }
////
////    public static Intent startNotificationActivity(Context context, String type, int id, boolean isReturnIntent, boolean isPush) {
////        boolean z = true;
////        Intent intent = null;
////        if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH)) {
////            intent = new Intent(context, ScoreBoardActivity.class);
////            intent.putExtra(AppConstants.EXTRA_SHOW_HEROES, true);
////            intent.putExtra(AppConstants.EXTRA_FROM_MATCH, true);
////            intent.putExtra(AppConstants.EXTRA_MATCH_ID, id);
////            intent.putExtra(AppConstants.EXTRA_FROM_NOTIFICATION, true);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH_PLAYER_HEROES)) {
////            intent = new Intent(context, ScoreBoardActivity.class);
////            intent.putExtra(AppConstants.EXTRA_SHOW_HEROES, true);
////            intent.putExtra(AppConstants.EXTRA_FROM_MATCH, true);
////            intent.putExtra(AppConstants.EXTRA_MATCH_ID, id);
////            intent.putExtra(AppConstants.EXTRA_POSITION, 0);
////            intent.putExtra(AppConstants.EXTRA_FROM_NOTIFICATION, true);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH_BATSMAN_HEROES)) {
////            intent = new Intent(context, ScoreBoardActivity.class);
////            intent.putExtra(AppConstants.EXTRA_SHOW_HEROES, true);
////            intent.putExtra(AppConstants.EXTRA_FROM_MATCH, true);
////            intent.putExtra(AppConstants.EXTRA_MATCH_ID, id);
////            intent.putExtra(AppConstants.EXTRA_POSITION, 1);
////            intent.putExtra(AppConstants.EXTRA_FROM_NOTIFICATION, true);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH_BOWLER_HEROES)) {
////            intent = new Intent(context, ScoreBoardActivity.class);
////            intent.putExtra(AppConstants.EXTRA_SHOW_HEROES, true);
////            intent.putExtra(AppConstants.EXTRA_FROM_MATCH, true);
////            intent.putExtra(AppConstants.EXTRA_MATCH_ID, id);
////            intent.putExtra(AppConstants.EXTRA_POSITION, 2);
////            intent.putExtra(AppConstants.EXTRA_FROM_NOTIFICATION, true);
////        } else if (type.equalsIgnoreCase("TEAM")) {
////            intent = new Intent(context, TeamDetailProfileActivity.class);
////            intent.putExtra(AppConstants.EXTRA_TEAM_ID, "" + id);
////        } else if (type.equalsIgnoreCase("PLAYER")) {
////            intent = new Intent(context, PlayerProfileActivity.class);
////            if (CricHeroes.getApp().isGuestUser()) {
////                intent.putExtra(AppConstants.EXTRA_MY_PROFILE, false);
////            } else {
////                String str = AppConstants.EXTRA_MY_PROFILE;
////                if (id != CricHeroes.getApp().getCurrentUser().getUserId()) {
////                    z = false;
////                }
////                intent.putExtra(str, z);
////            }
////            intent.putExtra(AppConstants.EXTRA_PLAYER_ID, id);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_UPCOMING_MATCH) || type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_UPCOMING_MATCH_SCHEDULE)) {
////            if (isPush) {
////                intent = new Intent(context, MatchesActivity.class);
////            } else {
////                intent = new Intent(context, MatchTeamActivity.class);
////                intent.putExtra(AppConstants.EXTRA_MATCHID, id);
////                intent.putExtra(AppConstants.FROM_NOTIFICATION, true);
////            }
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_HEROES_TOURNAMENT) || type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_BATSMAN_TOURNAMENT) || type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_BOWLER_TOURNAMENT)) {
////            intent = new Intent(context, TournamentMatchesActivity.class);
////            intent.putExtra(AppConstants.EXTRA_TOURNAMENTID, id);
////            intent.putExtra(AppConstants.EXTRA_POSITION, 3);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH_WALKOVER) || type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_MATCH_AB)) {
////            intent = new Intent(context, MatchTeamActivity.class);
////            intent.putExtra(AppConstants.EXTRA_MATCHID, id);
////            intent.putExtra(AppConstants.FROM_NOTIFICATION, true);
////        } else if (type.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_NEWS_UPDATE)) {
////            intent = new Intent(context, NewsDetailActivity.class);
////            intent.putExtra(AppConstants.EXTRA_NEWS_ID, id);
////        }
////        if (!(intent == null || isReturnIntent)) {
////            context.startActivity(intent);
////        }
////        return intent;
////    }
////
////    public static void removeNotifications(Context context) {
////        NotificationManagerCompat.from(context).cancelAll();
////    }
////
////    public static void highlightViewScalAnimation(Context context, View layTeamA2) {
////        layTeamA2.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scal_highlight_view));
////    }
////
////    public static void clearAnimation(View view) {
////        view.clearAnimation();
////    }
////
////    public static JSONObject getBatsmanJsonData(Player batsmanA) {
////        JSONObject object = new JSONObject();
////        try {
////            object.put("run", batsmanA.getBattingInfo().getRunScored());
////            object.put("ball", batsmanA.getBattingInfo().getBallFaced());
////            object.put(BALL_TYPE.FOUR, batsmanA.getBattingInfo().getRn4s() + " X 4");
////            object.put(BALL_TYPE.SIX, batsmanA.getBattingInfo().getRn6s() + " X 6");
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
////
////    public static JSONObject getBowlerJsonData(Player bowler) {
////        JSONObject object = new JSONObject();
////        try {
////            if (bowler.getBowlingInfo() != null) {
////                object.put("over", bowler.getBowlingInfo().getOvers());
////                object.put("maiden", bowler.getBowlingInfo().getMaidens());
////                object.put("run", bowler.getBowlingInfo().getRun());
////                object.put(BALL_TYPE.WICKET, bowler.getBowlingInfo().getWickets());
////            }
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
//
//    public static int getDayOfMatch(String matchDateTime) {
//        return daysBetween(getDateFromString(matchDateTime, "yyyy-MM-dd'T'HH:mm:ss"), new Date());
//    }
//
//    public static Calendar getDatePart(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(11, 0);
//        cal.set(12, 0);
//        cal.set(13, 0);
//        cal.set(14, 0);
//        return cal;
//    }
//
//    public static int daysBetween(Date startDate, Date endDate) {
//        Calendar sDate = getDatePart(startDate);
//        Calendar eDate = getDatePart(endDate);
//        int daysBetween = 1;
//        while (sDate.before(eDate)) {
//            sDate.add(5, 1);
//            daysBetween++;
//        }
//        return daysBetween;
//    }
//
////    public static JSONObject getMatchNoteTeamJson(Match match, MatchScore battingTeamMatchDetail) {
////        JSONObject object = new JSONObject();
////        try {
////            object.put("teamName", getTeamName(match, battingTeamMatchDetail));
////            object.put("totalRun", battingTeamMatchDetail.getTotalRun());
////            object.put("totalWicket", battingTeamMatchDetail.getTotalWicket());
////            object.put("totalExtra", battingTeamMatchDetail.getTotalExtra() + battingTeamMatchDetail.getPenaltyRun());
////            object.put("oversPlayed", battingTeamMatchDetail.getOversPlayed());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
////
////    public static JSONObject getMatchNotePlayerJson(Match match, MatchScore battingTeamMatchDetail, Player playerA) {
////        JSONObject object = new JSONObject();
////        try {
////            object.put("teamName", getTeamName(match, battingTeamMatchDetail));
////            object.put("playerName", playerA.getName());
////            object.put("runScored", playerA.getBattingInfo().getRunScored());
////            object.put("ballFaced", playerA.getBattingInfo().getBallFaced());
////            object.put("fours", playerA.getBattingInfo().getRn4s() + " X 4");
////            object.put("sixes", playerA.getBattingInfo().getRn6s() + " X 6");
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
//
////    public static JSONObject getMatchNotePartnershipJson(Match match, MatchScore battingTeamMatchDetail, Partnership partnership) {
////        JSONObject object = new JSONObject();
////        try {
////            object.put("forWicket", partnership.getForWicket());
////            object.put("totalRun", partnership.getRuns());
////            object.put("totalBall", partnership.getPlayerABalls() + partnership.getPlayerBBalls());
////            object.put("totalExtra", partnership.getRuns() - (partnership.getPlayerARuns() + partnership.getPlayerBRuns()));
////            object.put("oversPlayed", partnership.getOvers());
////            CricHeroes.getApp();
////            object.put("player1", CricHeroes.database.getPlayerFromID(partnership.getFkAPlayerId()).getName());
////            object.put("player1Runs", partnership.getPlayerARuns());
////            object.put("player1Balls", partnership.getPlayerABalls());
////            CricHeroes.getApp();
////            object.put("player2", CricHeroes.database.getPlayerFromID(partnership.getFkBPlayerId()).getName());
////            object.put("player2Runs", partnership.getPlayerBRuns());
////            object.put("player2Balls", partnership.getPlayerBBalls());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
////
////    public static JSONObject getMatchNoteEventJson(ScoringRuleData scoringRuleData, String event) {
////        JSONObject object = new JSONObject();
////        try {
////            object.put("teamName", getTeamName(scoringRuleData.match, scoringRuleData.battingTeamMatchDetail));
////            object.put("eventName", event);
////            object.put("totalRun", scoringRuleData.battingTeamMatchDetail.getTotalRun());
////            object.put("totalWicket", scoringRuleData.battingTeamMatchDetail.getTotalWicket());
////            object.put("totalExtra", scoringRuleData.battingTeamMatchDetail.getTotalExtra() + scoringRuleData.battingTeamMatchDetail.getPenaltyRun());
////            object.put("oversPlayed", scoringRuleData.battingTeamMatchDetail.getOversPlayed());
////            if (scoringRuleData.batsmanA != null) {
////                System.out.println("BAT A " + scoringRuleData.batsmanA.getPkPlayerId());
////                System.out.println("BAT DISS A" + (scoringRuleData.currentBall != null ? Integer.valueOf(scoringRuleData.currentBall.getFkDismissPlayerID()) : "NULL"));
////                if (scoringRuleData.currentBall == null || scoringRuleData.currentBall.getFkDismissPlayerID() != scoringRuleData.batsmanA.getPkPlayerId()) {
////                    object.put("player1", scoringRuleData.batsmanA.getName());
////                    object.put("player1Runs", scoringRuleData.batsmanA.getBattingInfo().getRunScored());
////                    object.put("player1Balls", scoringRuleData.batsmanA.getBattingInfo().getBallFaced());
////                } else {
////                    object.put("player1", "");
////                    object.put("player1Runs", 0);
////                    object.put("player1Balls", 0);
////                }
////            } else {
////                object.put("player1", "");
////                object.put("player1Runs", 0);
////                object.put("player1Balls", 0);
////            }
////            if (scoringRuleData.batsmanB != null) {
////                System.out.println("BAT B " + scoringRuleData.batsmanB.getPkPlayerId());
////                System.out.println("BAT DISS B" + (scoringRuleData.currentBall != null ? Integer.valueOf(scoringRuleData.currentBall.getFkDismissPlayerID()) : "NULL"));
////                if (scoringRuleData.currentBall == null || scoringRuleData.currentBall.getFkDismissPlayerID() != scoringRuleData.batsmanB.getPkPlayerId()) {
////                    object.put("player2", scoringRuleData.batsmanB.getName());
////                    object.put("player2Runs", scoringRuleData.batsmanB.getBattingInfo().getRunScored());
////                    object.put("player2Balls", scoringRuleData.batsmanB.getBattingInfo().getBallFaced());
////                } else {
////                    object.put("player2", "");
////                    object.put("player2Runs", 0);
////                    object.put("player2Balls", 0);
////                }
////            } else {
////                object.put("player2", "");
////                object.put("player2Runs", 0);
////                object.put("player2Balls", 0);
////            }
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        return object;
////    }
//
//    public static String getFloatForRunRate(double over, float run) {
//        float ball = (float) ((int) ((Math.floor(over) * 6.0d) + ((over % 1.0d) * 10.0d)));
//        System.out.println("BALL " + ball);
//        float runRate = (run / ball) * 6.0f;
//        System.out.println("runRate " + runRate);
//        String finalRunRate = new DecimalFormat("##.##").format((double) runRate);
//        System.out.println("runRate final " + finalRunRate);
//        return finalRunRate;
//    }
//
//    public static String getShortName(String name) {
//        if (name.length() > 15) {
//            return name.substring(0, 15) + "..";
//        }
//        return name;
//    }
//
////    public static void animateTextDigitChange(final TextView textView, String value) {
////        ValueAnimator animator = new ValueAnimator();
////        animator.setObjectValues(new Object[]{Integer.valueOf(0), Integer.valueOf(Integer.parseInt(value))});
////        animator.addUpdateListener(new AnimatorUpdateListener() {
////            public void onAnimationUpdate(ValueAnimator animation) {
////                textView.setText(animation.getAnimatedValue().toString());
////            }
////        });
////        animator.setDuration(1000);
////        animator.start();
////    }
//
//    public static File bitmapToFile(Context context, Bitmap thumb, String fileName) {
//        File f = new File(context.getCacheDir(), fileName + ".jpg");
//        try {
//            f.createNewFile();
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            thumb.compress(CompressFormat.PNG, 0, bos);
//            byte[] bitmapdata = bos.toByteArray();
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return f;
//    }
//
//    public static String getMyIpAddress(boolean useIPv4) {
//        try {
//            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
//                    if (!addr.isLoopbackAddress()) {
//                        boolean isIPv4;
//                        String sAddr = addr.getHostAddress();
//                        if (sAddr.indexOf(58) < 0) {
//                            isIPv4 = true;
//                        } else {
//                            isIPv4 = false;
//                        }
//                        if (useIPv4) {
//                            if (isIPv4) {
//                                return sAddr;
//                            }
//                        } else if (!isIPv4) {
//                            int delim = sAddr.indexOf(37);
//                            return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return "";
//    }
//
//    public static String getWifiIPAddress(Context context) {
//        return Formatter.formatIpAddress(((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress());
//    }
//
//    public static String getMobileIPAddress() {
//        try {
//            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
//                    if (!addr.isLoopbackAddress()) {
//                        return addr.getHostAddress();
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return "";
//    }
//
//    public static void hideDivider(View divider) {
//        divider.setVisibility(8);
//    }
//
//    public static Bitmap getBitmapfromUrl(String imageUrl) {
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            return BitmapFactory.decodeStream(connection.getInputStream());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
////    public static SpannableString getSpanText(Context context, String title, ArrayList<String> titleData) {
////        Typeface tfBold = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_sourcesans_pro_semibold));
////        SpannableString styledString = new SpannableString(title);
////        for (int i = 0; i < titleData.size(); i++) {
////            styledString.setSpan(new CustomTypefaceSpan("", tfBold), title.indexOf((String) titleData.get(i)), ((String) titleData.get(i)).length() + title.indexOf((String) titleData.get(i)), 34);
////        }
////        return styledString;
////    }
//
//    public static SpannableString getSubScriptSpan(String string, String sub) {
//        SpannableString styledString = new SpannableString(string);
//        styledString.setSpan(new RelativeSizeSpan(0.7f), string.indexOf(sub), string.indexOf(sub) + sub.length(), 17);
//        return styledString;
//    }
//
//    public static String getTwoDigit(int count) {
//        if (count < 10) {
//            return "0" + count;
//        }
//        return String.valueOf(count);
//    }
}
