package storm.starter.bigdata.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import storm.starter.bigdata.util.SingletonDB;

public class TweeterDataBolt extends BaseBasicBolt {

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String sql_tweet =  "INSERT INTO tweet_data (tweet_id, created_at, retweeted_count, text) "
                +"SELECT tweet_id, tweet_created_at, tweet_retweeted_count, tweet_text from `raw` ";

        String sql_author =  "INSERT INTO author_data (author_id, description, location, followers_count, friends_count) "
                +"SELECT tweet_author_id, tweet_author_description, tweet_author_location, tweet_author_followers_count, tweet_author_friends_count from `raw` ";

        SingletonDB.getInstance();
        SingletonDB.insertDB(sql_tweet);
        SingletonDB.insertDB(sql_author);
    }
}