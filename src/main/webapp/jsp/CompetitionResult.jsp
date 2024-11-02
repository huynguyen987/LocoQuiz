<!-- File: /jsp/competitionResults.jsp -->
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Đảm bảo rằng các thuộc tính đã được đặt trong servlet
    // Không cần thiết thiết lập lại tại đây
%>

<div class="competition-results" style="margin: 20px;">
    <h2>Kết Quả Cuộc Thi</h2>
    <c:if test="${empty competitionResults}">
        <p>Không có kết quả nào cho cuộc thi này.</p>
    </c:if>
    <c:if test="${not empty competitionResults}">
        <table border="1" cellpadding="10" cellspacing="0">
            <thead>
            <tr>
                <th>ID</th>
                <th>ID Cuộc Thi</th>
                <th>ID Học Sinh</th>
                <th>ID Lớp Học</th>
                <th>Điểm Số</th>
                <th>Thời Gian Tạo</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="result" items="${competitionResults}">
                <tr>
                    <td>${result.id}</td>
                    <td>${result.competitionId}</td>
                    <td>${result.userId}</td>
                    <td>${result.classId}</td>
                    <td>${result.score}</td>
                    <td>${result.createdAt}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <div class="actions" style="margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/CompetitionsServlet">Quay Lại Danh Sách Cuộc Thi</a>
    </div>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
