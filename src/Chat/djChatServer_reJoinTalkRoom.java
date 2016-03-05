package Chat;

import java.io.DataOutputStream;

import java.util.HashMap;


import org.json.simple.JSONArray;

import org.json.simple.JSONObject;


import DB.djChatServerDB;

import Server.djChatServerThread;


public class djChatServer_reJoinTalkRoom {
    djChatServerDB obj_DB;
    djChatServerThread obj_ServerThread;
    DataOutputStream mDos;
    
    public djChatServer_reJoinTalkRoom(djChatServerDB obj_DB, djChatServerThread obj_ServerThread){
        this.obj_DB = obj_DB;
        this.obj_ServerThread = obj_ServerThread;
    }
    public void makeTalkRoom(JSONObject json , String userId){

        String My_Id = userId;
        System.out.println("make TalkRoom : " +userId);
        //JSONObject jsonData = (JSONObject)json.get("ROOMINFO");
        JSONArray roomList = (JSONArray)json.get("ROOMINFO");
        mDos = obj_ServerThread.getter_Dos(My_Id);
        String index;
        int num;
        JSONObject roomInfo;
        JSONArray peopleArray;
        System.out.println("roomsize : "+roomList.size());
        for(int i=0; i<roomList.size(); i++){
            roomInfo = (JSONObject)roomList.get(i);
            num = (int)roomInfo.get("ROOMINDEX");
            index = Integer.toString(num);
            HashMap<String, DataOutputStream> talkroomHash = obj_ServerThread.getter_TalkHash(index);
            talkroomHash.put(My_Id, mDos);
            obj_ServerThread.setter_TalkHash(index, talkroomHash);
        }
    }    
}

