package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Chat.djChatServer_exitTalkRoom;
import Chat.djChatServer_inviteToTalk;
import Chat.djChatServer_makePacket;
import Chat.djChatServer_makeTalkRoom;
import Chat.djChatServer_notReadMsg;
import Chat.djChatServer_reJoinTalkRoom;
import Chat.djChatServer_roomList;
import Chat.djChatServer_updateTalkRoom;
import DB.djChatServerDB;
import Friend.djChatServer_addFriend;
import Friend.djChatServer_delFriend;
import Friend.djChatServer_recomFriend;
import Friend.djChatServer_searchFriend;
import Friend.djChatServer_showFriend;
import User.djChatServer_idCheck;
import User.djChatServer_login;
import User.djChatServer_nickCheck;
import User.djChatServer_userJoin;

public class djChatServerThread implements Runnable {
	//djChatServer obj_Server;
	djChatServerDB obj_DB;
	
	djChatServer_login obj_login;
	djChatServer_userJoin obj_join;
	djChatServer_idCheck obj_idCheck;
	djChatServer_nickCheck obj_nickCheck;
	djChatServer_addFriend obj_addFriend;
	djChatServer_delFriend obj_delFriend;
	djChatServer_showFriend obj_showFriend;
	djChatServer_recomFriend obj_recommFriend;
	djChatServer_roomList obj_roomlist;
	djChatServer_notReadMsg obj_notReadMsg;
	djChatServer_searchFriend obj_searchFriend;
	
	djChatServer_reJoinTalkRoom obj_reJoinTalkRoom;
	djChatServer_exitTalkRoom obj_exitTalkRoom;
	djChatServer_makePacket obj_makePacket;
	djChatServer_makeTalkRoom obj_makeTalkRoom;
	djChatServer_updateTalkRoom obj_updateTalkRoom;
	djChatServer_inviteToTalk obj_inviteTalkRoom;
    
    Socket soc;
	DataInputStream dis;
	DataOutputStream dos;
	
	private String user_id;
	 //대화방 만들때 필요한 HashMap
    protected static HashMap<String, HashMap> talkHash = new HashMap(); //대화방 저장 HashMap
    protected static HashMap<String, DataOutputStream> client = new HashMap(); //사용자 저장 HashMap
    ///추가
    HashMap<String,DataOutputStream> talkroomHash = new HashMap();
	
	public djChatServerThread(Socket soc) {
		// TODO Auto-generated constructor stub
		this.soc = soc;

		obj_DB = new djChatServerDB(this);
		obj_DB.connectDB();
		
		obj_login = new djChatServer_login(obj_DB, this);
		obj_join = new djChatServer_userJoin(obj_DB, this);
		obj_idCheck = new djChatServer_idCheck(obj_DB, this);
		obj_nickCheck = new djChatServer_nickCheck(obj_DB, this);
		obj_showFriend = new djChatServer_showFriend(obj_DB, this);
		obj_addFriend = new djChatServer_addFriend(obj_DB, this);
		obj_delFriend = new djChatServer_delFriend(obj_DB, this);
		obj_recommFriend = new djChatServer_recomFriend(obj_DB, this);
		obj_roomlist = new djChatServer_roomList(obj_DB, this);
		obj_notReadMsg = new djChatServer_notReadMsg(obj_DB, this);
		obj_exitTalkRoom = new djChatServer_exitTalkRoom(obj_DB, this);
		obj_makePacket = new djChatServer_makePacket(obj_DB, this);
		obj_makeTalkRoom = new djChatServer_makeTalkRoom(obj_DB, this);
		obj_inviteTalkRoom = new djChatServer_inviteToTalk(obj_DB, this);
		obj_searchFriend = new djChatServer_searchFriend(obj_DB, this);
		obj_updateTalkRoom = new djChatServer_updateTalkRoom(obj_DB, this);
		obj_reJoinTalkRoom = new djChatServer_reJoinTalkRoom(obj_DB, this);
		//obj_User = new djChatServer_user(obj_DB,this);
		//obj_Chat = new djChatServer_chat(obj_DB,this);
		//testFunc();
		
		Collections.synchronizedMap(client);
		
		makeStream();
		reMake_Server();
	}
	public void reMake_Server(){
		String userId;
		String index;
		int num;
		JSONArray jsonArray = new JSONArray();
		jsonArray = obj_DB.select_allTalkRoom();
		System.out.println("JSONARRAY : " +jsonArray);
		
		for(int i=0; i<jsonArray.size(); i++){
			System.out.println("size : " +i);
			HashMap<String, DataOutputStream> tempHash = new HashMap<>();
			JSONObject dataJSON = new JSONObject();
			JSONArray peopleArray = new JSONArray();
			dataJSON = (JSONObject) jsonArray.get(i);
			System.out.println("dataJSon : " +dataJSON);
			
			peopleArray = (JSONArray)dataJSON.get("PEOPLE");
			num = (int)dataJSON.get("INDEX");
			for(int j=0;j<peopleArray.size();j++){
				userId = (String) peopleArray.get(j);
				tempHash.put(userId, null);
			}

			index = Integer.toString(num);
			
			talkHash.put(index, tempHash);
		}
		System.out.println("TalkHash : " +talkHash);
	}
	
	public void reConnect_talkRoom(String id, DataOutputStream dos){
		JSONArray jsonArray = new JSONArray();
		jsonArray = obj_DB.userJoinRoom(id);
		String index;
		String userId;
		int num;
		
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject dataJSON = new JSONObject();
			num = (int)jsonArray.get(i);	
			index = Integer.toString(num);
			
			talkroomHash = talkHash.get(index);
			
			talkroomHash.put(user_id, dos);
			talkHash.put(index, talkroomHash);

		}
		System.out.println("reconnect hash : "+talkHash);
	}
	public HashMap<String, DataOutputStream> getHash(){
		return client;
	}
	public void putUserHash(String id){
		this.user_id = id;
		client.put(id, dos);
		reConnect_talkRoom(id,dos);
	}
    public DataOutputStream getter_Dos(String target){
    	return (DataOutputStream)client.get(target);
    }
//	public void setter_Dos(String target){
//		if(client.containsKey(target)){
//			dos = (DataOutputStream)client.get(target);
//		}
//	}
	public boolean setter_Dos(String target){
		if(client.containsKey(target)){
			dos = (DataOutputStream)client.get(target);
			return true;
		}
		return false;
	}
	public void setter_TalkHash(String index, HashMap<String,DataOutputStream> talkRoomHash){
		talkHash.put(index, talkRoomHash);
	}
	public HashMap<String,DataOutputStream> getter_TalkHash(String index){
		System.out.println("talk hash size : "+talkHash.size());
		System.out.println("talk hash : "+talkHash.get(index));
		return talkHash.get(index);
	}
	public void removeTalkHash(String index){
		talkHash.remove(index);
	}
	public djChatServer_makePacket getObj_makePacket(){
		return obj_makePacket;
	}
	public djChatServer_updateTalkRoom getObj_updateTalk(){
		return obj_updateTalkRoom;
	}
	public djChatServer_reJoinTalkRoom getObj_reJoinTalkRoom(){
		return obj_reJoinTalkRoom;
	}
	public void stop(String id){
		try {
			setter_Dos(id);
			//obj_Server.stopServerThread();
			dis.close();
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error : "+e);
				e.printStackTrace();
		}		
	}
	
	@Override
    public void run() {
        // TODO Auto-generated method stub
		String msg;

		try {
			while(!Thread.interrupted()){
				msg = dis.readUTF();
				checkJSONPacket(msg);
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block			
		}finally{
			System.out.println("this : "+user_id);
			stop(user_id);
			client.remove(user_id);
			System.out.println("헤쉬맵 제거 : "+client.size());
		}
	}
	public void makeStream(){
		try {
			dis = new DataInputStream(new BufferedInputStream(soc.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(soc.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public String reformJSON(JSONObject json){
		return json.toJSONString();
	}
	
	public void makeJSONPacket(String seperator,JSONObject dataJSON,String sender,JSONArray receiver){
		JSONObject packetJSON = new JSONObject();
		packetJSON.put("SEPARATOR", seperator);
		packetJSON.put("DATA", dataJSON);
		packetJSON.put("SENDER", sender);
		packetJSON.put("RECEIVER", receiver);
		
		sendPacket(reformJSON(packetJSON));
	}
	

		
	public void sendPacket(String sendMsg){
		try {
			System.out.println("send packet : "+sendMsg);
			dos.writeUTF(sendMsg);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void checkJSONPacket(String data){
		String separator=null;
		JSONObject receiveData =null;
		
		JSONParser jsonParser = new JSONParser();
		try {
			receiveData = (JSONObject) jsonParser.parse(data);
			separator = (String) receiveData.get("SEPARATOR");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ServerThread _ checkJSONPacket : "+data);
		String id = null;
		id = (String) receiveData.get("SENDER");
		switch(separator){
		case "REQ_USER_LOGIN":
			if(obj_login.userLogin(receiveData)){
				obj_recommFriend.showFriend(receiveData);
				obj_roomlist.userRoomList(id);
				obj_notReadMsg.recentMsg(id);
			}			
			break;
		case "REQ_USER_JOIN":
			obj_join.userJoin(receiveData);
			break;
		case "REQ_USER_IDEXIST":
			obj_idCheck.existUserIdCheck(receiveData);
			break;
		case "REQ_USER_NICKEXIST":
			obj_nickCheck.existUserIdCheck(receiveData);
			break;
		case "REQ_FRIEND_EXIST":
			obj_searchFriend.existFriendIdCheck(receiveData);
			break;
		case "REQ_FRIEND_LIST":
			//obj_showFriend.showFriendList(id);
			obj_showFriend.showFriend(receiveData);
			break;
		case "REQ_FRIEND_ADD":
			obj_addFriend.addFriend(receiveData);
			break;
		case "REQ_FRIEND_RECOMMANDADD":
			obj_addFriend.addRecommandFriend(receiveData);
			break;
		case "REQ_FRIEND_DELETE":
			obj_delFriend.delFriend(receiveData);
			break;
		case "REQ_FRIEND_RECOMMANDLIST":
			obj_recommFriend.showFriend(receiveData);
			break;
		case "REQ_TALK_MAKEROOM":
			obj_makeTalkRoom.makeTalkRoom(receiveData);
			break;
		case "REQ_TALK_SENDMSG": //대화방 내에서 채팅메시지 전달
			obj_makePacket.makeRoomMsg(receiveData);
			break;
		case "REQ_TALK_EXITROOM":
			obj_exitTalkRoom.exitTalkRoom(receiveData);
			break;
		case "REQ_TALK_INVITE": //대화방 내에서 추가로 초대하는 패킷전달
			obj_inviteTalkRoom.inviteToTalk(receiveData);
			break;
		case "REQ_TALK_RENAME":
			obj_DB.talkRoom_Rename(receiveData);
			break;
		case "REQ_USER_LASTREAD":
			// 마지막 읽은 대화 시간 체크
			obj_DB.userLastReadMsgTime(receiveData);
			break;
		}
	}
}
