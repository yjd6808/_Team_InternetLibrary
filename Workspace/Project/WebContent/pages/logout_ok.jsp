<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<% 
	session.removeAttribute("user");
	response.sendRedirect("main-view.jsp");	
%>
