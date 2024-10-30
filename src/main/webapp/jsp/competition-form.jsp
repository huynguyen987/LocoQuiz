<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Competition Form - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
    <div class="dashboard-content">
        <h1><c:choose>
            <c:when test="${not empty competition}">Edit Competition</c:when>
            <c:otherwise>Create New Competition</c:otherwise>
        </c:choose>
        </h1>

        <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/CompetitionController" method="post" class="form-container">
            <c:if test="${not empty competition}">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="${competition.id}">
            </c:if>
            <c:if test="${empty competition}">
                <input type="hidden" name="action" value="create">
            </c:if>

            <label for="name">Competition Name:</label>
            <input type="text" id="name" name="name" value="${competition.name}" required>

            <label for="description">Description:</label>
            <textarea id="description" name="description">${competition.description}</textarea>

            <label for="classId">Select Class:</label>
            <select id="classId" name="classId" required>
                <option value="">-- Select Class --</option>
                <c:forEach var="classEntity" items="${classes}">
                    <c:choose>
                        <c:when test="${not empty competition && competition.classId == classEntity.id}">
                            <option value="${classEntity.id}" selected>${classEntity.name} (Key: ${classEntity.classKey})</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${classEntity.id}">${classEntity.name} (Key: ${classEntity.classKey})</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <label for="quizId">Select Quiz:</label>
            <select id="quizId" name="quizId" required>
                <option value="">-- Select Quiz --</option>
                <c:forEach var="quizEntity" items="${quizzes}">
                    <c:choose>
                        <c:when test="${not empty competition && competition.quizId == quizEntity.id}">
                            <option value="${quizEntity.id}" selected>${quizEntity.name}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${quizEntity.id}">${quizEntity.name}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>

            <label for="timeLimit">Time Limit (minutes):</label>
            <input type="number" id="timeLimit" name="timeLimit" min="1" value="${competition.timeLimit / 60}" required>

            <label for="questionCount">Number of Questions:</label>
            <input type="number" id="questionCount" name="questionCount" min="1" value="${competition.questionCount}" required>

            <label for="shuffle">Shuffle Questions:</label>
            <input type="checkbox" id="shuffle" name="shuffle" <c:if test="${competition.shuffleQuestions}">checked</c:if>>

            <label for="accessStartTime">Access Start Time:</label>
            <input type="datetime-local" id="accessStartTime" name="accessStartTime" value="${competition.accessStartTime}" required>

            <label for="accessEndTime">Access End Time:</label>
            <input type="datetime-local" id="accessEndTime" name="accessEndTime" value="${competition.accessEndTime}" required>

            <button type="submit" class="submit-btn">
                <c:choose>
                    <c:when test="${not empty competition}">
                        <i class="fas fa-edit"></i> Update Competition
                    </c:when>
                    <c:otherwise>
                        <i class="fas fa-plus"></i> Create Competition
                    </c:otherwise>
                </c:choose>
            </button>
        </form>

        <a href="${pageContext.request.contextPath}/CompetitionController?action=list" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Competition List
        </a>
    </div>
</main>

<%@ include file="components/footer.jsp" %>
</body>
</html>
