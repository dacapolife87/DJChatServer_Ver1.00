//package Server;
//
//import java.util.Iterator;
//import java.util.StringTokenizer;
//
//import DB.djChatServerDB;
//
//public class djChatServer_user {
//	djChatServerDB obj_DB;
//	djChatServerThread obj_ServerThread;
//	private static final String DELEMETER = " '"; //�Ҹ޽��� ������
//	
//	private static final int RES_USER_YLOGIN = 2011;
//    private static final int RES_USER_NLOGIN = 2012;
//    private static final int RES_USER_DISCONNECT = 2013;
//    private static final int RES_FRIEND_LIST = 2021;
//    private static final int RES_FRIEND_RECOMMANDLIST = 2029; // ��õģ�� ���
//    private static final int RES_FRIEND_CONNECT = 2028;
//    private static final int RES_USER_YJOIN = 2014;
//    private static final int RES_USER_IDEXIST = 2015;
//    private static final int RES_USER_IDNOTEXIST = 2016;
//    private static final int RES_USER_NICKEXIST = 2017;
//    private static final int RES_USER_NICKNOTEXIST = 2018;
//    
//	public djChatServer_user(djChatServerDB obj_DB, djChatServerThread obj_ServerThread) {
//		// TODO Auto-generated constructor stub
//		this.obj_DB = obj_DB;
//		this.obj_ServerThread = obj_ServerThread;
//	}
//	//////////////////////////////////////////////////
////	public void logIn(String userInfo){
////	//���������� �ƴҋ�
////	
////		StringTokenizer st = new StringTokenizer(userInfo,DELEMETER);
////		String userId=null;
////		String userPassword=null;
////		if(st.hasMoreTokens()){
////			userId = st.nextToken();
////		}
////		if(st.hasMoreTokens()){
////			userPassword = st.nextToken();
////		}
////		
////		if(userId==null){
////			obj_ServerThread.makePacket(RES_USER_NLOGIN, null);
////		}else if(userPassword==null){
////			obj_ServerThread.makePacket(RES_USER_NLOGIN, null);
////		}else{
////			int result;
////			result = obj_DB.userLogin(userId,userPassword);
////			if(result ==1){
////				obj_ServerThread.putUserHash(userId);
////				obj_ServerThread.makePacket(RES_USER_YLOGIN, null);
////				showFriendList(userId);
////				showRecommandFriend(userId);
////			}else{
////				obj_ServerThread.makePacket(RES_USER_NLOGIN, null);
////			}
////		}
////	}
//	public void showRecommandFriend(String userId){
//		System.out.println("�α����� ��õģ���Լ� ȣ��");
//		obj_DB.select_showRecommandFriendList(userId);
//	}
//	// ģ����õ����Ʈ
////	public void recommandFriendList(String data,String userId){
////		System.out.println("recommandFriend data : "+data);
////		obj_ServerThread.getTargetDos(userId);
////		obj_ServerThread.makePacket(RES_FRIEND_RECOMMANDLIST, data);
////	}
//	public void showFriendList(String userId){
//		Iterator it = obj_ServerThread.client.keySet().iterator();
//		StringBuffer sb = new StringBuffer();
//		sb.setLength(0);
//	
//		while(it.hasNext()){
//			sb.append((String)it.next());
//			sb.append(DELEMETER);
//		}
//		
//		String msg = sb.toString();
//		msg = msg.substring(0, msg.length()-1); //������ " '" �� �����Ѵ�.
//	
//		//ó���� ���������� HashMap�� ����Ǿ��ִ� ��� ID�� �����׸� ������.
//		obj_ServerThread.getTargetDos(userId);
////		obj_ServerThread.makePacket(RES_FRIEND_LIST, msg);
//		
//		it = obj_ServerThread.client.keySet().iterator(); //getTargetDos�� �ٽ� ������� iterator�� �ʱ�ȭ ��Ų��.
//		
//	//������ �ִ� ����鿡�� �� ID�� ����
//		while(it.hasNext()){
//			String checkID = (String)it.next();
//			if(!(checkID.equals(userId))){
//				obj_ServerThread.getTargetDos(checkID);
////				obj_ServerThread.makePacket(RES_FRIEND_CONNECT, userId);
//			}
//		}
//	}
//	public void logOut(){}
////	public void userJoin(String joinInfo){
////
////		StringTokenizer st = new StringTokenizer(joinInfo,DELEMETER);
////		String userId;
////		String userPassword;
////		String userName;
////		String userPhoneNum;
////		String nickName=null;
////		userId = st.nextToken();
////		userPassword = st.nextToken();
////		userName = st.nextToken();
////		userPhoneNum = st.nextToken();
////		
////		// DB����
////		obj_DB.insert_UserJoin(userId,userPassword,userName,nickName,userPhoneNum);
////		// resMsg
////		obj_ServerThread.makePacket(RES_USER_YJOIN, null);
////	}
////	public void existUserIdCheck(String userJoinId){
////		// DB�˻�  // �˻������ ������ T->�������������� �˸�
////		
////		int result;
////		System.out.println("user Join Id Exist check : "+userJoinId);
////		result = obj_DB.select_UserId(userJoinId); 
////		if(result==0){
////			System.out.println("not exist");
////			obj_ServerThread.makePacket(RES_USER_IDNOTEXIST, null);
////		}else if(result == -1){
////		// ����
////		}else{
////			System.out.println("exist");
////			obj_ServerThread.makePacket(RES_USER_IDEXIST, null);
////		}
////	}
//	
////	public void existUserNickCheck(String userJoinNick){
////		// DB�˻�  // �˻������ ������ T->�������������� �˸�
////		
////		int result;
////		System.out.println("user Join Id Exist check : "+userJoinNick);
////		result = obj_DB.select_UserNick(userJoinNick); 
////		if(result==0){
////			System.out.println("not exist");
////			obj_ServerThread.makePacket(RES_USER_NICKNOTEXIST, null);
////		}else if(result == -1){
////		// ����
////		}else{
////			System.out.println("exist");
////			obj_ServerThread.makePacket(RES_USER_NICKEXIST, null);
////		}
////	}
//}
