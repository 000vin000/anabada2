<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고하기</title>
<link rel="stylesheet" type="text/css" href="/css/reportUser.css">
</head>
<body>
	<!-- 불러오기 : 신고당한 사람, 신고한 위치, 아이템 번호 -->
	<input type="text" id="warnDefendantUser" name="warnDefendantUser" readonly>
	<input type="hidden" id="warnWhere" name="warnWhere">
	<input type="hidden" id="warnItem" name="warnItem">
	
	<!-- 입력받기 : 신고 사유, 신고사유 상세 -->
	<h2>신고 사유 선택</h2>
	<section>
		<label><input type="radio" name="warnReason" value="SPAM" onclick="showDescription('spam')" checked>스팸홍보/도배글</label>
		<label><input type="radio" name="warnReason" value="PORNOGRAPHY" onclick="showDescription('pornography')">음란물</label>
		<label><input type="radio" name="warnReason" value="ILLEGALITY" onclick="showDescription('illegality')">불법정보 포함</label>
		<label><input type="radio" name="warnReason" value="HARM" onclick="showDescription('harm')">청소년에게 유해한 내용</label>
		<label><input type="radio" name="warnReason" value="ABUSE" onclick="showDescription('abuse')">욕설/비방 표현</label>
		<label><input type="radio" name="warnReason" value="PRIVACY" onclick="showDescription('privacy')">개인정보 노출 게시물</label>
		<label><input type="radio" name="warnReason" value="OTHER" onclick="showDescription('other')">기타</label>
   	</section>
   	
   	<!-- 설명 -->
	<div id="spam" class="description" style="display: block;">
		<ul>
			<li>사행성 오락이나 도박을 홍보하거나 권장하는 내용 등의 부적절한 스팸 홍보 행위</li>
			<li>동일하거나 유사한 내용 반복 게시</li>
		</ul>
	</div>
	<div id="pornography" class="description">
		<ul>
			<li>성적 수치심을 일으키는 내용</li>
			<li>아동이나 청소년을 성 대상화한 표현</li>
			<li>과도하거나 의도적인 신체 노출</li>
			<li>음란한 행위와 관련된 부적절한 내용</li>
		</ul>
	</div>
	<div id="illegality" class="description">
		<ul>
			<li>불법 행위, 불법 링크에 대한 정보 제공</li>
			<li>불법 상품을 판매하거나 유도하는 내용</li>
		</ul>
	</div>
	<div id="harm" class="description">
		<ul>
			<li>가출/왕따/학교폭력/자살 등 청소년에게 부정적인 영향을 조성하는 내용</li>
		</ul>
	</div>
	<div id="abuse" class="description">
		<ul>
			<li>신체, 외모, 취향 등 경멸의 의미로 비하하는 내용</li>
			<li>계층, 지역, 종교, 성별 등을 근거없이 차별하거나 혐오하는 내용</li>
			<li>죽음을 비아냥거리거나 비하하는 내용</li>
		</ul>
	</div>
	<div id="privacy" class="description">
		<ul>
			<li>법적으로 중요한 타인의 개인정보를 게재</li>
			<li>당사자 동의 없는 특정 개인을 인지할 수 있는 정보</li>
		</ul>
	</div>
	<div id="other" class="description">
		<label for="warnReasonDetail">상세 내용</label>
		<textarea class="warnReasonDetail" id="warnReasonDetail" name="warnReasonDetail" rows="4" cols="50"></textarea>
	</div>
	
	<button id="submitReport">신고하기</button>
	<button onclick="window.close()">닫기</button>
</body>
<script defer src="/js/admin/report.js"></script>
<script>
	function showDescription(id) {
	    const descriptions = document.querySelectorAll('.description');
	    descriptions.forEach(desc => desc.style.display = 'none');
	
	    const selected = document.getElementById(id);
	    if (selected) {
	        selected.style.display = 'block';
	    }
	}
</script>
</html>