<%@page import="ajax.RegisterBookServlet"%>
<%@page import="util.HanConv"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@ page import="java.nio.file.Path"%>
<%@ page import="java.nio.file.Paths"%>
<%@ page import="java.io.File"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<jsp:include page="session-update.jsp"></jsp:include>
<!DOCTYPE html>
<%    
	String fileName = HanConv.toKor((String)request.getParameter("filepath"));
	String filePath = RegisterBookServlet.getFileUploadFullPath()  + '\\' +  fileName;
	
    try {
    	 
        // ������ �о� ��Ʈ���� ���
        File file = new File(filePath);
        
        if (!file.exists()) {
        	%>
        	<script type="text/javascript">
	        	alert('���� �����Ͱ� �������� �ʽ��ϴ�.');
	        	history.go(-1);
			</script>
        	<%
        	return;
        }
        
        FileInputStream in = new FileInputStream(file);
        OutputStream os = null;
 
        String client = request.getHeader("User-Agent");
 
        // ���� �ٿ�ε� ��� ����
        response.reset();
        response.setContentType("application/octet-stream");
        
        
        // ���ͳ� �ͽ��÷η�
        if(client.indexOf("MSIE") != -1){
        	fileName = new String(fileName.getBytes("KSC5601"),"ISO8859_1");
            response.setHeader ("Content-Disposition", "attachment; filename=" + fileName);

        // ũ��, ���ĸ� ��
        } else {
        	
        	// HanConv.toKor �Լ����� iso-8859-1 ���ڵ� �������� �о euc_kr �������� ���ڵ��� �ٲ�����
        	// �ݴ�� euc_kr ���ڵ��� iso-8859-1 ���ڵ����� �ٲ�����Ѵ�.
        	fileName = new String(fileName.getBytes("euc_kr"),"iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
        } 
         
        response.setHeader ("Content-Length", ""+file.length() );

        
 	    //�� ���ٷ� �������� ������ ������ �κ��� �ʱ�ȭ �ϴ� �۾��� �Ѵٰ� �����ϸ� �˴ϴ�.
        out.clear(); // ��Ʈ���� ����ϰ� ����. �׷� ��δ� ������� �� ��θ� ������ Ż���� �ʿ��ϰ���!
        out = pageContext.pushBody(); // jsp �������� ���� ����(�ٿ�ε�)�� �����ϴ� ����� �Ѵ�.
        
        /*
	         jsp���� �ٸ� jsp �� �ִ� �������� ȣ���ؼ� �ٿ�ε� ������ �����ϴ� ��� 
			  �̹� ��Ʈ���� ���� �ִ� ���� �Դϴ�. ���� �߰������� ��Ʈ���� ������ �ϸ� ���� ���� ���� �޼����� �����ϴ� ���Դϴ�.
        */
        
        os = response.getOutputStream();
        byte b[] = new byte[(int)file.length()];
        int leng = 0;
         
        while( (leng = in.read(b)) > 0 ){
            os.write(b, 0, leng);
        }
 
        in.close();
        os.flush();
        os.close(); 
 
    }catch(Exception e){
    	
    	// https://okky.kr/article/291313
    	if (!e.getClass().getName().equals("org.apache.catalina.connector.ClientAbortException")) {
    		e.printStackTrace();
    	}

    }
%>   