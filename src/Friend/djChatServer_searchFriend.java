package Friend;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_searchFriend {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_searchFriend(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void existFriendIdCheck(JSONObject idInfo){
		String userId = null;
		JSONObject dataJSON = new JSONObject();
		
		dataJSON = (JSONObject) idInfo.get("DATA");
		userId = (String) dataJSON.get("DATA");
		
		if(obj_DB.existUserIdCheck(userId)){
			obj_ServerThread.makeJSONPacket("RES_FRIEND_IDNOTEXIST", null, null, null);
		}else{
			obj_ServerThread.makeJSONPacket("RES_FRIEND_IDEXIST", dataJSON, null, null);
		}		
	}
}