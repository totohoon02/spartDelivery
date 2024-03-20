const dashboard = document.querySelector("#dashboard");
const business = document.querySelector("#business");
const menu = document.querySelector("#menu");
const order = document.querySelector("#order");
const profile = document.querySelector("#profile");
const cart = document.querySelector("#cart");

dashboard.addEventListener("click", () => {
    window.location.href = "/dashboard";
});
business.addEventListener("click", () => {
    window.location.href = "/business";
});
menu.addEventListener("click", () => {
    window.location.href = "/menu";
});
order.addEventListener("click", () => {
    window.location.href = "/order";
});
if (profile) {
    profile.addEventListener("click", () => {
        window.location.href = "/profile";
    });
}
if (cart) {
    cart.addEventListener("click", () => {
        window.location.href = "/cart";
    });
}
