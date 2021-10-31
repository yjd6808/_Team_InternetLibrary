//작성자 : 윤정도

package bean;

import java.util.Date;

public class CommentBean {
	private int u_id;
	private int board_uid;
	private int user_uid;
	private int boardType;
	private String content;
	private Date createdDate;
	
	
	
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public int getBoard_uid() {
		return board_uid;
	}
	public void setBoard_uid(int board_uid) {
		this.board_uid = board_uid;
	}
	public int getUser_uid() {
		return user_uid;
	}
	public void setUser_uid(int user_uid) {
		this.user_uid = user_uid;
	}
	public int getBoardType() {
		return boardType;
	}
	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
