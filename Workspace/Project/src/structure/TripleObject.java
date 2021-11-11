//작성자 : 윤정도

package structure;

// https://stackoverflow.com/questions/450807/how-do-i-make-the-method-return-type-generic
// 자바는 제너릭 함수 이렇게 만든다.
public class TripleObject {
	Object item1;
	Object item2;
	Object item3;
	
	public <T> T getItem1(Class<T> type) {
	    return type.cast(item1);
	}
	
	public boolean isItem1Null() {
		return item1 == null;
	}
	
	public void setItem1(Object item1) {
		this.item1 = item1;
	}
	
	// =======================================
	
	public <T> T getItem2(Class<T> type) {
	    return type.cast(item2);
	}
	
	public boolean isItem2Null() {
		return item2 == null;
	}
	
	public void setItem2(Object item2) {
		this.item2 = item2;
	}
	
	// =======================================
	
	public <T> T getItem3(Class<T> type) {
	    return type.cast(item3);
	}
	
	public boolean isItem3Null() {
		return item3 == null;
	}
	
	public void setItem3(Object item3) {
		this.item3 = item3;
	}
}
