package Chat;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_inviteToTalk {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	djChatServer_updateTalkRoom obj_updateTalkRoom;
	
	public djChatServer_inviteToTalk(djChatServerDB obj_DB, djChatServerThread obj_ServerThread)
	{
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void callObj_updateTalkRoom(){
		this.obj_updateTalkRoom = obj_ServerThread.getObj_updateTalk();
	}
	
	public void inviteToTalk(JSONObject json){
		JSONObject jsonData = (JSONObject)json.get("DATA");
		JSONArray jsonArray = (JSONArray)json.get("RECEIVER");
		String index = (String)jsonData.get("INDEX");
		String id = null;
		DataOutputStream mDos;
		HashMap<String,DataOutputStream> inviteHash = obj_ServerThread.getter_TalkHash(index);
		for(int i=0; i<jsonArray.size(); i++){
			id = (String)jsonArray.get(i);
			mDos = obj_ServerThread.getter_Dos(id);
			inviteHash.put(id, mDos);
			obj_DB.insert_userJoinRoom(id, Integer.parseInt(index));
		}
		obj_ServerThread.setter_TalkHash(index, inviteHash);

		makeInviteUserRoom(jsonData, jsonArray);
	}
	
	public void makeInviteUserRoom(JSONObject jsonData, JSONArray jsonArray){
		callObj_updateTalkRoom();
    	String index = (String)jsonData.get("INDEX");
    	String My_Id = null;
    	//INDEX번호의 대화방의 모든 참여자들을 불러온다.
    	HashMap<String,DataOutputStream> joinUserHash = obj_ServerThread.getter_TalkHash(index);
    	Iterator it = joinUserHash.keySet().iterator();
    	JSONArray receiver = new JSONArray();
    	while(it.hasNext()){
    		receiver.add((String)it.next()); //INDEX번호 대화방의 모든 참여자들을 JSONARRAY로 저장한다.
    	}
    	
    	for(int i=0; i<jsonArray.size(); i++){
    		My_Id = (String)jsonArray.get(i);
    		obj_ServerThread.setter_Dos(My_Id);
    		obj_ServerThread.makeJSONPacket("RES_TALK_MAKEROOM", jsonData, My_Id, receiver);
    	}
    	obj_updateTalkRoom.updateTalkRoom(index);
    }
	
}
