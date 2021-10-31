<%@page import="bean.UserBean"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.function.BiConsumer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.inicis.std.util.SignatureUtil"%>
<%@page import="java.util.*"%>
<%@page import="constant.PointChargeMenuConstant"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>

<%
	UserBean userBean = (UserBean) session.getAttribute("user");

	//=================================================================================================================
	// �̴Ͻý� ���� ����
	// =================================================================================================================
	String mid					= "INIpayTest";		// ������ ID(������ ������ ����)					
	
	//����
	String signKey			    = "SU5JTElURV9UUklQTEVERVNfS0VZU1RS";	// �������� ������ �� ǥ�� ����Ű(������ ������ ����)
	String timestamp			= SignatureUtil.getTimestamp();			// util�� ���ؼ� �ڵ�����

	String oid					= mid+"_"+SignatureUtil.getTimestamp();	// ������ �ֹ���ȣ(���������� ���� ����)
	String price				= "100";								// ��ǰ����(Ư����ȣ ����, ���������� ���� ����)

	String cardNoInterestQuota	= "11-2:3:,34-5:12,14-6:12:24,12-12:36,06-9:12,01-3:4";		// ī�� ������ ���� ����(���������� ���� ����)
	String cardQuotaBase		= "2:3:4:5:6:11:12:24:36";		// ���������� ����� �Һ� ������ ����

	String mKey = SignatureUtil.hash(signKey, "SHA-256");
	
	Map<String, String> signParam = new HashMap<String, String>();

	signParam.put("oid", oid); 					// �ʼ�
	signParam.put("price", price);				// �ʼ�
	signParam.put("timestamp", timestamp);		// �ʼ�

	// signature ������ ���� (��⿡�� �ڵ����� signParam�� ���ĺ� ������ ������ NVP ������� ������ hash)
	String signature = SignatureUtil.makeSignature(signParam);
	String siteDomain = "http://121.145.173.195:8087/pages"; //������ ������ �Է�
	// =================================================================================================================
%>

<c:set var="chargeMenu" scope="page" value="<%= PointChargeMenuConstant.PRICE_POINT_MAP %>"></c:set>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>���� ������</title>

<link rel="stylesheet" href="../css/bootstrap/bootstrap.css" />
<link rel="stylesheet" href="../css/header.css" type="text/css">
<link rel="stylesheet" href="../css/left-content-ranking.css" type="text/css">

</head>
<body>
	<%
		{ // ������ �ߺ��� ���� ���
		  // �ڹٽ�ũ��Ʈ���� ���� ���̺� Ȯ���� ���� �ڵ��Դϴ�.
			Iterator<Map.Entry<Integer, PointChargeMenuConstant.ChargePoint>> iterator = PointChargeMenuConstant.PRICE_POINT_MAP.entrySet().iterator();
		    while (iterator.hasNext()) {
		        Map.Entry<Integer, PointChargeMenuConstant.ChargePoint> entry = iterator.next();
		        %>
		        	<div id="charge-<%= entry.getKey() %>" 
		        			data-point="<%= entry.getValue().getPoint() %>"
		        			data-bonus-point="<%= entry.getValue().getBonusPoint() %>">
	       			</div>
		        <%
		    }
		}
	%>
	
	<!-- �α��� �Ǿ��ִ��� -->
	<div id="page-variables" data-is-login="<%= userBean != null ? 1 : 0 %>"></div>
	
	<div class="bg-white mx-auto p-3" style="width: 1250px;">
		<div class="header container-xxl g-0">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div class="container-xxl mt-2 p-0 w-100">
			<div class="main-content">
				<jsp:include page="left-content-ranking.jsp"></jsp:include>
				<div class="right-content d-flex flex-column gap-2" style="width: 100%">
					<h4 class="mt-3 mb-3 fw-bold ms-1">����Ʈ ����</h4>
					<div class="border-container px-4">
						<form id="charge-form" name="" method="POST">
							<table class="table">
								<thead>
									<tr>
										<td style="width: 10%"></td>
										<td style="width: 25%">����Ʈ</td>
										<td style="width: 25%">���ʽ� ����Ʈ</td>
										<td>���� �ݾ�</td>
									</tr>
								</thead>
								<tbody>
									<%
										ArrayList<Integer> keys = new ArrayList<Integer>( PointChargeMenuConstant.PRICE_POINT_MAP.keySet());
										Collections.sort(keys);
										
										for (int i = 0; i < keys.size(); i++) {
											int key = keys.get(i);
											PointChargeMenuConstant.ChargePoint val = PointChargeMenuConstant.PRICE_POINT_MAP.get(key);
											
											%>
											<tr role="button" id="choose_charge_idx_<%= i + 1 %>" onclick="chooseChargeTableRow_OnClick(this.id)">
												<td><input type="radio" name="choose_charge_idx" value="<%=key %>" /></td>
												<td><strong><%= String.format("%,d", val.getPoint()) %>P</strong></td>
												<td><strong><%= String.format("%,d", val.getBonusPoint()) %>P</strong></td>
												<td><strong><%= String.format("%,d", key) %>��</strong></td>
											</tr>
											<%
										}
									%>
								</tbody>
							</table>

							<div class="w-100 text-center mt-4">
								<button type="button" class="btn btn-normal py-1 px-3" onclick="charge()">�����ϱ�</button>
							</div>


							<!-- ���� �ʼ� ���� -->
							<c:if test="${user != null}">
								<input  style="width:100%;" type="hidden" name="goodname" value="�׽�Ʈ" >
								<input  style="width:100%;" type="hidden" name="buyername" value="<%= userBean.getName() %>" >
								<input  style="width:100%;" type="hidden" name="returnUrl" value="<%= siteDomain %>/menu-charge-point-relay.jsp" >
								
								<input  style="width:100%;" type="hidden" name="version" value="1.0" >
								<input  style="width:100%;" type="hidden" name="mid" value="<%=mid%>" >
								<input  style="width:100%;" type="hidden" name="oid" value="<%=oid%>" >
								<input  style="width:100%;" type="hidden" name="price" value="<%=price%>" >
								<input  style="width:100%;" type="hidden" name="currency" value="WON" >
								<input  style="width:100%;" type="hidden" name="buyertel" value="010-1234-5678" >
								<input  style="width:100%;" type="hidden" name="buyeremail" value="test@inicis.com" >
								<input  style="width:100%;" type="hidden" name="timestamp" value="<%=timestamp %>" >
								<input  style="width:100%;" type="hidden" name="signature" value="<%=signature%>" >
								<input  style="width:100%;" type="hidden" name="mKey" value="<%=mKey%>" >
								
								<!-- ���� �⺻ ���� -->
								<input  style="width:100%;" type="hidden" name="gopaymethod" value="" >
								<input  style="width:100%;" type="hidden" name="offerPeriod" value="20151001-20151231" >
								<input  style="width:100%;" type="hidden" name="acceptmethod" value="CARDPOINT:HPP(1):no_receipt:va_receipt:below1000" >
								
								<!-- ǥ�� �ɼ� -->
								<input  style="width:100%;" type="hidden" name="languageView" value="" >
								<input  style="width:100%;" type="hidden" name="charset" value="" >
								<input  style="width:100%;" type="hidden" name="payViewType" value="popup" >
								<input  style="width:100%;" type="hidden" name="closeUrl" value="<%=siteDomain%>/menu-charge-point-close.jsp" >
								<input  style="width:100%;" type="hidden" name="popupUrl" value="<%=siteDomain%>/menu-charge-point-popup.jsp" >
								
								<!-- ���� ���ܺ� �ɼ� -->
								<input  style="width:100%;" type="hidden" name="quotabase" value="<%=cardQuotaBase%>" >
								<input  style="width:100%;" type="hidden" name="ini_onlycardcode" value="" >
								<input  style="width:100%;" type="hidden" name="ini_cardcode" value="" >
								<input  style="width:100%;" type="hidden" name="ansim_quota" value="" >
								
								<!-- �ֹι�ȣ ���� -->
								<input  style="width:100%;" type="hidden" name="INIregno" value="" >
								
								<!-- �߰� ���� / ��ȣȭ ���� / �ѱ� �ȵ�-->
								<!-- ����ٰ� ����� ������ �־���� -->
								<input  style="width:100%;" type="hidden" name="merchantData" value="" >
							</c:if>
							
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>


	<jsp:include page="footer.jsp"></jsp:include>

	
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script src="../js/bootstrap/bootstrap.js"></script>
	<script src="../js/bootstrap/bootstrap.bundle.js"></script>
	<script src="../js/jquery/jquery-3.6.0.js"></script>
	<script src="../js/header.js" charset="utf-8"></script>
	<script type="text/javascript" src="https://stgstdpay.inicis.com/stdjs/INIStdPay.js" charset="UTF-8"></script>
	<script src="../js/menu-charge-point.js" charset="utf-8"></script>

</body>
</html>