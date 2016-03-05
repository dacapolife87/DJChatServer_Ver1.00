package User;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_login {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_login(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public boolean userLogin(JSONObject joinInfo){
		JSONObject dataJSON = new JSONObject();
		JSONObject replydataJSON = new JSONObject();
		String userId = null;
		String userPassword = null;
		String userStatusMsg;
		
		dataJSON = (JSONObject) joinInfo.get("DATA");
		userId = (String) joinInfo.get("SENDER");
		
		
		userPassword = (String) dataJSON.get("USERPW");
		
		
		if(obj_DB.userLogin(userId, userPassword)){
			userStatusMsg=obj_DB.userStatusMsg(userId);
			replydataJSON.put("DATA", userStatusMsg);
			obj_ServerThread.putUserHash(userId);
			obj_ServerThread.makeJSONPacket("RES_USER_YLOGIN", replydataJSON, userId, null);
			
			return true;
		}else{
			obj_ServerThread.makeJSONPacket("RES_USER_NLOGIN", null, null, null);
			return false;
		}		
	}
}
