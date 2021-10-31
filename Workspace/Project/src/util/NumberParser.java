//작성자 : 윤정도

package util;

public class NumberParser {
	
	public static float tryParseFloat(String source, float defaultValue) {
		try {
			return Float.parseFloat(source);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static int tryParseInt(String source, int defaultValue) {
		try {
			return Integer.parseInt(source);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}
