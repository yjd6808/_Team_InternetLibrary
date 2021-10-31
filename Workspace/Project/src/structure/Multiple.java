//작성자 : 윤정도

package structure;

import java.util.List;

// 안쓸것 같지만 만들어 놓음
public class Multiple<T> {
	List<T> items;
	
	public Multiple(List<T> list) {
		items = list;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
