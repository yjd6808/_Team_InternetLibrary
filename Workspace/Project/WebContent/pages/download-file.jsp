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
    	 
        // 파일을 읽어 스트림에 담기
        File file = new File(filePath);
        
        if (!file.exists()) {
        	%>
        	<script type="text/javascript">
	        	alert('파일 데이터가 존재하지 않습니다.');
	        	history.go(-1);
			</script>
        	<%
        	return;
        }
        
        FileInputStream in = new FileInputStream(file);
        OutputStream os = null;
 
        String client = request.getHeader("User-Agent");
 
        // 파일 다운로드 헤더 지정
        response.reset();
        response.setContentType("application/octet-stream");
        
        
        // 인터넷 익스플로러
        if(client.indexOf("MSIE") != -1){
        	fileName = new String(fileName.getBytes("KSC5601"),"ISO8859_1");
            response.setHeader ("Content-Disposition", "attachment; filename=" + fileName);

        // 크롬, 사파리 등
        } else {
        	
        	// HanConv.toKor 함수에서 iso-8859-1 인코딩 형식으로 읽어서 euc_kr 형식으로 인코딩을 바꿨으니
        	// 반대로 euc_kr 인코딩을 iso-8859-1 인코딩으로 바꿔줘야한다.
        	fileName = new String(fileName.getBytes("euc_kr"),"iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
        } 
         
        response.setHeader ("Content-Length", ""+file.length() );

        
 	    //이 두줄로 페이지의 정보를 보내는 부분을 초기화 하는 작업을 한다고 생각하면 됩니다.
        out.clear(); // 스트림을 깔끔하게 비운다. 그럼 통로는 비웠으니 그 통로를 지나갈 탈것이 필요하겠죠!
        out = pageContext.pushBody(); // jsp 페이지에 대한 정보(다운로드)를 저장하는 기능을 한다.
        
        /*
	         jsp에서 다른 jsp 에 있는 페이지를 호출해서 다운로드 로직을 실행하는 경우 
			  이미 스트림이 열려 있는 상태 입니다. 따라서 추가적으로 스트림을 열려고 하면 위와 같은 에러 메세지를 생성하는 것입니다.
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