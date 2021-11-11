//작성자 : 윤정도

package constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 마이페이지 관련 상수 정의
//   1. 왼쪽 메뉴들
public class MyPageConstant {
	
	public static class MyPageMenu {
		
		private String menuName;
		private String url;
		
		public String getMenuName() {
			return menuName;
		}

		public String getUrl() {
			return url;
		}

		private MyPageMenu(String menuName, String url) {
			this.menuName = menuName;
			this.url = url;
		}
		
		static MyPageMenu createInstance(String menu, String url) {
			return new MyPageMenu(menu, url);
		}
	}
	
	public static final int MY_PAGE_BORROW_LIST = 1;
	public static final int MY_PAGE_BUY_LIST = 2;
	public static final int MY_PAGE_CHARGE_POINT_LOG = 3;
	public static final int MY_PAGE_USE_POINT_LOG = 4;
	public static final int MY_PAGE_CHANGE_ACCOUNT_INFO = 5;
	public static final int MY_PAGE_LEAVE = 6;
	
	@SuppressWarnings("serial")
	public static final Map<Integer, MyPageMenu> MY_PAGE_MAP =  
			Collections.unmodifiableMap(new HashMap<Integer, MyPageMenu>() {
	{
        put(MY_PAGE_BORROW_LIST, 			MyPageMenu.createInstance("대여 도서 목록", 	"menu-mypage-borrow-list.jsp"));
        put(MY_PAGE_BUY_LIST, 				MyPageMenu.createInstance("구매 도서 목록", 	"menu-mypage-buy-list.jsp"));
        put(MY_PAGE_CHARGE_POINT_LOG, 		MyPageMenu.createInstance("포인트 충전 내역", 	"menu-mypage-charge-point-log.jsp"));
        put(MY_PAGE_USE_POINT_LOG,			MyPageMenu.createInstance("포인트 사용 내역", 	"menu-mypage-use-point-log.jsp"));
        put(MY_PAGE_CHANGE_ACCOUNT_INFO, 	MyPageMenu.createInstance("회원 정보 변경", 	"menu-mypage-change-account-info-check.jsp"));
        put(MY_PAGE_LEAVE, 					MyPageMenu.createInstance("회원 탈퇴", 		"menu-mypage-leave.jsp"));
    }});
	
	// ==================================================================================================================
}
