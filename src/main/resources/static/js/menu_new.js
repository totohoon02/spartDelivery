const btnSubmit = document.querySelector("#submitButton");

const menuName = document.querySelector("#menuName");
const menuImage = document.querySelector("#menuImage");
const menuPrice = document.querySelector("#menuPrice");
const menuExplain = document.querySelector("#menuExplain");


btnSubmit.addEventListener("click", ()=>{
    if (menuName.value === "" || menuImage.value === "" || menuPrice.value === "" || menuExplain.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    fetch("/menu", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            menuName:menuName.value,
            menuImage:menuImage.value,
            menuPrice:menuPrice.value,
            menuExplain:menuExplain.value
        })
    })
        .then(res => {
            if (!res.ok) {
                alert("메뉴등록 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});
