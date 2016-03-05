package Friend;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_delFriend {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_delFriend(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void delFriend(JSONObject friendInfo){
		JSONObject dataJSON = new JSONObject();
		
		String userId = null;
		String friendId = null;
		
		dataJSON = (JSONObject) friendInfo.get("DATA");
		
		//JSONObject dataInfo = (JSONObject) dataJSON.get(0);
		
		friendId = (String) dataJSON.get("FRIENDID");
		userId = (String) friendInfo.get("SENDER");
		
		
		obj_DB.userDelFriend(userId, friendId);
		
		obj_ServerThread.makeJSONPacket("RES_FRIEND_DEL", dataJSON, userId, null);		
	}
}
