document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
        const menuId = card.getAttribute('data-menu-id');
        const storeId = document.querySelector('.header').dataset.storeId;

        if (confirm("이 메뉴를 장바구니에 추가하시겠습니까?")) {
            // 장바구니에 추가하는 API 호출
            fetch(`/cart/${menuId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: 1, // Assume this is the user ID for now
                    menuId: menuId,
                    storeId: storeId
                })
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error('Error:', error));
        }
    });
});
