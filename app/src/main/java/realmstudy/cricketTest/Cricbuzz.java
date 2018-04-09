/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realmstudy.cricketTest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.CharacterData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import realmstudy.data.CommanData;
import realmstudy.data.DetailedScoreData;
import realmstudy.data.OverAdapterData;
import realmstudy.data.RealmObjectData.Player;
import realmstudy.data.ScoreBoardData;
import realmstudy.data.ScoreCardDetailData;
import realmstudy.data.UpdateDetailScoreData;
import realmstudy.extras.Utils;
import realmstudy.interfaces.MsgToFragment;
import realmstudy.lib.Util;

/**
 * @author shivam.m
 */
public class Cricbuzz {


    String url = "http://synd.cricbuzz.com/j2me/1.0/livematches.xml";
    private String matchID = "1523111401";
    private HashMap<String, String> player = new HashMap<>();
    private String homeTeamID;
    private boolean curentOver = true;

    //    public Document getxml(String url) throws IOException {
//        Document doc;
//        doc = Jsoup.connect(url).get();
//        return doc;
//    }
//
    public Map<String, String> matchinfo(Element match) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", match.attr("id"));
        map.put("srs", match.attr("srs"));
        map.put("mchdesc", match.attr("mchdesc"));
        map.put("mnum", match.attr("mnum"));
        map.put("type", match.attr("type"));
        map.put("mchstate", match.select("state").attr("mchstate"));
        map.put("status", match.select("state").attr("status"));
        return map;
    }

    public Vector matches(Document doc) throws IOException {
        Vector matches = new Vector();
        try {
//            Document doc = getxml(url);
            Elements m = doc.select("match");
            for (Element x : m) {
                matches.add(matchinfo(x));
            }
            return matches;
        } catch (Exception e) {
            e.printStackTrace();
            return matches;

        }
    }

    //
    public Map<String, Map> livescore(Document doc) throws IOException {
        Map<String, Map> score = new HashMap<String, Map>();
        try {
            for (Element e : doc.select("c")) {
                System.out.println("naga\n\n" + e.ownText());
            }
//            System.out.println("naga commentary"+doc.select("comm").get(0));
//            System.out.println("commentary"+getCharacterDataFromElement(doc.select("comm").get(0)));
            if (doc.select("mscr").size() > 0) {
                Element mscr = doc.select("mscr").get(0);
                Element batting = mscr.select("bttm").get(0);
                Element bowling = mscr.select("blgtm").get(0);
                Elements batsman = mscr.select("btsmn");
                Elements bowler = mscr.select("blrs");

                HashMap<String, Vector<HashMap<String, String>>> b1 = new HashMap<String, Vector<HashMap<String, String>>>();
                Vector v1 = new Vector();
                HashMap<String, String> m1 = new HashMap<String, String>();
                m1.put("team", batting.attr("sname"));
                v1.add(m1);

                b1.put("team", v1);
                Vector v2 = new Vector();

                for (Element b : batsman) {
                    HashMap<String, String> m2 = new HashMap<String, String>();
                    m2.put("name", b.attr("sname"));
                    m2.put("runs", b.attr("r"));
                    m2.put("balls", b.attr("b"));
                    m2.put("fours", b.attr("frs"));
                    m2.put("six", b.attr("sxs"));
                    v2.addElement(m2);
                }

                b1.put("batsman", v2);
                Elements binnings = batting.select("inngs");
                Vector v3 = new Vector();
                for (Element i : binnings) {
                    HashMap<String, String> m3 = new HashMap<String, String>();
                    m3.put("desc", i.attr("desc"));
                    m3.put("runs", i.attr("r"));
                    m3.put("wickets", i.attr("wkts"));
                    m3.put("overs", i.attr("ovrs"));
                    v3.add(m3);
                }
                b1.put("score", v3);
                score.put("batting", b1);

                HashMap<String, Vector<HashMap<String, String>>> b2 = new HashMap<String, Vector<HashMap<String, String>>>();
                Vector v = new Vector();

                HashMap<String, String> m = new HashMap<String, String>();
                m.put("team", bowling.attr("sname"));
                v.add(m);
                b2.put("team", v);
                //System.out.print(b2);
                Vector v5 = new Vector();
                for (Element b : bowler) {
                    HashMap<String, String> m4 = new HashMap<String, String>();
                    m4.put("name", b.attr("sname"));
                    m4.put("overs", b.attr("ovrs"));
                    m4.put("maidens", b.attr("mdns"));
                    m4.put("runs", b.attr("r"));
                    m4.put("wickets", b.attr("wkts"));
                    v5.add(m4);
                }
                b2.put("bowler", v5);
                binnings = bowling.select("inngs");

                Vector v6 = new Vector();
                for (Element i : binnings) {
                    HashMap<String, String> m5 = new HashMap<String, String>();
                    m5.put("desc", i.attr("desc"));
                    m5.put("runs", i.attr("r"));
                    m5.put("wickets", i.attr("wkts"));
                    m5.put("overs", i.attr("ovrs"));
                    v6.add(m5);
                }
                b2.put("score", v6);
                score.put("bowling", b2);
            }

            return score;
        } catch (Exception e) {
            e.printStackTrace();
            return score;
        }

    }

    //
    public Map<String, Map> commentary(Document doc) throws IOException {
        Map<String, Map> score = new HashMap<String, Map>();
        try {
//            Document doc = getxml(url);

            Elements comm = doc.select("c");


            HashMap<String, Vector> h = new HashMap<String, Vector>();
            Vector v = new Vector();
            for (Element c : comm) {
                v.add(c.text());
            }
            h.put("text", v);
            score.put("commentary", h);

            return score;
        } catch (Exception e) {
            e.printStackTrace();
            return score;
        }
    }

    public static String ballsToOver(int balls) {
        String over = "0.0";
        if (balls >= 6) {
            over = (balls / 6) + "." + (balls % 6);
        } else if (balls > 0) {
            over = "0." + balls;
        }
        return over;
    }

    public static int overToBall(String over) {
        // String over = "0.0";
        int balls = 0;
        Float overs = Float.parseFloat(over);
        // if (overs > 1) {
        //  System.out.println("________vv"+(((int)(overs/1)) * 6)+"__"+((overs % 1)*10)+"__"+over);
        int overr = (((int) (overs / 1)) * 6) + (int) ((overs % 1) * 10);
        // System.out.println("________OO"+overr);
        balls = overr;
        //}

        return balls;
    }


    public String OverByOver(JSONObject data, DetailedScoreData detailedScoreData) {


        try {
            JSONArray commetaryArray = data.getJSONArray("comm_lines");
            JSONArray playerArray = data.getJSONArray("players");

            for (int i = 0; i < playerArray.length(); i++) {
                JSONObject jj = playerArray.getJSONObject(i);
                player.put(jj.getString("id"), jj.getString("name"));
            }


            ArrayList<OverAdapterData> overAdapterDataArrayList = new ArrayList<>();
            ScoreBoardData scoreBoardData = new ScoreBoardData();

            scoreBoardData.setAwayTeam(data.getJSONObject("team1").getString("s_name"));
            scoreBoardData.setHomeTeam(data.getJSONObject("team2").getString("s_name"));
            homeTeamID = data.getJSONObject("team1").getString("id");

            scoreBoardData.setBatsmanSwitched(false);
            scoreBoardData.setBowlerSwitched(false);
            JSONObject currentInnings = data.getJSONObject("score").getJSONObject("batting");
            JSONObject firstInnings = data.getJSONObject("score").getJSONObject("bowling");

            JSONObject striker = data.getJSONObject("score").getJSONArray("batsman").getJSONObject(0);
            JSONObject nonstriker = data.getJSONObject("score").getJSONArray("batsman").getJSONObject(1);

            if (firstInnings != null) {
                JSONObject fid = ((JSONObject) firstInnings.getJSONArray("innings").get(0));
                scoreBoardData.setFirstInnings(false);
                scoreBoardData.setFirstIinningsOver(fid.getString("overs"));
                scoreBoardData.setFirstIinningsWicket(Integer.parseInt(fid.getString("wkts")));
                scoreBoardData.setFirstInningsTotal(Integer.parseInt(fid.getString("score")));

            } else {
                scoreBoardData.setFirstInnings(true);
            }

            if (currentInnings != null) {
                JSONObject fid = ((JSONObject) currentInnings.getJSONArray("innings").get(0));
                scoreBoardData.setTotalOver(fid.getString("overs"));
                scoreBoardData.setTotal_wicket(Integer.parseInt(fid.getString("wkts")));
                scoreBoardData.setTotalRuns(Integer.parseInt(fid.getString("score")));
                scoreBoardData.setTotalBalls(scoreBoardData.getTotalBallsAuto());
            }


            scoreBoardData.setHomeTeamBatting(currentInnings.getString("id").equals(homeTeamID));
            String[] prev_overs = data.getJSONObject("score").getString("prev_overs").split(" ");
            scoreBoardData.setLastThreeOvers(new ArrayList<>(Arrays.asList(prev_overs)));
            scoreBoardData.setMatchQuote(data.getString("status"));
            JSONObject cbowler = data.getJSONObject("score").getJSONArray("bowler").getJSONObject(0);
            JSONObject nbowler = data.getJSONObject("score").getJSONArray("bowler").getJSONObject(1);
            if (striker != null) {
                scoreBoardData.striker.setBalls(Integer.parseInt(striker.getString("b")));
                scoreBoardData.striker.setFours(Integer.parseInt(striker.getString("4s")));
                scoreBoardData.striker.setSixes(Integer.parseInt(striker.getString("6s")));
                scoreBoardData.striker.setRuns(Integer.parseInt(striker.getString("r")));
                scoreBoardData.striker.setName(player.get(striker.getString("id")));
                scoreBoardData.striker.setStrikeRate(scoreBoardData.striker.getStrike_rateAuto());
            }

            if (nonstriker != null) {
                scoreBoardData.nonStriker.setBalls(Integer.parseInt(nonstriker.getString("b")));
                scoreBoardData.nonStriker.setFours(Integer.parseInt(nonstriker.getString("4s")));
                scoreBoardData.nonStriker.setSixes(Integer.parseInt(nonstriker.getString("6s")));
                scoreBoardData.nonStriker.setRuns(Integer.parseInt(nonstriker.getString("r")));
                scoreBoardData.nonStriker.setName(player.get(nonstriker.getString("id")));
                scoreBoardData.nonStriker.setStrikeRate(scoreBoardData.nonStriker.getStrike_rateAuto());
            }
            if (cbowler != null) {
                scoreBoardData.curr_bowlers.setBalls(CommanData.overToBall(cbowler.getString("o")));
                scoreBoardData.curr_bowlers.setMaiden(Integer.parseInt(cbowler.getString("m")));
                scoreBoardData.curr_bowlers.setRuns(Integer.parseInt(cbowler.getString("r")));
                scoreBoardData.curr_bowlers.setWicket(Integer.parseInt(cbowler.getString("w")));
                scoreBoardData.curr_bowlers.setName(player.get(cbowler.getString("id")));
                scoreBoardData.curr_bowlers.setEconomic_rate(scoreBoardData.curr_bowlers.getEcnomic_rateAuto());
            }

            if (nbowler != null) {
                scoreBoardData.next_bowlers.setBalls(CommanData.overToBall((nbowler.getString("o"))));
                scoreBoardData.next_bowlers.setMaiden(Integer.parseInt(nbowler.getString("m")));
                scoreBoardData.next_bowlers.setRuns(Integer.parseInt(nbowler.getString("r")));
                scoreBoardData.next_bowlers.setWicket(Integer.parseInt(nbowler.getString("w")));
                scoreBoardData.next_bowlers.setName(player.get(nbowler.getString("id")));
                scoreBoardData.next_bowlers.setEconomic_rate(scoreBoardData.next_bowlers.getEcnomic_rateAuto());
            }


            for (int i = 0; i < commetaryArray.length(); i++) {
                JSONObject commentaryData = commetaryArray.getJSONObject(i);
//                curentOver
                if (commentaryData.has("score") && (commentaryData.get("evt").equals("over-break"))) {
                    curentOver = false;
                    JSONArray batsmanArray = commentaryData.getJSONArray("batsman");
                    String batsman = "";
                    OverAdapterData overAdapterData = new OverAdapterData();
                    for (int n = 0; n < batsmanArray.length(); n++) {
                        JSONObject batsmanData = batsmanArray.getJSONObject(n);
                        String concat = ((n + 1) == batsmanArray.length()) ? "" : " & ";
                        batsman += player.get(batsmanData.getString("id")) + concat;
                    }

                    if (commentaryData.has("bowler")) {
                        JSONArray bowlArray = commentaryData.getJSONArray("bowler");
                        String bowler = "";
                        for (int n = 0; n < bowlArray.length(); n++) {
                            JSONObject batsmanData = bowlArray.getJSONObject(n);
                            String concat = ((n + 1) == bowlArray.length()) ? "" : " & ";
                            bowler += player.get(batsmanData.getString("id")) + concat;
                        }


                        overAdapterData.setBolwers(bowler);
                    }
                    overAdapterData.setBatsmans(batsman);
                    overAdapterData.setOver((int)Math.ceil(Double.parseDouble(commentaryData.getString("o_no")) ));
                    if (commentaryData.has("o_runs"))
                        overAdapterData.setTotal_run(Integer.parseInt(commentaryData.getString("o_runs")));
                    if (commentaryData.has("o_summary")) {
                        String[] delivery = commentaryData.getString("o_summary").split(" ");
                        overAdapterData.setDeliveries(new ArrayList<>(Arrays.asList(delivery)));
                    }
                    overAdapterDataArrayList.add(overAdapterData);

                }
            }
            detailedScoreData.setOverAdapterData(overAdapterDataArrayList);
            detailedScoreData.setScoreBoardData(scoreBoardData);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("InningsDetailData/" + matchID);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth != null) {
                UpdateDetailScoreData jsonObject = new UpdateDetailScoreData();
                jsonObject.uid = String.valueOf(mAuth.getCurrentUser().getUid());
                jsonObject.match_id = String.valueOf(matchID);
                jsonObject.data = Utils.toString(detailedScoreData);
                myRef.setValue(jsonObject);
            }

            System.out.println("haiii came" + detailedScoreData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String Scorecard(Document doc, DetailedScoreData detailedScoreData, MsgToFragment mi) throws IOException {
        Map<String, Map> score = new HashMap<String, Map>();
        try {
//            Document doc = getxml(url);

            Elements comm = doc.select("c");
            ScoreCardDetailData firstInningsScoreCard = new ScoreCardDetailData();


            if (doc.select("scrs").size() > 0) {
                Elements ee = doc.select("scrs").get(0).select("Inngs");
                firstInningsScoreCard.TeamRun_over = ee.attr("r") + " - " + ee.attr("wkts") + " (" + ee.attr("ovrs") + ")";
                firstInningsScoreCard.setCurrent_run_rate(Float.parseFloat(ee.attr("r")) /
                        (float) overToBall(ee.attr("ovrs")) * 6f);
                Element fi = ee.get(0);
                Elements batting = fi.select("btTm");
                Elements bowling = fi.select("blTm");
                Elements fow = fi.select("FOW");
                Elements extra = fi.select("xtras");
                for (Element e : batting.select("plyr")) {
                    ScoreCardDetailData.BatsmanDetail detail = new ScoreCardDetailData.BatsmanDetail();
                    detail.name = e.attr("sName");
                    detail.runs = Integer.parseInt(e.attr("r"));
                    detail.balls = Integer.parseInt(e.attr("b"));
                    detail.fours = Integer.parseInt(e.attr("frs"));
                    detail.sixes = Integer.parseInt(e.attr("six"));
                    detail.outAs = e.select("status").get(0).ownText();
                    detail.setStrike_rate(detail.getStrike_rateAuto());
                    firstInningsScoreCard.addBatsmanDetails(detail);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }

                for (Element e : bowling.select("plyr")) {


                    ScoreCardDetailData.BowlersDetail bowlersDetail = new ScoreCardDetailData.BowlersDetail();
                    bowlersDetail.name = e.attr("sName");
                    bowlersDetail.runs = Integer.parseInt(e.attr("rOff"));
                    bowlersDetail.overs = (e.attr("ovrs"));
                    bowlersDetail.wicket = Integer.parseInt(e.attr("wkts"));
                    bowlersDetail.setEcnomic_rate(bowlersDetail.getEcnomic_rateAuto());

                    firstInningsScoreCard.addBowlersDetails(bowlersDetail);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }
                for (Element e : fow.select("wkt")) {
                    System.out.println("naga\n\n" + e.attr("sName") + "___" + e.getElementsByAttribute("sName"));


                    ScoreCardDetailData.FOW fowk = new ScoreCardDetailData.FOW();
                    fowk.name = e.attr("btsmn");
                    fowk.score = Integer.parseInt(e.attr("r"));
                    fowk.overs = (e.attr("ovrs"));


                    firstInningsScoreCard.addFow(fowk);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }
                //bys="0" wds="0" nb="0" lb="0" pnlty="0" total="0"
                //"wd8,b0,lb0,nb0"
                firstInningsScoreCard.extras_detail = "wd " + extra.attr("wds")
                        + ",bys " + extra.attr("bys")
                        + ",lb " + extra.attr("lb")
                        + ",nb " + extra.attr("nb");
                firstInningsScoreCard.setTeamName(batting.attr("sName"));
                firstInningsScoreCard.total_extras = Integer.parseInt(extra.attr("total"));
            }


            ScoreCardDetailData secondInningsScoreCard = new ScoreCardDetailData();

            if (doc.select("scrs").size() > 0) {
                Elements ee = doc.select("scrs").get(0).select("Inngs");

                Element fi = ee.get(1);
                secondInningsScoreCard.TeamRun_over = fi.attr("r") + " - " + fi.attr("wkts") + " (" + fi.attr("ovrs") + ")";
                secondInningsScoreCard.setCurrent_run_rate(Float.parseFloat(fi.attr("r")) /
                        (float) overToBall(fi.attr("ovrs")) * 6f);
                Elements batting = fi.select("btTm");
                Elements bowling = fi.select("blTm");
                Elements fow = fi.select("FOW");
                Elements extra = fi.select("xtras");
                for (Element e : batting.select("plyr")) {
                    System.out.println("naga\n\n" + e.attr("sName") + "___" + e.getElementsByAttribute("sName"));
                    ScoreCardDetailData.BatsmanDetail detail = new ScoreCardDetailData.BatsmanDetail();
                    detail.name = e.attr("sName");
                    detail.runs = Integer.parseInt(e.attr("r"));
                    detail.balls = Integer.parseInt(e.attr("b"));
                    detail.fours = Integer.parseInt(e.attr("frs"));
                    detail.sixes = Integer.parseInt(e.attr("six"));
                    detail.outAs = e.select("status").get(0).ownText();
                    detail.setStrike_rate(detail.getStrike_rateAuto());
                    secondInningsScoreCard.addBatsmanDetails(detail);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }

                for (Element e : bowling.select("plyr")) {
                    System.out.println("naga\n\n" + e.attr("sName") + "___" + e.getElementsByAttribute("sName"));


                    ScoreCardDetailData.BowlersDetail bowlersDetail = new ScoreCardDetailData.BowlersDetail();
                    bowlersDetail.name = e.attr("sName");
                    bowlersDetail.runs = Integer.parseInt(e.attr("rOff"));
                    bowlersDetail.overs = (e.attr("ovrs"));
                    bowlersDetail.wicket = Integer.parseInt(e.attr("wkts"));
                    bowlersDetail.setEcnomic_rate(bowlersDetail.getEcnomic_rateAuto());

                    secondInningsScoreCard.addBowlersDetails(bowlersDetail);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }
                for (Element e : fow.select("wkt")) {
                    System.out.println("naga\n\n" + e.attr("sName") + "___" + e.getElementsByAttribute("sName"));


                    ScoreCardDetailData.FOW fowk = new ScoreCardDetailData.FOW();
                    fowk.name = e.attr("btsmn");
                    fowk.score = Integer.parseInt(e.attr("r"));
                    fowk.overs = (e.attr("ovrs"));


                    secondInningsScoreCard.addFow(fowk);
//                    Element batting = mscr.select("bttm").get(0);
//                    Element bowling = mscr.select("blgtm").get(0);
//                    Elements batsman = mscr.select("btsmn");
//                    Elements bowler = mscr.select("blrs");
                }
                //bys="0" wds="0" nb="0" lb="0" pnlty="0" total="0"
                //"wd8,b0,lb0,nb0"
                secondInningsScoreCard.extras_detail = "wd " + extra.attr("wds")
                        + ",bys " + extra.attr("bys")
                        + ",lb " + extra.attr("lb")
                        + ",nb " + extra.attr("nb");
                secondInningsScoreCard.setTeamName(batting.attr("sName"));
                secondInningsScoreCard.total_extras = Integer.parseInt(extra.attr("total"));
            }


            detailedScoreData.setScoreCardDetailData(secondInningsScoreCard);
            detailedScoreData.setSecscoreCardDetailData(firstInningsScoreCard);

            System.out.println("scorecardDetail" + detailedScoreData.toString());
            mi.msg("hai");

//                HashMap<String, Vector> h = new HashMap<String, Vector>();
//            Vector v = new Vector();
//            for (Element c : comm) {
//                v.add(c.text());
//            }
//            h.put("text", v);
//            score.put("commentary", h);

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getCharacterDataFromElement(Element e) {

        Elements list = e.getAllElements();
        String data;

        for (int index = 0; index < list.size(); index++) {
            if (list.get(index) instanceof CharacterData) {
                CharacterData child = (CharacterData) list.get(index);
                data = child.getData();

                if (data != null && data.trim().length() > 0)
                    return child.getData();
            }
        }
        return "";
    }
}
