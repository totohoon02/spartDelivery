// 장바구니에 메뉴 추가
document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
        if (confirm("이 메뉴를 장바구니에 추가하시겠습니까?")) {
            console.log("메뉴가 장바구니에 추가됨");
        }
    });
});