// all-quizzes.js

// Function to handle carousel scrolling
function scrollCarousel(section, direction) {
    const carousel = document.getElementById(`${section}-carousel`);
    if (carousel) {
        const scrollAmount = carousel.offsetWidth * 0.8; // Scroll 80% of the container width
        carousel.scrollBy({
            top: 0,
            left: direction * scrollAmount,
            behavior: 'smooth'
        });
    }
}

// Event Listener for Back to Home Button
document.addEventListener('DOMContentLoaded', function () {
    const backToHomeBtn = document.getElementById('backToHome');
    if (backToHomeBtn) {
        backToHomeBtn.addEventListener('click', function (event) {
            event.preventDefault();
            const role = this.getAttribute('data-role');
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2));
            switch (role) {
                case 'student':
                    window.location.href = `${contextPath}/jsp/student.jsp`;
                    break;
                case 'teacher':
                    window.location.href = `${contextPath}/jsp/teacher.jsp`;
                    break;
                case 'admin':
                    window.location.href = `${contextPath}/jsp/admin.jsp`;
                    break;
                default:
                    window.location.href = `${contextPath}/index.jsp`;
            }
        });
    }

    // Keyboard navigation for carousel controls
    const carouselControls = document.querySelectorAll('.carousel-control');
    carouselControls.forEach(control => {
        control.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                this.click();
            }
        });
    });
});

// Optional: Implement infinite scroll or snapping for better UX
// This can be added based on further requirements
