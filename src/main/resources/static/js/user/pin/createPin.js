/**
 * 2차 비번 최초 등록
 */
document.getElementById("pinForm").addEventListener("submit", async function (e) {
  e.preventDefault();
  const pin = document.getElementById("userPin").value;

  const res = await fetch("/user/pin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + localStorage.getItem("Token"),
    },
    body: JSON.stringify({ userPin: pin }),
  });

  if (res.ok) {
    document.getElementById("message").textContent = "2차 비밀번호가 등록되었습니다!";
    const next = new URLSearchParams(window.location.search).get("next") || "/";
    setTimeout(() => {
      window.location.href = next;
    }, 1000);
  } else {
    const text = await res.text();
    document.getElementById("message").textContent = "등록 실패: " + text;
  }
});
