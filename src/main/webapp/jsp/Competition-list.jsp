<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Competition List - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
    <div class="dashboard-content">
        <h1>Your Competitions</h1>

        <!-- Hiển thị thông điệp -->
        <c:if test="${not empty param.message}">
            <c:choose>
                <c:when test="${param.message == 'competitionCreated'}">
                    <div class="success-message">Competition created successfully.</div>
                </c:when>
                <c:when test="${param.message == 'competitionUpdated'}">
                    <div class="success-message">Competition updated successfully.</div>
                </c:when>
                <c:when test="${param.message == 'competitionDeleted'}">
                    <div class="success-message">Competition deleted successfully.</div>
                </c:when>
                <c:otherwise>
                    <div class="info-message">${param.message}</div>
                </c:otherwise>
            </c:choose>
        </c:if>

        <a href="${pageContext.request.contextPath}/CompetitionController?action=create" class="action-btn">
            <i class="fas fa-plus"></i> Create New Competition
        </a>

        <c:if test="${not empty competitions}">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Class</th>
                    <th>Quiz</th>
                    <th>Time Limit (minutes)</th>
                    <th>Question Count</th>
                    <th>Shuffle</th>
                    <th>Access Time</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="competition" items="${competitions}">
                    <tr>
                        <td>${competition.id}</td>
                        <td>${competition.name}</td>
                        <td>${competition.description}</td>
                        <td>${competition.classId}</td>
                        <td>${competition.quizId}</td>
                        <td>${competition.timeLimit / 60}</td>
                        <td>${competition.questionCount}</td>
                        <td><c:choose>
                            <c:when test="${competition.shuffleQuestions}">Yes</c:when>
                            <c:otherwise>No</c:otherwise>
                        </c:choose>
                        </td>
                        <td>${competition.accessStartTime} to ${competition.accessEndTime}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/CompetitionController?action=view&id=${competition.id}" class="action-btn view-btn">
                                <i class="fas fa-eye"></i> View
                            </a>
                            <a href="${pageContext.request.contextPath}/CompetitionController?action=edit&id=${competition.id}" class="action-btn edit-btn">
                                <i class="fas fa-edit"></i> Edit
                            </a>
                            <a href="${pageContext.request.contextPath}/CompetitionController?action=delete&id=${competition.id}" class="action-btn delete-btn" onclick="return confirm('Are you sure you want to delete this competition?');">
                                <i class="fas fa-trash-alt"></i> Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty competitions}">
            <p>You have not created any competitions yet.</p>
        </c:if>
    </div>
</main>

<%@ include file="components/footer.jsp" %>
</body>
</html>
