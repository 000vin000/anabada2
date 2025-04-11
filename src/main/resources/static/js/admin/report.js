window.addEventListener("DOMContentLoaded", () => {
	const token = localStorage.getItem("Token");

	if (!token) {
		alert("접근 권한이 없습니다. 로그인 후 다시 시도해주세요.");
		window.close();
		return;
	}

	// 새로고침 방지
	const preventRefresh = (e) => {
		e.preventDefault();
		e.returnValue = "";
	};
	window.addEventListener("beforeunload", preventRefresh);

	// 데이터 받아오기
	window.addEventListener("message", (event) => {
		if (event.origin !== window.location.origin) return;

		const { warnWhere, warnDefendantUser, warnItem } = event.data;

		document.querySelector("input[name='warnWhere']").value = warnWhere || '';
		document.querySelector("input[name='warnDefendantUser']").value = warnDefendantUser || '';
		document.querySelector("input[name='warnItem']").value = warnItem || '';

		document.getElementById("warnDefendantUser").value = warnDefendantUser;
		document.getElementById("warnWhere").textContent = warnWhere;
		document.getElementById("warnItem").textContent = warnItem;
	});

	window.opener.postMessage("READY_FOR_DATA", window.location.origin);

	const submitButton = document.getElementById("submitReport");
	const closeButton = document.getElementById("closeButton"); // ← 닫기 버튼 ID 사용

	submitButton.addEventListener("click", async function () {
		const reportData = {
			warnDefendantUser: document.getElementById("warnDefendantUser").value,
			warnWhere: document.getElementById("warnWhere").value,
			warnItem: document.getElementById("warnItem")?.value,
			warnReason: document.querySelector("input[name='warnReason']:checked").value,
			warnReasonDetail: document.getElementById("warnReasonDetail")?.value || "",
		};

		try {
			const response = await fetch("/report/submit", {
				method: "POST",
				headers: {
					"Authorization": `Bearer ${token}`,
					"Content-Type": "application/json",
				},
				body: JSON.stringify(reportData),
			});

			const data = await response.json();

			console.log("응답 상태 코드:", response.status);
			console.log("응답 본문:", data);

			if (response.ok) {
				alert(data.message);
				window.removeEventListener("beforeunload", preventRefresh); // 새로고침 방지 해제
				window.close();
			} else {
				alert(data.error || "신고 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
			}
		} catch (error) {
			console.error("Error:", error);
			alert("네트워크 오류가 발생하였습니다.");
		}
	});

	// 닫기 버튼 누르면 새로고침 방지 해제 후 창 닫기
	if (closeButton) {
		closeButton.addEventListener("click", () => {
			window.removeEventListener("beforeunload", preventRefresh);
			window.close();
		});
	}
});
