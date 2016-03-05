package User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_loginAlert {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_loginAlert(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void userLoginAlert(JSONObject joinInfo){
		JSONObject dataJSON = new JSONObject();
		JSONArray friendListJSON = new JSONArray();
		String userId = null;
		String friendList = null;
		
		userId = (String) joinInfo.get("SENDER");
		
		dataJSON = obj_DB.userFriendList(userId);
		friendListJSON = (JSONArray) dataJSON.get("FRIENDLIST");
		for(int i=0; friendListJSON.size()<i;i++){
			String friendId =null;
			friendId = (String) friendListJSON.get(i);
			obj_ServerThread.setter_Dos(friendId);
			
			obj_ServerThread.makeJSONPacket("RES_FRIEND_LOGIN", null, userId, friendListJSON);
		}
		
	}
}