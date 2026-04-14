function clearForm() {
    document.getElementById("login").value = "";
    document.getElementById("pass").value = "";
}

function togglePassword() {
    let pass = document.getElementById("pass");
    pass.type = pass.type === "password" ? "text" : "password";
}

document.addEventListener("DOMContentLoaded", function() {
    let showPassBtn = document.getElementById("showPass");
    if (showPassBtn) {
        showPassBtn.addEventListener("click", togglePassword);
    }

     let showPassRegister = document.getElementById("showPassRegister");
    if (showPassRegister) {
        showPassRegister.addEventListener("click", function() {
            let passField = document.getElementById("pass");
            if (passField) {
                passField.type = passField.type === "password" ? "text" : "password";
            }
        });
    }
});