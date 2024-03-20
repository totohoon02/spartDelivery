// 배달 완료 처리
document.addEventListener('DOMContentLoaded', function() {
    // 모든 '자세히 보기' 버튼에 이벤트 리스너를 추가합니다.
    const detailButtons = document.querySelectorAll('.button-delivery');
    detailButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            // 'event.target'을 사용하여 클릭된 버튼에 접근합니다.
            const orderId = event.target.getAttribute('data-order-id');
            markOrderAsDelivered(orderId);
        });
    });
});

function markOrderAsDelivered(orderId) {
    const token = localStorage.getItem('token');

    fetch('/order/' + orderId + '/deliver', {
        method: 'PUT',
    })
        .then(response => {
            if(response.ok) {
                console.log("Order marked as delivered.");
                window.location.href = "/order"
            } else {
                console.log("Error occurred while updating order status.");
            }
        })
        .catch(error => console.error('Error:', error));
}

// 자세히 보기
document.addEventListener('DOMContentLoaded', function() {
    // 모든 '자세히 보기' 버튼에 이벤트 리스너를 추가합니다.
    const detailButtons = document.querySelectorAll('.button-detail');
    detailButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            // 'event.target'을 사용하여 클릭된 버튼에 접근합니다.
            const orderId = event.target.getAttribute('data-order-id');
            goToOrderDetail(orderId);
        });
    });
});

function goToOrderDetail(orderId) {
    // 주문 ID를 URL에 포함시켜 상세 페이지로 이동
    window.location.href = '/order/boss/' + orderId;
}


