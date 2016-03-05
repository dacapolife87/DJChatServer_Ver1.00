package Chat;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_updateTalkRoom {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	djChatServer_makePacket obj_makePacket;
	
	public djChatServer_updateTalkRoom(djChatServerDB obj_DB, djChatServerThread obj_ServerThread)
	{
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	//	callObj_makePacket();
	}
	
	public void callObj_makePacket(){
		this.obj_makePacket = obj_ServerThread.getObj_makePacket();
	}
	
	public void updateTalkRoom(String index){
		callObj_makePacket();
		System.out.println("나가기 요청시 updateTalkRoom index : " +index);
		HashMap<String, DataOutputStream> updateHash = obj_ServerThread.getter_TalkHash(index);
		JSONObject jsonData = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonData.put("INDEX", index);
		if(updateHash.size() == 0){
			obj_ServerThread.removeTalkHash(index);
		}
		else
		{
			Iterator it = updateHash.keySet().iterator();
			while(it.hasNext()){
				jsonArray.add((String)it.next());
			}
		}
		obj_makePacket.makePacket_TalkRoom("RES_TALKROOM_UPDATE", jsonData, null, jsonArray);
		
	}
	
}
