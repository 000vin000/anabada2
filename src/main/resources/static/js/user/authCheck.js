document.addEventListener("DOMContentLoaded", function () {
    console.log("authCheck.js 로드됨");

    const token = localStorage.getItem("Token"); // ✅ 토큰 키 통일
    console.log("저장된 토큰:", token); // ✅ 저장된 토큰 확인

    // ✅ 현재 페이지 URL 확인
    const currentPath = window.location.pathname;
    console.log("현재 페이지:", currentPath);

    // ✅ 로그인 없이 접근 가능한 페이지 목록
    const publicPages = ["/", "/login", "/join"];

    if (!token && !publicPages.includes(currentPath)) {
        console.warn("❌ 토큰 없음: 로그인 페이지로 이동");
        window.location.href = "/login"; // ✅ 로그인 필요 페이지에서만 리디렉트
        return;
    }

    if (token) {
		fetch("/userlogin/check", {
		    method: "GET",
		    headers: {
		        "Authorization": "Bearer " + token, // ✅ 여기에서 문제 있는지 확인
		        "Content-Type": "application/json"
		    }
		})
        .then(response => {
            console.log("로그인 체크 응답 상태:", response.status);
            if (!response.ok) {
                throw new Error("인증 실패");
            }
            return response.json();
        })
        .then(data => {
            console.log("로그인 체크 응답 데이터:", data);

            if (data.authenticated) {
                console.log("✅ 로그인 유지됨");
                document.getElementById("loginBtn").style.display = "none";
                document.getElementById("mypageBtn").style.display = "inline";
                document.getElementById("logoutBtn").style.display = "inline";
            } else {
                console.log("❌ 로그인 정보 없음");
                localStorage.removeItem("Token");

                // ✅ 로그인 페이지에서만 리디렉트
                if (!publicPages.includes(currentPath)) {
                    window.location.href = "/login";
                }
            }
        })
        .catch(error => {
            console.error("로그인 체크 오류:", error);
            localStorage.removeItem("Token");

            // ✅ 로그인 페이지에서만 리디렉트
            if (!publicPages.includes(currentPath)) {
                window.location.href = "/login";
            }
        });
    }
});
