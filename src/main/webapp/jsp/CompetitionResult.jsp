<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/jsp/components/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/competition_result.css">

<div class="container">
    <h2>Kết Quả Cuộc Thi</h2>

    <c:choose>
        <c:when test="${empty competitionResults}">
            <h3>Bạn chưa tham gia cuộc thi này.</h3>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th>Tên Sinh Viên</th>
                    <th>Điểm</th>
                    <th>Thời Gian (giây)</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="competitionResult" items="${competitionResults}">
                    <tr>
                        <td>${student.username}</td>
                        <td>${competitionResult.score}</td>
                        <td>${competitionResult.timeTaken}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
