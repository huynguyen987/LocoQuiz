<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role"); // Assuming you store user role in session
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>QuizLoco - Your Ultimate Interactive Learning Platform</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
    <!-- Pass contextPath to JavaScript -->
    <script type="text/javascript">
        const contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<body id="body">
<!-- Header -->
<header>
    <div class="container">
        <!-- Sidebar Toggle Button -->
        <button id="sidebarToggle" class="sidebar-toggle-btn" aria-label="Open sidebar">
            <i class="fas fa-bars"></i>
        </button>
        <!-- Logo -->
        <a href="index.jsp" class="logo">QuizLoco</a>
        <!-- Navigation Menu -->
        <nav>
            <ul>
                <li><a href="#home">Home</a></li>
                <li><a href="#features">Features</a></li>
                <li><a href="#latest-quizzes">Latest Quizzes</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </nav>

        <!-- Search Container -->
        <div class="search-container">
            <input type="text" id="liveSearch" placeholder="Search quizzes..." aria-label="Search quizzes">
            <div id="searchResults" class="search-results"></div>
        </div>

        <!-- Authentication Links -->
        <div class="auth-links">
            <% if (username != null) { %>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/jsp/user-profile.jsp" class="user-profile">
                    <i class="fas fa-user-circle"></i>
                    <span class="username">Welcome, <%= username %></span>
                </a>
                <a href="LogoutServlet" class="btn-logout">Logout</a>
            </div>
            <% } else { %>
            <a href="jsp/login.jsp" class="btn-login">Login</a>
            <a href="jsp/register.jsp" class="btn-register">Register</a>
            <% } %>
        </div>
        <!-- Mobile Menu Toggle and Theme Toggle -->
        <button class="mobile-menu-toggle" aria-label="Toggle navigation">
            <i class="fas fa-bars"></i>
        </button>
        <div class="theme-toggle">
            <input type="checkbox" id="dark-mode-toggle" aria-label="Toggle dark mode">
            <label for="dark-mode-toggle">
                <i class="fas fa-moon"></i>
                <i class="fas fa-sun"></i>
            </label>
        </div>
    </div>
</header>

<!-- Main Content -->
<main>
    <!-- Hero Section -->
    <section id="home" class="hero">
        <div class="container">
            <h1 class="fade-in-up">Welcome to QuizLoco</h1>
            <p class="fade-in-up">Challenge yourself, learn, and have fun with our interactive quizzes and study
                tools!</p>

        </div>
    </section>


    <!-- Features Section -->
    <section id="features" class="features">
        <div class="container">
            <h2>Why Choose QuizLoco?</h2>
            <div class="features-grid">
                <div class="feature fade-in">
                    <i class="fas fa-brain"></i>
                    <h3>Diverse Topics</h3>
                    <p>Explore a wide range of subjects and challenge your knowledge.</p>
                </div>
                <div class="feature fade-in">
                    <i class="fas fa-chart-line"></i>
                    <h3>Track Progress</h3>
                    <p>Monitor your improvement with detailed performance analytics.</p>
                </div>
                <div class="feature fade-in">
                    <i class="fas fa-users"></i>
                    <h3>Compete & Collaborate</h3>
                    <p>Join study groups and compete on global leaderboards.</p>
                </div>
                <div class="feature fade-in">
                    <i class="fas fa-mobile-alt"></i>
                    <h3>Mobile Friendly</h3>
                    <p>Practice anytime, anywhere with our responsive design.</p>
                </div>
            </div>
        </div>
    </section>



    <!-- Latest Quizzes Section -->
    <section id="latest-quizzes" class="latest-quizzes">
        <div class="container">
            <h2>Latest Quizzes</h2>
            <div class="quiz-grid">
                <div class="quiz-card fade-in">
                    <img src="${pageContext.request.contextPath}/img/math.jpg" alt="Basic Algebra" loading="lazy">
                    <div class="quiz-content">
                        <h3>Basic Algebra</h3>
                        <p>Test your foundational algebra skills with this engaging quiz.</p>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=1" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
                <div class="quiz-card fade-in">
                    <img src="${pageContext.request.contextPath}/img/history.jpg" alt="World History" loading="lazy">
                    <div class="quiz-content">
                        <h3>World History</h3>
                        <p>Challenge your knowledge of global historical events.</p>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=2" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
                <div class="quiz-card fade-in">
                    <img src="${pageContext.request.contextPath}/img/math.jpg" alt="Basic Chemistry" loading="lazy">
                    <div class="quiz-content">
                        <h3>Basic Chemistry</h3>
                        <p>Assess your understanding of fundamental chemistry concepts.</p>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=3" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
                <div class="quiz-card fade-in">
                    <img src="${pageContext.request.contextPath}/img/literature.jpg" alt="English Literature"
                         loading="lazy">
                    <div class="quiz-content">
                        <h3>English Literature</h3>
                        <p>Explore classic and contemporary works in English literature.</p>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=4" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/AllQuizzesServlet" class="btn-view-all">View All Quizzes</a>
        </div>
    </section>

    <!-- Study Modes Section -->
    <section id="study-modes" class="study-modes">
        <div class="container">
            <h2>Multiple Ways to Learn</h2>
            <div class="mode-grid">
                <div class="mode-card fade-in">
                    <i class="fas fa-clock"></i>
                    <h3>Timed Quizzes</h3>
                    <p>Test your knowledge under pressure with our timed quiz mode.</p>
                </div>
                <div class="mode-card fade-in">
                    <i class="fas fa-users"></i>
                    <h3>Multiplayer Challenges</h3>
                    <p>Compete with friends or random opponents in real-time quiz battles.</p>
                </div>
                <div class="mode-card fade-in">
                    <i class="fas fa-graduation-cap"></i>
                    <h3>Adaptive Learning</h3>
                    <p>Our AI-powered system adjusts questions based on your performance.</p>
                </div>
                <div class="mode-card fade-in">
                    <i class="fas fa-sticky-note"></i>
                    <h3>Flashcards</h3>
                    <p>Review key concepts with our digital flashcard system.</p>
                </div>
            </div>
        </div>
    </section>



    <!-- Featured Quizzes Carousel -->
    <section id="featured-quizzes" class="featured-quizzes">
        <div class="container">
            <h2>Featured Quizzes</h2>
            <div class="carousel fade-in">
                <div class="carousel-slide">
                    <img src="${pageContext.request.contextPath}/img/physics.jpg" alt="Advanced Physics" loading="lazy">
                    <div class="carousel-caption">
                        <h3>Advanced Physics</h3>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=5" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
                <div class="carousel-slide">
                    <img src="${pageContext.request.contextPath}/img/geography.jpg" alt="World Geography"
                         loading="lazy">
                    <div class="carousel-caption">
                        <h3>World Geography</h3>
                        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=6" class="btn-quiz">Take
                            Quiz</a>
                    </div>
                </div>
            </div>
            <button class="carousel-control prev" aria-label="Previous Slide"><i class="fas fa-chevron-left"></i>
            </button>
            <button class="carousel-control next" aria-label="Next Slide"><i class="fas fa-chevron-right"></i></button>
        </div>
    </section>

    <!-- Testimonials Section -->
    <section id="testimonials" class="testimonials">
        <div class="container">
            <h2>What Our Users Say</h2>
            <div class="testimonial-slider fade-in">
                <div class="testimonial">
                    <p>"QuizLoco has revolutionized the way I study. The variety of subjects and quiz types keeps me
                        engaged and motivated!"</p>
                    <cite>- Sarah K., Student</cite>
                </div>
                <div class="testimonial">
                    <p>"As a teacher, I love using QuizLoco to create interactive quizzes for my students. It's so easy
                        to use!"</p>
                    <cite>- Mr. Johnson, High School Teacher</cite>
                </div>
                <div class="testimonial">
                    <p>"The multiplayer challenges are addictive! I've made new friends while improving my
                        knowledge."</p>
                    <cite>- Alex M., Quiz Enthusiast</cite>
                </div>
            </div>
        </div>
    </section>

    <!-- FAQ Section -->
    <section id="faq" class="faq">
        <div class="container">
            <h2>Frequently Asked Questions</h2>
            <div class="faq-accordion">
                <div class="faq-item">
                    <button class="faq-question" aria-expanded="false" aria-controls="faq1">How do I create a quiz?
                    </button>
                    <div class="faq-answer" id="faq1" aria-hidden="true">
                        <p>To create a quiz, navigate to the "Create Your Own Quiz" section, choose your subject, and
                            start adding questions. You can customize various settings like time limits, question types,
                            and more.</p>
                    </div>
                </div>
                <div class="faq-item">
                    <button class="faq-question" aria-expanded="false" aria-controls="faq2">Can I track my progress?
                    </button>
                    <div class="faq-answer" id="faq2" aria-hidden="true">
                        <p>Yes! Once you log in, you can access your dashboard where you can view your quiz history,
                            scores, and progress over time.</p>
                    </div>
                </div>
                <div class="faq-item">
                    <button class="faq-question" aria-expanded="false" aria-controls="faq3">Is QuizLoco free to use?
                    </button>
                    <div class="faq-answer" id="faq3" aria-hidden="true">
                        <p>QuizLoco offers both free and premium features. While many of our core functionalities are
                            free, premium subscriptions unlock additional features like advanced analytics and exclusive
                            quiz content.</p>
                    </div>
                </div>
                <div class="faq-item">
                    <button class="faq-question" aria-expanded="false" aria-controls="faq4">How can teachers benefit
                        from QuizLoco?
                    </button>
                    <div class="faq-answer" id="faq4" aria-hidden="true">
                        <p>Teachers can create customized quizzes, assign them to their classes, track student
                            performance, and gain insights into areas where students may need additional support.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>


    <!-- CTA Section -->
    <section id="cta" class="cta">
        <div class="container">
            <h2>Ready to Boost Your Knowledge?</h2>
            <p>Join millions of learners and quiz enthusiasts on QuizLoco today!</p>
            <a href="jsp/register.jsp" class="cta-button">Sign Up Now</a>
        </div>
    </section>
</main>

<!-- Footer Section -->
<footer id="contact">
    <div class="container">
        <div class="footer-content">
            <div class="footer-section">
                <h3>Quick Links</h3>
                <ul>
                    <li><a href="#home">Home</a></li>
                    <li><a href="#features">Features</a></li>
                    <li><a href="#latest-quizzes">Latest Quizzes</a></li>
                    <li><a href="#faq">FAQ</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div>
            <div class="footer-section">
                <h3>Contact Us</h3>
                <p>Email: huynphe186731@fpt.edu.vn</p>
                <p>Phone: 0965-915-509</p>
            </div>
        </div>
        <p class="copyright">&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
