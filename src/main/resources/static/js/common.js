// 메인폼 - X
// search - 검색어 저장
// cate - 카테고리 저장

window.onload = function() {
	const params = new URL(location).searchParams;
	
	// search
	const findType = params.get("findType");
	const keyword = params.get("keyword"); 
	
	// URL에서 검색 유형과 검색어를 가져와서 설정
	if (findType != null && keyword != null) {
		document.getElementById("findType").value = findType;
		document.getElementById("keyword").value = keyword;
	}
	
	// 정렬
	const savedSortOrder = sessionStorage.getItem("sortOrder");
	if (savedSortOrder != null) {
		document.getElementById("sortOrder").value = savedSortOrder;
		sessionStorage.removeItem("sortOrder");
	}
}

function updateItem(gender, clothesType, clothesTypeDetail) {
    const form = document.createElement('form');
    form.method = 'GET';
    form.action = `/category/${gender}`; // 폼을 전송할 URL 설정

    // clothesType, clothesTypeDetail을 hidden input으로 추가
    const clothesTypeInput = document.createElement('input');
    clothesTypeInput.type = 'hidden';
    clothesTypeInput.name = 'ct';
    clothesTypeInput.value = clothesType;

    const clothesTypeDetailInput = document.createElement('input');
    clothesTypeDetailInput.type = 'hidden';
    clothesTypeDetailInput.name = 'cd';
    clothesTypeDetailInput.value = clothesTypeDetail;

    // 폼에 입력 요소 추가
    form.appendChild(clothesTypeInput);
    form.appendChild(clothesTypeDetailInput);

    // 폼을 body에 추가하고 전송
    document.body.appendChild(form);
    form.submit();
}