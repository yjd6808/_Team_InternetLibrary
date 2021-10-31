//작성자 : 윤정도

package structure;

import java.util.List;

//안쓸것 같지만 만들어 놓음
public class MultipleObject {
	List<Object> items;
	
	public MultipleObject(List<Object> list) {
		items = list;
	}

	public List<Object> getItems() {
		return items;
	}

	public void setItems(List<Object> items) {
		this.items = items;
	}
	
	public <T> T at(int idx, Class<T> type) {
		return type.cast(items.get(idx)); 
	}
}
