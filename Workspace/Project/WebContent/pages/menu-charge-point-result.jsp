<%@page import="util.NumberParser"%>
<%@page import="database.manager.UserILDBManager"%>
<%@page import="database.result.DBResult"%>
<%@page import="bean.UserBean"%>
<%@page import="java.util.function.BiConsumer"%>
<%@ page import="com.inicis.std.util.ParseUtil"%>
<%@ page import="com.inicis.std.util.SignatureUtil"%>
<%@ page import="com.inicis.std.util.HttpUtil"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="session-update.jsp"></jsp:include>

<script src="../js/jquery/jquery-3.6.0.js"></script>
<script type="text/javascript">


if (!String.prototype.format) {
	String.prototype.format = function() {
    	var args = arguments;
    	return this.replace(/{(\d+)}/g, function(match, number) { 
      		return typeof args[number] != 'undefined'
        		? args[number]
        		: match
      		;
   		});
  	};
}

function showResult(additionalMessage) {
    const code = $('#pay-result').data('code');
    const message = $('#pay-result').data('message');
    const tradeId = $('#pay-result').data('trade-id');
    const payMethod = $('#pay-result').data('pay-method');
    const payPrice = $('#pay-result').data('pay-price');
    const tradeNum = $('#pay-result').data('trade-num');
    const merchantData = $('#pay-result').data('merchant-data') + '';

    const parsedMerchantDatas = merchantData.split('-');

    const price = parsedMerchantDatas[0];
    const point = parsedMerchantDatas[1];
    const bonusPoint = parsedMerchantDatas[2];
    	
    alert("{0}({1})\n거래번호 : {2}\n결제방식 : {3}\n실제 결제 금액 : {4}\n주문번호 : {5}\n선택한 결제 금액 : {6}\n충전포인트 : {7}\n충전보너스포인트 : {8}\n------------------\n{9}"
    		.format(message, code, tradeId, payMethod, payPrice, tradeNum, price, point, bonusPoint, additionalMessage));
    location.href = 'main-view.jsp';
}

  </script>
	<%
		UserBean userBean = (UserBean) session.getAttribute("user");
		if (userBean == null) {
		%>
			<script type="text/javascript">
				alert('잘못된 접근입니다. (-1)');
				location.href='main-view.jsp';
			</script>
		<%
		return;
	}
		
		
			
	try {

	//#############################
	// 인증결과 파라미터 일괄 수신
	//#############################
	request.setCharacterEncoding("UTF-8");

	Map<String, String> paramMap = new Hashtable<String, String>();

	Enumeration elems = request.getParameterNames();

	String temp = "";

	while (elems.hasMoreElements()) {
		temp = (String) elems.nextElement();
		paramMap.put(temp, request.getParameter(temp));
	}
	
	

	//#####################
	// 인증이 성공일 경우만
	//#####################
	if ("0000".equals(paramMap.get("resultCode"))) {

		//############################################
		// 1.전문 필드 값 설정(***가맹점 개발수정***)
		//############################################

		String mid = paramMap.get("mid"); // 가맹점 ID 수신 받은 데이터로 설정
		String signKey = "SU5JTElURV9UUklQTEVERVNfS0VZU1RS"; // 가맹점에 제공된 키(이니라이트키) (가맹점 수정후 고정) !!!절대!! 전문 데이터로 설정금지
		String timestamp = SignatureUtil.getTimestamp(); // util에 의해서 자동생성
		String charset = "UTF-8"; // 리턴형식[UTF-8,EUC-KR](가맹점 수정후 고정)
		String format = "JSON"; // 리턴형식[XML,JSON,NVP](가맹점 수정후 고정)
		String authToken = paramMap.get("authToken"); // 취소 요청 tid에 따라서 유동적(가맹점 수정후 고정)
		String authUrl = paramMap.get("authUrl"); // 승인요청 API url(수신 받은 값으로 설정, 임의 세팅 금지)
		String netCancel = paramMap.get("netCancelUrl"); // 망취소 API url(수신 받은 값으로 설정, 임의 세팅 금지)
		String ackUrl = paramMap.get("checkAckUrl"); // 가맹점 내부 로직 처리후 최종 확인 API URL(수신 받은 값으로 설정, 임의 세팅 금지)		
		String merchantData = paramMap.get("merchantData"); // 가맹점 관리데이터 수신

		//#####################
		// 2.signature 생성
		//#####################
		Map<String, String> signParam = new HashMap<String, String>();

		signParam.put("authToken", authToken); // 필수
		signParam.put("timestamp", timestamp); // 필수

		// signature 데이터 생성 (모듈에서 자동으로 signParam을 알파벳 순으로 정렬후 NVP 방식으로 나열해 hash)
		String signature = SignatureUtil.makeSignature(signParam);

		String price = ""; // 가맹점에서 최종 결제 가격 표기 (필수입력아님)

		// 1. 가맹점에서 승인시 주문번호가 변경될 경우 (선택입력) 하위 연결.  
		// String oid = "";             

		//#####################
		// 3.API 요청 전문 생성
		//#####################
		Map<String, String> authMap = new Hashtable<String, String>();

		authMap.put("mid", mid); // 필수
		authMap.put("authToken", authToken); // 필수
		authMap.put("signature", signature); // 필수
		authMap.put("timestamp", timestamp); // 필수
		authMap.put("charset", charset); // default=UTF-8
		authMap.put("format", format); // default=XML
		//authMap.put("price" 		,price);		    // 가격위변조체크기능 (선택사용)

		System.out.println("##승인요청 API 요청##");

		HttpUtil httpUtil = new HttpUtil();

		try {
			//#####################
			// 4.API 통신 시작
			//#####################

			String authResultString = "";

			authResultString = httpUtil.processHTTP(authMap, authUrl);

			//############################################################
			//5.API 통신결과 처리(***가맹점 개발수정***)
			//############################################################

			String test = authResultString.replace(",", "&").replace(":", "=").replace("\"", "").replace(" ", "")
			.replace("\n", "").replace("}", "").replace("{", "");

			//out.println("<pre>"+authResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</pre>");

			Map<String, String> resultMap = new HashMap<String, String>();

			resultMap = ParseUtil.parseStringToMap(test); //문자열을 MAP형식으로 파싱

			resultMap.forEach(new BiConsumer<String, String>() {
				@Override
				public void accept(String t, String u) {
					System.out.println(t + " / " + u);
				}
			});
			
			%>
			<div id="pay-result" 
				data-code="<%=resultMap.get("resultCode")%>" 
				data-message="<%=resultMap.get("resultMsg")%>" 
				data-trade-id="<%=resultMap.get("tid")%>"
				data-pay-method="<%=resultMap.get("payMethod")%>" 
				data-pay-price="<%=resultMap.get("TotPrice")%>" 
				data-trade-num="<%=resultMap.get("MOID")%>"
				data-merchant-data="<%=merchantData%>"></div>
			<%

			/*************************  결제보안 강화 2016-05-18 START ****************************/
			Map<String, String> secureMap = new HashMap<String, String>();
			secureMap.put("mid", mid); //mid
			secureMap.put("tstamp", timestamp); //timestemp
			secureMap.put("MOID", resultMap.get("MOID")); //MOID
			secureMap.put("TotPrice", resultMap.get("TotPrice")); //TotPrice

			// signature 데이터 생성 
			String secureSignature = SignatureUtil.makeSignatureAuth(secureMap);
			/*************************  결제보안 강화 2016-05-18 END ****************************/

			if ("0000".equals(resultMap.get("resultCode")) && secureSignature.equals(resultMap.get("authSignature"))) { //결제보안 강화 2016-05-18
			/*****************************************************************************
				* 여기에 가맹점 내부 DB에 결제 결과를 반영하는 관련 프로그램 코드를 구현한다.  
				
				 [중요!] 승인내용에 이상이 없음을 확인한 뒤 가맹점 DB에 해당건이 정상처리 되었음을 반영함
						  처리중 에러 발생시 망취소를 한다.
				******************************************************************************/
				String[] splitedMerchantData = merchantData.split("-");
		
				int selectedPrice = Integer.parseInt(splitedMerchantData[0]);
				int selectedPoint = Integer.parseInt(splitedMerchantData[1]);
				int selectedBonusPoint = Integer.parseInt(splitedMerchantData[2]);
		
				DBResult dbResult = UserILDBManager.getInstance().chargePoint(
						userBean.getU_id(), 
						selectedPrice,
						selectedPoint + selectedBonusPoint);
				dbResult.println();
	
				if (dbResult.getStatus() == DBResult.SUCCESS) {
				%>
					<script type="text/javascript">
		       			showResult('포인트 충전 및 결제가 성공적으로 이뤄졌습니다.');
       				</script>
				<%
				} else {
				%>
					<script type="text/javascript">
						showResult('결제에는 성공하였으나\n포인트 충전에 실패하였습니다.\n결제를 취소합니다.(-2)');
	       			</script>
				<%
				
					String netcancelResultString = httpUtil.processHTTP(authMap, netCancel); // 망취소 요청 API url(고정, 임의 세팅 금지)
					out.println("## 망취소 API 결과 ##");
					// 취소 결과 확인
					out.println("<p>" + netcancelResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</p>");
				}
		
			} else {
			%>
				<script type="text/javascript">
					showResult('결제에 실패하였습니다. (-2)');
			   </script>
			<%
			}
			
			
		//throw new Exception("강제 Exception");
		} catch (Exception ex) {

			//####################################
			// 실패시 처리(***가맹점 개발수정***)
			//####################################
		
			//---- db 저장 실패시 등 예외처리----//
			System.out.println(ex);
		
			//#####################
			// 망취소 API
			//#####################
			String netcancelResultString = httpUtil.processHTTP(authMap, netCancel); // 망취소 요청 API url(고정, 임의 세팅 금지)
		
			out.println("## 망취소 API 결과 ##");
		
			// 취소 결과 확인
			out.println("<p>" + netcancelResultString.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</p>");
			
			%>
			   <script type="text/javascript">
			   		showResult('인증은 정상처리 되었으나 오류가 발생하였습니다. (-3)');
			   </script>
			<%
		}

	} else {

		//#############
		// 인증 실패시
		//#############
		
		%>
		   <script type="text/javascript">
		   		alert('잘못된 접근 입니다. (-4)');
				location.href='main-view.jsp';
			</script>
		<%

	}

	} catch (Exception e) {
		System.out.println(e);
	}
%>

	
