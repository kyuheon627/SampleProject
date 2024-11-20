// mainScript.js

window.addEventListener("load", () => {
    const navbar = document.querySelector(".navbar");
    const content = document.querySelector(".content");
    const navbarHeight = navbar.offsetHeight; // 네비게이션 바의 실제 높이

    // .content에 navbar 높이만큼 padding-top을 적용
    content.style.paddingTop = `${navbarHeight}px`;
});

// navbarScript.js
document.addEventListener("DOMContentLoaded", () => {
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll(".navbar .menu li a");

    menuLinks.forEach(link => {
        if (link.getAttribute("href") === currentPath) {
            link.classList.add("active");
        }
    });
});
