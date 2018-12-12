package storm.starter.bigdata.bolt;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.json.JSONObject;
import storm.starter.bigdata.util.MyProperties;
import storm.starter.bigdata.util.SingletonDB;

import java.net.URI;
import java.net.URLEncoder;
import java.sql.ResultSet;

public class TweeterDataBolt extends BaseBasicBolt {

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {

        String sql_author =  "INSERT INTO `bigdata`.`author_data` (author_id, description, location, followers_count, friends_count) "
                +"SELECT tweet_author_id, tweet_author_description, tweet_author_location, tweet_author_followers_count, tweet_author_friends_count from `bigdata`.`raw`" +
                "ON DUPLICATE KEY UPDATE author_id=tweet_author_id";


        String sql_tweet =  "INSERT INTO `bigdata`.`tweet_data` (tweet_id, author_id, created_at, retweeted_count, text) "
                +"SELECT tweet_id, tweet_author_id, tweet_created_at, tweet_retweet_count, tweet_text from `bigdata`.`raw`," +
                " `bigdata`.`author_data`  where `bigdata`.`author_data`.`author_id` = `bigdata`.`raw`.`tweet_author_id` ";

        SingletonDB.getInstance();
        SingletonDB.insertDB(sql_author);
        SingletonDB.insertDB(sql_tweet);
        getdata();

    }


    public void getdata() {
        try {
            String sql = "SELECT tweet_id, text FROM `bigdata`.`tweet_data`";
            ResultSet rs = SingletonDB.selectDB(sql);
            while (rs.next()) {
                String text = rs.getString("text");
                Long tweet_id = rs.getLong("tweet_id");
                this.sendRequest(text, tweet_id);
            }
        } catch (Exception e) {
            System.out.println("Error trying the post request_0 : " + e);
        }
    }

    public void sendRequest(String text, Long tweet_id){
        try {
            String api_url = MyProperties.getProperties("api_url");
            String api_key = MyProperties.getProperties("api_key");
            HttpClient httpClient = HttpClientBuilder.create().build();
            URI uri = new URI(api_url);
            HttpPost post = new HttpPost(uri);

            post.setHeader("X-Mashape-Key", api_key);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            JSONObject body = new JSONObject();
            body.put("text", URLEncoder.encode(text, "utf-8"));
            StringEntity json = new StringEntity(body.toString());
            post.setEntity(json);
            HttpResponse response = httpClient.execute(post);
            String json_string = EntityUtils.toString(response.getEntity());
            System.out.println(json_string);
            JSONObject jsonObject = new JSONObject(json_string);
            String api_response = jsonObject.getString("score");
            String sql = "UPDATE `bigdata`.`tweet_data` " +
                    "SET `bigdata`.`tweet_data`.`poids` = \"" + api_response + "\" " +
                    "WHERE `bigdata`.`tweet_data`.`tweet_id` = " + tweet_id;
            SingletonDB.insertDB(sql);
        } catch (Exception e) {
            System.out.println("Error trying the post request_1 : " + e);
        }
    }

}