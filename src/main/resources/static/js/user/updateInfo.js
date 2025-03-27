/**
 * 정보수정
 */
const accessToken = localStorage.getItem("Token"); // ✅ accessToken 키 이름 수정

// 기본 정보 불러오기
window.onload = async () => {
  const res = await fetch("/user/pin/info", {
    method: "GET",
    headers: {
      "Authorization": "Bearer " + accessToken
    }
  });

  if (res.ok) {
    const data = await res.json();
    document.getElementById("userName").value = data.userName;
    document.getElementById("userNick").value = data.userNick;
    document.getElementById("userPhone").value = data.userPhone;
    document.getElementById("userAddress").value = data.userAddress;
  } else {
    document.getElementById("message").textContent = "회원 정보를 불러올 수 없습니다.";
  }
}

// 정보 수정 요청
document.getElementById("updateForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const payload = {
    userName: document.getElementById("userName").value,
    userNick: document.getElementById("userNick").value,
    userPhone: document.getElementById("userPhone").value,
    userAddress: document.getElementById("userAddress").value
  };

  const res = await fetch("/update/info", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + accessToken
    },
    body: JSON.stringify(payload)
  });

  if (res.ok) {
    document.getElementById("message").textContent = "회원정보가 성공적으로 수정되었습니다.";
  } else {
    document.getElementById("message").textContent = "수정에 실패했습니다.";
  }
});
