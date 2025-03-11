document.addEventListener("DOMContentLoaded", function () {
    const signupBtn = document.getElementById("signupBtn");
    const loginBtn = document.getElementById("loginBtn");

    if (signupBtn) {
        signupBtn.addEventListener("click", function (event) {
            event.preventDefault();
            handleSignup();
        });
    }

    if (loginBtn) {
        loginBtn.addEventListener("click", function (event) {
            event.preventDefault();
            handleLogin();
        });
    }
});

/**
 * AJAX 요청
 * @param {string} url - 요청 할 URL
 * @param {string} method - HTTP 요청 방식
 * @param {Object} data - 요청 데이터 (JSON)
 * @param {function} callback - 성공 시 실행할 콜백 함수
 */
function sendRequest(url, method, data, callback) {
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
        console.log("Response:", data);
        callback(data);
    })
    .catch(error => console.error("Error:", error));
}

// 회원가입
function handleSignup() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const email = document.getElementById("email").value;
    const nickname = document.getElementById("nickname").value;

    const requestData = {
        username: username,
        password: password,
        email: email,
    };

    if (nickname.trim() !== "") {
        requestData.nickname = nickname;
    }

    sendRequest("/auth/signup", "POST", requestData, function (response) {
        if (response.status === "OK") {
            alert("회원가입 성공! 로그인 페이지로 이동합니다.");
            window.location.href = "/auth/login";
        } else {
            let errorMessage = "회원가입 실패:\n";
            if (typeof response.errors === "object") {
                for (let key in response.errors) {
                    errorMessage += `- ${key}: ${response.errors[key]}\n`;
                }
            } else {
                errorMessage += response.message || "알 수 없는 오류 발생";
            }
            alert(errorMessage);
        }
    });
}

// 로그인
function handleLogin() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const requestData = {
        username: username,
        password: password,
    };

    sendRequest("/auth/login", "POST", requestData, function (response, xhr) {
//        if (response.status === "OK") {
        if (xhr.status === 200) {
            const token = xhr.getResponseHeader("Authoorization");
            if (token) {
                localStorage.setItem("jwt", token);
                alert("로그인 성공!");
                window.location.href = "/diary/calendar";
            } else {
                alert("로그인 실패: 응답 내 토큰이 없습니다.");
            }
        } else {
            alert("로그인 실패: " + response.message);
        }
    });
}
