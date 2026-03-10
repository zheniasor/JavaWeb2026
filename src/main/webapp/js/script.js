function validateForm() {
    let login = document.getElementById("login").value;
    let password = document.getElementById("pass").value;

    if (login.trim() === "") {
        alert("Введите логин!");
        return false;
    }

    if (password.trim() === "") {
        alert("Введите пароль!");
        return false;
    }

    if (password.length < 3) {
        alert("Пароль должен быть не менее 3 символов!");
        return false;
    }

    return true;
}

function togglePassword() {
    let passwordField = document.getElementById("pass");
    if (passwordField.type === "password") {
        passwordField.type = "text";
    } else {
        passwordField.type = "password";
    }
}

// Функция для очистки формы
function clearForm() {
    document.getElementById("login").value = "";
    document.getElementById("pass").value = "";
}

// Выполняется после загрузки страницы
document.addEventListener("DOMContentLoaded", function() {
    let showPassBtn = document.getElementById("showPass");
    if (showPassBtn) {
        showPassBtn.addEventListener("click", togglePassword);
    }
});