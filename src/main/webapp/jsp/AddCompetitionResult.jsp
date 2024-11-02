<!-- File: /jsp/addCompetitionResult.jsp -->
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="add-competition-result" style="margin: 20px;">
    <h2>Thêm Kết Quả Cuộc Thi</h2>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">
            <p>${errorMessage}</p>
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/AddCompetitionResultServlet" method="post">
        <input type="hidden" name="competitionId" value="${competitionId}" />
        <div>
            <label for="userId">ID Học Sinh:</label>
            <input type="number" id="userId" name="userId" required />
        </div>
        <div>
            <label for="classId">ID Lớp Học:</label>
            <input type="number" id="classId" name="classId" required />
        </div>
        <div>
            <label for="score">Điểm Số:</label>
            <input type="number" step="0.01" id="score" name="score" required />
        </div>
        <div style="margin-top: 10px;">
            <button type="submit">Thêm Kết Quả</button>
            <a href="${pageContext.request.contextPath}/CompetitionResultsServlet?competitionId=${competitionId}" style="margin-left: 20px;">Hủy</a>
        </div>
    </form>
</div>

<%@ include file="/jsp/components/footer.jsp" %>
