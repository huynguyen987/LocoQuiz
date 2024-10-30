<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="entity.classs, entity.quiz" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Configure Competition - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<h1>Create New Competition</h1>
<form action="${pageContext.request.contextPath}/TakeCompetitionServlet" method="post" class="form-container">
  <label for="name">Competition Name:</label>
  <input type="text" id="name" name="name" required>

  <label for="description">Description:</label>
  <textarea id="description" name="description"></textarea>

  <label for="classId">Select Class:</label>
  <select id="classId" name="classId" required>
    <c:if test="${not empty classes}">
      <c:forEach var="classEntity" items="${classes}">
        <option value="${classEntity.id}">${classEntity.name}</option>
      </c:forEach>
    </c:if>
    <c:if test="${empty classes}">
      <option value="">No classes available</option>
    </c:if>
  </select>

  <label for="quizId">Select Quiz:</label>
  <select id="quizId" name="quizId" required>
    <c:if test="${not empty quizzes}">
      <c:forEach var="quizEntity" items="${quizzes}">
        <option value="${quizEntity.id}">${quizEntity.name}</option>
      </c:forEach>
    </c:if>
    <c:if test="${empty quizzes}">
      <option value="">No quizzes available</option>
    </c:if>
  </select>

  <label for="timeLimit">Time Limit (minutes):</label>
  <input type="number" id="timeLimit" name="timeLimit" min="1" required>

  <label for="questionCount">Number of Questions:</label>
  <input type="number" id="questionCount" name="questionCount" min="1" required>

  <label for="shuffle">Shuffle Questions:</label>
  <input type="checkbox" id="shuffle" name="shuffle">

  <label for="accessStartTime">Access Start Time:</label>
  <input type="datetime-local" id="accessStartTime" name="accessStartTime" required>

  <label for="accessEndTime">Access End Time:</label>
  <input type="datetime-local" id="accessEndTime" name="accessEndTime" required>

  <button type="submit" class="submit-btn">
    <i class="fas fa-plus"></i> Create Competition
  </button>
</form>

<!-- Hiển thị thông báo lỗi nếu có -->
<c:if test="${not empty errorMessage}">
  <p class="error-message">${errorMessage}</p>
</c:if>

<!-- Back to Dashboard Link -->
<a href="${pageContext.request.contextPath}/jsp/teacher.jsp?action=dashboard" class="action-btn back-btn">
  <i class="fas fa-arrow-left"></i> Back to Dashboard
</a>
</body>
</html>
