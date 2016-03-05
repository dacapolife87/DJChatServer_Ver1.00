package Chat;

import java.io.DataOutputStream;
import java.util.HashMap;

import org.json.simple.JSONObject;

import DB.djChatServerDB;
import Server.djChatServerThread;

public class djChatServer_exitTalkRoom {
	djChatServerDB obj_DB;
	djChatServerThread obj_ServerThread;
	djChatServer_makePacket obj_makePacket;
	djChatServer_updateTalkRoom obj_updateTalkRoom;
	public djChatServer_exitTalkRoom(djChatServerDB obj_DB, djChatServerThread obj_ServerThread)
	{
		this.obj_DB = obj_DB;
		this.obj_ServerThread = obj_ServerThread;
	}
	
	public void callObj_makePacket(){
		this.obj_makePacket = obj_ServerThread.getObj_makePacket();
	}
	
	public void callObj_updateTalkRoom(){
		this.obj_updateTalkRoom = obj_ServerThread.getObj_updateTalk();
	}
	
	public void exitTalkRoom(JSONObject json){
		callObj_makePacket();
		callObj_updateTalkRoom();
		JSONObject jsonData = (JSONObject)json.get("DATA");
		String exit_UserId = (String)json.get("SENDER"); //������ ��û�� ���̵�
		String index = (String)jsonData.get("INDEX"); //������ ��û�� ���̵� ���� �ε��� ��ȣ
		
		HashMap<String, DataOutputStream> exitHash = obj_ServerThread.getter_TalkHash(index);
		exitHash.remove(exit_UserId);
		obj_ServerThread.setter_TalkHash(index, exitHash);
		obj_DB.delete_exitChatRoom(exit_UserId, Integer.parseInt(index));

		System.out.println("������ ��û�� ���� �ؽ��� : " +exitHash);
		if(exitHash.size() != 0){
			jsonData.put("MESSAGE", " ���� �����Ͽ����ϴ�.");
			JSONObject msg = new JSONObject();
			msg.put("SENDER", exit_UserId);
			msg.put("DATA", jsonData);
			obj_makePacket.makeRoomMsg(msg);
			obj_updateTalkRoom.updateTalkRoom(index);
		}
	}
}
