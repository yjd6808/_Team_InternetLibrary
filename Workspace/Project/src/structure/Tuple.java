//작성자 : 윤정도

package structure;

public class Tuple<T1, T2> {
	T1 item1;
	T2 item2;
	
	public T1 getItem1() {
		return item1;
	}
	public void setItem1(T1 item1) {
		this.item1 = item1;
	}
	public T2 getItem2() {
		return item2;
	}
	public void setItem2(T2 item2) {
		this.item2 = item2;
	}
}
