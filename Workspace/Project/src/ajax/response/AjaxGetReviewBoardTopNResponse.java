//작성자 : 윤정도

package ajax.response;

import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonFormat;

import bean.ReviewBoardShortcutBean;

@JsonFormat (shape = JsonFormat.Shape.OBJECT)
public class AjaxGetReviewBoardTopNResponse extends AjaxResponse {
	ArrayList<ReviewBoardShortcutBean> boards;

	public ArrayList<ReviewBoardShortcutBean> getBoards() {
		return boards;
	}

	public void setBoards(ArrayList<ReviewBoardShortcutBean> boards) {
		this.boards = boards;
	}
}
