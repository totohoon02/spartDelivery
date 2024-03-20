

// 페이지가 로드되었을 때 실행
document.addEventListener('DOMContentLoaded', function() {
    fetch('/cart')    // 변경 필요
        .then(response => response.json())
        .then(data => {
            updateCartPage(data);
        })
        .catch(error => console.error('Error:', error));

    const checkoutButton = document.getElementById('checkoutButton');

    checkoutButton.addEventListener('click', function (event) {

        fetch(`/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json(); // Make sure the server is sending valid JSON
            })
            .then(data => {
                console.log('Order processed successfully:', data);
                window.location.href = `/order/customer/${data.orderId}`; // Ensure the server response includes orderId
            })
            .catch(error => {
                console.error('Error:', error);
            });

    });

    document.querySelector('.menu-section').addEventListener('click', function (event) {
        if (event.target.classList.contains('increase-quantity')) {
            const container = event.target.closest('.menu-item');
            const quantityInput = container.querySelector('.menu-quantity');
            let quantity = parseInt(quantityInput.value);
            quantity = isNaN(quantity) ? 0 : quantity;
            quantity++;
            quantityInput.value = quantity;
            const menuId = container.getAttribute('data-menu-id');
            fetch(`/cart/${menuId}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ quantity })
            })
                .then(response => {
                    if (response.ok) {
                        console.log('수량이 성공적으로 추가되었습니다.');
                        updateItemTotalPrice(container);
                        updateCartTotalPrice();
                    } else {
                        console.error('메뉴 추가 중 에러 발생');
                    }
                })
                .catch(error => console.error('Error:', error));

        } else if (event.target.classList.contains('decrease-quantity')) {
            const container = event.target.closest('.menu-item');
            const quantityInput = container.querySelector('.menu-quantity');
            let quantity = parseInt(quantityInput.value);
            quantity = isNaN(quantity) ? 0 : quantity;
            if (quantity > 1) {
                quantity--;
                quantityInput.value = quantity;
                const menuId = container.getAttribute('data-menu-id');
                fetch(`/cart/${menuId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ quantity })
                })
                    .then(response => {
                        if (response.ok) {
                            console.log('수량이 성공적으로 삭제되었습니다.');
                            updateItemTotalPrice(container);
                            updateCartTotalPrice(); // 장바구니 총 가격을 업데이트
                        } else {
                            console.error('메뉴 삭제 중 에러 발생');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            } else {
                // 수량이 0 이하가 되면 삭제 요청을 보냅니다.
                const menuId = container.getAttribute('data-menu-id');
                fetch(`/cart/${menuId}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            console.log('메뉴가 성공적으로 삭제되었습니다.');
                            container.remove(); // 페이지에서 해당 항목을 제거
                            updateCartTotalPrice(); // 장바구니 총 가격을 업데이트
                        } else {
                            console.error('메뉴 삭제 중 에러 발생');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        } else if (event.target.classList.contains('delete-item-btn')) {
            const menuId = event.target.closest('.menu-item').getAttribute('data-menu-id');
            fetch(`/cart/${menuId}`, {
                method: 'DELETE'})
                .then(response => {
                    if (response.ok) {
                        console.log('Menu successfully deleted');
                        event.target.closest('.menu-item').remove();
                        updateCartTotalPrice();
                    } else {
                        console.error('Error deleting menu');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

    });

    // function updateCartPage(data) {
    //     const menuContainer = document.querySelector('.menu-section');
    //     menuContainer.innerHTML = ''; // 기존 목록을 비움
    //
    //     data.cartItems.forEach((cartItem, index) => {
    //         // 장바구니 항목을 위한 HTML 요소 생성
    //         const itemElement = document.createElement('div');
    //         itemElement.classList.add('menu-item');
    //         itemElement.setAttribute('data-price', cartItem.price);
    //
    //         // 메뉴 이름, 설명, 가격 등의 정보를 포함하는 요소를 생성하고 추가
    //         itemElement.innerHTML = `
    //         <div class="item-header">
    //         <h2 th:text="${cartItem.name}">Menu Name</h2>
    //         <p th:text="${cartItem.description}">Menu Description</p>
    //     </div>
    //     <p th:text="${cartItem.price} + '원'">Price</p>
    //
    //     <div class="quantity-control">
    //         <button th:attr="data-index='${iterStat.index}'" class="decrease-quantity">-</button>
    //         <label>
    //             <input type="text" th:value="${cartItem.quantity}" readonly class="menu-quantity">
    //         </label>
    //         <button th:attr="data-index='${iterStat.index}'" class="increase-quantity">+</button>
    //     </div>
    //     <p class = "total-price-item" th:text="${#numbers.formatDecimal(cartItem.price * cartItem.quantity, 1, 0)} + '원'">Total Price</p>
    //     <button th:attr="data-index='${iterStat.index}'" class="delete-item-btn">X</button>
    //     `;
    //
    //         // 생성한 요소를 페이지에 추가
    //         menuContainer.appendChild(itemElement);
    //     });
    //
    //     // 장바구니 전체 금액 업데이트
    //     const checkoutButton = document.querySelector('.checkout-button');
    //     checkoutButton.textContent = `${data.totalPrice}원 결제하기`;
    // }

    const updateItemTotalPrice = (item) => {
        const pricePerItem = parseFloat(item.dataset.price);
        const quantity = parseInt(item.querySelector('.menu-quantity').value);
        const totalPriceElement = item.querySelector('.total-price-item');
        const totalPrice = pricePerItem * quantity;
        totalPriceElement.textContent = totalPrice.toFixed(0) + '원';
    };

    const updateCartTotalPrice = () => {
        const items = document.querySelectorAll('.menu-item');
        let totalPriceCart = 0;
        items.forEach(item => {
            const quantity = parseInt(item.querySelector('.menu-quantity').value);
            const pricePerItem = parseFloat(item.dataset.price);
            totalPriceCart += quantity * pricePerItem;
        });
        document.querySelector('.checkout-button').textContent = totalPriceCart.toFixed(0) + '원 결제하기';

    };
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.increase-quantity').forEach(button => {
            button.addEventListener('click', function () {
                const container = this.closest('.menu-item');
                const quantityInput = container.querySelector('.menu-quantity');
                let quantity = parseInt(quantityInput.value);
                quantity = isNaN(quantity) ? 0 : quantity;

                quantity++;
                quantityInput.value = quantity;
                updateItemTotalPrice(container);
                updateCartTotalPrice();
            });
        });

        document.querySelectorAll('.decrease-quantity').forEach(button => {
            button.addEventListener('click', function () {
                const container = this.closest('.menu-item');
                const quantityInput = container.querySelector('.menu-quantity');
                let quantity = parseInt(quantityInput.value) - 1;

                if (quantity <= 0) {
                    // 수량이 0 이하가 되면 삭제 요청을 보냅니다.
                    const menuId = container.getAttribute('data-menu-id'); // 메뉴 ID를 얻어옵니다.
                    fetch(`/cart/${menuId}`, {
                        method: 'DELETE'
                    })
                        .then(response => {
                            if (response.ok) {
                                console.log('메뉴가 성공적으로 삭제되었습니다.');
                                container.remove(); // 페이지에서 해당 항목을 제거합니다.
                                updateCartTotalPrice(); // 장바구니 총 가격을 업데이트합니다.
                            } else {
                                console.error('메뉴 삭제 중 에러 발생');
                            }
                        })
                        .catch(error => console.error('Error:', error));
                } else {
                    // 수량을 감소시키고 서버에 변경 사항을 반영하는 로직을 추가합니다.
                    quantityInput.value = quantity;
                    updateItemTotalPrice(container);
                    updateCartTotalPrice();
                }
            });
        });


        document.querySelectorAll('.delete-item-btn').forEach(button => {
            button.addEventListener('click', function () {
                const menuId = this.getAttribute('data-menu-id'); // 메뉴 ID를 얻어옵니다.
                fetch(`/cart/${menuId}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            console.log('메뉴가 성공적으로 삭제되었습니다.');
                            this.closest('.menu-item').remove(); // 페이지에서 해당 항목을 제거합니다.
                            updateCartTotalPrice(); // 장바구니 총 가격을 업데이트합니다.
                        } else {
                            console.error('메뉴 삭제 중 에러 발생');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            });
        });
        updateCartTotalPrice();

    });
})