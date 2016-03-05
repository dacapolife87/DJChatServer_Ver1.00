package User;

import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_userJoin {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_userJoin(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void userJoin(JSONObject joinInfo){
		JSONObject dataJSON = new JSONObject();
		String userId = null;
		String userPassword = null;
		String userName = null;
		String userPhoneNum = null;
		String nickName=null;
		
		dataJSON = (JSONObject) joinInfo.get("DATA");
		
		userId = (String) dataJSON.get("USERID");
		userPassword = (String) dataJSON.get("USERPW");
		userName = (String) dataJSON.get("USERNAME");
		userPhoneNum = (String) dataJSON.get("USERPHONENUM");
		nickName = (String) dataJSON.get("USERNICKNAME");
	
		
		if(obj_DB.insert_UserJoin(userId,userPassword,userName,userPhoneNum,nickName)){
			obj_ServerThread.makeJSONPacket("RES_USER_YJOIN", null, null, null);
		}else{
			obj_ServerThread.makeJSONPacket("RES_USER_NJOIN", null, null, null);
		}		
	}
}
