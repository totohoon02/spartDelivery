const dashboard = document.querySelector("#dashboard");
const business = document.querySelector("#business");
const menu = document.querySelector("#menu");
const order = document.querySelector("#order");
const profile = document.querySelector("#profile");
const cart = document.querySelector("#cart");




if(dashboard){
dashboard.addEventListener("click", () => {
    window.location.href = "/store";
});
}
if(business){
business.addEventListener("click", () => {
    window.location.href = "/business";
});
}
if(menu){
menu.addEventListener("click", () => {
    window.location.href = "/menu";
});
}
if(order){
order.addEventListener("click", () => {
    window.location.href = "/order";
});
}

if (profile) {
    profile.addEventListener("click", () => {
        window.location.href = "/profile";
    });
}
if (cart) {
    cart.addEventListener("click", () => {
        console.log("cart click")
        window.location.href = "/cart";
    });
}


const makeLogoutButton = () => {
    const logoutButton = document.createElement("button");
    logoutButton.textContent = "Logout";

    // 스타일 적용
    logoutButton.style.position = "fixed";
    logoutButton.style.bottom = "20px";
    logoutButton.style.right = "20px";
    logoutButton.style.width = "50px";
    logoutButton.style.height = "50px";
    logoutButton.style.border = "none";
    logoutButton.style.margin = "0";
    logoutButton.style.borderRadius = "50%";
    logoutButton.style.color = "black";
    logoutButton.style.backgroundColor = "#F3CF98";



    logoutButton.addEventListener("click", function() {
        // 쿠키에서 "authentication" 삭제
        document.cookie = "Authorization" + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        window.location.href = "/login";
        // 여기에 로그아웃 동작을 수행하는 코드를 추가하세요.
        alert("로그아웃 되었습니다.");
    });

    document.body.appendChild(logoutButton);
}

makeLogoutButton();
