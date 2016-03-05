package Friend;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_addFriend {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_addFriend(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void addFriend(JSONObject friendInfo){
		JSONObject dataJSON = new JSONObject();
		String userId = null;
		String friendId = null;
		int friendStatus;
		
		dataJSON = (JSONObject) friendInfo.get("DATA");

		friendId = (String) dataJSON.get("DATA");
		userId = (String) friendInfo.get("SENDER");
		friendStatus = obj_DB.checkFriendStatus(userId, friendId);
		System.out.println("userId : "+userId);
		obj_ServerThread.setter_Dos(userId);
		if(friendStatus==0){
			obj_DB.userAddFriend(userId, friendId);
			obj_ServerThread.makeJSONPacket("RES_FRIEND_ADD", dataJSON, userId, null);
		}else{
			obj_ServerThread.makeJSONPacket("RES_FRIEND_ADDFAIL", dataJSON, userId, null);
		}		
	}
	public void addRecommandFriend(JSONObject friendInfo){
		JSONObject dataJSON = new JSONObject();
		String userId = null;
		String friendId = null;
		int friendStatus;
		
		dataJSON = (JSONObject) friendInfo.get("DATA");

		friendId = (String) dataJSON.get("DATA");
		userId = (String) friendInfo.get("SENDER");
		
		//friendStatus = obj_DB.checkFriendStatus(userId, friendId);
		
		
//		System.out.println("userId : "+userId);
		
		obj_ServerThread.setter_Dos(userId);
		obj_DB.recommandAddFriend(userId, friendId);
		obj_ServerThread.makeJSONPacket("RES_FRIEND_ADD", dataJSON, userId, null);
		
//		if(friendStatus==0){
//			obj_DB.userAddFriend(userId, friendId);
//			obj_ServerThread.makeJSONPacket("RES_FRIEND_ADD", dataJSON, userId, null);
//		}else{
//			obj_ServerThread.makeJSONPacket("RES_FRIEND_ADDFAIL", dataJSON, userId, null);
//		}		
	}
}
