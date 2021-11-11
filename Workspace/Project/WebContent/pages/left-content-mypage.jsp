<%@page import="util.NumberParser"%>
<%@page import="java.util.Collections"%>
<%@page import="constant.MyPageConstant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.UserBean"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<%
	UserBean userBean  = (UserBean)session.getAttribute("user");
	int selectedMenu = NumberParser.tryParseInt(request.getParameter("selectedMenu"), -1); 

	if (userBean == null) {
		return;
	}
	
	if (selectedMenu == -1) {
		%>
		<<script type="text/javascript">
			alert('올바르지 않은 매개변수입니다.\n마이페이지 메뉴');
			location.href='mein-view.jsp';
		</script>
		<%
		return;
	}
	
	ArrayList<Integer> menus = new ArrayList<Integer>(MyPageConstant.MY_PAGE_MAP.keySet());
	Collections.sort(menus);
%>
<c:set var="selectedMenu" scope="page"  value="<%= selectedMenu %>"></c:set>
<c:set var="menuList"     scope="page"  value="<%= menus %>"></c:set>
<c:set var="menuMap"	  scope="page"  value="<%= MyPageConstant.MY_PAGE_MAP %>"></c:set>


<!-- 참고 : https://postitforhooney.tistory.com/entry/JSPJSTL-JSTL-foreach%EC%97%90%EC%84%9C%EC%9D%98-varStatus-%EC%86%8D%EC%84%B1-%EC%9D%B4%EC%9A%A9 -->
<div class="left-content">
	<c:forEach items="${ menuList }" var="item" varStatus="status">
		<c:set var="menu" value="${menuMap[item]}"></c:set>
		<c:choose>
			<c:when test="${ item == selectedMenu }">
				<button class="btn btn-normal" onclick="location.href='${menu.url}'">${ menu.menuName }</button>
			</c:when>
			<c:otherwise>
				<button class="btn btn-reverse" onclick="location.href='${menu.url}'">${ menu.menuName }</button>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
</div>