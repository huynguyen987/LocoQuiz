<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Competition Exam - QuizLoco</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common2.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <!-- Pass contextPath to JavaScript -->
    <script type="text/javascript">
        var contextPath = '<%= request.getContextPath() %>';
    </script>
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>
<main>
    <div class="competition-exam-container">
        <h1>Competition Exam</h1>
        <div id="exam-details" class="mb-4">
            <!-- Exam details sẽ được tải bằng JavaScript -->
            <p>Loading exam details...</p>
        </div>
        <button id="start-exam-btn" class="btn btn-success" style="display: none;">
            <i class="fas fa-play"></i> Start Exam
        </button>
    </div>
</main>
<%@ include file="components/footer.jsp" %>

<!-- Include Bootstrap JS và các script tùy chỉnh -->
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/competition-exam.js"></script>
</body>
</html>
