package Friend;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_showFriend {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_showFriend(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
		// TODO Auto-generated constructor stub
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void showFriend(JSONObject friendInfo){
		String userId = null;
		JSONObject dataJSON;
		userId = (String) friendInfo.get("SENDER");
		
		dataJSON = obj_DB.userFriendList(userId);
		obj_ServerThread.makeJSONPacket("RES_FRIEND_LIST",dataJSON , userId,null);
	}
	
	public void showFriendList(String userId){
		JSONArray online = new JSONArray();
		Iterator it = obj_ServerThread.getHash().keySet().iterator();
		
		while(it.hasNext()){
			online.add((String)it.next());
			System.out.println("cycle");
		}
		System.out.println("array size : "+online.size());
		
		JSONObject tempJSON = new JSONObject();

		tempJSON.put("FRIENDLIST", online);
		
		obj_ServerThread.setter_Dos(userId);
		obj_ServerThread.makeJSONPacket("RES_FRIEND_LIST", tempJSON, userId, null);

		it = obj_ServerThread.getHash().keySet().iterator(); //getTargetDos를 다시 얻기위해 iterator를 초기화 시킨다.
		
	//접속해 있는 사람들에게 내 ID만 전송
		while(it.hasNext()){
			String checkID = (String)it.next();
			if(!(checkID.equals(userId))){
				obj_ServerThread.setter_Dos(checkID);
				JSONObject userIdJSON = new JSONObject();
//				userIdJSON = (JSONObject) userIdJSON.put("CONNECT", userId);
				userIdJSON.put("CONNECT", userId);
//				obj_ServerThread.makePacket(RES_FRIEND_CONNECT, userId);
				obj_ServerThread.makeJSONPacket("RES_FRIEND_CONNECT", userIdJSON, userId, null);
			}
		}
	}
}
