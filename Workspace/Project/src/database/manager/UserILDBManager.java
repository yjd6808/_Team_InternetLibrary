//작성자 : 윤정도

package database.manager;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import bean.UserBean;
import database.InternetLibraryDatabase;
import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import util.HanConv;

public class UserILDBManager extends ILDBManager {
	private static UserILDBManager instance = new UserILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private UserILDBManager() {
		// EMPTY
	}

	public static UserILDBManager getInstance() {
		return instance;
	}

	// ID 존재 유무 확인
	// 1 : 이미 존재함
	// 0 : 존재하지 않음
	public DBResult isExistAccount(String id) {
		int status = DBResult.TRUE;
		String sql = "select id from t_user where id = ?";
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			
			if (!rs.next()) {
				 status = 0;
			}
			
			return new DBResult(status);
		} catch (Exception e) {
			return new DBResultError("계정 유무 확인 도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	
	public DBResult register(String id, String pw, String name, String email) {
		DBResult existAccountDbResult = isExistAccount(id);
		if (existAccountDbResult.getStatus() == DBResult.ERROR) {
			return existAccountDbResult;
		} else {
			if (existAccountDbResult.getStatus() == DBResult.TRUE ) {
				return new DBResult(0, "이미 입력하신 계정이 존재합니다.");
			}
		}
		
		Connection connection = null;
		PreparedStatement psmt = null;
		
		String sql = "insert into t_user (U_ID, ID, PASS, NICK, EMAIL, REGISTRATION_DATE) values (t_user_uid_seq.nextval, ?, ?, ?, ?, ?)";
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.setString(3, HanConv.toKor(name));
			psmt.setString(4, email);
			psmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			
			int status = psmt.executeUpdate();
			
			if (status == 0) {
				status = -1;
			}
			
			return new DBResult(status);
		} catch (Exception e) {
			return new DBResultError("회원가입  중 오류가 발생하였습니다.", e);		
		} finally {
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	public DBResult login(String id, String pw) {
		String sql = "select * from t_user where id = ? and pass = ?";
		
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			
			rs = psmt.executeQuery();
			int status = DBResult.SUCCESS;
			UserBean userBean = null;
			
			if (rs.next()) {
				userBean = new UserBean();
				userBean.setU_id(rs.getInt(1));
				userBean.setId(rs.getString(2));
				userBean.setPw(rs.getString(3));
				userBean.setName(rs.getString(4));
				userBean.setEmail(rs.getString(5));
				userBean.setRegistrationDate(new Date(rs.getTimestamp(6).getTime()));
				userBean.setPoint(rs.getInt(7));
				userBean.setAdmin(rs.getInt(8) != 0 ? true : false);
			} else {
				status = DBResult.FAIL;
			}
			
			return new DBResultWithData<UserBean>(status, userBean);
		} catch (Exception e) {
			return new DBResultError("로그인 시도 중 오류가 발생하였습니다.", e);		
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	public DBResult updateUser(UserBean user) {
		String sql = "select * from t_user where u_id = ?";
		
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setInt(1, user.getU_id());
			
			rs = psmt.executeQuery();
			int status = DBResult.SUCCESS;
			
			if (rs.next()) {
				user.setU_id(rs.getInt(1));
				user.setId(rs.getString(2));
				user.setPw(rs.getString(3));
				user.setName(rs.getString(4));
				user.setEmail(rs.getString(5));
				user.setRegistrationDate(new Date(rs.getTimestamp(6).getTime()));
				user.setPoint(rs.getInt(7));
				user.setAdmin(rs.getInt(8) != 0 ? true : false);
			} else {
				status = DBResult.FAIL;
			}
			
			return new DBResult(status);
		} catch (Exception e) {
			return new DBResultError("유저 정보 업데이트 중 오류가 발생하였습니다.", e);		
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	public DBResult getUser(int user_uid) {
		String sql = "select * from t_user where u_id = ?";
		
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		UserBean userBean = new UserBean();
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setInt(1, user_uid);
			
			rs = psmt.executeQuery();
			int status = DBResult.SUCCESS;
			String message = "유저 정보를 성공적으로 획득하였습니다.";
			
			if (rs.next()) {
				userBean.setU_id(rs.getInt(1));
				userBean.setId(rs.getString(2));
				userBean.setPw(rs.getString(3));
				userBean.setName(rs.getString(4));
				userBean.setEmail(rs.getString(5));
				userBean.setRegistrationDate(new Date(rs.getTimestamp(6).getTime()));
				userBean.setPoint(rs.getInt(7));
				userBean.setAdmin(rs.getInt(8) != 0 ? true : false);
			} else {
				status = DBResult.FAIL;
				message = "유저 정보를 얻지 못했습니다.";
			}
			
			return new DBResultWithData<UserBean>(status, message, userBean);
		} catch (Exception e) {
			return new DBResultError("유저 정보 획득 중 오류가 발생하였습니다.", e);		
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	
	public DBResult chargePoint(int user_uid, int price, int chargePoint) {
		Connection conn = null;
		PreparedStatement updateUserPointPtmt = null;
		PreparedStatement insertChargePointLogPtmt = null;
		
		String updateUserPointSql = "update t_user set point = point + ? where u_id = ?";
		String insertChargePointLog = "insert into t_charge_point_log values (?, ?, ?, ?, t_charge_point_log_seq.nextval)";
		
		int status = DBResult.SUCCESS;
		String message = "포인트 충전에 실패하였습니다.";
		
		try {
			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			updateUserPointPtmt = conn.prepareStatement(updateUserPointSql);
			insertChargePointLogPtmt = conn.prepareStatement(insertChargePointLog);
			
			updateUserPointPtmt.setInt(1, price);
			updateUserPointPtmt.setInt(2, user_uid);
			
			if (updateUserPointPtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "유저 포인트 수정에 실패하였습니다.";
			}
			
			if (status == DBResult.SUCCESS) {
				insertChargePointLogPtmt.setInt(1, user_uid);
				insertChargePointLogPtmt.setInt(2, price);
				insertChargePointLogPtmt.setInt(3, chargePoint);
				insertChargePointLogPtmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				
				if (insertChargePointLogPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "로그 저장에 실패하였습니다.";
				}
			}
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
				message = "포인트 충전에 성공하였습니다.";				
			} 
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("포인트 충전 중 오류가 발생하였습니다.", e);
		} finally {
			
			RollbackConnection(conn);
			CloseStatement(updateUserPointPtmt);
			CloseStatement(insertChargePointLogPtmt);
			CloseConnection(conn);
		}
	}
	
	// 비밀번호 일치여부 확인
	public DBResult checkPassword(int user_uid, String password) {
		String sql = "select * from t_user where u_id = ? and pass = ?";
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setInt(1, user_uid);
			psmt.setString(2, password);
			rs = psmt.executeQuery();
			
			
			int status = DBResult.FALSE;
			String message = "비밀번호가 일치하지 않습니다.";
			
			if (rs.next()) {
				status = DBResult.TRUE;
				message = "비밀번호가 일치합니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("비밀번호 확인 도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	// 유저 정보 변경
	public DBResult modifyInfo(int user_uid, UserBean userBean) {
		Connection connection = null;
		PreparedStatement psmt = null;
		
		try {
			
			String sql = "update t_user" + 
						 "   set email = ?" + 
						 "     , nick = ?" + 
						 "     , pass = ?" + 
 						 " where u_id = ?";
			
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setString(1, userBean.getEmail());
			psmt.setString(2, userBean.getName());
			psmt.setString(3, userBean.getPw());
			psmt.setInt(4, user_uid);
			
			if (psmt.executeUpdate() == 0) {
				return new DBResult(DBResult.FAIL, "회원 정보 변경에 실패하였습니다.");
			}
			
			return new DBResult(DBResult.SUCCESS, "회원 정보를 성공적으로 변경하였습니다.");
		} catch (Exception e) {
			return new DBResultError("회원 정보 변경 도중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
	
	public DBResult deleteAccount(int user_uid) {
		
		Connection connection = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "delete t_user where u_id = ?";
			
			connection = database.getConnection();
			psmt = connection.prepareStatement(sql);
			psmt.setInt(1, user_uid);
			
			
			int status = DBResult.SUCCESS;
			String message = "성공적으로 탈퇴되었습니다.";
			
			if (psmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "회원 탈퇴를 실패하였습니다.";
			}
			
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("회원탈퇴 도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(psmt);
			CloseConnection(connection);
		}
	}
}
