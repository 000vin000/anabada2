import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';
document.addEventListener("DOMContentLoaded", function () {
    const searchBtn = document.getElementById("searchBtn");

    searchBtn.addEventListener("click", function () {
        const userId = document.getElementById("searchUserId").value.trim();

        if (!userId) {
            alert("아이디를 입력하세요.");
            return;
        }

        fetchWithAuth("/admin/search?userId=" + encodeURIComponent(userId))
            .then(res => {
                if (!res.ok) throw new Error("사용자 없음");
                return res.json();
            })
            .then(user => {
                if (user && user.userId && user.userType) {
                    renderUserInfo(user);
                } else {
                    alert("데이터 형식에 문제가 있습니다.");
                }
            })
            .catch((err) => {
                console.error("에러 발생:", err.message);
                alert("사용자를 찾을 수 없습니다.");
            });
    });
});

function renderUserInfo(user) {
    const resultDiv = document.getElementById("result");
    resultDiv.innerHTML = '';

    const userIdP = document.createElement("p");
    userIdP.textContent = "아이디: " + (user.userId || '정보 없음');
    resultDiv.appendChild(userIdP);

    const userTypeP = document.createElement("p");
    userTypeP.textContent = "현재 권한: " + (user.userType || '정보 없음');
    resultDiv.appendChild(userTypeP);

    // 부서 선택 (ADMIN 전환 시만 사용됨)
    const adminDeptDiv = document.createElement("div");
    adminDeptDiv.id = "adminDeptDiv";
    adminDeptDiv.style.display = "none";

    const label = document.createElement("label");
    label.textContent = "관리자 부서 선택:";
    adminDeptDiv.appendChild(label);

    ["관리부서", "재무부서"].forEach(dept => {
        const radio = document.createElement("input");
        radio.type = "radio";
        radio.name = "adminDept";
        radio.value = dept;
        if (dept === "관리부서") radio.checked = true;

        adminDeptDiv.appendChild(radio);
        adminDeptDiv.appendChild(document.createTextNode(dept));
    });

    resultDiv.appendChild(adminDeptDiv);

    const changeRoleButton = document.createElement("button");
    changeRoleButton.textContent = "권한 변경";
    changeRoleButton.onclick = function () {
        changeRole(user.userId, user.userType);
    };
    resultDiv.appendChild(changeRoleButton);

    if (user.userType !== "ADMIN") {
        adminDeptDiv.style.display = "block";
    }
}

function changeRole(userId, currentRole) {
    const isConfirmed = confirm("진짜로 권한을 변경하시겠습니까?");
    if (!isConfirmed) return;

    const newRole = currentRole === "ADMIN" ? "INDIVIDUAL" : "ADMIN";
    const data = {
        userId: userId,
        newRole: newRole
    };

    if (newRole === "ADMIN") {
        const selectedDept = document.querySelector("input[name='adminDept']:checked");
        if (selectedDept) {
            data.adminDept = selectedDept.value;
        }
    }

    fetch("/admin/change-role", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("변경 실패");
            return res.json();
        })
        .then(response => {
            alert(response.message || "권한 변경 성공");
            location.reload();
        })
        .catch((err) => {
            console.error("권한 변경 에러:", err.message);
            alert("권한 변경 중 오류가 발생했습니다.");
        });
}
