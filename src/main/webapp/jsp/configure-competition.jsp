<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="entity.classs, entity.quiz" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Configure Competition - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<!-- Include Header -->
<%@ include file="components/header.jsp" %>

<!-- Main Content -->
<main>
  <div class="form-container">
    <h1>Create New Competition</h1>

    <!-- Form -->
    <form action="${pageContext.request.contextPath}/TakeCompetitionServlet" method="post">
      <!-- Competition Name -->
      <label for="name">Competition Name:</label>
      <input type="text" id="name" name="name" required>

      <!-- Description -->
      <label for="description">Description:</label>
      <textarea id="description" name="description" placeholder="Enter competition description..."></textarea>

      <!-- Select Class -->
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

      <!-- Select Quiz -->
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

      <!-- Time Limit -->
      <label for="timeLimit">Time Limit (minutes):</label>
      <input type="number" id="timeLimit" name="timeLimit" min="1" required>

      <!-- Number of Questions -->
      <label for="questionCount">Number of Questions:</label>
      <input type="number" id="questionCount" name="questionCount" min="1" required>

      <!-- Shuffle Questions -->
      <label for="shuffle">Shuffle Questions:</label>
      <input type="checkbox" id="shuffle" name="shuffle">

      <!-- Access Start Time -->
      <label for="accessStartTime">Access Start Time:</label>
      <input type="datetime-local" id="accessStartTime" name="accessStartTime" required>

      <!-- Access End Time -->
      <label for="accessEndTime">Access End Time:</label>
      <input type="datetime-local" id="accessEndTime" name="accessEndTime" required>

      <!-- Submit Button -->
      <button type="submit" class="submit-btn">
        <i class="fas fa-plus"></i> Create Competition
      </button>
    </form>

    <!-- Display Error Message if Any -->
    <c:if test="${not empty errorMessage}">
      <p class="error-message">${errorMessage}</p>
    </c:if>
  </div>
  <%--chỉnh close button xuống thấp hơn 1 ti--%>
</main>

<!-- Back to Dashboard Link -->
<div class="close-button">
  <a href="${pageContext.request.contextPath}/jsp/teacher.jsp?action=dashboard" class="button back-btn">
    <i class="fas fa-arrow-left"></i> Back to Dashboard
  </a>
  <style>
    .close-button {
      margin-top: 60px;
    }
  </style>
</div>

<!-- Include Footer -->
<%@ include file="components/footer.jsp" %>
</body>
</html>
