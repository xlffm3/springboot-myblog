function confirmPassword() {
    const firstInputPassword = document.getElementById("password").value;
    const confirmedInputPassword = document.getElementById("password-confirm").value;

    if (firstInputPassword === confirmedInputPassword) {
        document.getElementById("signup-form").submit();
        alert("정상적으로 회원 가입이 완료되었습니다.");
    } else {
        document.getElementById("password-confirm-miss").innerText = "동일한 패스워드를 입력하세요.";
    }
}
