package server;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;
@ServerEndpoint(value = "/hello")
public class DispatchServer {
	private static Connection conn = DBConn.getConnection(); 
	
	
    private static final Logger LOGGER = 
            Logger.getLogger(DispatchServer.class.getName());
    
    @OnOpen
    public void onOpen(Session session) {
        LOGGER.log(Level.INFO, "New connection with client: {0}", 
                session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException, EncodeException {   	
    	Statement stmt = null;
    	String sql = null;
    	if (message.endsWith("") || message==null){
    		 sql= "SELECT id, time, lati, longi FROM info;";
    	}else{
    		 sql = "SELECT id, time, lati, longi FROM info i "
    				+ "LEFT JOIN twit_keyword tk ON i.id = tk.twit_id "
    				+ "LEFT JOIN keyword k ON k.kid = tk.key_id "
    				+ "WHERE k.kword='"+message+"';";
    	}
    	LOGGER.log(Level.INFO, "onMessage for client: {0}", 
                session.getId());
    	LOGGER.log(Level.INFO, "SQL: "+sql);   	
    	
    	JSONObject outer =new JSONObject();
    	    	
        for (Session peer : session.getOpenSessions()) {
//            while (true) {
            	ResultSet rs=DBConn.doSelect(sql, conn, stmt);
            	try {
            		int i=0;
        			while(rs.next()){
        				JSONObject inner = new JSONObject ();
        				//System.out.println(rs.getString("id"));
        				inner.put("latitude", rs.getString("lati"));
        				inner.put("longitude", rs.getString("longi"));
        				outer.put("loc"+i, inner);
        				i++;        				
        			}
        		} catch (SQLException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	System.out.println(outer);
            	//obj.put(key, value)
            	DBConn.closeResultSet(rs, stmt);
                
				peer.getBasicRemote().sendText(outer.toString());
                //peer.getBasicRemote().sendText("Test Message ");
                //Thread.sleep(5000);
//            }
        }
        
        
//        LOGGER.log(Level.INFO, "New message from Client [{0}]: {1}", 
//                new Object[] {session.getId(), message});
//        return "Server received [" + message + "]";
    }
    
    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close connection for client: {0}", 
                session.getId());
    }
    
    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}