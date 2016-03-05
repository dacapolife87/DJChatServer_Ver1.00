//package Server;
//
//import java.io.DataOutputStream;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.StringTokenizer;
//
//import DB.djChatServerDB;
//
//public class djChatServer_chat {
//	djChatServerThread obj_ServerThread;
//	djChatServerDB obj_DB;
//	
//	private static final String DELEMETER = " '"; //소메시지 구분자
//	private static final int RES_TALK_EXITROOM = 2032; //
//    private static final int RES_TALK_MAKEROOM = 2033; //Client가 대화방을 만들라는 요청할때 서버가 클라이언트에 보내는 패킷
//    private static final int RES_TALKROOM_MSG = 2034; //대화방 내에서의 채팅 msg
//    private static final int RES_TALKROOM_UPDATE = 2035; //대화방내에 변화를 업데이트하는 패킷
//    private static final int RES_TALK_RECENTMSG = 2036;
//    private static final int RES_TALK_ROOMUPDATE = 2037; //대화방 내용을 업데이트시키다.
//    private static final int RES_TALK_INVITE = 2038; //기존 대화방에 초대된 사용자들이 보내는 패킷
//    
//	public djChatServer_chat(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
//		// TODO Auto-generated constructor stub
//		this.obj_DB = obj_DB;
//		this.obj_ServerThread = obj_ServerThread;
//	}
//		//처음 대화방을 개설하는 함수
//		public void makeTalkRoom(String data){
//			// data = 요청한 사람의 id와 체크한 사람의 id
//			System.out.println("djChatServerThread makeTalkRoom() data : " +data);
//			StringTokenizer st = new StringTokenizer(data , DELEMETER);
//			String id = null;
//			String index = null;
//			String chatType = "S";
//			int chatRoomNum;
//			
//			
//			obj_DB.insert_chatRoom();
//			chatRoomNum = obj_DB.select_chatRoomNum();
//			index = Integer.toString(chatRoomNum);
//			
//			
//			DataOutputStream mDos;
//			HashMap<String,DataOutputStream> talkroomHash = new HashMap();
//			
//			//대화방에 참여할 id와 dos를 구해서 HashMap에 저장
//			while(st.hasMoreTokens()){
//				id = st.nextToken();
//				mDos = obj_ServerThread.client.get(id);
//				talkroomHash.put(id, mDos);
//				obj_DB.insert_userJoinRoom(id, chatRoomNum);
//				// 
//			}
//			if(talkroomHash.size()>2){
//				chatType="M";
//			}
//			
//			obj_DB.update_chatRoomInfo(chatRoomNum, talkroomHash.size(), chatType);
//			//index = Integer.toString(indexCount); //indexCount를 String으로 형변환
//			obj_ServerThread.talkHash.put(index, talkroomHash); //대화방 정보를 HashMap에 저장
//			//indexCount++; //indexCount를 1 증가 시킨다.
//			
//			successMakeRoom(data,index);
//			
//			
//		}
//		/*
//		 * successMakeRoom() 함수 시작
//		 * RES_TALK_MAKEROOM 패킷에 index 번호도 같이 보냄으로써 client측에서 대화방 구별을 할 수 있게한다.
//		 * RES_TALK_MAKEROOM 패킷을 받은 clinet는 자동적으로 대화방에 초대되고 djChatTalkRoom의 setVisible(true)로 창을 띄운다.
//		 */
//		public void successMakeRoom(String data, String index){
//			System.out.println("ServerThread successMakeRoom data : " +data);
//			StringTokenizer st = new StringTokenizer(data,DELEMETER);
//			String id = null;
//			
//			//data = 대화방에 들어가는 id들
//			while(st.hasMoreTokens()){
//				id = st.nextToken();
//				System.out.println("djChatServerThread successMakeRoom id : " +id);
//				obj_ServerThread.getTargetDos(id); //초대한 사용자와 초대받는 사용자에게만 메시지를 보낸다.
////				obj_ServerThread.makePacket(RES_TALK_MAKEROOM, index, id, data); //index, My_id, JoinUserID			
//			}
//		}
//		
//		//대화방 나가기 처리 함수
//		public void exitTalkRoom(String data){
//			StringTokenizer st = new StringTokenizer(data, DELEMETER);
//			String index = st.nextToken();
//			String exit_UserId = st.nextToken();
//			HashMap<String,DataOutputStream> exitHash = new HashMap(); //id와 OutputStream
//			
//			exitHash = obj_ServerThread.talkHash.get(index);//index에 해당하는 대화방 정보를 얻어온다.
//			exitHash.remove(exit_UserId);
//			obj_DB.delete_exitChatRoom(exit_UserId, Integer.parseInt(index));
//			//나가기 DB 코드를 넣을 곳
//			if(exitHash.size() != 0){ //나간 대화방에 사람이 있을 경우에만 뿌려준다.
//				StringBuffer sb = new StringBuffer();
//				sb.setLength(0);
//				sb.append(index);
//				sb.append(DELEMETER);
//				sb.append("[" + exit_UserId + "]" + "님이 퇴장하였습니다.");
//				makeRoomMsg(sb.toString()); //대화방에 남아있는 사람들에게 퇴장메시지 보여주기
//				
//				updateTalkRoom(index);
//			}
//		}
//		//대화방내에서 나가기 버튼을 클릭했을 시에 제거하는 함수
//		public void updateTalkRoom(String index){
//			HashMap<String, DataOutputStream> updateHash = new HashMap();
//			updateHash = obj_ServerThread.talkHash.get(index);
//			
//			StringBuffer sb = new StringBuffer();
//			sb.setLength(0);
//			sb.append(index);
//			sb.append(DELEMETER);
//			Iterator it = updateHash.keySet().iterator();
//			if(updateHash.size() == 0){
//				obj_ServerThread.talkHash.remove(index);
//				//나가기 DB코드를 넣을 곳
//			}
//			while(it.hasNext()){ //새로운 대화방의 id들을 StringBuffer에 저장한다.
//				sb.append((String)it.next());
//				sb.append(DELEMETER);
//			}
//			//대화방내의 패킷을 만들어서 index번호의 대화방에 뿌려준다.
//			makePacket_TalkRoom(RES_TALKROOM_UPDATE, index, sb.toString());
//		}
//		//대화방 내에서 사용하는 패킷을 만드는 함수
//	    public void makePacket_TalkRoom(int res ,String index ,String data){
//	    	HashMap<String, DataOutputStream> findHash = obj_ServerThread.talkHash.get(index);
//	    	Iterator it = findHash.keySet().iterator();
//	    	
//	    	while(it.hasNext()){
//	    		obj_ServerThread.getTargetDos((String)it.next());
////	    		obj_ServerThread.makePacket(res, data);
//	    	}
//	    }
//	  //대화방 내에서 채팅 메시지를 처리하는 함수
//	    public void makeRoomMsg(String data){
//	    	StringTokenizer st = new StringTokenizer(data, DELEMETER);
//	    	String index = st.nextToken(); //index 번호 추출
//	    	
//	    	makePacket_TalkRoom(RES_TALKROOM_MSG, index, data);
//	    }
//	    
//	    //대화방 내에서 추가로 초대하는 함수
//	    public void inviteToTalk(String data){
//			StringTokenizer st = new StringTokenizer(data, DELEMETER);
//			//data = index 'id1 'id2 'id3 ' .....
//			String index = null;
//			String id = null;
//			index = st.nextToken();
//			String inviteUser = data.substring(index.length() + 2);
//			//inviteUser = id1 'id2 'id3 '..... 추가로 초대된 유저들
//			DataOutputStream mDos;
//			HashMap<String,DataOutputStream> inviteHash = new HashMap();
//			inviteHash = obj_ServerThread.talkHash.get(index);
//	/*		
//			Iterator it = inviteHash.keySet().iterator();
//			while(it.hasNext()){
//				getTargetDos((String)it.next());
//				makePacket(RES_TALK_ROOMUPDATE, inviteUser);
//			}
//		*/	
//			while(st.hasMoreTokens()){
//				id = st.nextToken();
//				mDos = obj_ServerThread.client.get(id);
//				inviteHash.put(id, mDos);
//				obj_DB.insert_userJoinRoom(id, Integer.parseInt(index));
//			}
//			//대화방 HashMap에 저장한다.
//			obj_ServerThread.talkHash.put(index, inviteHash);
//			
//			//여기에서 index 번호와 index대화방에 들어있는 DB에서 사용자들을 꺼내와서
//			//아래의 함수를 호출한다.
//			makeInviteUserRoom(data);
//		}
//	    
//	    //새로 추가된 사용자들에게 채팅방을 띄어주는 함수
//	    public void makeInviteUserRoom(String data){
//	    	StringTokenizer st = new StringTokenizer(data, DELEMETER);
//	    	//data = index '추가로 초대된 사용자들의 ID
//	    	String index = st.nextToken();
//	    	String joinUsers = data.substring(index.length() + 2);
//	    	String id = null;
//	    	
//	    	while(st.hasMoreTokens()){
//	    		id = st.nextToken();
//	    		obj_ServerThread.getTargetDos(id);
////	    		obj_ServerThread.makePacket(RES_TALK_MAKEROOM, index, id, joinUsers);
//	    	}
//	    	
//	    }
//	    
//	    /*
//		 * 데이터가 끊킨사이 도착한메시지들을 DB에서 찾아 보내주는 함수
//		 * 함수호출이 생기면 마지막으로 접속했던 시간이후의 메시지를 
//		 * DB에서 찾아 메시지 패킷을 보내준다
//		 * 마지막 접속시간과 접속자 아이디 필요
//		 * */
//		public void notyetReadMsg(String data){
//			long time1 = System.currentTimeMillis();
//			
//			Timestamp time;
//			
//			time = new Timestamp(time1);
//			
//			StringTokenizer st = new StringTokenizer(data,DELEMETER);
//			String str_time;
//			String userId = null;
//			str_time = st.nextToken();
//			
//			int lastReadTime = 0;
//			obj_DB.select_chatRecentReadMsg(userId,time);
//		}
//		public void sendRecentMsg(String msg,int chatRoomNum,String userId,int readUserCount,Timestamp sendTime){
//			StringBuffer sb = new StringBuffer();
//			sb.setLength(0);
//			sb.append(msg);
//			sb.append(DELEMETER);
//			sb.append(chatRoomNum);
//			sb.append(DELEMETER);
//			sb.append(userId);
//			sb.append(DELEMETER);
//			sb.append(readUserCount);
//			sb.append(DELEMETER);
//			sb.append(sendTime);
////			obj_ServerThread.makePacket(RES_TALK_RECENTMSG,sb.toString());
//		}
//		public void dbMsg(String data){
//			long time = System.currentTimeMillis();
//			System.out.println("msgTime : "+new Date(time));
//
//			StringTokenizer st = new StringTokenizer(data,DELEMETER);
//			String chatRoomId;
//			int chatRoomNum;
//			String userId=null;
//			String msg = null;
//			
//			chatRoomId = st.nextToken();
//			userId = st.nextToken();
//			chatRoomNum = Integer.parseInt(chatRoomId);
//			msg = data.substring(chatRoomId.length()+userId.length()+4);
//			
//			obj_DB.insert_chatMsgInfo(msg,chatRoomNum, userId, new java.sql.Timestamp(time));
//		}
//	    
//}
