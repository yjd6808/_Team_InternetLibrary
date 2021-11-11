<%@page import="database.manager.UserILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	UserBean userBean = (UserBean) session.getAttribute("user");
	
	if (userBean != null) {
		DBResult dbResult = UserILDBManager.getInstance().updateUser(userBean);
		session.setAttribute("user", userBean);
		dbResult.println();
		
		if (dbResult.getStatus() ==  DBResult.FAIL ||
			dbResult.getStatus() ==  DBResult.ERROR) {
			session.setAttribute("user", null);
		}
	}
%>