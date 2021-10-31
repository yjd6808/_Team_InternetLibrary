//작성자 : 윤정도

package database.manager;

import bean.BookBean;
import bean.MyBookBean;
import database.InternetLibraryDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import searchoption.BookSearchOption;
import util.TimeUtil;

public class BookILDBManager extends ILDBManager {
	private static BookILDBManager instance = new BookILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private BookILDBManager() {
		// EMPTY
	}

	public static BookILDBManager getInstance() {
		return instance;
	}
	
	public DBResult register(BookBean bookBean) {
		//'t_book_uid_seq'
		String sql = "insert into t_book ("
				+ "U_ID, NAME, WRITER_NAME, PUBLISHER_NAME, B_CODE, AGE_LIMIT, BORROW_POINT, BUY_POINT, "
				+ "DATA_FILENAME, IMAGE_FILENAME, SCORE, SCORE_GIVER_COUNT) values (t_book_uid_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		PreparedStatement ptmt = null;
		
		try {
			int status = DBResult.SUCCESS;
			String message = "도서 등록에 성공하였습니다!";
			
			connection = database.getConnection();
			ptmt = connection.prepareStatement(sql);
			ptmt.setString(1, bookBean.getName());
			ptmt.setString(2, bookBean.getWriterName());
			ptmt.setString(3, bookBean.getPublisherName());
			ptmt.setString(4, bookBean.getCode());
			ptmt.setInt(5, bookBean.getAgeLimit());
			ptmt.setInt(6, bookBean.getBorrowPoint());
			ptmt.setInt(7, bookBean.getBuyPoint());
			ptmt.setString(8, bookBean.getDataFileName());
			ptmt.setString(9, bookBean.getImageFileName());
			ptmt.setFloat(10, bookBean.getScore());
			ptmt.setInt(11, bookBean.getScoreGiverCount());
			
			status = ptmt.executeUpdate();
			
			if (status == 0) {
				status = DBResult.FAIL;
				message= "도서 등록에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("도서 등록도중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(connection);
		}
	}
	
	
	// 도서 검색결과를 가져옴
	public DBResult listBooks(int pageNumber, PageOption pageOption, BookSearchOption searchOption) {
		
		int totalPageCount = 0;
		int totalBookCount = 0;
		ArrayList<BookBean> books = new ArrayList<BookBean>();
		
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		try {
			
			String sqlPrefix = "select count(*) from t_book ";
			String sqlCondition = "";
			
			switch (searchOption.getSearchOption()) {
			case BookSearchOption.SEARCH_OPTION_NAME:
				sqlCondition = "where name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_WRITER_NAME:
				sqlCondition = "where writer_name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_PUBLISHER_NAME:
				sqlCondition = "where publisher_name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_CODE:
				sqlCondition = "where b_code like ?";
				break;
			}
			
			String sql = sqlPrefix + sqlCondition;
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			if (searchOption.getSearchOption() != BookSearchOption.SEARCH_OPTION_NONE)
				ptmt.setString(1, String.format("%%%s%%", searchOption.getKeyword()));
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				totalBookCount = rs.getInt(1);
			}
			
			
			// ex 1)
			// 도서가 87권이고 한 페이지당 10권씩 보여줄 경우
			// 총 페이지 수는 9
			
			// ex 2)
			// 도서가 80권이고 한 페이지당 10권씩 보여줄 경우
			// 총 페이지 수는 8
			totalPageCount = totalBookCount % pageOption.getPageSize() == 0 ?
					totalBookCount / pageOption.getPageSize() :
					totalBookCount / pageOption.getPageSize() + 1;
					
			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("도서 개수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
		
		try {
			
			
			
			String sqlPrefix = "select * from t_book ";
			String sqlCondition = "";
			String sqlSuffix = " order by u_id desc";
			
			switch (searchOption.getSearchOption()) {
			case BookSearchOption.SEARCH_OPTION_NAME:
				sqlCondition = "where name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_WRITER_NAME:
				sqlCondition = "where writer_name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_PUBLISHER_NAME:
				sqlCondition = "where publisher_name like ?";
				break;
			case BookSearchOption.SEARCH_OPTION_CODE:
				sqlCondition = "where b_code like ?";
				break;
			}
			
			String sql = sqlPrefix + sqlCondition + sqlSuffix;
			
			
			// 참고사항
			// [ResultSet 타입]
			// 1) TYPE_FORWARD_ONLY : scroll이 불가능한 forwad only 형
			// 2) TYPE_SCROLL_INSENSITIVE : scroll은 가능하나, 변경된 사항은 적용되지 않음
			// 3) TYPE_SCROLL_SENSITIVE : scroll은 가능하며, 변경된 사항이 적용됨
			
			// [Concurrency 타입]
			// 1) CONCUR_READ_ONLY : resultset object의 변경이 불가능
			// 2) CONCUR_UPDATABLE : resultset object의 변경이 가능
			
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			if (searchOption.getSearchOption() != BookSearchOption.SEARCH_OPTION_NONE)
				ptmt.setString(1, String.format("%%%s%%", searchOption.getKeyword()));
			rs = ptmt.executeQuery();
			
			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}
			
			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;
			
			if (rs.next()) {
				rs.absolute(startIdx);
				
				for (int i = 0; i < pageOption.getPageSize(); i++) {
					
					BookBean bookBean = new BookBean();
					bookBean.setU_id(rs.getInt("U_ID"));
					bookBean.setName(rs.getString("NAME"));
					bookBean.setWriterName(rs.getString("WRITER_NAME"));
					bookBean.setPublisherName(rs.getString("PUBLISHER_NAME"));
					bookBean.setCode(rs.getString("B_CODE"));
					bookBean.setAgeLimit(rs.getInt("AGE_LIMIT"));
					bookBean.setBorrowPoint(rs.getInt("BORROW_POINT"));
					bookBean.setBuyPoint(rs.getInt("BUY_POINT"));
					bookBean.setDataFileName(rs.getString("DATA_FILENAME"));
					bookBean.setImageFileName(rs.getString("IMAGE_FILENAME"));
					bookBean.setScore(rs.getFloat("SCORE"));
					bookBean.setScoreGiverCount(rs.getInt("SCORE_GIVER_COUNT"));
					books.add(bookBean);
					
					if (rs.isLast()) {
						break;
					}
					
					rs.next();
				}
			}
			
			
			Page<BookBean> page = new Page<BookBean>(totalPageCount, pageNumber, pageOption, books);
			return new DBResultWithData<Page<BookBean>>(DBResult.SUCCESS, "검색 성공!", page);
		} catch (Exception e) {
			return new DBResultError("도서 검색 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	
	/*
	
	[등수 확인 테스트 쿼리]
	select name
	     , round(score / decode(score_giver_count, 0, 1, score_giver_count), 2) "avg_score"
	     , rank() over(
	            order by score / decode(score_giver_count, 0, 1, score_giver_count) desc,
	                     u_id desc) "rank"
	  from t_book;
	
	 */
	
	
	// rank로 전닳한 등수까지만 가져옴
	public DBResult listBooksByScoreRank(int rank) {
		ArrayList<BookBean> books = new ArrayList<BookBean>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select *" + 
				     "  from t_book" + 
				     " order by score / decode(score_giver_count, 0, 1, score_giver_count) desc," + 
				     "       score_giver_count desc, "
			       + "		 u_id desc";
		
		try {
			conn = database.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int count = 0;

			while (rs.next() && count < rank) {
				BookBean bookBean = new BookBean();
				bookBean.setU_id(rs.getInt("U_ID"));
				bookBean.setName(rs.getString("NAME"));
				bookBean.setWriterName(rs.getString("WRITER_NAME"));
				bookBean.setPublisherName(rs.getString("PUBLISHER_NAME"));
				bookBean.setCode(rs.getString("B_CODE"));
				bookBean.setAgeLimit(rs.getInt("AGE_LIMIT"));
				bookBean.setBorrowPoint(rs.getInt("BORROW_POINT"));
				bookBean.setBuyPoint(rs.getInt("BUY_POINT"));
				bookBean.setDataFileName(rs.getString("DATA_FILENAME"));
				bookBean.setImageFileName(rs.getString("IMAGE_FILENAME"));
				bookBean.setScore(rs.getFloat("SCORE"));
				bookBean.setScoreGiverCount(rs.getInt("SCORE_GIVER_COUNT"));
				books.add(bookBean);
				count++;
			}
			
			return new DBResultWithData<ArrayList<BookBean>>(
					DBResult.SUCCESS, 
					String.format("도서정보를 점수 랭킹 %d등까지 가져왔습니다.\n가져온 데이터 수 : %d", rank, count), 
					books);
		} catch (Exception e) {
			return new DBResultError(String.format("도서정보를 점수 랭킹 %d등까지 가져오다가 오류가 발생하였습니다.", rank), e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(stmt);
			CloseConnection(conn);
		}
	}
	
	// 도서 아이디값에 해당하는 도서 정보를 얻어온다.
	public DBResult getBook(int book_uId) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		String sql = "select * from t_book where u_id = ?";
		BookBean bookBean = new BookBean();
		
		try {
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, book_uId);
			rs = ptmt.executeQuery();
			
			int status = DBResult.SUCCESS;
			String message = "도서 정보를 얻어오는데 성공하였습니다.";

			if (rs.next()) {
				bookBean.setU_id(rs.getInt("U_ID"));
				bookBean.setName(rs.getString("NAME"));
				bookBean.setWriterName(rs.getString("WRITER_NAME"));
				bookBean.setPublisherName(rs.getString("PUBLISHER_NAME"));
				bookBean.setCode(rs.getString("B_CODE"));
				bookBean.setAgeLimit(rs.getInt("AGE_LIMIT"));
				bookBean.setBorrowPoint(rs.getInt("BORROW_POINT"));
				bookBean.setBuyPoint(rs.getInt("BUY_POINT"));
				bookBean.setDataFileName(rs.getString("DATA_FILENAME"));
				bookBean.setImageFileName(rs.getString("IMAGE_FILENAME"));
				bookBean.setScore(rs.getFloat("SCORE"));
				bookBean.setScoreGiverCount(rs.getInt("SCORE_GIVER_COUNT"));
			} else {
				status = DBResult.FAIL;
				message = "도서 정보를 얻어오는데 실패하였습니다.";
			}
			
			return new DBResultWithData<BookBean>(status, message, bookBean);
		} catch (Exception e) {
			return new DBResultError("도서 정보를 얻어오는데 실패하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	// 소장중인 도서 정보를 얻어온다.
	public DBResult getMyBook(int book_uId, int user_uId) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		String sql = "select * from t_mybook where book_uid = ? and user_uid = ?";
		MyBookBean myBooBean = null;
		
		try {
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, book_uId);
			ptmt.setInt(2, user_uId);
			rs = ptmt.executeQuery();
			
			int status = DBResult.SUCCESS;
			String message = "도서 정보를 얻어오는데 성공하였습니다.";

			if (rs.next()) {
				myBooBean = new MyBookBean();
				myBooBean.setBook_uid(rs.getInt("BOOK_UID"));
				myBooBean.setUser_uid(rs.getInt("USER_UID"));
				myBooBean.setStartDate(new Date(rs.getTimestamp("START_DATE").getTime()));
				
				Timestamp endDate = rs.getTimestamp("END_DATE");
				myBooBean.setEndDate(endDate == null ? null : new Date(endDate.getTime())); 
				
			} else {
				message = "소장중인 도서가 아닙니다.";
				status = DBResult.FAIL;
			}
			
			return new DBResultWithData<MyBookBean>(status, message, myBooBean);
		} catch (Exception e) {
			return new DBResultError("내가 소장중인 도서 정보를 얻어오는데 실패하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	// 3회 Data Manipulation이 이뤄지기 때문에 Transaction을 사용
	// 트랜잭션이란 : https://mommoo.tistory.com/62
	// 예제 코드 : https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html
	
	// 도서를 빌린다.
	public DBResult borrowBook(int book_uid, int user_uid, int day, int price) {
		Connection conn = null;
		PreparedStatement updateUserPtmt = null;
		PreparedStatement insertMyBookPtmt = null;
		PreparedStatement insertLogPtmt = null;
		
		String updateUserSql = "update t_user set point = point - ? where u_id = ?";
		String insertMyBookSql = "insert into t_mybook (book_uid, user_uid, start_date, end_date) values (?, ?, ?, ?)";
		String insertLogSql = "insert into t_use_point_log values (?, ?, ?, ?, t_use_point_log_seq.nextval)";
		int status = DBResult.SUCCESS;
		String message = "";
		
		try {
			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			updateUserPtmt = conn.prepareStatement(updateUserSql);
			insertMyBookPtmt = conn.prepareStatement(insertMyBookSql);
			insertLogPtmt = conn.prepareStatement(insertLogSql);
			
			updateUserPtmt.setInt(1, price);
			updateUserPtmt.setInt(2, user_uid);
			
			if (updateUserPtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "유저 포인트 수정에 실패하였습니다.";
			}
			
			if (status == DBResult.SUCCESS) {
				insertLogPtmt.setInt(1, user_uid);
				insertLogPtmt.setInt(2, price);
				insertLogPtmt.setTimestamp(3, TimeUtil.currentTimestamp());
				insertLogPtmt.setString(4, "도서 대여");
				
				if (insertLogPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 대여 로그를 남기는데 실패하였습니다.";
				}
			}
			
			if (status == DBResult.SUCCESS) {
				insertMyBookPtmt.setInt(1, book_uid);
				insertMyBookPtmt.setInt(2, user_uid);
				insertMyBookPtmt.setTimestamp(3, TimeUtil.currentTimestamp());
				insertMyBookPtmt.setTimestamp(4, new Timestamp(TimeUtil.addDay(System.currentTimeMillis(), 7)));
				
				if (insertMyBookPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 대여에 실패하였습니다.";
				}
			}
			
			
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
				message = String.format("도서 대여에 성공하였습니다. (기간 : %d)", day);				
			} 
			
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("도서 대여 중 오류가 발생하였습니다.", e);
		} finally {
			
			// 닫기전에 롤백 또는 커밋 해줘야함.
			// @참고 : https://stackoverflow.com/questions/218350/does-java-connection-close-rollback
			// According to the javadoc, you should try to either commit or roll back
			// before calling the close method. The results otherwise are implementation-defined.
			
			RollbackConnection(conn);
			CloseStatement(insertLogPtmt);
			CloseStatement(updateUserPtmt);
			CloseStatement(insertMyBookPtmt);
			CloseConnection(conn);
		}
	}
	
	public DBResult buyBook(int book_uId, int user_uid, int price) {
		Connection conn = null;
		PreparedStatement updateUserPtmt = null;
		PreparedStatement insertMyBookPtmt = null;
		PreparedStatement insertLogPtmt = null;
		
		String updateUserSql = "update t_user set point = point - ? where u_id = ?";
		String insertMyBookSql = "insert into t_mybook (book_uid, user_uid, start_date) values (?, ?, ?)";
		String insertLogSql = "insert into t_use_point_log values (?, ?, ?, ?, t_use_point_log_seq.nextval)";
		
		int status = DBResult.SUCCESS;
		String message = "";
		
		try {
			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			updateUserPtmt = conn.prepareStatement(updateUserSql);
			insertMyBookPtmt = conn.prepareStatement(insertMyBookSql);
			insertLogPtmt = conn.prepareStatement(insertLogSql);
			
			updateUserPtmt.setInt(1, price);
			updateUserPtmt.setInt(2, user_uid);
			
			if (updateUserPtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "유저 포인트 수정에 실패하였습니다.";
			}
			
			if (status == DBResult.SUCCESS) {
				insertLogPtmt.setInt(1, user_uid);
				insertLogPtmt.setInt(2, price);
				insertLogPtmt.setTimestamp(3, TimeUtil.currentTimestamp());
				insertLogPtmt.setString(4, "도서 구매");
				
				if (insertLogPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 구매 로그를 남기는데 실패하였습니다.";
				}
			}
			
			if (status == DBResult.SUCCESS) {
				insertMyBookPtmt.setInt(1, book_uId);
				insertMyBookPtmt.setInt(2, user_uid);
				insertMyBookPtmt.setTimestamp(3, TimeUtil.currentTimestamp());
				
				if (insertMyBookPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 구매에 실패하였습니다.";
				}
			}
			
			
			
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
				message = String.format("도서 구매에 성공하였습니다.");
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("도서 구매 중 오류가 발생하였습니다.", e);
		} finally {
			
			RollbackConnection(conn);
			CloseStatement(updateUserPtmt);
			CloseStatement(insertLogPtmt);
			CloseStatement(insertMyBookPtmt);
			CloseConnection(conn);
		}
	}
}
