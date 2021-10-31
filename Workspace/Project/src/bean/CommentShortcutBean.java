//작성자 : 윤정도

package bean;

public class CommentShortcutBean implements PageData {
	int user_uid;
	int board_uid;
	int comment_uid;
	String name;
	String createdDate;
	String content;
	public int getUser_uid() {
		return user_uid;
	}
	public void setUser_uid(int user_uid) {
		this.user_uid = user_uid;
	}
	public int getBoard_uid() {
		return board_uid;
	}
	public void setBoard_uid(int board_uid) {
		this.board_uid = board_uid;
	}
	public int getComment_uid() {
		return comment_uid;
	}
	public void setComment_uid(int comment_uid) {
		this.comment_uid = comment_uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
