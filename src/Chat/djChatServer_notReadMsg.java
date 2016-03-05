package Chat;

import java.sql.Timestamp;

import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_notReadMsg {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	public djChatServer_notReadMsg(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	public void recentMsg(String userId){
		JSONObject data = new JSONObject();
		obj_DB.select_chatRoomRecentMsg(userId);
//		data = obj_DB.select_chatRoomRecentMsg(userId);
//		System.out.println("server check :"+data);
//		if(data!=null){
//			obj_ServerThread.makeJSONPacket("RES_TALK_NOTREADMSG", data, userId, null);
//
//		}
	}
}