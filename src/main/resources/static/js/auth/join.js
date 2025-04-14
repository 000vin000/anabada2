document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("joinSelectForm");

  form.addEventListener("submit", (e) => {
    e.preventDefault(); 

    const selectedType = document.querySelector("input[name='userType']:checked");

    if (!selectedType) {
      alert("회원 유형을 선택해주세요.");
      return;
    }

	if (selectedType.value === "individual") {
	  location.href = "/join/individual/emailAuth";
	} else if (selectedType.value === "brand") {
	  location.href = "/auth/join/brand/사업자등록번호확인.html"; 
	}

  });
});
