/**
 * 정보수정
 */

import { fetchWithAuth } from "./fetchWithAuth.js";
import { getAccessToken } from "./tokenUtil.js";

document.getElementById("updateBtn").addEventListener("click", () => {
  const userNick = document.getElementById("userNick").value;
  const phone1 = document.getElementById("phone1").value;
  const phone2 = document.getElementById("phone2").value;
  const phone3 = document.getElementById("phone3").value;
  const userPhone = `${phone1}-${phone2}-${phone3}`;
  const baseAddress = document.getElementById("baseAddress").value;
  const detailAddress = document.getElementById("detailAddress").value;

  const updateData = {
    userNick,
    userPhone,
    baseAddress,
    detailAddress,
  };

  console.log("🔄 수정할 사용자 정보:", updateData);

  fetchWithAuth("/update/info", {
    method: "PUT", 
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(updateData),
  })
  .then(() => {
    alert("회원정보가 성공적으로 수정되었습니다.");
    window.location.href = "/mypage#"; 
    })
    .catch(error => {
      console.error("회원정보 수정 실패:", error);
      alert("회원정보 수정에 실패했습니다. 다시 시도해주세요.");
    });
});

