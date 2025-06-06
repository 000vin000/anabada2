/**
 요청할 때 Access Token을 자동으로 헤더에 붙이고
 401이면 /auth/reissue로 재발급 시도
 */

import { getAccessToken, setAccessToken, removeAccessToken } from "./tokenUtil.js";

export async function fetchWithAuth(url, options = {}) {
  const token = getAccessToken();

  const headers = options.headers || {};
  headers["Authorization"] = "Bearer " + token;
  headers["Content-Type"] = "application/json";
  options.headers = headers;

  let response = await fetch(url, options);

  if (response.status === 401) {
    console.warn("Access Token 만료됨 재발급 시도");

    const reissueRes = await fetch("/auth/reissue", { method: "POST" });

    if (reissueRes.ok) {
      const { accessToken } = await reissueRes.json();
      setAccessToken(accessToken);
      headers["Authorization"] = "Bearer " + accessToken;

      // 재시도
      response = await fetch(url, options);
    } else {
      console.warn("Refresh Token도 만료 → 로그아웃 처리");
      removeAccessToken();
      window.location.href = "/login";
    }
  }
  
  return response;
}

export async function fetchWithoutRedirect(url, options = {}) {
	const token = getAccessToken();
	const headers = options.headers || {};

	if (token) {
		headers["Authorization"] = "Bearer " + token;
	}

	headers["Content-Type"] = "application/json";
	options.headers = headers;

	let response = await fetch(url, options);

	if (response.status === 401 && token) {
		console.warn("Access Token 만료됨 재발급 시도");
		const reissueRes = await fetch("/auth/reissue", { method: "POST" });

		if (reissueRes.ok) {
			const { accessToken } = await reissueRes.json();
			setAccessToken(accessToken);
			
			const newHeaders = { ...headers }; // 기존 헤더 복사
			newHeaders["Authorization"] = "Bearer " + accessToken;
			options.headers = newHeaders; // 옵션 객체 직접 수정

			response = await fetch(url, options);
		} else {
			console.warn("Refresh Token도 만료 (fetchApiData)");
			removeAccessToken();
		}
	}
	
	return response;
}