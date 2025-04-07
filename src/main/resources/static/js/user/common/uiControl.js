/**
 * UI 처리 - 로그인 여부에 따라 버튼 보이기/숨기기
 */
export function updateUI(authenticated) {
  console.log("UI 업데이트 시작됨, 인증 상태:", authenticated);

  const loginBtn = document.getElementById("loginBtn");
  const joinBtn = document.getElementById("joinBtn");
  const logoutBtn = document.getElementById("logoutBtn");
  const mypageBtn = document.getElementById("mypageBtn");
  const itemUpload = document.getElementById("itemUpload");

  console.log("loginBtn DOM:", loginBtn);

  if (authenticated) {
    if (loginBtn) loginBtn.style.display = "none";
    if (joinBtn) joinBtn.style.display = "none";
    if (logoutBtn) logoutBtn.style.display = "inline-block";
    if (mypageBtn) mypageBtn.style.display = "inline-block";
    if (itemUpload) itemUpload.style.display = "block";
  } else {
    if (loginBtn) loginBtn.style.display = "inline-block";
    if (joinBtn) joinBtn.style.display = "inline-block";
    if (logoutBtn) logoutBtn.style.display = "none";
    if (mypageBtn) mypageBtn.style.display = "none";
    if (itemUpload) itemUpload.style.display = "none";
  }
}
