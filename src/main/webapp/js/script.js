document.addEventListener('DOMContentLoaded', function() {
    // Mobile Menu Toggle
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const navUl = document.querySelector('nav ul');

    mobileMenuToggle.addEventListener('click', function() {
        navUl.classList.toggle('show');
        mobileMenuToggle.classList.toggle('active');
    });

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

    prevBtn.addEventListener('click', () => {
        prevSlideFunc();
        resetInterval();
    });

    nextBtn.addEventListener('click', () => {
        nextSlide();
        resetInterval();
    });

    function resetInterval() {
        clearInterval(slideInterval);
        slideInterval = setInterval(nextSlide, 5000);
    }

    // Initialize Carousel
    showSlide(currentSlide);

    // Testimonial Slider (Swipe Support)
    const testimonialSlider = document.querySelector('.testimonial-slider');
    let startX, endX;

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
                    if(data.success){
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
                    if(data.success){
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
