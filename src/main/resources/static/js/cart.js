
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
    button.addEventListener('click', function() {
    const container = this.closest('.menu-item');
    const quantityInput = container.querySelector('.menu-quantity');
        let quantity = parseInt(quantityInput.value);
        quantity = isNaN(quantity) ? 0 : quantity;

        if (quantity > 1) {
    quantity--;
    quantityInput.value = quantity;
        updateItemTotalPrice(container);
        updateCartTotalPrice();} else {
            container.remove();
            updateCartTotalPrice();}
});
});

    document.querySelectorAll('.delete-item-btn').forEach(button => {
    button.addEventListener('click', function() {
    this.closest('.menu-item').remove();
        updateCartTotalPrice();

    });
});

    updateCartTotalPrice();

});