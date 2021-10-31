//작성자 : 윤정도

package ajax.response;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxLikeBoardResponse extends AjaxResponse {
	int user_uid;
	int board_uid;
	int totalLikeCount;
	
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
	public int getTotalLikeCount() {
		return totalLikeCount;
	}
	public void setTotalLikeCount(int totalLikeCount) {
		this.totalLikeCount = totalLikeCount;
	}
}
