package test;

import twitter4j.Location;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class TwitterAPI {
    /**
     * Main entry of this application.
     *
     * @param args
     */
    public static void main(String[] args) throws TwitterException {
    	//just fill this
    	 ConfigurationBuilder cb = new ConfigurationBuilder();    	 
    	 
         cb.setDebugEnabled(true)
           .setOAuthConsumerKey("mxtpBJiYQ7l8UzZTIX89kLj0W")
           .setOAuthConsumerSecret("a6FxQi6qB91AM9UbhHpBRihVgt2T4grTqWFJdDuIByjPEvcprG")
           .setOAuthAccessToken("3009085449-OnTm8hNFHRe1hA0Px3OhF0entdMgaPP0H4yaK1n")
           .setOAuthAccessTokenSecret("mpvTsEtmndA9Z0weMFyWsGFfaSAIALtIsBmMvfP4zZHEz");
                       
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
//        TwitterAdapter ta = new TwitterAdapter();
//        ta.gotGeoDetails(null);
        
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if(status.getGeoLocation()!=null)
            		System.out.println("ID:"+status.getGeoLocation()+ status.getCreatedAt()+ status.getId());
                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText()+" Place: "+status.getGeoLocation());
            }
            
            
            
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                //System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.sample();
    }
}