//작성자 : 윤정도

package structure;

public class Triple<T1, T2, T3> {
	T1 item1;
	T2 item2;
	T3 item3;
	
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
	public T3 getItem3() {
		return item3;
	}
	public void setItem3(T3 item3) {
		this.item3 = item3;
	}
}
