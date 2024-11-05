<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- No scriptlet imports or code --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/competition_result.css">
<%-- Show results of all students in a class --%>
<c:choose>
    <c:when test="${empty competitionResults}">
        <div class="container">
            <h2>Chưa có ai tham gia cuộc thi</h2>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container">
            <h2>Kết quả cuộc thi</h2>
            <table class="table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên học sinh</th>
                    <th>Điểm</th>
                    <th>Thời gian làm bài</th>
                </tr>
                </thead>
                <tbody>
                    <%-- Iterate over competitionResults and display data --%>
                <c:forEach var="competitionResult" items="${competitionResults}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                            <c:if test="${usersMap[competitionResult.userId] != null}">
                                ${usersMap[competitionResult.userId].username}
                            </c:if>
                            <c:if test="${usersMap[competitionResult.userId] == null}">
                                Unknown Student
                            </c:if>
                        </td>
                        <td>${competitionResult.score}</td>
                        <td>${competitionResult.timeTaken} giây</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>


