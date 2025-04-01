document.addEventListener("DOMContentLoaded", function () {
    checkAdminAndGetList();
});

// 관리자 검증
function checkAdminAndGetList() {
    const token = localStorage.getItem("Token");

    if (!token) {
        window.location.href = "/error/noAdmin"; 
        return;
    }

    fetch("/api/admin/conversionList", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
        }
    })
    .then(response => {
        if (response.status === 401) {
            window.location.href = "/error/noAdmin"; 
            return;
        }
        if (response.status === 204) {
            window.location.href = "/error/noAdmin"; 
            return;
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            console.log(data);
            showToCoinConversionList(data);
            showToCashConversionList(data);
        }
    })
    .catch(error => {
        console.error("Fetch error: ", error);
    });
}


// ✅ 코인 전환 리스트 표시
function showToCoinConversionList(data) {
    const tbody = document.querySelector("#acceptConversionToCoin tbody");
    tbody.innerHTML = "";

    data.conList
        .filter(conversion => conversion.conversionType === "TOCOIN")
        .forEach(conversion => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${conversion.conversionNo}</td>
                <td>${formatDate(conversion.conversionReqAt)}</td>
                <td>${conversion.userNo?.userId || "알 수 없음"}</td> <!-- ✅ 안전한 접근 -->
                <td>${conversion.conversionAmount}</td>
                <td><button onclick="acceptConversion(${conversion.conversionNo})">수락</button></td>
            `;

            tbody.appendChild(row);
        });
}

// ✅ 현금 전환 리스트 표시
function showToCashConversionList(data) {
    const tbody = document.querySelector("#acceptConversionToCash tbody");
    tbody.innerHTML = "";

    data.conList
        .filter(conversion => conversion.conversionType === "TOCASH")
        .forEach(conversion => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${conversion.conversionNo}</td>
                <td>${formatDate(conversion.conversionReqAt)}</td>
                <td>${conversion.userNo?.userId || "알 수 없음"}</td> <!-- ✅ 안전한 접근 -->
                <td>${conversion.conversionAmount}</td>
                <td><button onclick="acceptConversion(${conversion.conversionNo})">수락</button></td>
            `;

            tbody.appendChild(row);
        });
}

// ✅ 전환 수락 처리
function acceptConversion(conversionNo) {
    const token = localStorage.getItem("Token");

    fetch(`/api/admin/acceptConversion/${conversionNo}`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("전환 승인 실패");
        }
        return response.json();
    })
    .then(data => {
        alert(data.message || "전환이 승인되었습니다.");
        checkAdminAndGetList(); // ✅ 목록 새로고침
    })
    .catch(error => {
        console.error("전환 승인 에러:", error);
    });
}

// ✅ 날짜 형식 변환 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}
