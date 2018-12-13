package storm.starter.bigdata;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;
import storm.starter.bigdata.bolt.TweetDataBolt;
import storm.starter.bigdata.bolt.TwitterToMysqlBolt;
import storm.starter.bigdata.spout.TwitterSpout;
import storm.starter.bigdata.util.SingletonDB;

public class TwitterMysqlTopology {



    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("twitterinput",new TwitterSpout("6vvelHfedlTAO5XiC5o13qR6A",
                "wnrh79YaBjHUjUR5aa3xpn82p0DXIYKNXFOKH40H27CJJF7pRq",
                "283183778-kWrT8PD3fYHV7OxI8cjGQpMq8exCd6yrUn7TYZxR",
                "CFaoub9q6dLgQ52Rsi5CKELAUgLp8CFnkoCE5I2G0HA6Q"));

        builder.setBolt("push", new TwitterToMysqlBolt(), 4).shuffleGrouping("twitterinput");

        builder.setBolt("data",new TweetDataBolt(),4).shuffleGrouping("push");

        Config conf = new Config();
        conf.setDebug(false);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        else {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("twitterfilter", conf, builder.createTopology());
            Utils.sleep(30000);
            cluster.killTopology("twitterfilter");
            cluster.shutdown();
        }
    }
}
