function confirmPassword() {
    const inputPassword = document.getElementById('password').value;
    const confirmationPassword = document.getElementById('password-confirm').value;

    if (inputPassword === confirmationPassword) {
        document.getElementById('signup-form').submit();
    } else {
        document.getElementById('password-confirm-miss').innerText = '패스워드를 정확하게 입력해주세요.';
    }
}
