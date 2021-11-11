//작성자 : 윤정도

package database.manager;

import bean.BorrowBookShortcutBean;
import bean.BuyBookShortcutBean;
import database.InternetLibraryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import structure.Tuple;
import util.TimeUtil;

public class MyBookILDBManager extends ILDBManager {
	private static MyBookILDBManager instance = new MyBookILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private MyBookILDBManager() {
		// EMPTY
	}

	public static MyBookILDBManager getInstance() {
		return instance;
	}
	
	
	// 기간이 지난 빌린 도서를 제거해준다.
	public DBResult updateBorrowBooks(int user_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;

		try {

			String sql = "delete t_mybook where end_date <= sysdate and user_uid = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, user_uid);
			ptmt.executeUpdate();
			return new DBResult(DBResult.SUCCESS, "기간이 지난 도서를 성공적으로 제거하였습니다.");
		} catch (Exception e) {
			return new DBResultError("기간이 지난 도서 제거 중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
		
	}
	
	// 마이페이지 - 대여 중인 도서 목록 정보 가져오기
	// 		대여중인 도서는 end_date 칼럼이 null이 아니다.
	public DBResult listBorrowBooks(int user_uid, int pageNumber, PageOption pageOption) {
		ArrayList<BorrowBookShortcutBean> borrows = new ArrayList<BorrowBookShortcutBean>();

		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		int totalMyBorrowBooksCount = 0;
		int totalPageCount = 0;

		try {
			String sql = "select count(*) from t_mybook where user_uid = ? and end_date is not null";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			if (rs.next()) {
				totalMyBorrowBooksCount = rs.getInt(1);
			}

			totalPageCount = totalMyBorrowBooksCount % pageOption.getPageSize() == 0
					? totalMyBorrowBooksCount / pageOption.getPageSize()
					: totalMyBorrowBooksCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("대여 도서 갯수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select m.book_uid" + 
					"     , b.name" + 
					"     , m.start_date" + 
					"     , m.end_date" +  
					"  from t_mybook m join t_book b" + 
					"    on m.book_uid = b.u_id" + 
					" where m.user_uid = ? and m.end_date is not null" + 
					" order by m.start_date desc";

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
					BorrowBookShortcutBean borrowBookBean = new BorrowBookShortcutBean();
					borrowBookBean.setBook_uid(rs.getInt(1));
					borrowBookBean.setBookName(rs.getString(2));
					
					Timestamp startDate = rs.getTimestamp(3);
					Timestamp endDate = rs.getTimestamp(4);
					
					// HH : 14시
					// hh :  2시
					// mm : 분
					// ss : 초
					// dd : 일
					// MM : 월
					// yyyy : 년도
					
					borrowBookBean.setStartDate(TimeUtil.format(startDate, "yyyy-MM-dd") + "<br>" + TimeUtil.format(startDate, "hh:mm:ss"));
					borrowBookBean.setEndDate(TimeUtil.format(endDate, "yyyy-MM-dd") + "<br>" + TimeUtil.format(endDate, "hh:mm:ss"));
					
					String leftTime = TimeUtil.parseDiff(endDate.getTime() - System.currentTimeMillis()
							         , TimeUtil.TYPE_SEC
							         , TimeUtil.TYPE_DAY
							         , false);
				    
				    borrowBookBean.setLeftTime(leftTime);
					borrows.add(borrowBookBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<BorrowBookShortcutBean> page = new Page<BorrowBookShortcutBean>(totalPageCount, pageNumber, pageOption, borrows);
			
			Tuple<Integer, Page<BorrowBookShortcutBean>> data = new Tuple<Integer, Page<BorrowBookShortcutBean>>();
			
			data.setItem1(totalMyBorrowBooksCount);
			data.setItem2(page);
			
			return new DBResultWithData<Tuple<Integer, Page<BorrowBookShortcutBean>>>(DBResult.SUCCESS, "대여중인 도서 목록 가져오기 성공!", data);
		} catch (Exception e) {
			return new DBResultError("대여중인 도서 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	// 마이페이지 - 구매완료한 도서 목록 정보 가져오기
	// 		구매한 도서는 end_date 칼럼이 null이다.
	public DBResult listBuyBooks(int user_uid, int pageNumber, PageOption pageOption) {
		ArrayList<BuyBookShortcutBean> buys = new ArrayList<BuyBookShortcutBean>();

		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		int totalMyBuyBooksCount = 0;
		int totalPageCount = 0;

		try {
			String sql = "select count(*) from t_mybook where user_uid = ? and end_date is null";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			if (rs.next()) {
				totalMyBuyBooksCount = rs.getInt(1);
			}

			totalPageCount = totalMyBuyBooksCount % pageOption.getPageSize() == 0
					? totalMyBuyBooksCount / pageOption.getPageSize()
					: totalMyBuyBooksCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("구매 도서 갯수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select m.book_uid" + 
						"     , b.name" + 
						"     , m.start_date " +  
						"  from t_mybook m join t_book b" + 
						"    on m.book_uid = b.u_id" + 
						" where m.user_uid = ? and m.end_date is null" + 
						" order by m.start_date desc";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ptmt.setInt(1, user_uid);
			rs = ptmt.executeQuery();

			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}

			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;

			if (rs.next()) {
				rs.absolute(startIdx);
				// 날짜 포맷 참고 : https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
				
				for (int i = 0; i < pageOption.getPageSize(); i++) {
					BuyBookShortcutBean buyBookBean = new BuyBookShortcutBean();
					buyBookBean.setBook_uid(rs.getInt(1));
					buyBookBean.setBookName(rs.getString(2));
					buyBookBean.setBuyDate(TimeUtil.format(rs.getTimestamp(3), "yyyy-MM-dd a hh:mm:ss"));
					buys.add(buyBookBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<BuyBookShortcutBean> page = new Page<BuyBookShortcutBean>(totalPageCount, pageNumber, pageOption, buys);
			
			Tuple<Integer, Page<BuyBookShortcutBean>> data = new Tuple<Integer, Page<BuyBookShortcutBean>>();
			
			data.setItem1(totalMyBuyBooksCount);
			data.setItem2(page);
			
			return new DBResultWithData<Tuple<Integer, Page<BuyBookShortcutBean>>>(DBResult.SUCCESS, "구매중인 도서 목록 가져오기 성공!", data);
		} catch (Exception e) {
			return new DBResultError("구매중인 도서 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
}
