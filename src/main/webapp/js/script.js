// Ensure that the script runs after the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Mobile Menu Toggle
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const navUl = document.querySelector('nav ul');

    mobileMenuToggle.addEventListener('click', function() {
        navUl.classList.toggle('show');
        mobileMenuToggle.classList.toggle('active');
    });

    // Sidebar Toggle
    const sidebarToggleBtn = document.getElementById('sidebarToggle');
    const sidebarCloseBtn = document.getElementById('sidebarClose');
    const sidebar = document.getElementById('sidebar');
    const body = document.body;

    sidebarToggleBtn.addEventListener('click', function() {
        body.classList.toggle('sidebar-open');
    });

    sidebarCloseBtn.addEventListener('click', function() {
        body.classList.remove('sidebar-open');
    });

    // Close sidebar when clicking outside
    document.addEventListener('click', function(event) {
        if (body.classList.contains('sidebar-open')) {
            if (!sidebar.contains(event.target) && !sidebarToggleBtn.contains(event.target)) {
                body.classList.remove('sidebar-open');
            }
        }
    });

    // Dark Mode Toggle
    const toggleSwitch = document.getElementById('dark-mode-toggle');
    const currentTheme = localStorage.getItem('theme');

    if (currentTheme === 'dark') {
        document.body.classList.add('dark-theme');
        toggleSwitch.checked = true;
    }

    toggleSwitch.addEventListener('change', () => {
        if (toggleSwitch.checked) {
            document.body.classList.add('dark-theme');
            localStorage.setItem('theme', 'dark');
        } else {
            document.body.classList.remove('dark-theme');
            localStorage.setItem('theme', 'light');
        }
    });

    // Live Search functionality
    const searchInput = document.getElementById('liveSearch');
    const searchResults = document.getElementById('searchResults');

    if (searchInput && searchResults) {
        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            if (query.length > 2) {
                fetchSearchResults(query);
            } else {
                searchResults.style.display = 'none';
            }
        });

        function fetchSearchResults(query) {
            fetch(`${contextPath}/search?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    displaySearchResults(data);
                })
                .catch(error => console.error('Error:', error));
        }

        function displaySearchResults(results) {
            searchResults.innerHTML = '';
            if (results.length > 0) {
                results.forEach(result => {
                    const div = document.createElement('div');
                    div.classList.add('search-result-item');
                    div.textContent = result.name; // Use 'name' as returned by the servlet
                    div.addEventListener('click', () => {
                        window.location.href = `${contextPath}/jsp/quiz-detail.jsp?id=${result.id}`;
                    });
                    searchResults.appendChild(div);
                });
                searchResults.style.display = 'block';
            } else {
                searchResults.style.display = 'none';
            }
        }

        // Close search results when clicking outside
        document.addEventListener('click', function(event) {
            if (!searchResults.contains(event.target) && event.target !== searchInput) {
                searchResults.style.display = 'none';
            }
        });
    }

    // FAQ Accordion with ARIA
    const faqQuestions = document.querySelectorAll('.faq-question');

    faqQuestions.forEach(question => {
        question.addEventListener('click', () => {
            const isActive = question.classList.toggle('active');
            const answer = question.nextElementSibling;

            question.setAttribute('aria-expanded', isActive);
            answer.setAttribute('aria-hidden', !isActive);

            if (isActive) {
                answer.style.maxHeight = answer.scrollHeight + 'px';
            } else {
                answer.style.maxHeight = null;
            }

            // Close other open FAQs
            faqQuestions.forEach(otherQuestion => {
                if (otherQuestion !== question && otherQuestion.classList.contains('active')) {
                    otherQuestion.classList.remove('active');
                    otherQuestion.setAttribute('aria-expanded', false);
                    otherQuestion.nextElementSibling.setAttribute('aria-hidden', true);
                    otherQuestion.nextElementSibling.style.maxHeight = null;
                }
            });
        });
    });

    // Featured Quizzes Carousel
    const slides = document.querySelectorAll('.carousel-slide');
    const prevBtn = document.querySelector('.carousel-control.prev');
    const nextBtn = document.querySelector('.carousel-control.next');
    let currentSlide = 0;
    const totalSlides = slides.length;
    let slideInterval = setInterval(nextSlide, 5000); // Change slide every 5 seconds

    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.style.display = i === index ? 'block' : 'none';
        });
    }

    function nextSlide() {
        currentSlide = (currentSlide + 1) % totalSlides;
        showSlide(currentSlide);
    }

    function prevSlideFunc() {
        currentSlide = (currentSlide - 1 + totalSlides) % totalSlides;
        showSlide(currentSlide);
    }

    if (prevBtn && nextBtn) {
        prevBtn.addEventListener('click', () => {
            prevSlideFunc();
            resetInterval();
        });

        nextBtn.addEventListener('click', () => {
            nextSlide();
            resetInterval();
        });
    }

    function resetInterval() {
        clearInterval(slideInterval);
        slideInterval = setInterval(nextSlide, 5000);
    }

    // Initialize Carousel
    if (slides.length > 0) {
        showSlide(currentSlide);
    }

    // Testimonial Slider (Swipe Support)
    const testimonialSlider = document.querySelector('.testimonial-slider');
    let startX, endX;

    if (testimonialSlider) {
        testimonialSlider.addEventListener('touchstart', (e) => {
            startX = e.changedTouches[0].screenX;
        }, false);

        testimonialSlider.addEventListener('touchend', (e) => {
            endX = e.changedTouches[0].screenX;
            handleSwipe();
        }, false);

        function handleSwipe() {
            if (endX < startX - 50) {
                nextSlideTestimonial();
            }
            if (endX > startX + 50) {
                prevSlideTestimonial();
            }
        }

        function nextSlideTestimonial() {
            testimonialSlider.scrollBy({ left: 300, behavior: 'smooth' });
        }

        function prevSlideTestimonial() {
            testimonialSlider.scrollBy({ left: -300, behavior: 'smooth' });
        }
    }

    // Newsletter Subscription Form (AJAX)
    const newsletterForm = document.querySelector('.newsletter-form');
    const formMessage = document.querySelector('.form-message');

    if (newsletterForm) {
        newsletterForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const formData = new FormData(newsletterForm);
            const email = formData.get('email');

            fetch(newsletterForm.action, {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        formMessage.textContent = 'Thank you for subscribing!';
                        formMessage.style.color = '#2ecc71';
                        newsletterForm.reset();
                    } else {
                        formMessage.textContent = 'Something went wrong. Please try again.';
                        formMessage.style.color = '#e74c3c';
                    }
                })
                .catch(error => {
                    formMessage.textContent = 'An error occurred. Please try again.';
                    formMessage.style.color = '#e74c3c';
                });
        });
    }

    // Footer Newsletter Subscription Form (AJAX)
    const footerNewsletterForm = document.querySelector('.footer-newsletter-form');
    const footerFormMessage = footerNewsletterForm?.nextElementSibling;

    if (footerNewsletterForm) {
        footerNewsletterForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const formData = new FormData(footerNewsletterForm);
            const email = formData.get('email');

            fetch(footerNewsletterForm.action, {
                method: 'POST',
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        footerFormMessage.textContent = 'Thank you for subscribing!';
                        footerFormMessage.style.color = '#2ecc71';
                        footerNewsletterForm.reset();
                    } else {
                        footerFormMessage.textContent = 'Something went wrong. Please try again.';
                        footerFormMessage.style.color = '#e74c3c';
                    }
                })
                .catch(error => {
                    footerFormMessage.textContent = 'An error occurred. Please try again.';
                    footerFormMessage.style.color = '#e74c3c';
                });
        });
    }
});
