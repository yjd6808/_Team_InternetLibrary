//작성자 : 윤정도

package database.manager;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import bean.RequestBoardBean;
import bean.RequestBoardShortcutBean;
import bean.ReviewBoardBean;
import bean.ReviewBoardShortcutBean;
import constant.ActivityConstant;
import constant.BoardConstant;
import database.InternetLibraryDatabase;
import database.result.DBResult;
import database.result.DBResultError;
import database.result.DBResultWithData;
import pagination.Page;
import pagination.PageOption;
import structure.TripleObject;
import structure.Tuple;
import util.TimeUtil;

public class BoardILDBManager extends ILDBManager {
	private static BoardILDBManager instance = new BoardILDBManager();
	private InternetLibraryDatabase database = new InternetLibraryDatabase();

	private BoardILDBManager() {
		// EMPTY
	}

	public static BoardILDBManager getInstance() {
		return instance;
	}
	
	
	// 후기 게시글 삭제
	public DBResult deleteReviewBoard(int board_uid, int writer_uid, int book_uid, float rollbackScore) {
		Connection conn = null;
		PreparedStatement clearCommentsPtmt = null;
		PreparedStatement deleteBoardPtmt = null;
		PreparedStatement rollbackBookScorePtmt = null;
		
		try {

			String clearCommentsSql = "delete t_comment "
									+ " where board_uid = ? and board_type = ?";					// 해당 게시글에 달린 모든 댓글제거
			String rollbackBookScoreSql = "update t_book set score = score - ?, score_giver_count = score_giver_count - 1 "
										+ " where u_id = ?";										// 도서에 부여된 점수 롤백
			String deleteBoardSql = "delete t_review_board where u_id = ? and user_uid = ?";		// 게시글 삭제

			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			clearCommentsPtmt = conn.prepareStatement(clearCommentsSql);
			deleteBoardPtmt = conn.prepareStatement(deleteBoardSql);
			rollbackBookScorePtmt = conn.prepareStatement(rollbackBookScoreSql);
			
			int status = DBResult.SUCCESS;
			String message = "후기 게시글 삭제에 성공하였습니다.";
			
			// ↓ 댓글 삭제 ============================================= 
			clearCommentsPtmt.setInt(1, board_uid);
			clearCommentsPtmt.setInt(2, BoardConstant.BOARD_TYPE_REVIEW);
			
			
			int deleteCommentCount = clearCommentsPtmt.executeUpdate();
			
			
			// ↓ 점수 롤백  =============================================
			rollbackBookScorePtmt.setFloat(1, rollbackScore);
			rollbackBookScorePtmt.setInt(2, book_uid);
			
			if (rollbackBookScorePtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "해당 후기 게시글의 도서에 부여된 점수 회수에 실패하였습니다.";
			}
			
			
			// ↓ 게시글 삭제  =============================================
			
			if (status == DBResult.SUCCESS) {
				deleteBoardPtmt.setInt(1, board_uid);
				deleteBoardPtmt.setInt(2, writer_uid);
				
				
				if (deleteBoardPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "후기 게시글 삭제에 실패하였습니다.";
				}
			}
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("후기 게시글 삭제 중 오류가 발생하였습니다.", e);
		} finally {
			RollbackConnection(conn);
			CloseStatement(clearCommentsPtmt);
			CloseStatement(rollbackBookScorePtmt);
			CloseStatement(deleteBoardPtmt);
			CloseConnection(conn);
		}
	}
	
	
	
	
	// 후기 게시글 수정
	public DBResult modifyReviewBoard(ReviewBoardBean reviewBoardBean) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			String sql = "update t_review_board set title = ?, content = ? "
					   + " where u_id = ? and user_uid = ? and book_uid = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, reviewBoardBean.getTitle());
			ptmt.setString(2, reviewBoardBean.getContent());
			ptmt.setInt(3, reviewBoardBean.getU_id());
			ptmt.setInt(4, reviewBoardBean.getUser_uid());
			ptmt.setInt(5, reviewBoardBean.getBook_uid());
			
			
			int status = DBResult.SUCCESS;
			String message = "후기 게시글 수정에 성공하였습니다.";
			
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "후기 게시글 수정에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("후기 게시글 수정 중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}

	// 후기 도서게시글 작성
	public DBResult registerReviewBoard(ReviewBoardBean reviewBoardBean) {
		Connection conn = null;
		PreparedStatement updatePointPtmt = null;
		PreparedStatement insertBookPtmt = null;
		PreparedStatement updateBookScorePtmt = null;

		String updatePointSql = "update t_user set point = point + ? where u_id = ?";
		String insertBookSql = "insert into t_review_board (U_ID, USER_UID, BOOK_UID, TITLE, CONTENT, CREATED_DATE, SCORE)"
							 + "values (t_review_board_seq.nextval, ?, ?, ?, ?, ?, ?)";
		String updateBookTotalScoreSql = "update t_book set score = score + ?, "
													+ "     score_giver_count = score_giver_count + 1 " + "where u_id = ?";

		try {
			int status = DBResult.SUCCESS;
			String message = String.format("후기 도서 글을 등록하는데 성공하였습니다.\n포인트 획득(+%dP)",
					ActivityConstant.POINT_WRITING_REVIEW_BOARD);

			conn = database.getConnection();
			conn.setAutoCommit(false);

			updatePointPtmt = conn.prepareStatement(updatePointSql);
			updatePointPtmt.setInt(1, ActivityConstant.POINT_WRITING_REVIEW_BOARD);
			updatePointPtmt.setInt(2, reviewBoardBean.getUser_uid());

			if (updatePointPtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "유저 포인트 수정에 실패하였습니다.";
			}

			if (status == DBResult.SUCCESS) {
				insertBookPtmt = conn.prepareStatement(insertBookSql);
				insertBookPtmt.setInt(1, reviewBoardBean.getUser_uid());
				insertBookPtmt.setInt(2, reviewBoardBean.getBook_uid());
				insertBookPtmt.setString(3, reviewBoardBean.getTitle());
				insertBookPtmt.setString(4, reviewBoardBean.getContent());
				insertBookPtmt.setTimestamp(5, TimeUtil.convertToTimestamp(reviewBoardBean.getCreatedDate()));
				insertBookPtmt.setFloat(6, reviewBoardBean.getScore());

				if (insertBookPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "후기 도서 글을 등록하는데 실패하였습니다.";
				}
			}

			if (status == DBResult.SUCCESS) {
				updateBookScorePtmt = conn.prepareStatement(updateBookTotalScoreSql);
				updateBookScorePtmt.setFloat(1, reviewBoardBean.getScore());
				updateBookScorePtmt.setInt(2, reviewBoardBean.getBook_uid());

				if (updateBookScorePtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 후기 게시글을 등록하는데 실패하였습니다.\n 도서 점수 반영 실패";
				}
			}

			if (status == DBResult.SUCCESS) {
				conn.commit();
			}

			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("도서 후기 글쓰기 중 오류가 발생하였습니다.", e);
		} finally {
			RollbackConnection(conn);
			CloseStatement(updatePointPtmt);
			CloseStatement(insertBookPtmt);
			CloseStatement(updateBookScorePtmt);
			CloseConnection(conn);
		}
	}
	
	// 신청 게시글 수정
	public DBResult deleteRequestBoard(int board_uid, int writer_uid) {
		Connection conn = null;
		PreparedStatement clearCommentsPtmt = null;
		PreparedStatement deleteBoardPtmt = null;
		
		try {
			// 해당 게시글에 달린 모든 댓글 삭제
			String clearCommentsSql = "delete t_comment "
									+ " where board_uid = ? and board_type = ?";
			String deleteBoardSql = "delete t_request_board where u_id = ? and user_uid = ?";

			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			clearCommentsPtmt = conn.prepareStatement(clearCommentsSql);
			deleteBoardPtmt = conn.prepareStatement(deleteBoardSql);
			
			
			clearCommentsPtmt.setInt(1, board_uid);
			clearCommentsPtmt.setInt(2, BoardConstant.BOARD_TYPE_REQUEST);
			int deleteCommentCount = clearCommentsPtmt.executeUpdate();
			
			deleteBoardPtmt.setInt(1, board_uid);
			deleteBoardPtmt.setInt(2, writer_uid);
			
			int status = DBResult.SUCCESS;
			String message = "신청 게시글 삭제에 성공하였습니다.";
			
			if (deleteBoardPtmt.executeUpdate() != 0) {
				conn.commit();
			} else {
				status = DBResult.FAIL;
				message = "신청 게시글 삭제에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("신청 게시글 삭제 중 오류가 발생하였습니다.", e);
		} finally {
			RollbackConnection(conn);
			CloseStatement(clearCommentsPtmt);
			CloseStatement(deleteBoardPtmt);
			CloseConnection(conn);
		}
	}
	
	
	// 신청 게시글 수정
	public DBResult modifyRequestBoard(RequestBoardBean requestBoardBean) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			String sql = "update t_request_board set title = ?, content = ? "
					   + " where u_id = ? and user_uid = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setString(1, requestBoardBean.getTitle());
			ptmt.setString(2, requestBoardBean.getContent());
			ptmt.setInt(3, requestBoardBean.getU_id());
			ptmt.setInt(4, requestBoardBean.getUser_uid());
			
			
			int status = DBResult.SUCCESS;
			String message = "신청 게시글 수정에 성공하였습니다.";
			
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "신청 게시글 수정에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("신청 게시글 수정 중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}

	// 도서 신청 게시글 작성
	public DBResult registerRequestBoard(RequestBoardBean requestBoardBean) {
		Connection conn = null;
		PreparedStatement updatePointPtmt = null;
		PreparedStatement insertBookPtmt = null;

		String updatePointSql = "update t_user set point = point + ? where u_id = ?";
		String insertBoardSql = "insert into t_request_board (U_ID, USER_UID, TITLE, CONTENT, CREATED_DATE)"
							  + "values (t_request_board_seq.nextval, ?, ?, ?, ?)";

		try {
			int status = DBResult.SUCCESS;
			String message = String.format("도서 신청 게시글을 등록하는데 성공하였습니다.\n포인트 획득(+%dP)",
					ActivityConstant.POINT_WRITING_REVIEW_BOARD);

			conn = database.getConnection();
			conn.setAutoCommit(false);

			updatePointPtmt = conn.prepareStatement(updatePointSql);
			updatePointPtmt.setInt(1, ActivityConstant.POINT_WRITING_REQUEST_BOARD);
			updatePointPtmt.setInt(2, requestBoardBean.getUser_uid());

			if (updatePointPtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "유저 포인트 수정에 실패하였습니다.";
			}

			if (status == DBResult.SUCCESS) {
				insertBookPtmt = conn.prepareStatement(insertBoardSql);
				insertBookPtmt.setInt(1, requestBoardBean.getUser_uid());
				insertBookPtmt.setString(2, requestBoardBean.getTitle());
				insertBookPtmt.setString(3, requestBoardBean.getContent());
				insertBookPtmt.setTimestamp(4, TimeUtil.convertToTimestamp(requestBoardBean.getCreatedDate()));

				if (insertBookPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message = "도서 신청 게시글을 등록하는데 실패하였습니다.";
				}
			}
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
			}

			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("도서 신청 글쓰기 중 오류가 발생하였습니다.", e);
		} finally {
			RollbackConnection(conn);
			CloseStatement(updatePointPtmt);
			CloseStatement(insertBookPtmt);
			CloseConnection(conn);
		}
	}
	

	
	public DBResult addVisitCountRequestBoard(int board_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			String updateVisitCountSql = "update t_request_board set visit_count = visit_count + 1 where u_id = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(updateVisitCountSql);
			ptmt.setInt(1, board_uid);
			
			int status = DBResult.SUCCESS;
			String message = "신청 게시글 조회수 수정에 성공하였습니다.";
			
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "신청 게시글 조회수 수정에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("신청 게시글 조회수 수정 중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	public DBResult addVisitCountReviewBoard(int board_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		
		try {
			String updateVisitCountSql = "update t_review_board set visit_count = visit_count + 1 where u_id = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(updateVisitCountSql);
			ptmt.setInt(1, board_uid);
			
			int status = DBResult.SUCCESS;
			String message = "후기 게시글 조회수 수정에 성공하였습니다.";
			
			if (ptmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "후기 게시글 조회수 수정에 실패하였습니다.";
			}
			
			return new DBResult(status, message);
		} catch (Exception e) {
			return new DBResultError("후기 게시글 조회수 수정 중 오류가 발생하였습니다.", e);
		} finally {
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}

	public DBResult getReviewBoard(int board_uid, int user_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		
		boolean canLike = false;
		Date likeDate = null;
		
		// 로그인하여 게시글을 보는 경우에만 체크한다.
		if (user_uid != -1) {
			try {
				String sql = "select like_date " + 
				    	     "  from t_review_board_like" + 
					      	 " where user_uid = ? and board_uid = ?";

				conn = database.getConnection();
				ptmt = conn.prepareStatement(sql);
				ptmt.setInt(1, user_uid);
				ptmt.setInt(2, board_uid);
				rs = ptmt.executeQuery();
				
				if (rs.next()) {
					likeDate = rs.getTimestamp(1);
					
					// 현재시간이 좋아요한 날 + 1일 한 시간보다 큰 경우 좋아요가 가능하다.
					//   좋아요 한 날에 대한 데이터 추가는 likeReviewBoard() 함수에서 진행한다.
					if (likeDate.compareTo(new Date(System.currentTimeMillis())) < 0) {
						canLike = true;
					}
				} else {
					canLike = true;
				}
				
				
			} catch (Exception e) {
				return new DBResultError("좋아요 가능여부 확인 중 오류가 발생하였습니다.", e);
			} finally {
				CloseResultSet(rs);
				CloseStatement(ptmt);
				CloseConnection(conn);
			}
		}
		
		

		try {
			String sql = "select * from t_review_board where u_id = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, board_uid);
			rs = ptmt.executeQuery();

			String message = "후기 게시글 정보를 성공적으로 불러왔습니다.";
			int status = DBResult.SUCCESS;
			ReviewBoardBean reviewBoardBean = new ReviewBoardBean();

			if (rs.next()) {
				reviewBoardBean.setU_id(rs.getInt("U_ID"));
				reviewBoardBean.setUser_uid(rs.getInt("USER_UID"));
				reviewBoardBean.setBook_uid(rs.getInt("BOOK_UID"));
				reviewBoardBean.setTitle(rs.getString("TITLE"));
				reviewBoardBean.setContent(rs.getString("CONTENT"));
				reviewBoardBean.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
				reviewBoardBean.setVisitCount(rs.getInt("VISIT_COUNT"));
				reviewBoardBean.setLikeCount(rs.getInt("LIKE_COUNT"));
				reviewBoardBean.setScore(rs.getFloat("SCORE"));
			} else {
				status = DBResult.FAIL;
				message = "후기 게시글 정보가 존재하지 않습니다.";
			}
			
			// item1 : 좋아요 가능여부
			// item2 : 보드 데이터
			// item3 : 좋아요 한 날짜
			TripleObject data = new TripleObject();
					
			data.setItem1(canLike);
			data.setItem2(reviewBoardBean);
			data.setItem3(likeDate);

			return new DBResultWithData<TripleObject>(status, message, data);
		} catch (Exception e) {
			return new DBResultError("후기 게시물을 불러오는 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}

	// 도서 신청 게시글을 가져옴
	public DBResult getRequestBoard(int board_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from t_request_board where u_id = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, board_uid);
			rs = ptmt.executeQuery();

			String message = "신청 게시글 정보를 성공적으로 불러왔습니다.";
			int status = DBResult.SUCCESS;
			RequestBoardBean requestBoardBean = new RequestBoardBean();

			if (rs.next()) {
				requestBoardBean.setU_id(rs.getInt("U_ID"));
				requestBoardBean.setUser_uid(rs.getInt("USER_UID"));
				requestBoardBean.setTitle(rs.getString("TITLE"));
				requestBoardBean.setContent(rs.getString("CONTENT"));
				requestBoardBean.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
				requestBoardBean.setVisitCount(rs.getInt("VISIT_COUNT"));
			} else {
				status = DBResult.FAIL;
				message = "신청 게시글 정보가 존재하지 않습니다.";
			}

			return new DBResultWithData<RequestBoardBean>(status, message, requestBoardBean);
		} catch (Exception e) {
			return new DBResultError("신청 게시글 정보를 불러오는 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}

	// 좋아요를 누른 게시글의 U_ID
	// 좋아요를 누른 사람의 U_ID
	@SuppressWarnings("unchecked")
	public DBResult likeReviewBoard(int board_uid, int user_uid) {
		Connection conn = null;
		PreparedStatement updateLikeCountPtmt = null;
		PreparedStatement deleteLikeDatePtmt = null;
		PreparedStatement insertLikeDatePtmt = null;
		
		DBResult likeCountDBResult = getReviewBoardLikeCount(board_uid);
		int likeCount = -1;

		if (likeCountDBResult.getStatus() == DBResult.SUCCESS) {
			likeCount = ((DBResultWithData<Integer>) likeCountDBResult).getData();
		} else {
			return likeCountDBResult;
		}
		
		int status = DBResult.SUCCESS;
		String message = "좋아요!";

		try {
			
			String deleteLikeDateSql = "delete t_review_board_like where user_uid = ? and board_uid = ?";		// 좋아요 기록 삭제
			String insertLikeDateSql = "insert into t_review_board_like (user_uid, board_uid, like_date) " +	// 내일 날짜 기록 
									   "values (?, ?, sysdate + 1)";				
			String updateLikeSql = "update t_review_board set like_count = like_count + 1 where u_id = ?";		// 좋아요 진행

			conn = database.getConnection();
			conn.setAutoCommit(false);
			
			deleteLikeDatePtmt = conn.prepareStatement(deleteLikeDateSql);
			deleteLikeDatePtmt.setInt(1, user_uid);
			deleteLikeDatePtmt.setInt(2, board_uid);
			deleteLikeDatePtmt.executeUpdate(); // 삭제되든 안되든 오류만 안나면됨
			
			insertLikeDatePtmt = conn.prepareStatement(insertLikeDateSql);
			insertLikeDatePtmt.setInt(1, user_uid);
			insertLikeDatePtmt.setInt(2, board_uid);
			
			if (insertLikeDatePtmt.executeUpdate() == 0) {
				status = DBResult.FAIL;
				message = "좋아요 다음 날짜 기록을 실패하였습니다.";
			}
			
			if (status == DBResult.SUCCESS) {
				updateLikeCountPtmt = conn.prepareStatement(updateLikeSql);
				updateLikeCountPtmt.setInt(1, board_uid);

				if (updateLikeCountPtmt.executeUpdate() == 0) {
					status = DBResult.FAIL;
					message= "좋아요에 실패하였습니다.";
				}	
			}
			
			if (status == DBResult.SUCCESS) {
				conn.commit();
			}
			
			return new DBResultWithData<Integer>(status, message, likeCount + 1);
		} catch (Exception e) {
			return new DBResultError("좋아요 중 오류가 발생하였습니다.", e);
		} finally {
			RollbackConnection(conn);
			CloseStatement(deleteLikeDatePtmt);
			CloseStatement(insertLikeDatePtmt);
			CloseStatement(updateLikeCountPtmt);
			CloseConnection(conn);
		}
	}

	// 해당 게시글의 좋아요 수 가져오기
	public DBResult getReviewBoardLikeCount(int board_uid) {
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		try {
			String sql = "select like_count from t_review_board where u_id = ?";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, board_uid);
			rs = ptmt.executeQuery();

			int likeCount = -1;
			int status = DBResult.SUCCESS;
			String message = "좋아요 갯수 확인에 성공하였습니다.";

			if (rs.next()) {
				likeCount = rs.getInt(1);
			} else {
				status = DBResult.FAIL;
				message = "좋아요 갯수 확인에 실패하였습니다.";
			}

			return new DBResultWithData<Integer>(status, message, likeCount);
		} catch (Exception e) {
			return new DBResultError("좋아요 갯수 확인중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
	}
	
	
	public DBResult listReviewBoardTopN(int book_uid, int topN) {
		
		ArrayList<ReviewBoardShortcutBean> reviewBoards = new ArrayList<ReviewBoardShortcutBean>();
		
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;

		try {
			// 좋아요 수 기준으로 먼저 가져옴, 그 다음 조회수, 그다음이 글쓴 순서
			String sql = "select b.u_id, b.title, u.nick, to_char(b.created_date, 'YYYY-MM-DD'), b.visit_count, b.like_count" + 
						 "  from t_review_board b join t_user u" + 
					  	 "    on b.user_uid = u.u_id" + 
					 	 " where b.book_uid = ?" + 
						 " order by b.like_count desc, b.visit_count desc, b.u_id desc";

			conn = database.getConnection();
			ptmt = conn.prepareStatement(sql);
			ptmt.setInt(1, book_uid);
			rs = ptmt.executeQuery();
			
			int count = 0;
			
			while (rs.next()) {
				ReviewBoardShortcutBean reviewBoardBean = new ReviewBoardShortcutBean();
				reviewBoardBean.setU_id(rs.getInt(1));
				reviewBoardBean.setTitle(rs.getString(2));
				reviewBoardBean.setWriterName(rs.getString(3));
				reviewBoardBean.setCreatedDate(rs.getString(4));
				reviewBoardBean.setVisitCount(rs.getInt(5));
				reviewBoardBean.setLikeCount((rs.getInt(6)));
				reviewBoards.add(reviewBoardBean);
				count++;
				
				if (count == topN) {
					break;
				}
			}

			return new DBResultWithData<ArrayList<ReviewBoardShortcutBean>>(DBResult.SUCCESS, "해당하는 도서의 후기 게시글을 가져왔습니다.", reviewBoards);
		} catch (Exception e) {
			return new DBResultError("해당하는 도서 게시글을 가져오는 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(ptmt);
			CloseConnection(conn);
		}
		
		
	}

	// 후기 도서리뷰 게시글 결과를 가져옴
	public DBResult listReviewBoards(int pageNumber, PageOption pageOption) {
		ArrayList<ReviewBoardShortcutBean> boards = new ArrayList<ReviewBoardShortcutBean>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		int totalReviewBoardsCount = 0;
		int totalPageCount = 0;

		try {

			String sql = "select count(*) from t_review_board ";

			conn = database.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				totalReviewBoardsCount = rs.getInt(1);
			}

			totalPageCount = totalReviewBoardsCount % pageOption.getPageSize() == 0
					? totalReviewBoardsCount / pageOption.getPageSize()
					: totalReviewBoardsCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("도서 개수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(stmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select b.u_id, b.title, u.nick, to_char(b.created_date, 'YYYY-MM-DD'), b.visit_count, b.like_count"
					   + "  from t_review_board b join t_user u" 
					   + "    on b.user_uid = u.u_id" 
					   + " order by b.u_id desc";

			conn = database.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);

			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}

			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;

			if (rs.next()) {
				rs.absolute(startIdx);

				for (int i = 0; i < pageOption.getPageSize(); i++) {
					ReviewBoardShortcutBean reviewBoardBean = new ReviewBoardShortcutBean();
					reviewBoardBean.setU_id(rs.getInt(1));
					reviewBoardBean.setTitle(rs.getString(2));
					reviewBoardBean.setWriterName(rs.getString(3));
					reviewBoardBean.setCreatedDate(rs.getString(4));
					reviewBoardBean.setVisitCount(rs.getInt(5));
					reviewBoardBean.setLikeCount((rs.getInt(6)));
					boards.add(reviewBoardBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<ReviewBoardShortcutBean> page = new Page<ReviewBoardShortcutBean>(totalPageCount, pageNumber,
					pageOption, boards);
			return new DBResultWithData<Page<ReviewBoardShortcutBean>>(DBResult.SUCCESS, "도서 후기 게시글 목록 가져오기 성공!", page);
		} catch (Exception e) {
			return new DBResultError("도서 후기 게시글 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(stmt);
			CloseConnection(conn);
		}
	}

	// 후기 도서리뷰 게시글 결과를 가져옴
	public DBResult listRequestBoards(int pageNumber, PageOption pageOption) {
		ArrayList<RequestBoardShortcutBean> boards = new ArrayList<RequestBoardShortcutBean>();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		int totalReviewBoardsCount = 0;
		int totalPageCount = 0;

		try {

			String sql = "select count(*) from t_request_board ";

			conn = database.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				totalReviewBoardsCount = rs.getInt(1);
			}

			totalPageCount = totalReviewBoardsCount % pageOption.getPageSize() == 0
					? totalReviewBoardsCount / pageOption.getPageSize()
					: totalReviewBoardsCount / pageOption.getPageSize() + 1;

			if (totalPageCount <= 0) {
				totalPageCount = 1;
			}
		} catch (Exception e) {
			return new DBResultError("도서 개수 확인도중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(stmt);
			CloseConnection(conn);
		}

		try {
			String sql = "select b.u_id, b.title, u.nick, to_char(b.created_date, 'YYYY-MM-DD'), b.visit_count"
					  + "  from t_request_board b join t_user u" 
					  + "    on b.user_uid = u.u_id" 
					  + " order by b.u_id desc";

			conn = database.getConnection();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);

			// 예외처리 10개씩으로 마지막 페이지에서 보다가 50개씩 보기로 변경해버리는 경우 같은..
			if (totalPageCount < pageNumber) {
				pageNumber = totalPageCount;
			}

			int startIdx = (pageNumber - 1) * pageOption.getPageSize() + 1;

			if (rs.next()) {
				rs.absolute(startIdx);

				for (int i = 0; i < pageOption.getPageSize(); i++) {
					RequestBoardShortcutBean requestBoardBean = new RequestBoardShortcutBean();
					requestBoardBean.setU_id(rs.getInt(1));
					requestBoardBean.setTitle(rs.getString(2));
					requestBoardBean.setWriterName(rs.getString(3));
					requestBoardBean.setCreatedDate(rs.getString(4));
					requestBoardBean.setVisitCount(rs.getInt(5));
					boards.add(requestBoardBean);

					if (rs.isLast()) {
						break;
					}

					rs.next();
				}
			}

			Page<RequestBoardShortcutBean> page = new Page<RequestBoardShortcutBean>(totalPageCount, pageNumber,
					pageOption, boards);
			return new DBResultWithData<Page<RequestBoardShortcutBean>>(DBResult.SUCCESS, "도서 신청 게시글 목록 가져오기 성공!", page);
		} catch (Exception e) {
			return new DBResultError("도서 후기 게시글 목록 가져오기 중 오류가 발생하였습니다.", e);
		} finally {
			CloseResultSet(rs);
			CloseStatement(stmt);
			CloseConnection(conn);
		}
	}
}
