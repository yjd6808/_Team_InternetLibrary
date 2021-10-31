//작성자 : 윤정도

package util;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 시간계산관련 ..
// 검색할 시간에 만들어서 쓰는게 나을듯
public class TimeUtil {
	public static Date convertToDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	
	public static Timestamp convertToTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	public static Timestamp currentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Date currentDate() {
		return new Date(System.currentTimeMillis());
	}
	
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String formatNormal(Date date) {
		return new SimpleDateFormat("yyyy.MM.dd a h:mm:ss").format(date);
	}
	
	public static String formatNormalDate(Date date) {
		return new SimpleDateFormat("yyyy.MM.dd").format(date);
	}
	
	public static String formatNormalTime(Date date) {
		return new SimpleDateFormat("a h:mm:ss").format(date);
	}
	
	public final static int TYPE_DAY = 5;
	public final static int TYPE_HOUR = 4;
	public final static int TYPE_MIN = 3;
	public final static int TYPE_SEC = 2;
	public final static int TYPE_MILI = 1;
	
	public final static long UNIT_DAY = 1000L * 60 * 60 * 24;
	public final static long UNIT_HOUR = 1000L * 60 * 60;
	public final static long UNIT_MIN = 1000L * 60;
	public final static long UNIT_SEC = 1000L;
	public final static long UNIT_MILI = 1L;
	
	public static long getDay(long milisec) {
		return milisec / (UNIT_DAY);
	}
	
	public static long subtractDay(long milisec, long day) {
		return milisec - (UNIT_DAY * day);
	}
	
	public static long getHour(long milisec) {
		return milisec / (UNIT_HOUR);
	}
	
	public static long subtractHour(long milisec, long hour) {
		return milisec - (UNIT_HOUR * hour);
	}

	public static long getMinute(long milisec) {
		return milisec / (UNIT_MIN);
	}
	
	public static long subtractMinute(long milisec, long minute) {
		return milisec - (UNIT_MIN * minute);
	}
	
	public static long getSecond(long milisec) {
		return milisec / (UNIT_SEC);
	}
	
	public static long subtractSecond(long milisec, long second) {
		return milisec - (UNIT_SEC * second);
	}
	
	public static long addDay(long milisec, long day) {
		return milisec + (UNIT_DAY * day);
	}
	
	public static long addHour(long milisec, long hour) {
		return milisec + (UNIT_HOUR * hour);
	}
	
	public static long addMinute(long milisec, long min) {
		return milisec + (UNIT_MIN * min);
	}
	
	public static long addSecond(long milisec, long sec) {
		return milisec + (UNIT_SEC * sec);
	}
	
	public static long get(long milisec, int type) throws Exception {
		if (type < TYPE_MILI || type > TYPE_DAY) {
			throw new Exception("get() / 시간 타입이 올바르지 않습니다.");
		}
		
		switch (type) {
		case TYPE_DAY:
			return getDay(milisec);
		case TYPE_HOUR:
			return getHour(milisec);
		case TYPE_MIN:
			return getMinute(milisec);
		case TYPE_SEC:
			return getSecond(milisec);
		case TYPE_MILI:
			return milisec;
		}
		return milisec;
	}
	
	public static long subtract(long milisec, int type, long val) throws Exception {
		if (type < TYPE_MILI || type > TYPE_DAY) {
			throw new IllegalArgumentException("subtract() / 시간 타입이 올바르지 않습니다.");
		}
		
		switch (type) {
		case TYPE_DAY:
			return subtractDay(milisec, val);
		case TYPE_HOUR:
			return subtractHour(milisec, val);
		case TYPE_MIN:
			return subtractMinute(milisec, val);
		case TYPE_SEC:
			return subtractSecond(milisec, val);
		case TYPE_MILI:
			return milisec;
		}
		return milisec;
	}
	
	// type 까지 계산하여 반환함
	// ex)
	//  startType : MINUTE
	//    endType : HOUR
	//includeZero : true
	//    5343시간 49분 
	//       0시간 49분
	
	//  startType : MINUTE
	//    endType : HOUR
	//includeZero : false
	//    5343시간 49분 
	//            49분
	
	// startType이 endType보다 작거나 같은 타입이어야 함 (ex) startType : TYPE_HOUR, endType : TYPE_DAY)
	
	public static String parseDiff(long mili, int startType, int endType, boolean includeZero) throws Exception {
		
		if (startType < TYPE_MILI || startType > TYPE_DAY) {
			throw new IllegalArgumentException("startType 타입이 올바르지 않습니다.");
		}
		
		if (endType < TYPE_MILI || endType > TYPE_DAY) {
			throw new IllegalArgumentException("startType 타입이 올바르지 않습니다.");
		}
		
		if (startType > endType) {
			throw new IllegalArgumentException("startType이 endType보다 크면 안되요..");
		}
		
		StringBuilder builder = new StringBuilder(50);
		final Map<Integer, String> timeStringMap = new HashMap<Integer, String>();
		
		timeStringMap.put(TYPE_DAY, "일");
		timeStringMap.put(TYPE_HOUR, "시간");
		timeStringMap.put(TYPE_MIN, "분");
		timeStringMap.put(TYPE_SEC, "초");
		timeStringMap.put(TYPE_MILI, "`");
		
		for (int i = endType; i >= startType; i-- ) {
			long val = get(mili, i);
			
			
			if (val == 0 && includeZero) {
				builder.append(0 + timeStringMap.get(i) + " ") ;				
			} else if (val > 0) {
				builder.append(val + timeStringMap.get(i) + " ") ;
			}
			
			mili = subtract(mili, i, val);
		}
			
		return builder.toString();
	}
	
	// 테스트
	public static void main(String[] args) throws Exception {
		Date current = new Date(System.currentTimeMillis());
		Date after = new Date(System.currentTimeMillis() +
				UNIT_DAY * 0 + 
				UNIT_HOUR * 0 + 
				UNIT_MIN * 55 + 
				UNIT_SEC * 52 + 
				UNIT_MILI * 844);
		
		long diff = after.getTime() - current.getTime();
		System.out.println(parseDiff(diff, TYPE_MILI, TYPE_DAY, false));
	}
}
