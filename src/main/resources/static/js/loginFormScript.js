// loginFormScript.js

document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const emailInput = document.querySelector("#email");
    const passwordInput = document.querySelector("#password");

    form.addEventListener("submit", (event) => {
        let isValid = true;

        if (!emailInput.value) {
            alert("Email is required");
            isValid = false;
        }

        if (!passwordInput.value) {
            alert("Password is required");
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
});
