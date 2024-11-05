<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- No scriptlet imports or code --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/competition_result.css">
<c:choose>
    <c:when test="${empty competitionResults}">
        <div class="container">
            <h2>No one has participated in the competition</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container">
            <h2>Competition Result</h2>
            <table class="table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Student name</th>
                    <th>Score</th>
                    <th>Time taken</th>
                </tr>
                </thead>
                <tbody>
                    <%-- Iterate over competitionResults and display data --%>
                <c:forEach var="competitionResult" items="${competitionResults}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${student.username}</td>
                        <td>${competitionResult.score}</td>
                        <td>${competitionResult.timeTaken} seconds</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>


