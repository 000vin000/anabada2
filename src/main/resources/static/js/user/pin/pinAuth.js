/**
 * 2차 비번 인증
 */

document.getElementById("pinForm").addEventListener("submit", async function (e) {
  e.preventDefault();
  const pin = document.getElementById("userPin").value;

  const res = await fetch("/user/pin/check", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + localStorage.getItem("Token") 
    },
    body: JSON.stringify({ userPin: pin })
  });

  if (res.ok) {
	window.location.href = "/user/update/individual/IndividualUpdateinfo.html";
  } else {
    document.getElementById("message").textContent = "2차 비밀번호가 일치하지 않습니다.";
  }
});
