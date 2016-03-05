package Chat;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_makeTalkRoom {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	
	public djChatServer_makeTalkRoom(djChatServerDB obj_DB, djChatServerThread obj_ServerThread)
	{
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void makeTalkRoom(JSONObject json){
		String My_Id = null;
		String joinUserId = null;
		String index = null;
		String chatType = "S";
		int chatRoomNum;
		
		JSONArray jsonReceiver = (JSONArray) json.get("RECEIVER");
		My_Id = (String)json.get("SENDER");
		
		obj_DB.insert_chatRoom();
		chatRoomNum = obj_DB.select_chatRoomNum();
		index = Integer.toString(chatRoomNum); //index번호를 DB에서 불러온다.
		
		DataOutputStream mDos;
		HashMap<String,DataOutputStream> talkroomHash = new HashMap();		

		jsonReceiver.add(My_Id); //받는 사람에 초대를 신청한 내 ID도 등록한다.

		//jsonReceiver에 저장된 초대목록 사람들을 HashMap에 저장한다.
		for(int i=0; i<jsonReceiver.size(); i++){
			joinUserId = (String)jsonReceiver.get(i);
			mDos = obj_ServerThread.getter_Dos(joinUserId);
			talkroomHash.put(joinUserId, mDos);
			obj_DB.insert_userJoinRoom(joinUserId, chatRoomNum);
		}
		
		if(talkroomHash.size()>2){
			chatType="M";
		}
		
		obj_DB.update_chatRoomInfo(chatRoomNum, talkroomHash.size(), chatType);
		obj_ServerThread.setter_TalkHash(index, talkroomHash);
		//받은 json내용에 여기에서 얻은 index번호를 JSONObject형태로 추가시켜준다.
		JSONObject jsonData = new JSONObject();
		jsonData.put("INDEX", index);
		
		//jsonReceiver에 저장된 사람들에게 모두 대화방을 만들라는 패킷을 보낸다.
		//joinUserId : 대화방에 참여할 내 아이디 , jsonReceiver : 대화방내에 있는 참여한 모든 사람의 아이디 jsonArray
		for(int i=0; i<jsonReceiver.size(); i++){
			joinUserId = (String)jsonReceiver.get(i);
			obj_ServerThread.setter_Dos(joinUserId);
			obj_ServerThread.makeJSONPacket("RES_TALK_MAKEROOM", jsonData, joinUserId, jsonReceiver);
		}
		
	}
}
