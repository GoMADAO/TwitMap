package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.Global;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class DataUpdate {
	
	private static Connection conn = DBConn.getConnection();
	private static HashMap<String, Integer> keywords = new HashMap<String, Integer>();
	
	public static boolean isInRange(double low, double hi, double val){
		if (Double.compare(low, val)<=0 && Double.compare(val, hi)<=0)
			return true;
		return false;		
	}
	
	public static void getKeywords(){
		String sql = "SELECT * FROM keyword;";
		Statement stmt =null;
		ResultSet rs = DBConn.doSelect(sql, conn,stmt);
		
		try {
			while (rs.next()){
				int id = rs.getInt("kid");
				String key = rs.getString("kword");
				keywords.put(key, id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConn.closeResultSet(rs, stmt);
		}
	}
	
	public static void main(String[] args) throws TwitterException {
    	//just fill this
    	 ConfigurationBuilder cb = new ConfigurationBuilder();    	 
    	 
         cb.setDebugEnabled(true)
           .setOAuthConsumerKey(Global.ConsumerKey)
           .setOAuthConsumerSecret(Global.ConsumerSecret)
           .setOAuthAccessToken(Global.AccessToken)
           .setOAuthAccessTokenSecret(Global.AccessTokenSecret);
                       
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
        if(keywords.size()==0){
        	getKeywords();
        }
        
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if(status.getGeoLocation()!=null){
            		System.out.println("ID:"+status.getGeoLocation()+ status.getCreatedAt()+ status.getId());
            		
            		if(conn!=null){
            			String temp =""+status.getGeoLocation();
                		temp = temp.substring("GeoLocation".length());
                		Pattern p=Pattern.compile("\\{|\\}");
            			Matcher m=p.matcher(temp);                			
            			temp=m.replaceAll("");	
            			Pattern p1=Pattern.compile("longitude=|latitude=");
            			m=p1.matcher(temp);        			
            			String location=m.replaceAll("");
            			String[] l = location.split(", ");
            			System.out.println(l[0]);
            			      
            			
            			String time =""+status.getCreatedAt();
            			String s[] = time.split(" ");
            			String datetime = s[5]+"-"+s[1]+"-"+s[2]+" "+s[3];
            			System.out.println(datetime);	
            			
            			
            			
            			String sqlinfo ="INSERT INTO info VALUES ("
            					+ "'"+status.getId()+"',"
            					+ "STR_TO_DATE('"+datetime+"','%Y-%M-%d %H:%i:%s'),"
            					+ "'"+l[0]+"',"
            					+ "'"+l[1]+"'"
            					+ ");";
            			System.out.println(sqlinfo);
            			DBConn.doInsert(sqlinfo, conn);
            			
            			Iterator<String> it = keywords.keySet().iterator();
            			while(it.hasNext()){
            				String key = it.next();
            				Pattern prela = Pattern.compile(key);
                			Matcher m3 = prela.matcher(status.getText());
                			if (m3.find()){
                				String sqlrela ="INSERT INTO twit_keyword VALUES ("
                						+ "'"+status.getId()+"',"
                						+ ""+keywords.get(key)
                						+ ");";
                				System.out.println(sqlrela);
                				DBConn.doInsert(sqlrela, conn);
                				
                			}
            			}
            			           			
            			
            		}
            		
            		
            	}
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
