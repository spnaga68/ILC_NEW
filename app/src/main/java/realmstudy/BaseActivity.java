package realmstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import realmstudy.extras.Utils;


public class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadNoInternetConnectionView(int resIdShowView, int resIdHideView) {
        loadNoInternetConnectionView(resIdShowView, resIdHideView, null);
    }

    public void loadNoInternetConnectionView(int resIdShowView, int resIdHideView, OnClickListener retryClickListener) {
        final View layoutNoInternetView = findViewById(resIdShowView);
        final View vHide = findViewById(resIdHideView);
        //final Button btnRetry = (Button) layoutNoInternetView.findViewById(R.id.btnRetry);
        final OnClickListener onClickListener = retryClickListener;
//        btnRetry.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (Utils.isNetworkAvailable(v.getContext())) {
//                    layoutNoInternetView.setVisibility(View.GONE);
//                    vHide.setVisibility(View.VISIBLE);
//                    if (onClickListener != null) {
//                        onClickListener.onClick(btnRetry);
//                    }
//                }
//            }
//        });
        layoutNoInternetView.setVisibility(View.VISIBLE);
        vHide.setVisibility(View.GONE);
    }

    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            CharSequence s = new SpannableString(title);
//            s.setSpan(new TypefaceSpan(this, getString(R.string.font_roboto_slab_regular)), 0, s.length(), 33);
            getSupportActionBar().setTitle(s);
            Utils.findTextViewTitle(s.toString(), getSupportActionBar(), this);
            return;
        }
        super.setTitle(title);
    }

    public void openInAppBrowser(String url) {
       // new Builder(this).theme(R.style.FinestWebViewTheme).titleDefault("").showUrl(false).titleSize(24).titleFont(getString(R.string.font_roboto_slab_regular)).statusBarColorRes(R.color.colorPrimaryDark).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.finestWhite).urlColor(R.color.finestWhite).urlColorRes(R.color.colorAccentSelected).iconDefaultColorRes(R.color.finestWhite).progressBarColorRes(R.color.orange).stringResCopiedToClipboard(R.string.copied_to_clipboard).showSwipeRefreshLayout(true).swipeRefreshColorRes(R.color.colorPrimaryDark).menuSelector(R.drawable.selector_light_theme).titleSizeRes(R.dimen.sp_22).menuTextGravity(3).menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft).dividerHeight(2).gradientDivider(false).setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down).show(url);
    }

    public void openInAppBrowserFaq(String url, int resTitle) {
       // new Builder(this).theme(R.style.FinestWebViewTheme).showUrl(false).titleSize(24).titleFont(getString(R.string.font_roboto_slab_regular)).titleDefaultRes(resTitle).updateTitleFromHtml(false).statusBarColorRes(R.color.colorPrimaryDark).toolbarColorRes(R.color.colorPrimary).titleColorRes(R.color.finestWhite).urlColor(R.color.finestWhite).urlColorRes(R.color.colorAccentSelected).iconDefaultColorRes(R.color.finestWhite).progressBarColorRes(R.color.orange).stringResCopiedToClipboard(R.string.copied_to_clipboard).showSwipeRefreshLayout(false).swipeRefreshColorRes(R.color.colorPrimaryDark).menuSelector(R.drawable.selector_light_theme).titleSizeRes(R.dimen.sp_22).menuTextGravity(3).menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft).dividerHeight(2).gradientDivider(false).setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down).show(url);
    }
}
