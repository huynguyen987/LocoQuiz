<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Subject Lists - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
</head>
<body>
<%-- Include header --%>
<header>
    <div class="container">
        <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/index.jsp#about">About</a></li>
                <li><a href="${pageContext.request.contextPath}/index.jsp#services">Services</a></li>
                <li><a href="${pageContext.request.contextPath}/index.jsp#contact">Contact</a></li>
            </ul>
        </nav>

        <!-- Display login/register or user details based on session -->
        <div class="auth-links">
            <%
                String username = (String) session.getAttribute("username");
                if (username != null) {
            %>
            <!-- Display username and logout button when user is logged in -->
            <div class="user-info">
                <p>Hello, <%= username %>!</p>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
            </div>
            <%
            } else {
            %>
            <!-- Display login button if user is not logged in -->
            <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="btn-login">Login</a>
            <%
                }
            %>
        </div>

        <div class="mobile-menu-toggle">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</header>

<main>
    <div class="content-with-sidebar">
        <%-- Include sidebar --%>
        <aside class="sidebar">
            <h2>Quick Links</h2>
            <!-- Sidebar Links -->
            <ul>
                <li><a href="${pageContext.request.contextPath}/jsp/blog-lists.jsp">Blog Lists</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/quiz-lists.jsp">Quiz Lists</a></li>
                <li><a href="${pageContext.request.contextPath}/jsp/view-lessons.jsp">View Quizzes</a></li>
            </ul>

        </aside>
        <div class="main-content">
            <div class="container">
                <h2>Quiz Lists</h2>
                <p>Browse through the list of available quizzes:</p>
                <ul class="subject-list">
                    <li>Mathematics</li>
                    <li>Science</li>
                    <li>History</li>
                    <li>Geography</li>
                    <li>Language Arts</li>
                </ul>
            </div>
        </div> <!-- End of main-content -->
    </div> <!-- End of content-with-sidebar -->
</main>

<%-- Include footer --%>
<footer id="contact">
    <div class="container">
        <p>&copy; 2024 QuizLoco. All rights reserved.</p>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
