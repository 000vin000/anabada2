// 메인폼 - X
// search - 검색어 저장
// cate - 카테고리 저장

window.onload = function() {
	const params = new URL(location).searchParams;
	
	// search
	const findType = params.get("findType");
	const keyword = params.get("keyword"); 
	
	// category
	const gender = params.get("gender");
	const clothesType= params.get("clothesType");
	
	// URL에서 검색 유형과 검색어를 가져와서 설정
	if (findType != null && keyword != null) {
		document.getElementById("findType").value = findType;
		document.getElementById("keyword").value = keyword;
	}
	
	// URL에서 성별과 종류를 가져와서 설정
	if (gender != null && clothesType != null) {
		document.getElementById("gender").value = gender;
		document.getElementById("clothesType").value = clothesType;
	}
	
	// 정렬
	const savedSortOrder = sessionStorage.getItem("sortOrder");
	if (savedSortOrder != null) {
		document.getElementById("sortOrder").value = savedSortOrder;
		sessionStorage.removeItem("sortOrder");
	}
}

function updateItem() {
	const gender = document.getElementById('gender').value;
	const clothesType = document.getElementById('clothesType').value;
	
	const form = document.createElement('form');
	form.method = 'GET';
	form.action = '/category';
	
	const genderInput = document.createElement('input');
	genderInput.type = 'hidden';
	genderInput.name = 'gender';
	genderInput.value = gender;
	
	const clothesTypeInput = document.createElement('input');
	clothesTypeInput.type = 'hidden';
	clothesTypeInput.name = 'clothesType';
	clothesTypeInput.value = clothesType;
	
	form.appendChild(genderInput);
	form.appendChild(clothesTypeInput);
	document.body.appendChild(form);
	form.submit();
}