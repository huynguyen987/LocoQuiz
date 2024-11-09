<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.quiz" %>
<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    List<quiz> latestQuizzes = (List<quiz>) request.getAttribute("latestQuizzes");
    List<quiz> popularQuizzes = (List<quiz>) request.getAttribute("popularQuizzes");
    List<quiz> allQuizzes = (List<quiz>) request.getAttribute("allQuizzes");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalPages = (int) request.getAttribute("totalPages");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Quizzes - QuizLoco</title>
    <!-- External CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all-quizzes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>

<header class="header">
    <div class="header-container">
        <h1>QuizLoco</h1>
<%--        <nav>--%>
<%--            <ul>--%>
<%--                <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>--%>
<%--                <li><a href="#latest-quizzes">Latest</a></li>--%>
<%--                <li><a href="#popular-quizzes">Popular</a></li>--%>
<%--                <li><a href="#all-quizzes">All Quizzes</a></li>--%>
<%--            </ul>--%>
<%--        </nav>--%>
        <a href="#" id="backToHome" class="btn-back-home" data-role="<%= role %>">
            <i class="fas fa-home"></i> Back to Home
        </a>
    </div>
</header>

<main class="container">
    <!-- Latest Quizzes Section -->
    <section id="latest-quizzes" aria-labelledby="latest-quizzes-heading">
        <h2 id="latest-quizzes-heading">Latest Quizzes</h2>
        <div class="quiz-carousel">
            <button class="carousel-control prev" onclick="scrollCarousel('latest', -1)" aria-label="Previous Latest Quizzes">
                <i class="fas fa-chevron-left"></i>
            </button>
            <div class="carousel-wrapper" id="latest-carousel">
                <% for (quiz q : latestQuizzes) { %>
                <div class="quiz-card">
                    <h3><%= q.getName() %></h3>
                    <p><%= q.getDescription() %></p>
                    <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Details</a>
                </div>
                <% } %>
            </div>
            <button class="carousel-control next" onclick="scrollCarousel('latest', 1)" aria-label="Next Latest Quizzes">
                <i class="fas fa-chevron-right"></i>
            </button>
        </div>
    </section>

    <!-- Popular Quizzes Section -->
    <section id="popular-quizzes" aria-labelledby="popular-quizzes-heading">
        <h2 id="popular-quizzes-heading">Popular Quizzes</h2>
        <div class="quiz-carousel">
            <button class="carousel-control prev" onclick="scrollCarousel('popular', -1)" aria-label="Previous Popular Quizzes">
                <i class="fas fa-chevron-left"></i>
            </button>
            <div class="carousel-wrapper" id="popular-carousel">
                <% for (quiz q : popularQuizzes) { %>
                <div class="quiz-card">
                    <h3><%= q.getName() %></h3>
                    <p><%= q.getDescription() %></p>
                    <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Details</a>
                </div>
                <% } %>
            </div>
            <button class="carousel-control next" onclick="scrollCarousel('popular', 1)" aria-label="Next Popular Quizzes">
                <i class="fas fa-chevron-right"></i>
            </button>
        </div>
    </section>

    <!-- All Quizzes Section -->
    <section id="all-quizzes" aria-labelledby="all-quizzes-heading">
        <h2 id="all-quizzes-heading">All Quizzes</h2>
        <div class="quiz-list">
            <% for (quiz q : allQuizzes) { %>
            <div class="quiz-card">
                <h3><%= q.getName() %></h3>
                <p><%= q.getDescription() %></p>
                <p><strong>Views:</strong> <%= q.getViews() %></p>
                <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Details</a>
            </div>
            <% } %>
        </div>

        <!-- Pagination Controls -->
        <div class="pagination" aria-label="Quiz Pagination">
            <% if (currentPage > 1) { %>
            <a href="AllQuizzesServlet?page=<%= currentPage - 1 %>" class="page-link" aria-label="Previous Page">&laquo; Previous</a>
            <% } %>

            <% for (int i = 1; i <= totalPages; i++) { %>
            <a href="AllQuizzesServlet?page=<%= i %>" class="page-link <%= (i == currentPage) ? "active" : "" %>" aria-current="<%= (i == currentPage) ? "page" : "false" %>"><%= i %></a>
            <% } %>

            <% if (currentPage < totalPages) { %>
            <a href="AllQuizzesServlet?page=<%= currentPage + 1 %>" class="page-link" aria-label="Next Page">Next &raquo;</a>
            <% } %>
        </div>
    </section>
</main>

<footer class="footer">
    <p>&copy; <%= java.time.Year.now().getValue() %> QuizLoco. All rights reserved.</p>
</footer>

<!-- External JavaScript -->
<script src="${pageContext.request.contextPath}/js/all-quizzes.js"></script>

</body>
</html>
