/**
 *  정보 조회
 */
import { fetchWithAuth } from "./fetchWithAuth.js"; 

fetchWithAuth("/update/info", {
  method: "GET",
})
  .then(response => response.json()) 
  .then(user => {
    console.log("불러온 사용자 정보:", user);

    document.getElementById("userId").value = user.userId;
    document.getElementById("userEmail").value = user.userEmail;
    document.getElementById("userName").value = user.userName;
    document.getElementById("userNick").value = user.userNick;

    const phoneParts = user.userPhone.split("-");
    if (phoneParts.length === 3) {
      document.getElementById("phone1").value = phoneParts[0];
      document.getElementById("phone2").value = phoneParts[1];
      document.getElementById("phone3").value = phoneParts[2];
    }

    document.getElementById("baseAddress").value = user.baseAddress;
    document.getElementById("detailAddress").value = user.detailAddress;
  })
  .catch(error => {
    console.error("회원정보 조회 실패:", error);
  });
