const btnSubmit = document.querySelector("#btnSubmit");
const searchInput = document.querySelector("#search-input");

btnSubmit.addEventListener("click", () => {
    const searchValue = searchInput.value.trim();
    console.log(typeof(searchValue))

    if (searchValue === "") {
        alert("입력 값을 확인해주세요");
        return;
    }

    // 사용자를 서버의 /stores 엔드포인트로 리다이렉션
    // searchValue 쿼리 파라미터 포함
    window.location.href = `/store?searchValue=${encodeURIComponent(searchValue)}`;
});
