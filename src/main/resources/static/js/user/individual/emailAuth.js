async function sendAuthCode() {
  const userEmail = document.getElementById("userEmail").value;
  const sendBtn = document.getElementById("sendCodeButton");
  const resultBox = document.getElementById("authResult");

  if (!userEmail) {
    alert("이메일을 입력해주세요.");
    return;
  }

  try {
    const response = await fetch(`/auth/email/send?email=${encodeURIComponent(userEmail)}`, {
      method: "POST"
    });

    const message = await response.text();

    if (response.ok) {
      resultBox.innerText = "인증번호가 발송되었습니다.";
      resultBox.style.color = "green";

      let count = 60;
      sendBtn.disabled = true;
      sendBtn.innerText = `${count}초 후 재전송 가능`;

      const timer = setInterval(() => {
        count--;
        sendBtn.innerText = `${count}초 후 재전송 가능`;

        if (count <= 0) {
          clearInterval(timer);
          sendBtn.disabled = false;
          sendBtn.innerText = "인증번호 받기";
        }
      }, 1000);
    } else {
      resultBox.innerText = "인증번호 전송 실패: " + message;
      resultBox.style.color = "red";
    }
  } catch (error) {
    resultBox.innerText = "요청 중 오류 발생";
    resultBox.style.color = "red";
    console.error(error);
  }
}

async function verifyAuthCode() {
  const userEmail = document.getElementById("userEmail").value;
  const authCode = document.getElementById("authCode").value;
  const resultBox = document.getElementById("authResult");

  if (!userEmail || !authCode) {
    alert("이메일과 인증번호를 모두 입력해주세요.");
    return;
  }

  try {
    const response = await fetch("/auth/email/verify", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email: userEmail, code: authCode })
    });

    const message = await response.text();

    if (response.ok) {
      resultBox.innerText = "인증 완료되었습니다. 다음 단계로 이동합니다.";
      resultBox.style.color = "green";


      localStorage.setItem("verifiedEmail", userEmail);
      setTimeout(() => {
		location.href = "/auth/join/individual/IndividualJoin.html";
      }, 1500);
    } else {
      resultBox.innerText = "인증 실패: " + message;
      resultBox.style.color = "red";
    }
  } catch (error) {
    resultBox.innerText = "서버 요청 중 오류가 발생했습니다.";
    resultBox.style.color = "red";
    console.error(error);
  }
}

document.getElementById("sendCodeButton").addEventListener("click", sendAuthCode);
document.getElementById("verifyCodeButton").addEventListener("click", verifyAuthCode);
