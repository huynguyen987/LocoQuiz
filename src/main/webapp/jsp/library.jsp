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
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Quiz Library</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
  <script src="<%= request.getContextPath() %>/js/library.js" defer></script>
</head>
<body>
<div class="library-container">
  <header>
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
</html>
