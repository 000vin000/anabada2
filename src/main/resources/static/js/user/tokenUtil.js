/**
 * 로컬스토리지 관련
 */
export function getAccessToken() {
  return localStorage.getItem("Token");
}

export function setAccessToken(token) {
  localStorage.setItem("Token", token);
}

export function removeAccessToken() {
  localStorage.removeItem("Token");
}

export function getToken() {
    return localStorage.getItem("Token");
}
