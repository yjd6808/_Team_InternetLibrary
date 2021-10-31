//작성자 : 윤정도

package util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
	public static boolean AddCookieInCurrentDirectory(HttpServletResponse response, String name, String value, int age) {
		try {
			Cookie newCookie = new Cookie(name, value);
			newCookie.setMaxAge(age);
			newCookie.setPath("/");	// 현재 디렉토리에 추가
			response.addCookie(newCookie);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Cookie GetCookieInCurrentDirectory(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			return null;
		}
		
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				return cookies[i];
			}
		}
		
		return null;
	}
	
	public static boolean RemoveCookieInCurrentDirectory(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		
		if (cookies == null) {
			return false;
		}
		
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				cookies[i].setPath("/");
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
				return true;
			}
		}
		
		return false;
	}
}
