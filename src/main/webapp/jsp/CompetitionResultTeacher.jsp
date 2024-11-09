<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/jsp/components/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/competition_result.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

<div class="container">
    <h2>Kết Quả Cuộc Thi</h2>

    <!-- Display messages -->
    <c:if test="${not empty param.message}">
        <div class="message">
            <c:choose>
                <c:when test="${param.message == 'resetSuccess'}">
                    Kết quả đã được reset thành công.
                </c:when>
                <c:when test="${param.message == 'resetFailed'}">
                    Không thể reset kết quả.
                </c:when>
                <c:when test="${param.message == 'exportFailed'}">
                    Không thể xuất kết quả.
                </c:when>
                <c:otherwise>
                    ${param.message}
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty competitionResults}">
            <h3>Chưa có ai tham gia cuộc thi này.</h3>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên Sinh Viên</th>
                    <th>Điểm</th>
                    <th>Thời Gian (giây)</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="competitionResult" items="${competitionResults}" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                            <c:out value="${usersMap[competitionResult.userId].username}" default="Unknown"/>
                        </td>
                        <td>${competitionResult.score}</td>
                        <td>${competitionResult.timeTaken}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/ResetCompetitionResultServlet" method="post">
                                <input type="hidden" name="competitionId" value="${competition.id}" />
                                <input type="hidden" name="userId" value="${competitionResult.userId}" />
                                <button type="submit">Reset Kết Quả</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- Export Button -->
            <form method="get" action="${pageContext.request.contextPath}/ExportCompetitionResultsServlet">
                <input type="hidden" name="competitionId" value="${competition.id}" />
                <button type="submit">Xuất kết quả ra Excel</button>
            </form>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
