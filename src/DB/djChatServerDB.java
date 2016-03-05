package DB;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.PreparedStatement;

import Server.djChatServerThread;


public class djChatServerDB {
	djChatDB_chat obj_DBChat;
	djChatDB_user obj_DBUser;
	djChatDB_friend obj_DBFriend;
	djChatServerThread obj_ServerThread;
	
	private static final String DELEMETER = " '"; //소메시지 구분자
	
	protected Connection conn = null;
	protected Statement stmt = null;
	protected Statement stmt2 = null;
	
	public djChatServerDB(djChatServerThread obj_ServerThread){
		this.obj_ServerThread = obj_ServerThread;
	}
	public void connectDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/DJChatDB";
			String db_Id = "root";
			String db_Password = "1234";
			String property = "?characterEncoding=EUC_KR";
			conn = DriverManager.getConnection(url+property,db_Id,db_Password);
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			
			
			obj_DBChat = new djChatDB_chat(stmt,stmt2,obj_ServerThread);
			obj_DBUser = new djChatDB_user(stmt);
			obj_DBFriend = new djChatDB_friend(stmt,obj_ServerThread);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/*아래 함수 세분화*/
	public int select_chatRoomNum(){
		int chatRoomNum;
		chatRoomNum = obj_DBChat.select_chatRoomNum();
		return chatRoomNum;
	}
	public void insert_chatRoom(){
		String chatType="S";
		obj_DBChat.insert_chatRoomInfo(1,chatType);
		
	}
	public void insert_userJoinRoom(String userId,int chatRoomNum){
		obj_DBChat.insert_userJoinRoom(userId, chatRoomNum);
	}
	public void update_chatRoomInfo(int chatRoomNum,int userCount,String chatType){
		obj_DBChat.update_chatRoomInfo(chatRoomNum, userCount, chatType);
	}
	///////////////////////////////////chat
	public void insert_chatMakeRoom(String userId,String friendList){
		String chatType="S";
		obj_DBChat.insert_chatRoomInfo(1,chatType);
		int chatRoomNum = obj_DBChat.select_chatRoomNum();
		String friendId;
		
		int userCount=1;
		StringTokenizer st = new StringTokenizer(friendList,DELEMETER);
		friendId = st.nextToken();
		while(st.hasMoreTokens()){
			friendId=st.nextToken();
			userCount++;
			obj_DBChat.insert_userJoinRoom(friendId,chatRoomNum);
		}
		if(userCount>2){
			chatType="M";
		}
		obj_DBChat.update_chatRoomInfo(chatRoomNum, userCount, chatType);
	}
	public void delete_exitChatRoom(String userId,int chatRoomNum){
		obj_DBChat.delete_exitChatRoom(userId, chatRoomNum);
	}
	
	
	/*
	public void select_chatReadMsg(int roomNum){
		obj_DBChat.select_chatReadMsg(roomNum);
	}*/
//	public void checkFriend(String userId, String friendId) {
//		// TODO Auto-generated method stub
//		obj_DBFriend.checkFriend(userId, friendId);
//	}


	public void select_showRecommandFriendList(String userId){
		obj_DBFriend.showRecommandFriendList(userId);
	}
	////////////////////////////////8월9일 변경사항
	public boolean insert_UserJoin(String userId,String password,String name,String phoneNum,String nickName){
		return obj_DBUser.insert_UserJoin(userId, password, name, nickName, phoneNum);
	}
	public boolean existUserIdCheck(String userId){
		return obj_DBUser.existUserId(userId);
	}
	public boolean existUserNickCheck(String userId){
		return obj_DBUser.existUserNick(userId);
	}
	public void userAddFriend(String userId, String friendId){
		obj_DBFriend.userAddFriend(userId, friendId);		
	}
	public void userDelFriend(String userId, String friendId){
		obj_DBFriend.userDelFriend(userId, friendId);
	}
	public boolean userLogin(String userId,String userPassword){
		return obj_DBUser.userLogin(userId, userPassword);
	}
	public JSONObject userFriendList(String userId){
		return obj_DBFriend.showFriendList(userId);
	}
	public JSONObject userRecommFriendList(String userId){
		return obj_DBFriend.showRecommandFriendList(userId);
	}
	public boolean existFriendIdCheck(String friendId){
		return obj_DBFriend.existFriendId(friendId);
	}
	public int checkFriendStatus(String userId,String friendId){
		return obj_DBFriend.checkFriendStatus(userId, friendId);
	}
	public void recommandAddFriend(String userId,String friendId){
		obj_DBFriend.userAddFriend(userId, friendId);
	}
	public String userStatusMsg(String userId){
		return obj_DBUser.userStatusMsg(userId);
	}
	public JSONObject userJoinChatRoom(String userId){
		return obj_DBChat.select_chatRoomNum(userId);
	}
	public void insert_chatMsgInfo(String msg, int chatRoomNum, String userId,Timestamp date) {
		// TODO Auto-generated method stub
		obj_DBChat.insert_chatMsgInfo(msg, chatRoomNum, userId, date);
	}
	public void userLastReadMsgTime(JSONObject json){
		obj_DBChat.userLastReadMsgTime(json);
	}
	public Timestamp lastReadTime(String userId){
		Timestamp time = null;
		time = obj_DBUser.lastReadMsgTime(userId);
		return time;
	}
	public void select_chatRoomRecentMsg(String userId){
		obj_DBChat.userChatRoomNum(userId);
//		Timestamp time = null;
//		int chatRoomNum;
//		
//		time = lastReadTime(userId);
//		
//		if(time!=null){
//			obj_DBChat.userChatRoomNum(userId,time);
//		}
	}

	public JSONArray select_allTalkRoom(){
		return obj_DBChat.all_TalkRoomNum();
	}
	public JSONArray userJoinRoom(String userId){
		return obj_DBChat.userJoinRoom(userId);
	}
	public void talkRoom_Rename(JSONObject json){
		obj_DBChat.talkRoomRename(json);
	}
}
