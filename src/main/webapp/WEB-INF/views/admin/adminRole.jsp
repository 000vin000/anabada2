<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>관리자 권한 설정</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        input, button { margin: 5px; padding: 8px; }
        #result { margin-top: 20px; }
        
         nav {
            margin-bottom: 20px;
        }
        nav ul {
            list-style: none;
            display: flex;
            justify-content: center;
            margin: 0;
            padding: 0;
        }
        nav ul li {
            margin: 0 15px;
        }
        nav ul li a {
            color: #007bff;
            text-decoration: none;
            font-size: 18px;
        }
        nav ul li a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
	 <nav>
        <ul>
            <li><a href="/admin/dashboard">재무관리</a></li> 
            <li><a href="/admin/management">고객관리</a></li>
            <li><a href="/admin/fees">수수료관리</a></li>
           	<li><a href="/admin/acceptConversion">코인 전환 신청</a></li>
            <li><a href="/admin/depositWithdrawal">입/출금 관리</a></li>
            <li><a href="/admin/adminRole">관리자 권한 설정</a></li>
        </ul>
    </nav>

    <h2>관리자 권한 설정</h2>

    <input type="text" id="searchUserId" placeholder="사용자 아이디 입력" />
    <button id="searchBtn">검색</button>

    <div id="result"></div>

    <script>
        document.getElementById("searchBtn").addEventListener("click", function () {
            const userId = document.getElementById("searchUserId").value.trim();
            console.log("입력된 아이디:", userId);

            if (!userId) {
                alert("아이디를 입력하세요.");
                return;
            }

            const url = "/admin/search?userId=" + encodeURIComponent(userId);
            console.log("요청 URL:", url);

            fetch(url)
                .then(res => {
                    if (!res.ok) {
                        throw new Error("사용자 없음");
                    }
                    return res.json(); // JSON 형태로 응답 받기
                })
                .then(user => {
                    console.log("받은 사용자 데이터:", user);

                    if (user && user.userId && user.userType) {
                        const resultDiv = document.getElementById("result");
                        console.log("resultDiv 존재 여부:", resultDiv);

                        // 기존 내용 초기화
                        resultDiv.innerHTML = '';

                        // 아이디와 권한을 p 요소로 추가
                        const userIdP = document.createElement("p");
                        userIdP.textContent = "아이디: " + (user.userId || '정보 없음');
                        resultDiv.appendChild(userIdP);

                        const userTypeP = document.createElement("p");
                        userTypeP.textContent = "현재 권한: " + (user.userType || '정보 없음');
                        resultDiv.appendChild(userTypeP);

                        // 권한 변경 버튼 추가
                        const changeRoleButton = document.createElement("button");
                        changeRoleButton.textContent = "권한 변경";
                        changeRoleButton.onclick = function () {
                            changeRole(user.userId, user.userType);
                        };
                        resultDiv.appendChild(changeRoleButton);
                    } else {
                        console.error("받은 데이터에 문제가 있습니다.", user);
                        alert("데이터 형식에 문제가 있습니다.");
                    }
                })
                .catch((err) => {
                    console.error("에러 발생:", err.message);
                    alert("사용자를 찾을 수 없습니다.");
                });
        });

        function changeRole(userId, currentRole) {
            // 사용자에게 확인 메시지 표시
            const isConfirmed = confirm("진짜로 권한을 변경하시겠습니까?");
            if (!isConfirmed) {
                return; // 사용자가 취소를 선택하면 아무 작업도 하지 않음
            }

            const newRole = currentRole === "ADMIN" ? "INDIVIDUAL" : "ADMIN";

            fetch("/admin/change-role", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ userId: userId, newRole: newRole })
            })
            .then(res => {
                if (!res.ok) {
                    throw new Error("변경 실패");
                }
                return res.text();
            })
            .then(msg => {
                alert(msg);
                location.reload();  // 권한 변경 후 페이지 새로고침
            })
            .catch((err) => {
                console.error("권한 변경 에러:", err.message);
                alert("권한 변경 중 오류가 발생했습니다.");
            });
        }

    </script>
</body>
</html>
