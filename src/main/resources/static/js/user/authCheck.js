import { checkAuth } from "./authService.js";
import { updateUI } from "./uiControl.js";

document.addEventListener("DOMContentLoaded", async function () {
  console.log("authCheck.js 시작됨");

  const isAuthenticated = await checkAuth();
  console.log("인증 상태:", isAuthenticated);

  updateUI(isAuthenticated);

  const currentPath = window.location.pathname;

 
  if (isAuthenticated && (currentPath === "/login" || currentPath === "/join")) {
    console.log("로그인 상태인데 로그인/회원가입 페이지 접근 → 홈으로 이동");
    window.location.href = "/";
  }

  //아래 조건 제거 (보안은 백엔드가 담당)
  // if (!isAuthenticated && !publicPages.includes(currentPath)) {
  //   window.location.href = "/auth/login.html";
  // }
});
