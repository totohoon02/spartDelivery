const btnSubmit = document.querySelector("#submitButton");

const storeName = document.querySelector("#storeName");
const category = document.querySelector("#category");
const phoneNumber = document.querySelector("#phoneNumber");
const storeAddress = document.querySelector("#storeAddress");


btnSubmit.addEventListener("click", ()=>{
    if (storeName.value === "" || category.value === "" || phoneNumber.value === "" || storeAddress.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    fetch("/store", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            storeName:storeName.value,
            category:category.value,
            phoneNumber:phoneNumber.value,
            storeAddress:storeAddress.value
        })
    })
        .then(res => {
            if (!res.ok) {
                alert("업장등록 실패!");
            }
            // redirect if signup success
            window.location.href = "/";
        })
});
