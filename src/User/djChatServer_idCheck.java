package User;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_idCheck {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_idCheck(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void existUserIdCheck(JSONObject idInfo){
		String userId = null;
		
		userId = (String) idInfo.get("SENDER");
		
		if(obj_DB.existUserIdCheck(userId)){
			obj_ServerThread.makeJSONPacket("RES_USER_IDNOTEXIST", null, null, null);
		}else{
			obj_ServerThread.makeJSONPacket("RES_USER_IDEXIST", null, null, null);
		}		
	}
}
