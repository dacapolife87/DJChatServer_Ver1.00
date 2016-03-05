package Chat;

import java.io.DataOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_makePacket {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_makePacket(djChatServerDB obj_DB, djChatServerThread obj_ServerThread)
	{
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void makeRoomMsg(JSONObject json){
		JSONObject jsonData = (JSONObject)json.get("DATA");
		String index = (String)jsonData.get("INDEX");
		String msg = (String)jsonData.get("MESSAGE");
		String My_Id = (String)json.get("SENDER");
		System.out.println("json msg : "+json);
		System.out.println(" msg : "+msg);
		long time = System.currentTimeMillis();
		Timestamp date = new Timestamp(time);
		obj_DB.insert_chatMsgInfo(msg,Integer.parseInt(index), My_Id,date);
		
		HashMap<String,DataOutputStream> findHash = obj_ServerThread.getter_TalkHash(index);
		Iterator it = findHash.keySet().iterator();
		
		JSONArray jsonReceiver = new JSONArray();
		while(it.hasNext()){
			jsonReceiver.add((String)it.next());
		}
		
		makePacket_TalkRoom("RES_TALKROOM_MSG", jsonData, My_Id, jsonReceiver);
	}
	
	public void makePacket_TalkRoom(String separator, JSONObject jsonData, String sender, JSONArray receiver){
		String index = (String)jsonData.get("INDEX");
		
		for(int i =0; i<receiver.size(); i++){
			if(obj_ServerThread.setter_Dos((String)receiver.get(i))){
				obj_ServerThread.makeJSONPacket(separator, jsonData, sender, receiver);
			}
		}
	}
	
}
