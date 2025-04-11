import { fetchWithAuth } from "/js/user/common/fetchWithAuth.js";

document.addEventListener("DOMContentLoaded", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const email = urlParams.get("email");
  const name = urlParams.get("name");

  document.getElementById("userEmail").value = email || "";
  document.getElementById("userName").value = name || "";

  const form = document.getElementById("socialJoinForm");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const userNick = document.getElementById("userNick").value.trim();
    const phone1 = document.getElementById("phone1").value.trim();
    const phone2 = document.getElementById("phone2").value.trim();
    const phone3 = document.getElementById("phone3").value.trim();
    const userPhone = `${phone1}-${phone2}-${phone3}`;

    const baseAddress = document.getElementById("baseAddress").value.trim();
    const detailAddress = document.getElementById("detailAddress").value.trim();

    const userEmail = document.getElementById("userEmail").value;
    const userName = document.getElementById("userName").value;

    // 유효성 검사
    if (userNick.length < 2) {
      alert("닉네임은 2자 이상 입력해주세요.");
      return;
    }

    const phoneRegex = /^010-\d{4}-\d{4}$/;
    if (!phoneRegex.test(userPhone)) {
      alert("전화번호 형식이 올바르지 않습니다.");
      return;
    }

    if (!baseAddress || !detailAddress) {
      alert("주소를 정확히 입력해주세요.");
      return;
    }


    const data = {
      userEmail,
      userName,
      userNick,
      userPhone,
      baseAddress,       
      detailAddress,     
      userType: "INDIVIDUAL"
    };

    try {
      const res = await fetchWithAuth("/user/social/join", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });

      if (res.ok) {
        const result = await res.json();
        if (result.token) {
          localStorage.setItem("Token", result.token);
        }
        alert("소셜 회원가입이 완료되었습니다.");
        window.location.href = "/user/mypage";
      } else {
        const err = await res.text();
        alert("회원가입 실패: " + err);
      }
    } catch (error) {
      console.error("오류 발생:", error);
      alert("서버 오류가 발생했습니다.");
    }
  });
});
