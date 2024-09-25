// js/script.js
document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const navUl = document.querySelector('nav ul');

    mobileMenuToggle.addEventListener('click', function() {
        navUl.classList.toggle('active');
        mobileMenuToggle.classList.toggle('active');
    });
});
