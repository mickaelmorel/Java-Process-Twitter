package storm.starter.bigdata.bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import storm.starter.bigdata.util.MyProperties;
import twitter4j.Status;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

public  class TwitterToMysqlBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 42L;

    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("tweet"));
    }

    public void execute(Tuple input, BasicOutputCollector collector)
    {

        Status status = (Status) input.getValueByField("tweet");
        if(status != null) {


            SimpleDateFormat cet =
                    new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss zzz");

            cet.setTimeZone(TimeZone.getTimeZone("CET"));

            SimpleDateFormat utc =
                    new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss zzz");

            utc.setTimeZone(TimeZone.getTimeZone("UTC"));
            double longitude = 0; double latitude = 0;
            String placeGeometryType = ""; String placeCountry = ""; String placeCountryCode = ""; String placeName = "";
            String placeFullName = ""; String placeId = ""; String placeUrl = ""; Long retweet_id =0L ;
            if(status.getGeoLocation() != null) {
                longitude = status.getGeoLocation().getLongitude();
                latitude = status.getGeoLocation().getLatitude();
            }
            else {
                longitude = 0;
                latitude = 0;
            }
            if(status.getPlace() != null)
            {
                placeGeometryType = status.getPlace().getGeometryType();
                placeCountry  = status.getPlace().getCountry();
                placeCountryCode =status.getPlace().getCountryCode();
                placeName =status.getPlace().getName();
                placeFullName =status.getPlace().getFullName();
                placeId =status.getPlace().getId();
                placeUrl =status.getPlace().getURL();
            }
            if(status.getRetweetedStatus() != null)
                retweet_id = status.getRetweetedStatus().getId();


            String sql = "INSERT INTO `bigdata`.`raw` (tweet_author_id, tweet_author_name, tweet_author_screen_name, tweet_author_lang, tweet_author_created_at ,"
                    +" tweet_author_created_at_str_cet, tweet_author_created_at_str_utc, tweet_author_description,"
                    +"tweet_author_url,tweet_id, tweet_created_at , tweet_created_at_str_cet, tweet_created_at_str_utc,"
                    +"tweet_in_reply_to_tweet,  tweet_in_reply_to_user  , tweet_retweeted,   tweet_retweet_count,"
                    +"tweet_favorited, tweet_author_protected, tweet_author_followers_count, tweet_author_friends_count,"
                    +"tweet_author_listed_count, tweet_author_favorites_count, tweet_author_statuses_count,"
                    +" tweet_author_geo_enabled, tweet_source, tweet_author_location, tweet_author_display_url,"
                    +"tweet_author_utc_offset, tweet_author_time_zone, tweet_lang, tweet_coordinates_longitude,"
                    +" tweet_coordinates_latitude, tweet_coordinates_type, tweet_place_country, tweet_place_country_code,"
                    +"tweet_place_name, tweet_place_full_name , tweet_place_id, tweet_place_url, tweet_text,"
                    +" tweet_author_profile_background_color, tweet_author_profile_background_image, tweet_author_profile_image)"
                    +"VALUES(\""+ status.getUser().getId()+ "\", "
                    +"\""+status.getUser().getName()+ "\", "
                    +"\""+status.getUser().getScreenName()+ "\", "
                    +"\""+status.getUser().getLang()+ "\", "
                    +"\""+status.getUser().getCreatedAt()+ "\", "
                    +"\""+cet.format(status.getUser().getCreatedAt())+ "\", "
                    +"\""+utc.format(status.getUser().getCreatedAt())+ "\", "
                    +"\""+status.getUser().getDescription()+ "\", "
                    +"\""+status.getUser().getURL()+ "\", "
                    +"\""+status.getId()+ "\", "
                    +"\""+status.getCreatedAt()+ "\", "
                    +"\""+cet.format(status.getCreatedAt())+ "\", "
                    +"\""+utc.format(status.getCreatedAt())+ "\", "
                    +"\""+status.getInReplyToStatusId()+ "\", "
                    +"\""+status.getInReplyToUserId()+ "\", "
                    +"\""+Long.toString(retweet_id)+ "\", "
                    +"\""+status.getRetweetCount()+ "\", "
                    +"\""+status.isFavorited()+ "\", "
                    +"\""+status.getUser().isProtected()+ "\", "
                    +"\""+status.getUser().getURL()+ "\", "
                    +"\""+status.getUser().getFriendsCount()+ "\", "
                    +"\""+status.getUser().getListedCount()+ "\", "
                    +"\""+status.getUser().getFavouritesCount()+ "\", "
                    +"\""+status.getUser().getStatusesCount()+ "\", "
                    +"\""+status.getUser().isGeoEnabled()+ "\", "
                    +"\""+status.getSource().replaceAll("\"", "''")+ "\", "
                    +"\""+status.getUser().getLocation()+ "\", "
                    +"\""+status.getUser().getURL()+ "\", "
                    +"\""+status.getUser().getUtcOffset()+ "\", "
                    +"\""+status.getUser().getTimeZone()+ "\", "
                    +"\""+status.getUser().getLang()+ "\", "
                    +"\""+Double.toString(longitude)+ "\", "
                    +"\""+Double.toString(latitude)+ "\", "
                    +"\""+placeGeometryType+ "\", "
                    +"\""+placeCountry+ "\", "
                    +"\""+placeCountryCode+ "\", "
                    +"\""+placeName+ "\", "
                    +"\""+placeFullName+ "\", "
                    +"\""+placeId+ "\", "
                    +"\""+placeUrl+ "\", "
                    +"\""+status.getText().replaceAll("\"", "''")+ "\", "
                    +"\""+status.getUser().getProfileBackgroundColor()+ "\", "
                    +"\""+status.getUser().getProfileBackgroundImageURL()+ "\", "
                    +"\""+status.getUser().getProfileImageURL()+ "\") ";
            this.insertDB(sql.replaceAll("'","''"));
            collector.emit(new Values(status));
        }
    }

    /**
     * Play a request on MySQL Database
     * @param sql
     */
    private void insertDB(String sql)
    {
        String url = MyProperties.getProperties("mysql_string");
        String username = MyProperties.getProperties("mysql_user");
        String password = MyProperties.getProperties("mysql_password");

        System.out.println(url);
        System.out.println(username);
        System.out.println(password);

        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            Statement st = (Statement) connection.createStatement();

            st.executeUpdate(sql);

            connection.close();
        } catch (SQLException e) {
            System.out.println("Error while executing request : "+ e);
        }
    }


    public Map<String, Object> getComponentConfiguration() { return null; }
}