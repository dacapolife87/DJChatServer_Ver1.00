package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Server.djChatServerThread;

public class djChatDB_friend {
	protected Statement stmt;
	djChatServerThread obj_ServerThread;
	
	private static final String DELEMETER = " '"; //소메시지 구분자
	
	public djChatDB_friend(Statement stmt,djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.stmt = stmt;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void userAddFriend(String userId,String friendId){
		String query = "INSERT INTO userFriend (userId,friendId,friendStatus)VALUES" +
				"('"
				+userId+"','"
				+friendId+"','1')";
		try {
			stmt.executeUpdate(query);
			checkFriend(friendId,userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void checkFriend(String userId,String friendId){
		int statusNum;
		String query = "SELECT friendStatus FROM userFriend where (userId = '"
				+userId+"' AND friendId = '"
				+friendId+"')";
		try {
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()){
				statusNum = rs.getInt(1);
				if(statusNum==1){
					changeFriendRelation(userId,friendId, 2);
					changeFriendRelation(friendId,userId, 2);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int checkFriendStatus(String userId,String friendId){
		int statusNum=0;
		String query = "SELECT friendStatus FROM userFriend where (userId = '"
				+userId+"' AND friendId = '"
				+friendId+"')";
		try {
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()){
				statusNum = rs.getInt(1);
				return statusNum;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statusNum;
	}

	public void changeFriendRelation(String userId,String friendId,int statusNum){
		String query = "update userFriend set friendStatus='"+statusNum+"' where userId = '"
					+userId+"' AND friendId = '"
				+friendId+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void userDelFriend(String userId,String friendId){
		String query = "delete from userFriend where userId='"+userId+"' AND friendId ='"+friendId+"'";
		try {
			stmt.execute(query);
			changeFriendRelation(friendId,userId, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JSONObject showFriendList(String userId){
		JSONArray friendJSONARRAY = new JSONArray();
		JSONObject dataJSON = new JSONObject();
		String friendId;
		
		String query = "SELECT * FROM userFriend where userId = '"
				+userId+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
					
			while(rs.next()){
				System.out.println("db_friend list : "+rs.getString(2));
				friendId = rs.getString(2);

				friendJSONARRAY.add(friendId);
			}
			System.out.println("db_friend json array : "+friendJSONARRAY);
			dataJSON.put("FRIENDLIST", friendJSONARRAY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataJSON;
	}
	
	public JSONObject showRecommandFriendList(String userId){
		JSONArray friendJSONARRAY = new JSONArray();
		JSONObject dataJSON = new JSONObject();
		
		String friendId;
		System.out.println("DB_Friend : "+userId);
		String query = "SELECT * FROM userFriend where friendId = '"
				+userId+"' AND friendStatus = '1'";
		try {
			ResultSet rs = stmt.executeQuery(query);
					
			while(rs.next()){
				friendId = rs.getString(1);
				System.out.println("DB_Friend |"+friendId+"|");
				friendJSONARRAY.add(friendId);
			}
			dataJSON.put("RECOMMANDFRIENDLIST", friendJSONARRAY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DB_Friend : "+dataJSON);
		return dataJSON;
	}
	public boolean existFriendId(String friendId){
		int count=0;
		String query = "SELECT count(*) FROM userInfo where userId = '"
				+friendId+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			
			count = rs.getInt(1);
			if(count>0){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
