document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
        const menuId = card.getAttribute('data-menu-id');

        if (confirm("이 메뉴를 장바구니에 추가하시겠습니까?")) {
            addToCart(menuId);
        }
    });
});
function addToCart(menuId) {
    fetch(`/cart/${menuId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (response.ok) {
                alert('Item added to the cart successfully.');
                return response.json();
            } else if (response.status === 409) {
            } else {
                throw new Error('장바구니에 메뉴 추가에 실패했습니다.');
            }
        })
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
}
function clearCartAndAddItem(menuId) {
    fetch('/cart/clear', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('장바구니 삭제 실패');
            }
            return addToCart(menuId);
        })
        .catch(error => console.error('장바구니 삭제 실패:', error));
}