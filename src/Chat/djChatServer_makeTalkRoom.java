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
		index = Integer.toString(chatRoomNum); //index��ȣ�� DB���� �ҷ��´�.
		
		DataOutputStream mDos;
		HashMap<String,DataOutputStream> talkroomHash = new HashMap();		

		jsonReceiver.add(My_Id); //�޴� ����� �ʴ븦 ��û�� �� ID�� ����Ѵ�.

		//jsonReceiver�� ����� �ʴ��� ������� HashMap�� �����Ѵ�.
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
		//���� json���뿡 ���⿡�� ���� index��ȣ�� JSONObject���·� �߰������ش�.
		JSONObject jsonData = new JSONObject();
		jsonData.put("INDEX", index);
		
		//jsonReceiver�� ����� ����鿡�� ��� ��ȭ���� ������ ��Ŷ�� ������.
		//joinUserId : ��ȭ�濡 ������ �� ���̵� , jsonReceiver : ��ȭ�泻�� �ִ� ������ ��� ����� ���̵� jsonArray
		for(int i=0; i<jsonReceiver.size(); i++){
			joinUserId = (String)jsonReceiver.get(i);
			obj_ServerThread.setter_Dos(joinUserId);
			obj_ServerThread.makeJSONPacket("RES_TALK_MAKEROOM", jsonData, joinUserId, jsonReceiver);
		}
		
	}
}
