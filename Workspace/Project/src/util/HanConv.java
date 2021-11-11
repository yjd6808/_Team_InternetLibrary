//작성자 : 윤정도

package util;

import java.io.UnsupportedEncodingException;
import java.net.*;

public class HanConv {
	public static String toKor(String str) {
		if (str == null || str.length() <= 0) {
			return "str is null";
		}
		
		try {
			return new String(str.getBytes("8859_1"), "euc_kr");
		} catch (UnsupportedEncodingException e) {
			return "euc_kr convert to fail";
		}
	}
	
	public static String toKor(String str, String getFormat, String outFormat) {
		if (str == null || str.length() <= 0) {
			return "str is null";
		}
		
		try {
			return new String(str.getBytes(getFormat), outFormat);
		} catch (UnsupportedEncodingException e) {
			return "euc_kr convert to fail";
		}
	}
	
	public static String toUrlEncoding(String str) {
	      if ((str==null) || (str.equals("null")) ) {
	         return str;
	      }
	      
	      try {
	         String result ="";
	         result = URLEncoder.encode(str, "euc-kr"); // �ѱ� �ѱ涧 
	         return result;
	      } catch (Exception e) {
	         e.printStackTrace();
	         return str;
	      }
	}
}
