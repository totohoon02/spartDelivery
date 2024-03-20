const btnSubmit = document.querySelector("#btnSubmit");
const btnCancel = document.querySelector("#btnCancel");

const menuImage = document.querySelector("#menuImage");
const menuName = document.querySelector("#menuName");
const menuPrice = document.querySelector("#menuPrice");
const menuExplain = document.querySelector("#menuExplain");


btnSubmit.addEventListener("click", ()=>{
    if (menuImage.value === "" || menuName.value === "" || menuPrice.value === "" || menuExplain.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }
    const menuId = btnSubmit.getAttribute("data-menu-id");

    fetch(`/menu/${menuId}`, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            menuName:menuName.value,
            imageUrl:menuImage.value,
            price:menuPrice.value,
            description:menuExplain.value
        })
    })
        .then(res => {
            if (!res.ok) {
                alert("메뉴수정 실패!");
            }
            // redirect if signup success
            window.location.href = "/menu";
        })
});

btnCancel.addEventListener("click", ()=>{window.location.href="/menu"});
