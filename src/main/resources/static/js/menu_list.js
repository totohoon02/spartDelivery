// 메뉴 추가하기
document.querySelector('#submitButton').addEventListener('click', () => {
    window.location.href = "/create-menu";
});
// 메뉴 수정하기
document.querySelectorAll('.button').forEach(button => {
    button.addEventListener('click', function() {
        const menuData = this.getAttribute('data-menu');
        localStorage.setItem('menuData', menuData); // 메뉴 데이터를 localStorage에 저장
        window.location.href = '/update-menu';
    });
});
