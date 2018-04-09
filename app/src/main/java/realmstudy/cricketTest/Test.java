
package realmstudy.cricketTest;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import realmstudy.data.DetailedScoreData;
import realmstudy.interfaces.MsgToFragment;

public class Test implements MsgToFragment {
    Cricbuzz c = null;
    protected static final String MATCHES = "matches";
    protected static final String LIVE = "live";
    String matchID = "20064";
    protected static final String SCOREOVERBYOVER = "obo";
    DetailedScoreData detailedScoreData;
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private String commurl="http://synd.cricbuzz.com/j2me/1.0/match/2018/IPL_2018/SRH_RR_APR09/scorecard.xml";

    //    public static void main(String[] args) throws IOException{
    public void mains(DetailedScoreData detailedScoreData) throws IOException  {
        c = new Cricbuzz();
        this.detailedScoreData = detailedScoreData;
        new MatchDetail().execute(LIVE, commurl);


    }

    private void printMatches(Vector<HashMap<String, String>> matches) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(matches);
        System.out.println("naga cricket" + json);

        Integer i = 0;
//        for (i = 0; i < matches.size(); i++) {
//            String id = matches.get(i).get("id");
//            Map<String, Map> score = c.livescore(id);


//            json = gson.toJson(score);
//            System.out.println("naga cricket@ " + json);
//
//            Map<String, Map> comm = c.commentary(id);
//            json = gson.toJson(comm);
//            System.out.println("naga cricket" + json);
//
//            break;
//        }


    }

    private void printMatches(Map<String, Map> score) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = "";


        Integer i = 0;
//        for (i = 0; i < matches.size(); i++) {
//            String id = matches.get(i).get("id");
//            Map<String, Map> score = c.livescore(id);


        json = gson.toJson(score);
        System.out.println("naga cricket" + json);
//            System.out.println("naga cricket@ " + json);
//
//            Map<String, Map> comm = c.commentary(id);
//            json = gson.toJson(comm);
//            System.out.println("naga cricket" + json);
//
//            break;
//        }


    }

    @Override
    public void msg(String s) {
        new MatchDetailOver().execute(SCOREOVERBYOVER, overbyoverurl);
    }


    public class MatchDetail extends AsyncTask<String, Integer, Document> {
        String type = "";

        protected Document doInBackground(String... type) {
            this.type = type[0];
            String url = type[1];
            String inputLine, result;
            System.out.println("url   " + url);
            if (type[0].equals(LIVE))
                try {
                    return Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else
                try {
                    //

                    URL myUrl = new URL(url);
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    //Set methods and timeouts
                    connection.setRequestMethod(REQUEST_METHOD);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);

                    //Connect to our url
                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return null;

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Document result) {
            try {
                if (type.equals(LIVE)) {
                    c.Scorecard(result,detailedScoreData,Test.this);
//                    Map<String, Map> score = new HashMap<String, Map>();
//                    String query = String.format("match[id = %s]", "2");
//                    Element match = ((Document) result).select(query).get(0);
//                    score.put("matchinfo", matchinfo(match));
//
//                    commurl = match.attr("datapath") + "scorecard.xml";
//                    System.out.println("liveurl" + commurl);
//                    new MatchDetail().execute(LIVE, commurl);

                } else if (type.equals(SCOREOVERBYOVER)) {
                    try {
                        System.out.println("liveurlres" + c.OverByOver(new JSONObject(result.toString()), detailedScoreData));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    printMatches();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }










    public class MatchDetailOver extends AsyncTask<String, Integer, Object> {
        String type = "";

        protected Object doInBackground(String... type) {
            this.type = type[0];
            String url = type[1];
            String inputLine, result;
            System.out.println("url   " + url);

                try {
                    //

                    URL myUrl = new URL(url);
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    //Set methods and timeouts
                    connection.setRequestMethod(REQUEST_METHOD);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);

                    //Connect to our url
                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return "";

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Object result) {
            try {
                if (type.equals(SCOREOVERBYOVER)) {
                    try {
                        System.out.println("liveurlres" + c.OverByOver(new JSONObject(result.toString()), detailedScoreData));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    printMatches();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






//       String url = "http://synd.cricbuzz.com/j2me/1.0/livematches.xml";

    String overbyoverurl = "http://push.cricbuzz.com/match-api/" + matchID + "/commentary.json";

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
}
    

