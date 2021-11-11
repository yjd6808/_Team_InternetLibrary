//작성자 : 윤정도

package bean;

// 마이페이지 - 구매한 도서 목록
public class BuyBookShortcutBean implements PageData {
	int book_uid;
	String bookName;
	String buyDate;
	
	public int getBook_uid() {
		return book_uid;
	}
	public void setBook_uid(int book_uid) {
		this.book_uid = book_uid;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
}
