<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%
	 Enumeration<String> headerNames = request.getHeaderNames();
	while  (headerNames.hasMoreElements()) {
		String name = headerNames.nextElement();
		out.println(name + " : " + request.getHeader(name) + "<br>");
	}
%>