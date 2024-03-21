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
                alert('장바구니에 성공적으로 추가되었습니다.');
                return response.json();
            } else if (response.status === 409) {
                if (confirm("다른 상점의 메뉴가 장바구니에 추가되어 있습니다. 장바구니를 초기화하시겠습니까?")) {
                    clearCartAndAddItem(menuId);
                }
            } else {
                throw new Error('장바구니에 메뉴 추가에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
}

function clearCartAndAddItem(menuId) {
    fetch('/cart/clear', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (response.ok) {
                // 장바구니가 비워진 후 새로운 메뉴를 추가
                return addToCart(menuId);
            } else {
                throw new Error('장바구니 초기화에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
}
