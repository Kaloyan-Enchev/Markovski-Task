
const passwordFields = document.querySelectorAll("[type='password']");
const togglePasswordIcons = document.querySelectorAll(".password-toggle-icon i");

function togglePasswordVisibility(index) {
    if (passwordFields[index].type === "password") {
        passwordFields[index].type = "text";
        togglePasswordIcons[index].classList.remove("fa-eye");
        togglePasswordIcons[index].classList.add("fa-eye-slash");
    } else {
        passwordFields[index].type = "password";
        togglePasswordIcons[index].classList.remove("fa-eye-slash");
        togglePasswordIcons[index].classList.add("fa-eye");
    }
}

togglePasswordIcons.forEach((icon, index) => {
    icon.addEventListener("click", function () {
        togglePasswordVisibility(index);
    });
});