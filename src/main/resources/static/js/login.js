// redirect to signup
const btnSignup = document.querySelector("#signup");
btnSignup.addEventListener("click", ()=>{
    window.location.href="/signup";
});

const btnLogin = document.querySelector("#login");
const email = document.querySelector("#email");
const password = document.querySelector("#password");

btnLogin.addEventListener("click", () => {
    if (email.value === "" || password.value === "") {
        alert("입력 값을 확인해 주세요.");
        return;
    }

    fetch("/login2", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email.value,
            password: password.value
        })
    })
        .then(res => {
            if (!res.ok) {
                throw new Error('Login failed');
            }
            return res.json();
        })
        .then(data => {
            const userRole = data.role;

            // Redirect based on role
            switch(userRole) {
                case 'BOSS':
                    window.location.href = "/store/create-store";
                    break;
                case 'CLIENT':
                    window.location.href = "/store";
                    break;
                default:
                    throw new Error('Unknown role');
            }
        })
        .catch(error => {
            console.error('Error during login:', error);
            alert("로그인에 실패했습니다.");
        });
});

