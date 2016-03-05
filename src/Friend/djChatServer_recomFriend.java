package Friend;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_recomFriend {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_recomFriend(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void showFriend(JSONObject friendInfo){
		String userId = null;
		JSONObject dataJSON;
		userId = (String) friendInfo.get("SENDER");
		dataJSON = obj_DB.userRecommFriendList(userId);
		obj_ServerThread.makeJSONPacket("RES_FRIEND_RECOMMANDLIST",dataJSON , userId,null);
	}
}