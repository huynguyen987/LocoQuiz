<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Users" %>
<%
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  List<quiz> libraryQuizzes = (List<quiz>) request.getAttribute("libraryQuizzes");

    String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quiz Library</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/library.css">
  <script src="<%= request.getContextPath() %>/js/library.js" defer></script>
  <style>
    .btn-back-home {
      position: absolute;
      top: 20px;
      left: 20px;
      padding: 10px 20px;
      background-color: #007bff;
      color: #fff;
      text-decoration: none;
      border-radius: 4px;
      font-weight: 500;
      font-size: 1rem;
      transition: background-color 0.3s ease, color 0.3s ease;
    }

    .btn-back-home:hover {
      background-color: #0056b3;
      color: #f8f9fa;
    }


  </style>
</head>
<body>
<div class="library-container">
  <header>
    <a href="#" id="backToHome" class="btn-back-home">Back to Home</a>
    <h1>Quiz Library</h1>
    <div class="controls">
      <select id="sort-box" class="sort-box">
        <option value="az">Sort A - Z</option>
        <option value="latest">Latest Quizzes</option>
        <option value="newest">Newest Added</option>
      </select>
      <input type="text" id="search-box" class="search-box" placeholder="Search quizzes...">
      <a href="<%= request.getContextPath() %>/AllQuizzesServlet" id="add-quiz" class="add-quiz-btn">Add More Quiz</a>
    </div>
  </header>
  <div id="quiz-cards-container" class="quiz-cards-container">
    <%
      if (libraryQuizzes != null && !libraryQuizzes.isEmpty()) {
        for (quiz quizItem : libraryQuizzes) {
    %>
    <div class="quiz-card"
         data-created-at="<%= quizItem.getCreated_at() %>"
         data-added-at="<%= quizItem.getAddedAt() %>">
      <h2 class="quiz-title"><%= quizItem.getName() %></h2>
      <p class="quiz-description"><%= quizItem.getDescription() %></p>
      <div class="quiz-actions">
        <!-- Thay đổi tham số từ quizId thành id -->
        <a href="<%= request.getContextPath() %>/jsp/quiz-details.jsp?id=<%= quizItem.getId() %>" class="view-quiz-btn">View Quiz</a>
        <form action="<%= request.getContextPath() %>/RemoveFromLibraryServlet" method="post" style="display:inline;">
          <input type="hidden" name="quizId" value="<%= quizItem.getId() %>">
          <button type="submit" class="remove-quiz-btn">Remove</button>
        </form>
      </div>
    </div>
    <%
      }
    } else {
    %>
    <p>Your library is empty.</p>
    <%
      }
    %>
  </div>
</div>
</body>

<script>
  document.getElementById('backToHome').addEventListener('click', function(event) {
    event.preventDefault();
    var role = '<%= role %>';
    if (role === 'student') {
      window.location.href = '<%= request.getContextPath() %>/jsp/student.jsp';
    } else if (role === 'teacher') {
      window.location.href = '<%= request.getContextPath() %>/jsp/teacher.jsp';
    } else if (role === 'admin') {
      window.location.href = '<%= request.getContextPath() %>/jsp/admin.jsp';
    } else {
      window.location.href = '<%= request.getContextPath() %>/index.jsp';
    }
  });
</script>

</html>
