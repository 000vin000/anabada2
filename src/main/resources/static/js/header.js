function submitSearch() {
    const findType = document.getElementById('findType').value;
    const keyword = document.getElementById('keyword').value.trim();

    if (!findType) {
        alert('검색유형을 선택하세요.');
        return;
    }
    if (!keyword) {
        alert('검색어를 입력하세요.');
        return;
    }

    const form = document.createElement('form');
    form.method = 'GET';
    form.action = '/search';

    const findTypeInput = document.createElement('input');
    findTypeInput.type = 'hidden';
    findTypeInput.name = 'findType';
    findTypeInput.value = findType;

    const keywordInput = document.createElement('input');
    keywordInput.type = 'hidden';
    keywordInput.name = 'keyword';
    keywordInput.value = keyword;

    form.appendChild(findTypeInput);
    form.appendChild(keywordInput);
    document.body.appendChild(form);
    form.submit();
}

function handleKeyPress(event) {
    if (event.key === 'Enter') {
        submitSearch();
    }
}

document.getElementById('mypageBtn').addEventListener('click', function () {
    fetch(`/mypage`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('Token')}`
        }
    	, credentials : "include"
    }).then(response => response.json())
	.then(html => {
		document.open();
		document.write(html);
		document.close();
	})
	.catch(error => {
        console.error('오류 발생:', error);
        // alert('서버 오류가 발생했습니다.');
    });
});