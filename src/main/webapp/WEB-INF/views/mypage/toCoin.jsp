<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인으로 전환하기</title>
<style>
    body {
        font-family: 'Pretendard', 'Noto Sans KR', sans-serif;
        background-color: #f9fafb;
        margin: 0;
        padding: 30px;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    form#toCoin {
        background-color: #ffffff;
        padding: 30px 40px;
        border-radius: 16px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        width: 360px;
        box-sizing: border-box;
    }

    #currentCash {
        font-size: 18px;
        color: #333;
        margin-bottom: 24px;
        text-align: center;
        font-weight: 600;
    }

    p {
        font-size: 15px;
        color: #333;
        margin-bottom: 8px;
        font-weight: 500;
    }

    input[type="number"] {
        width: 100%;
        padding: 10px 12px;
        font-size: 14px;
        border: 1px solid #ddd;
        border-radius: 8px;
        box-sizing: border-box;
        margin-bottom: 20px;
    }

    input[type="submit"] {
        width: 100%;
        padding: 12px 0;
        font-size: 15px;
        background-color: #00b894;
        color: white;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-weight: 600;
        transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover {
        background-color: #019875;
    }
</style>

</head>
<body>
    <form id="toCoin" method="post" action="/changeChargeToCoin" onsubmit="return toCoinSubmit(event)">
        <!-- 잔액 표시 -->
        <div id="currentCash"></div>

        <!-- 전환할 금액 -->
        <div>
            <p>전환할 금액</p>
            <input type="number" id="changeCashToCoin" name="changeCashToCoin" min="0" step="1">
            <input type="submit" value="전환 신청">
        </div>
    </form>
</body>
<script src="../js/getCurrentCash.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("Token");

    if (!token) {
    	alert("로그인이 필요합니다.");
    	window.close();
    	return;
    }

    loadCurrentCash();
});

// 음수 및 소수점 입력 방지
document.getElementById("changeCashToCoin").addEventListener("input", function () {
    this.value = Math.max(0, Math.floor(this.value)); // 소수점 제거 및 음수 방지
});

// 폼 제출 
function toCoinSubmit(event) {
    event.preventDefault();

    let amount = document.getElementById("changeCashToCoin").value;
    if (amount <= 0) {
        alert("전환할 금액은 1원 이상이어야 합니다.");
        return false;
    }

    let token = localStorage.getItem("Token");
    let formData = new FormData(document.getElementById("toCoin"));

    fetch('/changeChargeToCoin', {
        method: 'POST',
        body: formData,
        headers: {
            'Authorization': 'Bearer ' + token,
        }
    })
    .then(response => response.json())
    .then(data => {
        alert(data.message || "전환 신청 실패");
        setTimeout(function() {
            document.getElementById("toCoin").submit();
            window.close();
        }, 500);
    })
    .catch(error => {
        alert("전환 실패: " + error);
    });

    return true;
}
</script>
</html>
