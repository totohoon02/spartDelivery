const btnSubmit = document.querySelector("#btnSubmit");
const btnCancel = document.querySelector("#btnCancel");
const btnDelete = document.querySelector("#btnDelete");

const menuImage = document.querySelector("#menuImage");
const menuName = document.querySelector("#menuName");
const menuPrice = document.querySelector("#menuPrice");
const menuExplain = document.querySelector("#menuExplain");


btnSubmit.addEventListener("click", ()=>{
    if (menuImage.value === "" || menuName.value === "" || menuPrice.value === "" || menuExplain.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    fetch("/update-menu", {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json'
        },
        body: {
            menuImage, menuName, menuPrice, menuExplain
        }
    })
        .then(res => {
            if (!res.ok) {
                alert("업장등록 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});

btnCancel.addEventListener("click", ()=>{window.location.href="/"});

btnDelete.addEventListener("click", ()=>{
    fetch("/update-store", {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(res => {
            if (!res.ok) {
                alert("메뉴삭제 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});