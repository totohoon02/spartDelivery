const btnSubmit = document.querySelector("#btnSubmit");
const btnCancel = document.querySelector("#btnCancel");
const btnDelete = document.querySelector("#btnDelete");

const storeName = document.querySelector("#storeName");
const category = document.querySelector("#category");
const phoneNumber = document.querySelector("#phoneNumber");
const storeAddress = document.querySelector("#storeAddress");
const storeImage = document.querySelector("#storeImage");


btnSubmit.addEventListener("click", ()=>{
    if (storeName.value === "" || category.value === "" || phoneNumber.value === "" || storeAddress.value === "") {
        alert("입력 값을 확인해주세요.");
        return;
    }

    const storeId = document.querySelector("#btnSubmit").getAttribute(("data-store-id"));

    fetch(`/store/${storeId}`, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            storeName:storeName.value,
            category:category.value,
            phoneNumber:phoneNumber.value,
            storeAddress:storeAddress.value,
            storeImage:storeImage.value

        })
    })
        .then(res => {
            if (!res.ok) {
                alert("업장등록 실패!");
            }
            // redirect if signup success
            window.location.href = "/store";
        })
});

btnCancel.addEventListener("click", ()=>{window.location.href="/store"});

btnDelete.addEventListener("click", ()=>{
    const storeId = document.querySelector("#btnDelete").getAttribute(("data-store-id"));

    fetch(`/store/${storeId}`, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(res => {
            if (!res.ok) {
                alert("업장삭제 실패!");
            }
            // redirect if signup success
            window.location.href = "/store";
        })
});