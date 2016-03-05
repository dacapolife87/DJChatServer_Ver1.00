package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.PreparedStatement;

import Server.djChatServerThread;

public class djChatDB_chat {
	protected Statement stmt;
	protected Statement stmt2;
	//protected PreparedStatement pstmt;
	djChatServerThread obj_ServerThread;
	
	public djChatDB_chat(Statement stmt,Statement stmt2, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.stmt = stmt;
		this.stmt2 = stmt2;
		this.obj_ServerThread = obj_ServerThread;
	}
	public void insert_userJoinRoom(String userId,int chatRoomNum){
		String query = "INSERT INTO userJoinRoom(userId,chatRoomNum)VALUES" +
				"('"
				+ userId+"','"
				+ chatRoomNum+"')";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void talkRoomRename(JSONObject json){
		JSONObject jsonData = (JSONObject) json.get("DATA");
		String My_Id = (String) json.get("SENDER");
		String index = (String) jsonData.get("INDEX");
		String roomName = (String) jsonData.get("ROOMNAME");
		String query = "update userJoinRoom set talkRoomName='"+roomName+"'where chatRoomNum='"+index+"'AND userId='"+My_Id+"'";
		try{
			stmt.executeUpdate(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void update_chatRoomInfo(int chatRoomNum,int joinUserCount,String chatType){
		String query = "update chatRoomInfo set joinUserCount='"+joinUserCount+"',"
				+ "chatType='"+chatType+"' where chatRoomNum = '"
				+chatRoomNum+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insert_chatRoomInfo(int joinUserCount,String chatType){
		int chatRoomNum=0;
		String query = "INSERT INTO chatRoomInfo (joinUserCount,chatType)VALUES" +
				"('"
				+ joinUserCount+"','"
				+ chatType+"')";
		try {
			stmt.executeUpdate(query);
			//chatRoomNum = select_chatRoomInfo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int select_chatRoomNum(){
		int count=0;
		String query = "SELECT chatRoomNum FROM chatRoomInfo Order By chatRoomNum DESC";
		try {
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()){
				count = rs.getInt(1);
				return count;
			}else{
				return 0;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
//	public int chatRoomCount(){
//		int count=0;
//		String query = "SELECT count(*) FROM chatRoomInfo";
//		try {
//			ResultSet rs = stmt.executeQuery(query);
//			
//			if(rs.next()){
//				count = rs.getInt(1);
//				return count;
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return count;
//	}
	public void delete_exitChatRoom(String userId,int chatRoomNum){
		String query = "delete from userJoinRoom where userId='"+userId+"' AND chatRoomNum ='"+chatRoomNum+"'";
		try {
			stmt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void insert_chatMsgInfo(String msg,int chatRoomNum ,String userId,Timestamp date){
		String query = "INSERT INTO chatMsgInfo ( msg,chatRoomNum,userId,sendTime)VALUES" +
				"('"
				+ msg+"','"
				+ chatRoomNum+"','"
				+ userId+"','"
				+ date+"')";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JSONObject select_chatRoomNum(String userId){
		JSONArray roomListJSONARRAY = new JSONArray();

		JSONObject dataJSON = new JSONObject();
		String query = "SELECT chatRoomNum, talkRoomName FROM userJoinRoom where userId = '"+userId+"'";
		ResultSet rs;
		try {
			int chatRoomNum;
			String roomName;
			rs = stmt.executeQuery(query);
			while(rs.next()){
				chatRoomNum = rs.getInt(1);
				roomName = rs.getString(2);
				JSONArray roomPeopleJSONARRAY = new JSONArray();
				JSONObject roomInfoJSON = new JSONObject();

				
				roomPeopleJSONARRAY = select_chatRoomPeople(chatRoomNum,userId);
				
				roomInfoJSON.put("ROOMINDEX", chatRoomNum);
				roomInfoJSON.put("ROOMNAME", roomName);
				roomInfoJSON.put("PEOPLEARRAY", roomPeopleJSONARRAY);
				
				roomListJSONARRAY.add(roomInfoJSON);
			}

			dataJSON.put("ROOMINFO", roomListJSONARRAY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataJSON;
	}
	
	public String select_chatRoomName(int chatRoomNum, String My_Id){
		String query = "SELECT talkRoomName FROM userJoinRoom where chatRoomNum='"+chatRoomNum+"' AND userId='"+My_Id+"'";
		String roomName = null;
		try{
			ResultSet rs3 = stmt2.executeQuery(query);
			roomName = rs3.getString(1);
			System.out.println("===============RoomName : " +roomName);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return roomName;
	}
	
	public JSONArray select_chatRoomPeople(int chatRoomNum,String myId){
		JSONArray roomPeopleJSONARRAY = new JSONArray();
		String query = "SELECT userId FROM userJoinRoom where chatRoomNum = '"+chatRoomNum+"'";
		try {
			String userId;
			ResultSet rs1 = stmt2.executeQuery(query);
			while(rs1.next()){

				userId = rs1.getString(1);
				if(!myId.equals(userId)){
					roomPeopleJSONARRAY.add(userId);
				}
			}
			rs1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roomPeopleJSONARRAY;
	}
	
	public void userChatRoomNum(String userId){
//		JSONArray chatRoomMsgArray = new JSONArray();
//		JSONArray chatRoomListArray = new JSONArray();
//		JSONObject dataJSON = new JSONObject();
		
		String query = "SELECT chatRoomNum ,lastread FROM userJoinRoom where userId = '"+userId+"'";
		try {
			int chatRoomNum;
			Timestamp lastread;
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				chatRoomNum = rs.getInt(1);
				lastread = rs.getTimestamp(2);
				select_chatRecentReadMsg(chatRoomNum,lastread);
				//chatRoomMsgArray = select_chatRecentReadMsg(chatRoomNum,time);
				
				//chatRoomListArray.add(chatRoomMsgArray);
			}
			//dataJSON.put("CHATROOMMSG", chatRoomListArray);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void select_chatRecentReadMsg(int chatRoomNum, Timestamp time) {
		// TODO Auto-generated method stub
		JSONObject chatRoomMsgJSONDATA = new JSONObject();
		JSONArray chatRoomMsgJSONARRAY = new JSONArray();
		JSONObject dataJSON = new JSONObject();
		
		String msg;
		String userId = null;
		int readUserCount;
		Timestamp sendTime;

		String query = "select * from chatMsgInfo where chatRoomNum = '"+chatRoomNum+"' AND sendTime > '"+time+"'";
		try {
			ResultSet rs1 = stmt2.executeQuery(query);
			System.out.println("msg check");
			rs1.next();
			while(rs1.next()){
				msg = rs1.getString(2);
				chatRoomNum = rs1.getInt(3);
				userId = rs1.getString(4);
				//readUserCount = rs1.getInt(6);
				sendTime = rs1.getTimestamp(7);
				
				chatRoomMsgJSONDATA.put("MSG", msg);
				chatRoomMsgJSONDATA.put("ROOMNUM", chatRoomNum);
				chatRoomMsgJSONDATA.put("USERID", userId);
				chatRoomMsgJSONDATA.put("TIME", sendTime.toString());
				
				
				System.out.println("msg info : "+msg);
				System.out.println("msg info : "+chatRoomNum);
				System.out.println("msg info : "+userId);
				//System.out.println("msg info : "+readUserCount);
				System.out.println("msg info : "+sendTime);
				System.out.println("===============================");
				chatRoomMsgJSONARRAY.add(chatRoomMsgJSONDATA);
			}
			dataJSON.put("CHATROOMMSG",chatRoomMsgJSONARRAY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		obj_ServerThread.makeJSONPacket("RES_TALK_NOTREADMSG", dataJSON, userId, null);
	}
	public JSONArray all_TalkRoomNum(){
		JSONArray roomList = new JSONArray();
		JSONArray peopleList = new JSONArray();
		
		
		int chatRoomNum = 0 ;

		String query = "SELECT DISTINCT chatRoomNum FROM userJoinRoom";
		ResultSet rs;
		
		try{
			rs = stmt.executeQuery(query);
			while(rs.next()){
				JSONObject roomInfo = new JSONObject();
				chatRoomNum = rs.getInt(1);
				peopleList = talkRoomPeople(chatRoomNum);
				System.out.println("djCHatDB NUM : "+chatRoomNum);
				System.out.println("djCHatDB PEOPLE : "+peopleList);
				roomInfo.put("INDEX", chatRoomNum);
				roomInfo.put("PEOPLE", peopleList);
				
				roomList.add(roomInfo);
			}
		}catch(Exception e){
			System.out.println("asdfasdfasdfasdf");
		}
		return roomList;		
	}
	public JSONArray talkRoomPeople(int chatRoomNum){
		JSONArray peopleList = new JSONArray();

		
		String userId;
		String query = "SELECT userId FROM userJoinRoom where chatRoomNum ='"+chatRoomNum+"'";
		ResultSet rs1;
		
		try{
			rs1 = stmt2.executeQuery(query);
			while(rs1.next()){
				userId = rs1.getString(1);
				peopleList.add(userId);
			}
		}catch(Exception e){
			System.out.println("asdfasdfasdfasdf");
		}	
		
		return peopleList;
	}
	public JSONArray all_talkRoom(){
		JSONArray roomList = new JSONArray();
		JSONArray roomPeopleList = new JSONArray();
//		JSONObject dataJSON = new JSONObject();
		
		int chatRoomNum = 0 ;
		String userId;
		String query = "SELECT chatRoomNum, userId FROM userJoinRoom";
		ResultSet rs;
		
		try{
			rs = stmt.executeQuery(query);
			while(rs.next()){
				JSONObject dataJSON = new JSONObject();
				chatRoomNum = rs.getInt(1);
				userId = rs.getString(2);
				dataJSON.put("INDEX", chatRoomNum);
				dataJSON.put("USERID", userId);
				System.out.println("DB chatRoomNum : " +chatRoomNum);
				System.out.println("DB userId : " +userId);
				roomList.add(dataJSON);
				System.out.println("DB JSONARRAY : " +roomList);
			}
		}catch(Exception e){
			System.out.println("asdfasdfasdfasdf");
		}
		return roomList;
	}
	public JSONArray userJoinRoom(String userId){
		JSONArray roomList = new JSONArray();
		int chatRoomNum = 0 ;
		String query = "SELECT chatRoomNum FROM userJoinRoom where userId='"+userId+"'";
		ResultSet rs;
		try{
			rs = stmt.executeQuery(query);
			while(rs.next()){
				chatRoomNum = rs.getInt(1);
				roomList.add(chatRoomNum);
			}
		}catch(Exception e){
			System.out.println("asdfasdfasdfasdf");
		}
		return roomList;
	}

	
	/*
	public void select_chatReadMsg(int roomNum){
		String msg;
		int chatRoomNum;
		String userId;
		int readUserCount;
		Timestamp sendTime;
		String query = "SELECT * FROM chatMsgInfo where chatRoomNum = '"
				+roomNum+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				msg = rs.getString(2);
				chatRoomNum = rs.getInt(3);
				userId = rs.getString(4);
				readUserCount = rs.getInt(6);
				sendTime = rs.getTimestamp(7);
				System.out.println("msg info : "+msg);
				System.out.println("msg info : "+chatRoomNum);
				System.out.println("msg info : "+userId);
				System.out.println("msg info : "+readUserCount);
				System.out.println("msg info : "+sendTime);
				System.out.println("===============================");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public void userLastReadMsgTime(JSONObject dataJSON){
		long time = System.currentTimeMillis();
		Timestamp date = new Timestamp(time);
		String userId;
		String index;
		int roomNum;
		JSONObject data = new JSONObject();
		data = (JSONObject) dataJSON.get("DATA");
		index = (String) data.get("DATA");
		roomNum = Integer.parseInt(index);
		userId = (String) dataJSON.get("SENDER");
		String query = "update userJoinRoom set lastread='"+date+"' where userId = '"
				+userId+"' AND chatRoomNum ='"+roomNum+"'";
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
