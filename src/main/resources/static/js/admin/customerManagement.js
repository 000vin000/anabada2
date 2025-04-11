import { fetchWithAuth } from '/js/user/common/fetchWithAuth.js';

document.addEventListener("DOMContentLoaded", () => {

    // 질문 삭제
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", async function () {
            const questionNo = this.getAttribute("data-question-no");
            if (!questionNo) return alert("질문 번호가 올바르지 않습니다.");
            if (!confirm("정말 삭제하시겠습니까?")) return;

            try {
                const response = await fetchWithAuth(`/api/question/delete/${questionNo}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('Token')}`,
                        'Content-Type': 'application/json',
                    }
                });
                const responseData = await response.json();
                if (!response.ok) return alert(responseData.error || "삭제 실패");
                alert(responseData.message || "삭제되었습니다.");
                location.reload();
            } catch (error) {
                console.error("삭제 오류:", error);
                alert("삭제 중 오류 발생");
            }
        });
    });

    // 공지사항 삭제
    document.querySelectorAll(".delete-notice-btn").forEach(button => {
        button.addEventListener("click", async function () {
            const noticeNo = this.getAttribute("data-notice-no");
            if (!confirm("정말 삭제하시겠습니까?")) return;

            try {
                const response = await fetchWithAuth(`/api/notice/delete/${noticeNo}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    }
                });
                if (!response.ok) return alert("공지사항 삭제 실패!");
                alert("공지사항이 삭제되었습니다.");
                this.closest("tr").remove();
            } catch (error) {
                console.error("공지사항 삭제 오류:", error);
                alert("삭제 중 오류 발생");
            }
        });
    });

	// 신고 처리 by수연
	document.querySelectorAll(".submitWarnResult").forEach(button => {
	    button.addEventListener("click", function () {
	        const warnNo = button.getAttribute("data-warn-no");  // 여기서 warnNo 받아옴
	        const days = document.getElementById("warnSuspensionDays").value;
	        const warnResult = document.querySelector("input[name='warnResult']:checked")?.value;

	        fetch(`/warn/submitWarnResult/${warnNo}`, {
	            method: "POST",
	            headers: {
	                "Authorization": `Bearer ${localStorage.getItem('Token')}`,
	                "Content-Type": "application/json"
	            },
	            body: JSON.stringify({
	                warnResult: warnResult,
	                warnSuspensionDays: days
	            })
	        })
	        .then(response => {
	            if (!response.ok) throw new Error("신고 처리 오류");
	            return response.json();
	        })
	        .then(data => {
	            alert(data.message);
	            location.reload();
	        })
	        .catch(error => {
	            console.error("신고 처리 오류 발생: ", error);
	        });
	    });
	});

});
