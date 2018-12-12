package storm.starter.bigdata.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;
import storm.starter.bigdata.util.MyProperties;
import storm.starter.bigdata.util.SingletonDB;

import java.sql.ResultSet;

public class TweetApiBolt {

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            String sql = "SELECT id_tweet, text FROM tweet_data";
            ResultSet rs = SingletonDB.selectDB(sql);
            while (rs.next()) {
                String text = rs.getString("text");
                int tweet_id = rs.getInt("id_tweet");
                this.sendRequest(text, tweet_id);
            }
        } catch (Exception e) {
            System.out.println("Error trying the post request : " + e);
        }
    }

    public void sendRequest(String text, int tweet_id){
        try {
            String api_url = MyProperties.getProperties("api_url");
            String api_key = MyProperties.getProperties("api_key");
            HttpClient httpClient = HttpClientBuilder.create().build();
            String params = "api_key=" + api_key + "&text=" + text;
            String url = api_url + "?" + params;
            URI uri = new URI(url);
            HttpPost post = new HttpPost(uri);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = httpClient.execute(post);
            String json_string = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(json_string);
            String api_response = jsonObject.getString("response");
            String sql = "UPDATE tweet_data SET poids = \"" + api_response + "\" WHERE tweet_id = " + tweet_id;
            SingletonDB.insertDB(sql);
        } catch (Exception e) {
            System.out.println("Error trying the post request : " + e);
        }
    }

}
