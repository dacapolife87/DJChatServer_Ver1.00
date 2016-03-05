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
//	private static final String DELEMETER = " '"; //�Ҹ޽��� ������
//	private static final int RES_TALK_EXITROOM = 2032; //
//    private static final int RES_TALK_MAKEROOM = 2033; //Client�� ��ȭ���� ������ ��û�Ҷ� ������ Ŭ���̾�Ʈ�� ������ ��Ŷ
//    private static final int RES_TALKROOM_MSG = 2034; //��ȭ�� �������� ä�� msg
//    private static final int RES_TALKROOM_UPDATE = 2035; //��ȭ�泻�� ��ȭ�� ������Ʈ�ϴ� ��Ŷ
//    private static final int RES_TALK_RECENTMSG = 2036;
//    private static final int RES_TALK_ROOMUPDATE = 2037; //��ȭ�� ������ ������Ʈ��Ű��.
//    private static final int RES_TALK_INVITE = 2038; //���� ��ȭ�濡 �ʴ�� ����ڵ��� ������ ��Ŷ
//    
//	public djChatServer_chat(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
//		// TODO Auto-generated constructor stub
//		this.obj_DB = obj_DB;
//		this.obj_ServerThread = obj_ServerThread;
//	}
//		//ó�� ��ȭ���� �����ϴ� �Լ�
//		public void makeTalkRoom(String data){
//			// data = ��û�� ����� id�� üũ�� ����� id
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
//			//��ȭ�濡 ������ id�� dos�� ���ؼ� HashMap�� ����
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
//			//index = Integer.toString(indexCount); //indexCount�� String���� ����ȯ
//			obj_ServerThread.talkHash.put(index, talkroomHash); //��ȭ�� ������ HashMap�� ����
//			//indexCount++; //indexCount�� 1 ���� ��Ų��.
//			
//			successMakeRoom(data,index);
//			
//			
//		}
//		/*
//		 * successMakeRoom() �Լ� ����
//		 * RES_TALK_MAKEROOM ��Ŷ�� index ��ȣ�� ���� �������ν� client������ ��ȭ�� ������ �� �� �ְ��Ѵ�.
//		 * RES_TALK_MAKEROOM ��Ŷ�� ���� clinet�� �ڵ������� ��ȭ�濡 �ʴ�ǰ� djChatTalkRoom�� setVisible(true)�� â�� ����.
//		 */
//		public void successMakeRoom(String data, String index){
//			System.out.println("ServerThread successMakeRoom data : " +data);
//			StringTokenizer st = new StringTokenizer(data,DELEMETER);
//			String id = null;
//			
//			//data = ��ȭ�濡 ���� id��
//			while(st.hasMoreTokens()){
//				id = st.nextToken();
//				System.out.println("djChatServerThread successMakeRoom id : " +id);
//				obj_ServerThread.getTargetDos(id); //�ʴ��� ����ڿ� �ʴ�޴� ����ڿ��Ը� �޽����� ������.
////				obj_ServerThread.makePacket(RES_TALK_MAKEROOM, index, id, data); //index, My_id, JoinUserID			
//			}
//		}
//		
//		//��ȭ�� ������ ó�� �Լ�
//		public void exitTalkRoom(String data){
//			StringTokenizer st = new StringTokenizer(data, DELEMETER);
//			String index = st.nextToken();
//			String exit_UserId = st.nextToken();
//			HashMap<String,DataOutputStream> exitHash = new HashMap(); //id�� OutputStream
//			
//			exitHash = obj_ServerThread.talkHash.get(index);//index�� �ش��ϴ� ��ȭ�� ������ ���´�.
//			exitHash.remove(exit_UserId);
//			obj_DB.delete_exitChatRoom(exit_UserId, Integer.parseInt(index));
//			//������ DB �ڵ带 ���� ��
//			if(exitHash.size() != 0){ //���� ��ȭ�濡 ����� ���� ��쿡�� �ѷ��ش�.
//				StringBuffer sb = new StringBuffer();
//				sb.setLength(0);
//				sb.append(index);
//				sb.append(DELEMETER);
//				sb.append("[" + exit_UserId + "]" + "���� �����Ͽ����ϴ�.");
//				makeRoomMsg(sb.toString()); //��ȭ�濡 �����ִ� ����鿡�� ����޽��� �����ֱ�
//				
//				updateTalkRoom(index);
//			}
//		}
//		//��ȭ�泻���� ������ ��ư�� Ŭ������ �ÿ� �����ϴ� �Լ�
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
//				//������ DB�ڵ带 ���� ��
//			}
//			while(it.hasNext()){ //���ο� ��ȭ���� id���� StringBuffer�� �����Ѵ�.
//				sb.append((String)it.next());
//				sb.append(DELEMETER);
//			}
//			//��ȭ�泻�� ��Ŷ�� ���� index��ȣ�� ��ȭ�濡 �ѷ��ش�.
//			makePacket_TalkRoom(RES_TALKROOM_UPDATE, index, sb.toString());
//		}
//		//��ȭ�� ������ ����ϴ� ��Ŷ�� ����� �Լ�
//	    public void makePacket_TalkRoom(int res ,String index ,String data){
//	    	HashMap<String, DataOutputStream> findHash = obj_ServerThread.talkHash.get(index);
//	    	Iterator it = findHash.keySet().iterator();
//	    	
//	    	while(it.hasNext()){
//	    		obj_ServerThread.getTargetDos((String)it.next());
////	    		obj_ServerThread.makePacket(res, data);
//	    	}
//	    }
//	  //��ȭ�� ������ ä�� �޽����� ó���ϴ� �Լ�
//	    public void makeRoomMsg(String data){
//	    	StringTokenizer st = new StringTokenizer(data, DELEMETER);
//	    	String index = st.nextToken(); //index ��ȣ ����
//	    	
//	    	makePacket_TalkRoom(RES_TALKROOM_MSG, index, data);
//	    }
//	    
//	    //��ȭ�� ������ �߰��� �ʴ��ϴ� �Լ�
//	    public void inviteToTalk(String data){
//			StringTokenizer st = new StringTokenizer(data, DELEMETER);
//			//data = index 'id1 'id2 'id3 ' .....
//			String index = null;
//			String id = null;
//			index = st.nextToken();
//			String inviteUser = data.substring(index.length() + 2);
//			//inviteUser = id1 'id2 'id3 '..... �߰��� �ʴ�� ������
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
//			//��ȭ�� HashMap�� �����Ѵ�.
//			obj_ServerThread.talkHash.put(index, inviteHash);
//			
//			//���⿡�� index ��ȣ�� index��ȭ�濡 ����ִ� DB���� ����ڵ��� �����ͼ�
//			//�Ʒ��� �Լ��� ȣ���Ѵ�.
//			makeInviteUserRoom(data);
//		}
//	    
//	    //���� �߰��� ����ڵ鿡�� ä�ù��� ����ִ� �Լ�
//	    public void makeInviteUserRoom(String data){
//	    	StringTokenizer st = new StringTokenizer(data, DELEMETER);
//	    	//data = index '�߰��� �ʴ�� ����ڵ��� ID
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
//		 * �����Ͱ� ��Ų���� �����Ѹ޽������� DB���� ã�� �����ִ� �Լ�
//		 * �Լ�ȣ���� ����� ���������� �����ߴ� �ð������� �޽����� 
//		 * DB���� ã�� �޽��� ��Ŷ�� �����ش�
//		 * ������ ���ӽð��� ������ ���̵� �ʿ�
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
