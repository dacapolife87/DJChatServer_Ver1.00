package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class djChatDB_user {
	Statement stmt;
	public djChatDB_user(Statement stmt) {
		// TODO Auto-generated constructor stub
		this.stmt = stmt;
	}
	
	public boolean insert_UserJoin(String userId,String password,String name,String nickName,String phoneNum){
		String statusMsg = " ";
		String query = "INSERT INTO userInfo (userId,password,name,nickName,phoneNum,statusMsg) VALUES" +
				"('"
				+ userId+"','"
				+ password+"','"
				+ name+"','"
				+ nickName+"','"
				+ phoneNum+"','"
				+ statusMsg+"')";
		
		try {
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean existUserId(String userId){
		int count=0;
		String query = "SELECT count(*) FROM userInfo where userId = '"
				+userId+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			
			count = rs.getInt(1);
			if(count>0){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean existUserNick(String userNick){
		int count=0;
		String query = "SELECT count(*) FROM userInfo where nickName = '"
				+userNick+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("select user NickName ");
			rs.next();

			count = rs.getInt(1);
			if(count>0){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean userLogin(String userId,String userPassword){
		int result = 0;
		try {
			String sql = "select count(*) from  userInfo where userID = "
					+ "'"
					+ userId
					+ "'"
					+ "and password= '"
					+ userPassword + "'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			result = rs.getInt(1);
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}	
	}
	public String userStatusMsg(String userId){
		String statusMsg = null;
		try {
			String sql = "select statusMsg from  userInfo where userID = "
					+ "'"
					+ userId
					+ "'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			statusMsg = rs.getString(1);
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		return statusMsg;
	}
//	public void userLastReadMsgTime(String userId){
//		long time = System.currentTimeMillis();
//		Timestamp date = new Timestamp(time);
//		
//		String query = "update userInfo set lastread='"+date+"' where userId = '"
//				+userId+"'";
//		try {
//			stmt.executeUpdate(query);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public Timestamp lastReadMsgTime(String userId){
		Timestamp time = null;
		String query = "SELECT lastread FROM userInfo where userId = '"+userId+"'";
		try {
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			
			time = rs.getTimestamp(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
}
