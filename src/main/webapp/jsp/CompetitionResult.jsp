<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mt-5">
    <h2>Competition Results</h2>
    <p>Your score: ${score}%</p>
    <p>You answered correctly ${correctCount} out of ${totalQuestions} questions.</p>
    <a href="${pageContext.request.contextPath}/StudentDashboard.jsp" class="btn btn-primary">Back to Dashboard</a>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
