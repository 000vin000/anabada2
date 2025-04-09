function getUserIdFromToken(token) {
    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.userId;
    } catch (e) {
        return null;
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("Token");

    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "/error/noAdmin";
        return;
    }

    // ✅ 관리자 권한 사전 확인
    fetch("/admin/check-auth", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("권한 없음");
        }
    })
    .catch(() => {
        window.location.href = "/error/noAdmin";
    });

    // ✅ 검색 버튼 클릭 이벤트
    const searchBtn = document.getElementById("searchBtn");

    searchBtn.addEventListener("click", function () {
        const userId = document.getElementById("searchUserId").value.trim();
        if (!userId) {
            alert("아이디를 입력하세요.");
            return;
        }

        fetch(`/admin/search?userId=${encodeURIComponent(userId)}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        })
            .then(response => {
                if (response.status === 401 || response.status === 403 || response.status === 204) {
                    alert("관리자 권한이 없습니다.");
                    window.location.href = "/error/noAdmin";
                    return;
                }
                return response.json();
            })
            .then(user => {
                if (user && user.userId && user.userType) {
                    renderUserInfo(user);
                } else {
                    alert("사용자 정보가 올바르지 않습니다.");
                }
            })
            .catch(err => {
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
    const confirmChange = confirm("정말 권한을 변경하시겠습니까?");
    if (!confirmChange) return;

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

    const token = localStorage.getItem("Token");
    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "/login"; // 로그인 페이지로 이동
        return;
    }

    const loggedInUserId = getUserIdFromToken(token); // 현재 로그인한 사용자 ID

    fetch("/admin/change-role", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error("권한 변경 실패");
        return response.json();
    })
    .then(res => {
        alert(res.message || "권한 변경 성공");
		
		if (res.selfChange) {
		       alert("본인 권한이 변경되어 로그아웃됩니다.");
		       localStorage.removeItem("Token");
		       window.location.href = "/"; // 로그인 페이지로 리다이렉트
        } else {
            location.reload(); // 다른 사람이면 페이지 새로고침
        }
    })
    .catch(err => {
        console.error("권한 변경 에러:", err.message);
        alert("권한 변경 중 오류가 발생했습니다.");
    });
}
