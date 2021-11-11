//작성자 : 윤정도

package database.manager;

import bean.ChargePointLogBean;
import bean.UsePointLogBean;
import database.InternetLibraryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import structure.Tuple;

public class LogILDBManager extends ILDBManager {
	private static LogILDBManager instance = new LogILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private LogILDBManager() {
		// EMPTY
	}

	public static LogILDBManager getInstance() {
		return instance;
	}

	
	// 마이페이지 - 포인트 사용 로그목록
	public DBResult listUsePointLogs(int user_uid, int pageNumber, PageOption pageOption) {
		ArrayList<UsePointLogBean> logs = new ArrayList<UsePointLogBean>();

		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		int totalLogCount = 0;
		int totalPageCount = 0;

		try {
			String sql = "select count(*) from t_use_point_log where user_uid = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			if (rs.next()) {
				totalLogCount = rs.getInt(1);
			}

			totalPageCount = totalLogCount % pageOption.getPageSize() == 0
					? totalLogCount / pageOption.getPageSize()
					: totalLogCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("포인트 사용 로그 갯수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select *" + 
						 "  from t_use_point_log " + 
						 " where user_uid = ?" + 
						 " order by u_id desc";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}

			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;

			if (rs.next()) {
				rs.absolute(startIdx);
				// 날짜 포맷 참고 : https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
				
				for (int i = 0; i < pageOption.getPageSize(); i++) {
					UsePointLogBean usePointLogBean = new UsePointLogBean();
					
					usePointLogBean.setU_id(rs.getInt("U_ID"));
					usePointLogBean.setUser_uid(rs.getInt("USER_UID"));
					usePointLogBean.setUsePoint(rs.getInt("USE_POINT"));
					usePointLogBean.setContent(rs.getString("USE_CONTENT"));
					usePointLogBean.setUseDate(rs.getTimestamp("USE_DATE"));
					logs.add(usePointLogBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<UsePointLogBean> page = new Page<UsePointLogBean>(totalPageCount, pageNumber, pageOption, logs);
			
			Tuple<Integer, Page<UsePointLogBean>> data = new Tuple<Integer, Page<UsePointLogBean>>();
			
			data.setItem1(totalLogCount);
			data.setItem2(page);
			
			return new DBResultWithData<Tuple<Integer, Page<UsePointLogBean>>>(DBResult.SUCCESS, "포인트 사용 로그 목록 가져오기 성공!", data);
		} catch (Exception e) {
			return new DBResultError("포인트 사용 로그 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	// 마이페이지 - 포인트 사용 로그목록
	public DBResult listChargePointLogs(int user_uid, int pageNumber, PageOption pageOption) {
		ArrayList<ChargePointLogBean> logs = new ArrayList<ChargePointLogBean>();

		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		int totalLogCount = 0;
		int totalPageCount = 0;

		try {
			String sql = "select count(*) from t_charge_point_log where user_uid = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			if (rs.next()) {
				totalLogCount = rs.getInt(1);
			}

			totalPageCount = totalLogCount % pageOption.getPageSize() == 0
					? totalLogCount / pageOption.getPageSize()
					: totalLogCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("포인트 충전 로그 갯수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select *" + 
						 "  from t_charge_point_log " + 
						 " where user_uid = ?" + 
						 " order by u_id desc";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}

			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;

			if (rs.next()) {
				rs.absolute(startIdx);
				// 날짜 포맷 참고 : https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
				
				for (int i = 0; i < pageOption.getPageSize(); i++) {
					ChargePointLogBean chargePointLogBean = new ChargePointLogBean();
					
					chargePointLogBean.setU_id(rs.getInt("U_ID"));
					chargePointLogBean.setUser_uid(rs.getInt("USER_UID"));
					chargePointLogBean.setPrice(rs.getInt("PRICE"));
					chargePointLogBean.setChargePoint(rs.getInt("CHARGE_POINT"));
					chargePointLogBean.setChargeDate(rs.getTimestamp("CHARGE_DATE"));
					logs.add(chargePointLogBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<ChargePointLogBean> page = new Page<ChargePointLogBean>(totalPageCount, pageNumber, pageOption, logs);
			
			Tuple<Integer, Page<ChargePointLogBean>> data = new Tuple<Integer, Page<ChargePointLogBean>>();
			
			data.setItem1(totalLogCount);
			data.setItem2(page);
			
			return new DBResultWithData<Tuple<Integer, Page<ChargePointLogBean>>>(DBResult.SUCCESS, "포인트 충전 로그 목록 가져오기 성공!", data);
		} catch (Exception e) {
			return new DBResultError("포인트 충전 로그 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
}
