package Chat;

import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_roomList {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	djChatServer_reJoinTalkRoom obj_reJoinTalkRoom;
	public djChatServer_roomList(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	public void callObjTalkRoom(){
		this.obj_reJoinTalkRoom = obj_ServerThread.getObj_reJoinTalkRoom();
	}
	public void userRoomList(String userId){
		callObjTalkRoom();
		JSONObject dataJSON = new JSONObject();
		dataJSON = obj_DB.userJoinChatRoom(userId);
		
		obj_ServerThread.makeJSONPacket("RES_TALK_ROOMLIST", dataJSON, userId, null);
		//obj_reJoinTalkRoom.makeTalkRoom(dataJSON, userId);
		
	}
}
