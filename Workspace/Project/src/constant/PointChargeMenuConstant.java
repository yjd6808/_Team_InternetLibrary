//작성자 : 윤정도

package constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class PointChargeMenuConstant {


	// 포인트 충전, 보너스포인트
	public static class ChargePoint {
		int point;
		int bonusPoint;
		
		ChargePoint(int point, int bonusPoint) {
			super();
			this.point = point;
			this.bonusPoint = bonusPoint;
		}
		
		public int getPoint() {
			return point;
		}
		public void setPoint(int point) {
			this.point = point;
		}
		public int getBonusPoint() {
			return bonusPoint;
		}
		public void setBonusPoint(int bonusPoint) {
			this.bonusPoint = bonusPoint;
		}
	}
	
	
	// 1. 불변 맵 만드는 법
	//   	https://stackoverflow.com/questions/22636575/unmodifiablemap-java-collections-vs-immutablemap-google 
	// 2. 맵 초기화 동시에 선언하는 방법
	// 		https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
	
	@SuppressWarnings("serial")
	public static final Map<Integer, ChargePoint> PRICE_POINT_MAP =  
			Collections.unmodifiableMap(new HashMap<Integer, ChargePoint>() {
	{
        put(5000, new ChargePoint(5000, 0));
        put(10000, new ChargePoint(10000, 1000));
        put(20000, new ChargePoint(20000, 4000));
        put(50000, new ChargePoint(50000, 10000));
        put(100000, new ChargePoint(100000, 25000));
    }});
}
