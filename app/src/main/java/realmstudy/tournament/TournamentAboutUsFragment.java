package realmstudy.tournament;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import realmstudy.R;
import realmstudy.view.TextView;

/**
 * Created by developer on 30/10/17.
 */

public class TournamentAboutUsFragment extends Fragment {
//    @BindView(2131755701)
//    CardView cardAssociation;
//    @BindView(2131756174)
//    ImageView ivImage;
//    @BindView(2131755702)
//    LinearLayout layAssociations;
//    @BindView(2131755697)
//    LinearLayout layOrganizer;
//    @BindView(2131755698)
//    LinearLayout layOrganizerDetail;
//    @BindView(2131755699)
//    LinearLayout layTournament;
//    @BindView(2131755700)
//    LinearLayout layTournamentDetail;
//    String mesg;
//    @BindView(2131756150)
//    TextView tvDetail;
//    @BindView(2131755208)
//    TextView tvTitle;
//    @BindView(2131755196)
//    View viewEmpty;
//    @BindView(2131755703)
//    WebView web;

    LinearLayout
            layOrganizer, layOrganizerDetail, layTournament, layTournamentDetail, layAssociations;
    android.support.v7.widget.CardView
            cardAssociation;
    WebView
            tvAboutUs;
    ImageView ivImage;
    TextView
            txt_error, tvTitle, tvDetail;
    String mesg;
    private ViewGroup viewEmpty;
    private TextView aboutText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        //   this.mesg = getResources().getString(R.string.about_blank_stat);

        layOrganizer = (LinearLayout
                ) rootView.findViewById(R.id.layOrganizer);
        layOrganizerDetail = (LinearLayout
                ) rootView.findViewById(R.id.layOrganizerDetail);
        layTournament = (LinearLayout
                ) rootView.findViewById(R.id.layTournament);
        layTournamentDetail = (LinearLayout
                ) rootView.findViewById(R.id.layTournamentDetail);
        ivImage = (ImageView
                ) rootView.findViewById(R.id.ivImage);
        cardAssociation = (android.support.v7.widget.CardView
                ) rootView.findViewById(R.id.cardAssociation);
        layAssociations = (LinearLayout
                ) rootView.findViewById(R.id.layAssociations);
        tvAboutUs = (WebView
                ) rootView.findViewById(R.id.tvAboutUs);
        viewEmpty = (ViewGroup
                ) rootView.findViewById(R.id.viewEmpty);
        txt_error = (TextView
                ) rootView.findViewById(R.id.txt_error);
        tvTitle=(TextView
        ) rootView.findViewById(R.id.tvTitle);
        tvDetail=(TextView
        ) rootView.findViewById(R.id.tvDetail);
        emptyViewVisibility(true);
        return rootView;
    }

    public void setData(JSONObject data, String mesg) {
        this.mesg = mesg;
        aboutText.setText(Html.fromHtml(mesg));
//        if (itemArrayList != null) {
//            if (Utils.isEmptyString(itemArrayList.optString("about_us"))) {
//                this.web.setVisibility(8);
//            } else {
//                this.web.getSettings().setBuiltInZoomControls(true);
//                this.web.getSettings().setDisplayZoomControls(false);
//                this.web.setScrollbarFadingEnabled(true);
//                this.web.setVerticalScrollBarEnabled(false);
//                this.web.loadData(itemArrayList.optString("about_us"), "text/html", HttpRequest.CHARSET_UTF8);
//            }
//            if (getActivity() instanceof TournamentMatchesActivity) {
//                this.layOrganizerDetail.removeAllViews();
//                this.layTournamentDetail.removeAllViews();
//                this.layAssociations.removeAllViews();
//                if (!Utils.isEmptyString(itemArrayList.optString("organizer_name"))) {
//                    addOrganizerData("Name", itemArrayList.optString("organizer_name"));
//                }
//                if (!Utils.isEmptyString(itemArrayList.optString("name"))) {
//                    addTournamentData("Name", itemArrayList.optString("name"));
//                }
//                if (!Utils.isEmptyString(itemArrayList.optString("from_date"))) {
//                    if (!Utils.isEmptyString(itemArrayList.optString("to_date"))) {
//                       // addTournamentData(HttpRequest.HEADER_DATE, Utils.changeDateformate(itemArrayList.optString("from_date"), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy") + " to " + Utils.changeDateformate(itemArrayList.optString("to_date"), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy"));
//                    }
//                }
//                if (!Utils.isEmptyString(itemArrayList.optString("city_name"))) {
//                    JSONArray array = itemArrayList.optJSONArray("grounds");
//                    HashMap<String, String> grounds = new HashMap();
//                    if (array == null || array.length() <= 0) {
//                        addTournamentData("Locations", itemArrayList.optString("city_name").replace(",", ShellUtils.COMMAND_LINE_END));
//                    } else {
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            String city = object.getString("city_name");
//                            if (grounds.size() <= 0 || !grounds.containsKey(city)) {
//                                try {
//                                    grounds.put(city, object.getString("ground_name"));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                grounds.put(object.getString("city_name"), ((String) grounds.get(city)) + ", " + object.getString("ground_name"));
//                            }
//                        }
//                        String locations = "";
//                        for (Map.Entry<String, String> entry : grounds.entrySet()) {
//                            String key = (String) entry.getKey();
//                            String value = (String) entry.getValue();
//                            if (Utils.isEmptyString(locations)) {
//                                locations = key + " - " + value;
//                            } else {
//                                locations = locations + ShellUtils.COMMAND_LINE_END + key + " - " + value;
//                            }
//                        }
//                        addTournamentData("Locations", locations);
//                    }
//                }
//                if (!Utils.isEmptyString(itemArrayList.optString(AppConstants.EXTRA_BALLTYPE))) {
//                    addTournamentData("Ball Type", itemArrayList.optString(AppConstants.EXTRA_BALLTYPE));
//                }
//                if (!Utils.isEmptyString(itemArrayList.optString("association_name"))) {
//                    addAssociationData("Association", itemArrayList.optString("association_name"));
//                    return;
//                }
//                return;
//            }
//            return;
//        }
        emptyViewVisibility(true);
    }

    private void emptyViewVisibility(boolean b) {
        if (b) {
            this.viewEmpty.setVisibility(View.VISIBLE);
            this.ivImage.setImageResource(R.drawable.ic_cricket_player_with_bat);
            this.tvTitle.setText(this.mesg);
            this.tvDetail.setVisibility(View.GONE);
            return;
        }
        this.viewEmpty.setVisibility(View.GONE);
    }

//    private void addOrganizerData(String title, String value) {
//        this.layOrganizer.setVisibility(0);
//        View headerBatting = getActivity().getLayoutInflater().inflate(R.layout.raw_tournament_about, null);
//        View divider = getActivity().getLayoutInflater().inflate(R.layout.raw_divider, null);
//        TextView tvValue = (TextView) headerBatting.rootView.findViewById(R.id.tvValue);
//        ((TextView) headerBatting.rootView.findViewById(R.id.tvTitle)).setText(title);
//        tvValue.setText(value);
//        if (this.layOrganizerDetail.getChildCount() > 0) {
//            this.layOrganizerDetail.addView(divider);
//        }
//        this.layOrganizerDetail.addView(headerBatting);
//    }
//
//    private void addTournamentData(String title, String value) {
//        this.layTournament.setVisibility(0);
//        View divider = getActivity().getLayoutInflater().inflate(R.layout.raw_divider, null);
//        View headerBatting = getActivity().getLayoutInflater().inflate(R.layout.raw_tournament_about, null);
//        TextView tvValue = (TextView) headerBatting.rootView.findViewById(R.id.tvValue);
//        ((TextView) headerBatting.rootView.findViewById(R.id.tvTitle)).setText(title);
//        tvValue.setText(value);
//        if (this.layTournamentDetail.getChildCount() > 0) {
//            this.layTournamentDetail.addView(divider);
//        }
//        this.layTournamentDetail.addView(headerBatting);
//    }
//
//    private void addAssociationData(String title, String value) {
//        this.cardAssociation.setVisibility(0);
//        View divider = getActivity().getLayoutInflater().inflate(R.layout.raw_divider, null);
//        View headerBatting = getActivity().getLayoutInflater().inflate(R.layout.raw_tournament_about, null);
//        TextView tvValue = (TextView) headerBatting.rootView.findViewById(R.id.tvValue);
//        ((TextView) headerBatting.rootView.findViewById(R.id.tvTitle)).setText(title);
//        tvValue.setText(value);
//        if (this.layAssociations.getChildCount() > 0) {
//            this.layAssociations.addView(divider);
//        }
//        this.layAssociations.addView(headerBatting);
//    }
}
