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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all-quizzes.css">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

  <!-- Include any additional CSS or JS here -->
</head>
<body>

<main class="container">
  <!-- Latest Quizzes Section -->
  <section id="latest-quizzes">
    <h2>Latest Quizzes</h2>
    <div class="quiz-carousel">
      <button class="carousel-control prev" onclick="scrollCarousel('latest', -1)">&#10094;</button>
      <div class="carousel-wrapper" id="latest-carousel">
        <% for (quiz q : latestQuizzes) { %>
        <div class="quiz-card">
          <h3><%= q.getName() %></h3>
          <p><%= q.getDescription() %></p>
          <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Detail</a>
        </div>
        <% } %>
      </div>
      <button class="carousel-control next" onclick="scrollCarousel('latest', 1)">&#10095;</button>
    </div>
  </section>

  <!-- Popular Quizzes Section -->
  <section id="popular-quizzes">
    <h2>Popular Quizzes</h2>
    <div class="quiz-carousel">
      <button class="carousel-control prev" onclick="scrollCarousel('popular', -1)">&#10094;</button>
      <div class="carousel-wrapper" id="popular-carousel">
        <% for (quiz q : popularQuizzes) { %>
        <div class="quiz-card">
          <h3><%= q.getName() %></h3>
          <p><%= q.getDescription() %></p>
          <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Detail</a>
        </div>
        <% } %>
      </div>
      <button class="carousel-control next" onclick="scrollCarousel('popular', 1)">&#10095;</button>
    </div>
  </section>

  <!-- All Quizzes Section -->
  <section id="all-quizzes">
    <h2>All Quizzes</h2>
    <div class="quiz-list">
      <% for (quiz q : allQuizzes) { %>
      <div class="quiz-card">
        <h3><%= q.getName() %></h3>
        <p><%= q.getDescription() %></p>
        <p><strong>Views:</strong> <%= q.getViews() %></p>
        <a href="${pageContext.request.contextPath}/jsp/quiz-details.jsp?id=<%= q.getId() %>" class="btn-quiz">View Detail</a>
      </div>
      <% } %>
    </div>

    <!-- Pagination Controls -->
    <div class="pagination">
      <% if (currentPage > 1) { %>
      <a href="AllQuizzesServlet?page=<%= currentPage - 1 %>" class="page-link">&laquo; Previous</a>
      <% } %>

      <% for (int i = 1; i <= totalPages; i++) { %>
      <a href="AllQuizzesServlet?page=<%= i %>" class="page-link <% if (i == currentPage) { %>active<% } %>"><%= i %></a>
      <% } %>

      <% if (currentPage < totalPages) { %>
      <a href="AllQuizzesServlet?page=<%= currentPage + 1 %>" class="page-link">Next &raquo;</a>
      <% } %>
    </div>
  </section>

</main>

<!-- Include any necessary JavaScript -->
<script>
  function scrollCarousel(section, direction) {
    const carousel = document.getElementById(section + '-carousel');
    const scrollWidth = carousel.offsetWidth;
    carousel.scrollBy({
      top: 0,
      left: direction * scrollWidth,
      behavior: 'smooth'
    });
  }
</script>


<style>
  /* Add necessary styles */
  .quiz-carousel {
    position: relative;
    display: flex;
    align-items: center;
    overflow: hidden;
    margin-bottom: 2rem;
  }
  .carousel-wrapper {
    display: flex;
    overflow-x: auto;
    scroll-behavior: smooth;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none;  /* Internet Explorer 10+ */
  }
  .carousel-wrapper::-webkit-scrollbar {
    display: none; /* Safari and Chrome */
  }
  .quiz-card {
    min-width: 200px;
    margin: 0 1rem;
    background-color: #fff;
    padding: 1rem;
    border-radius: 5px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  }
  .carousel-control {
    background-color: rgba(0,0,0,0.5);
    color: #fff;
    border: none;
    font-size: 2rem;
    padding: 0.5rem;
    cursor: pointer;
  }
  .quiz-list {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
  }
  .btn-load-more {
    display: block;
    margin: 2rem auto;
    padding: 0.75rem 1.5rem;
    background-color: #3498db;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  .btn-load-more:hover {
    background-color: #2980b9;
  }
</style>
</body>
</html>
