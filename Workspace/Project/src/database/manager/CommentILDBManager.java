//작성자 : 윤정도

package database.manager;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.CommentBean;
import bean.CommentShortcutBean;
import database.InternetLibraryDatabase;
import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import util.TimeUtil;

public class CommentILDBManager extends ILDBManager {
	private static CommentILDBManager instance = new CommentILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private CommentILDBManager() {
		// EMPTY
	}

	public static CommentILDBManager getInstance() {
		return instance;
	}

	public DBResult registerComment(CommentBean commentBean) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			
			String sql = "insert into t_comment (BOARD_UID, USER_UID, BOARD_TYPE, CONTENT, CREATED_DATE, U_ID"
					+ ") values (?, ?, ?, ?, ?, t_comment_seq.nextval)";
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, commentBean.getBoard_uid());
			ptmt.setInt(2, commentBean.getUser_uid());
			ptmt.setInt(3, commentBean.getBoardType());
			ptmt.setString(4, commentBean.getContent());
			ptmt.setTimestamp(5, TimeUtil.currentTimestamp());
			
			int status = DBResult.SUCCESS;
			String message = "댓글을 성공적으로 달았습니다.";
			 
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "댓글을 다는데 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("댓글을 달다가 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	public DBResult deleteComment(CommentBean commentBean ) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			
			String sql = "delete t_comment" + 
						 " where board_uid = ? and user_uid = ? and u_id = ? and board_type = ?";
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, commentBean.getBoard_uid());
			ptmt.setInt(2, commentBean.getUser_uid());
			ptmt.setInt(3, commentBean.getU_id());
			ptmt.setInt(4, commentBean.getBoardType());
			
			int status = DBResult.SUCCESS;
			String message = "댓글 삭제에 성공하였습니다.";
			 
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "댓글 삭제에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("댓글 삭제중에 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	public DBResult modifyComment(CommentBean commentBean) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			
			String sql = "update t_comment set content = ? " + 
						 " where board_uid = ? and user_uid = ? and u_id = ? and board_type = ?";
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, commentBean.getContent());
			ptmt.setInt(2, commentBean.getBoard_uid());
			ptmt.setInt(3, commentBean.getUser_uid());
			ptmt.setInt(4, commentBean.getU_id());
			ptmt.setInt(5, commentBean.getBoardType());
			
			int status = DBResult.SUCCESS;
			String message = "댓글을 성공적으로 수정하였습니다.";
			 
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "댓글을 수정에 실패하였습니다. 해당 댓글을 찾을 수 없습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("댓글 수정중에 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	public DBResult commentsCount(int boardType, int board_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		int totalCommentsCount = 0;
		
		try {
			
			String sql = "select count(*) from t_comment where board_type = ? and board_uid = ?";
			
			int status = DBResult.SUCCESS;
			String message = "댓글 갯수 확인에 성공하였습니다.";
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, boardType);
			ptmt.setInt(2, board_uid);
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				totalCommentsCount = rs.getInt(1);
			}
			
			return new DBResultWithData<Integer>(status, message, totalCommentsCount);
		} catch (Exception e) {
			return new DBResultError("댓글 갯수 확인 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	public DBResult listComments(int pageNumber,  int boardType, int board_uid, PageOption pageOption) {
		ArrayList<CommentShortcutBean> comments = new ArrayList<CommentShortcutBean>();
		
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		int totalCommentsCount = 0;
		int totalPageCount = 0; 
		
		try {
			
			String sql = "select count(*) from t_comment where board_type = ? and board_uid = ?";
		
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, boardType);
			ptmt.setInt(2, board_uid);
			rs = ptmt.executeQuery();
			
			if (rs.next()) {
				totalCommentsCount = rs.getInt(1);
			}
			
			
			totalPageCount = totalCommentsCount % pageOption.getPageSize() == 0 ?
						totalCommentsCount / pageOption.getPageSize() :
						totalCommentsCount / pageOption.getPageSize() + 1;
					
			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
			
		} catch (Exception e) {
			return new DBResultError("댓글 갯수 확인 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
		
		
		try {
			String sql = "select c.u_id" + 
						 "     , c.board_uid" + 
						 "     , c.user_uid" + 
						 "     , u.nick" + 
						 "     , to_char(c.created_date, 'YYYY.MM.DD AM HH:MI:SS')" + 
						 "     , c.content" + 
						 "  from t_comment c join t_user u" + 
						 "    on c.user_uid = u.u_id" + 
						 " where c.board_type = ? and c.board_uid = ?" + 
						 " order by c.u_id desc"; 
			
			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			ptmt.setInt(1, boardType);
			ptmt.setInt(2, board_uid);
			
			rs = ptmt.executeQuery();
			
			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}
			
			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;
			
			if (rs.next()) {
				rs.absolute(startIdx);
				
				for (int i = 0; i < pageOption.getPageSize(); i++) {
					CommentShortcutBean commentShortcutBean = new CommentShortcutBean();
					commentShortcutBean.setComment_uid(rs.getInt(1));
					commentShortcutBean.setBoard_uid(rs.getInt(2));
					commentShortcutBean.setUser_uid(rs.getInt(3));
					commentShortcutBean.setName(rs.getString(4));
					commentShortcutBean.setCreatedDate(rs.getString(5));
					commentShortcutBean.setContent(rs.getString(6));
					comments.add(commentShortcutBean);
					
					if (rs.isLast()) {
						break;
					}
					
					rs.next();
				}
			}
			
			Page<CommentShortcutBean> page = new Page<CommentShortcutBean>(totalPageCount, pageNumber, pageOption, comments);
			return new DBResultWithData<Page<CommentShortcutBean>>(DBResult.SUCCESS, "성공적으로 댓글 목록을 가져왔습니다.", page);
		} catch (Exception e) {
			return new DBResultError("댓글 목록 로딩하는 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
}
