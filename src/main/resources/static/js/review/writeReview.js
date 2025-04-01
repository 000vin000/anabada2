document.getElementById('reviewForm').addEventListener('submit', async function (e) {
	e.preventDefault();
	const rating = document.querySelector('input[name="reviewRating"]:checked')?.value;
	const content = document.getElementById('reviewContent').value;
	
	if (!rating || !content) {
	  alert("별점과 리뷰를 입력해주세요!");
	  return;
	}
	
	const response = await fetch('/api/reviews/write', {
	  method: 'POST',
	  headers: {
		  'Authoraization': `Bearer ${localStorage.getItem("Token")}`,
		  'Content-Type': 'application/json' },
	  body: JSON.stringify({ rating, content })
	});
	
	if (response.ok) {
	  alert("리뷰가 등록되었습니다!");
	  location.reload(); // 또는 리뷰 리스트 갱신
	} else {
	  alert("리뷰 등록에 실패했습니다.");
	}
});
