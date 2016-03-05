package User;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_nickCheck {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_nickCheck(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void existUserIdCheck(JSONObject nickInfo){
		JSONObject dataJSON = new JSONObject();
		
		String userNickName = null;

		
		dataJSON = (JSONObject) nickInfo.get("data");
		
		JSONObject dataInfo = (JSONObject) dataJSON.get(0);
		
		userNickName = (String) dataInfo.get("USERNICKNAME");
		
		if(obj_DB.existUserNickCheck(userNickName)){
			obj_ServerThread.makeJSONPacket("RES_USER_NICKNOTEXIST", null, null, null);
		}else{
			obj_ServerThread.makeJSONPacket("RES_USER_NICKEXIST", null, null, null);
		}		
	}
}
