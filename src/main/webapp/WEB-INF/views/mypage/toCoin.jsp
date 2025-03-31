<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코인으로 전환하기</title>
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
<script src="../js/cashToCoin.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {
    let token = localStorage.getItem("Token");

    if (!token) {
        document.getElementById("currentCash").innerText = "로그인 필요";
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
        }, 1000);
    })
    .catch(error => {
        alert("전환 실패: " + error);
    });

    return true;
}
</script>
</html>
