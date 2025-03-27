/**
 * 2차 비번 만들기 최초인증 
 */

document.getElementById("pinForm").addEventListener("submit", async function (e) {
  e.preventDefault();
  const pin = document.getElementById("userPin").value;

  const res = await fetch("/user/pin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + localStorage.getItem("Token") // ✅ accessToken → Token 으로 수정
    },
    body: JSON.stringify({ userPin: pin })
  });

  if (res.ok) {
    document.getElementById("message").textContent = "2차 비밀번호가 등록되었습니다!";
    setTimeout(() => {
      window.location.href = "/user/updateinfo.html";
    }, 1000);
  } else {
    const text = await res.text();
    document.getElementById("message").textContent = "등록 실패: " + text;
  }
});
