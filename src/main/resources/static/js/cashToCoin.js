function loadCurrentCash() {
    fetch("/api/coin/cashToCoin", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("Token")}`,
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("서버 응답 실패");
        }
        return response.json();
    })
    .then(data => {
        const currentCash = document.getElementById("currentCash");
		currentCash.innerHTML = "";
        if (data && data.goods && data.goods.goodsCash !== undefined) {
            console.log("사용자 데이터:", data);
            currentCash.innerHTML = `<p>보유 금액 : ${data.goods.goodsCash} 원</p>`;
        } else {
            currentCash.innerHTML = "<p>잔액 정보를 불러올 수 없습니다.</p>";
        }
    })
    .catch(error => {
        console.error("사용자 데이터 로딩 실패: ", error);
        document.getElementById("currentCash").innerHTML = "<p>잔액 정보를 불러올 수 없습니다.</p>";
        alert("잔액 정보를 가져오는 중 오류가 발생했습니다.");
    });
}
