<%--
  Created by IntelliJ IDEA.
  User: hient
  Date: 9/26/2024
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Quiz Questions - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%-- Include header --%>
<header>
    <!-- Header content -->
</header>

<main>
    <div class="container">
        <h2>Questions for Quiz ID: ${quizId}</h2>
        <c:choose>
            <c:when test="${empty questionList}">
                <p>No questions available for this quiz.</p>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/SubmitQuizServlet" method="post">
                    <!-- Thêm hidden field chứa quizId -->
                    <input type="hidden" name="quizId" value="${quizId}" />

                    <c:forEach var="question" items="${questionList}" varStatus="status">
                        <div class="question-block">
                            <p><strong>Question ${status.index + 1}:</strong> ${question.content}</p>
                            <c:forEach var="answer" items="${question.answers}" varStatus="ansStatus">
                                <div class="answer-option">
                                    <input type="radio" name="question_${question.id}" id="q${question.id}_a${ansStatus.index}" value="${answer}" required>
                                    <label for="q${question.id}_a${ansStatus.index}">${answer}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                    <button type="submit" class="btn-submit">Submit Quiz</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<%-- Include footer --%>
<footer id="contact">
    <!-- Footer content -->
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>

