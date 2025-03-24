import { checkAuth } from "./authService.js";
import { updateUI } from "./uiControl.js";

document.addEventListener("DOMContentLoaded", async function () {
  console.log("authCheck.js 시작됨");

  const isAuthenticated = await checkAuth();
  console.log("인증 상태:", isAuthenticated);

  updateUI(isAuthenticated);

  const currentPath = window.location.pathname;
  const publicPages = ["/", "/login", "/join"];

  if (!isAuthenticated && !publicPages.includes(currentPath)) {
    console.warn("비로그인 상태 - 로그인 페이지로 이동");
    window.location.href = "/login";
  }

  if (isAuthenticated && (currentPath === "/login" || currentPath === "/join")) {
    console.log("로그인 상태인데 로그인/회원가입 페이지 접근 → 홈으로 이동");
    window.location.href = "/";
  }
});
