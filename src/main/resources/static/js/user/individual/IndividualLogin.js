document.addEventListener("DOMContentLoaded", function () {
    console.log("login.js 로드됨");

    // 로그인 버튼 이벤트 연결
    document.getElementById("loginForm").addEventListener("submit", loginUser);

    // 쿼리 파라미터에서 error 읽기
    const params = new URLSearchParams(window.location.search);
    const errorMessage = params.get("error");

    // 에러 메시지가 있으면 팝업으로 출력
    if (errorMessage) {
        alert(decodeURIComponent(errorMessage));
    }
});

function loginUser(event) {
	event.preventDefault(); // 폼의 기본 제출 동작을 막음
	
    // 기존 토큰 제거
    localStorage.removeItem("Token");
    localStorage.removeItem("refreshToken");

    const userId = document.getElementById("userId").value;
    const userPw = document.getElementById("userPw").value;

    const loginData = {
        userId: userId,
        userPw: userPw
    };

    console.log("로그인 요청 데이터:", JSON.stringify(loginData));

    fetch("/login/individual/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        console.log("로그인 응답 데이터:", data);

        if (data.message) alert(data.message);

        if (data.accessToken) {
            localStorage.setItem("Token", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
			// 로그인 성공시 한시간짜리 토큰을 쿠키에 발행 - jhu
			document.cookie = `Token = ${data.accessToken}; path=/; max-age=3600`;
            console.log("토큰 저장 완료");

            // 관리자 권한 확인 후 페이지 이동
            fetch("/admin/dashboard", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + data.accessToken
                }
            })
            .then(res => {
                if (res.ok) {
                    console.log("관리자 권한 확인 완료, 이동 중...");
                    window.location.href = "/admin/dashboard";
                } else {
                    console.warn("권한 없음 또는 일반 유저, 홈으로 이동");
                    window.location.href = "/";
                }
            });
        } else {
            console.warn("accessToken 없음. 로그인 실패");
        }
    })
    .catch(error => {
        console.error("로그인 오류:", error);
        alert("로그인 오류: " + error.message);
    });
}
