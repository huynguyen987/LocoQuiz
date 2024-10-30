<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Competition Details - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
  <div class="dashboard-content">
    <h1>Competition Details</h1>

    <c:if test="${not empty competition}">
      <div class="competition-detail">
        <h2>${competition.name}</h2>
        <p><strong>Description:</strong> ${competition.description}</p>
        <p><strong>Class ID:</strong> ${competition.classId}</p>
        <p><strong>Quiz ID:</strong> ${competition.quizId}</p>
        <p><strong>Time Limit:</strong> ${competition.timeLimit / 60} minutes</p>
        <p><strong>Number of Questions:</strong> ${competition.questionCount}</p>
        <p><strong>Shuffle Questions:</strong> <c:choose>
          <c:when test="${competition.shuffleQuestions}">Yes</c:when>
          <c:otherwise>No</c:otherwise>
        </c:choose></p>
        <p><strong>Access Time:</strong> ${competition.accessStartTime} to ${competition.accessEndTime}</p>
        <p><strong>Created At:</strong> ${competition.createdAt}</p>
      </div>
      <div class="competition-actions">
        <a href="${pageContext.request.contextPath}/CompetitionController?action=edit&id=${competition.id}" class="action-btn edit-btn">
          <i class="fas fa-edit"></i> Edit
        </a>
        <a href="${pageContext.request.contextPath}/CompetitionController?action=delete&id=${competition.id}" class="action-btn delete-btn" onclick="return confirm('Are you sure you want to delete this competition?');">
          <i class="fas fa-trash-alt"></i> Delete
        </a>
      </div>
    </c:if>
    <c:if test="${empty competition}">
      <p>Competition not found.</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/CompetitionController?action=list" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Competition List
    </a>
  </div>
</main>

<%@ include file="components/footer.jsp" %>
</body>
</html>
