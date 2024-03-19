// redirect to signup
const btnSignup = document.querySelector("#signup");
btnSignup.addEventListener("click", ()=>{
    window.location.href="/signup";
});

//login
const btnLogin = document.querySelector("#login");
const email = document.querySelector("#email");
const password = document.querySelector("#password");
btnLogin.addEventListener("click", ()=>{
    if(email.value === "" || password.value === ""){
        alert("입력 값을 확인해 주세요.");
        return;
    }
    fetch("/login2", {
        method:"POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email:email.value,
            password:password.value
        })
    })
        .then(res =>{
            if(!res.ok){
                alert("로그인 실패!");
            }
            // redirect if login success
            window.location.href = "/";
        })
})
