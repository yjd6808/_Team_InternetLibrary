//작성자 : 윤정도

package bean;

import java.util.Date;

public class MyBookBean implements PageData {
	int book_uid;
	int user_uid;
	Date startDate;
	Date endDate;
	
	public int getBook_uid() {
		return book_uid;
	}
	public void setBook_uid(int book_uid) {
		this.book_uid = book_uid;
	}
	public int getUser_uid() {
		return user_uid;
	}
	public void setUser_uid(int user_uid) {
		this.user_uid = user_uid;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	// 영구제 도서인지
	public boolean isPermanent() {
		return endDate == null;
	}
	
	
	// 기간이 초과된 도서인지
	public boolean isExpired() {
		if (endDate != null) {
			return false;
		}
		
		return new Date(System.currentTimeMillis()).compareTo(endDate) > 0;
	}
}
