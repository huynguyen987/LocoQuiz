<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Phân tích Dữ liệu - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
    <div class="dashboard-content">
        <h1>Phân tích Dữ liệu</h1>

        <!-- Thống kê tổng quan -->
        <div class="row">
            <div class="col-md-3">
                <div class="stat-card panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Tổng số Quiz</h3>
                    </div>
                    <div class="panel-body">
                        <p>${totalQuizzes}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Tổng số lần làm Quiz</h3>
                    </div>
                    <div class="panel-body">
                        <p>${totalAttempts}</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Điểm trung bình</h3>
                    </div>
                    <div class="panel-body">
                        <p><fmt:formatNumber value="${averageScore}" type="number" maxFractionDigits="2"/></p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Thời gian làm bài trung bình (phút)</h3>
                    </div>
                    <div class="panel-body">
                        <p><fmt:formatNumber value="${averageTime}" type="number" maxFractionDigits="2"/></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Biểu đồ xu hướng điểm số -->
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Xu hướng Điểm số theo Thời gian</h3>
                    </div>
                    <div class="panel-body">
                        <canvas id="scoreTrendChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bảng phân tích điểm số theo ngày -->
        <div class="row">
            <div class="col-md-12">
                <h2>Điểm trung bình theo ngày</h2>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Ngày</th>
                        <th>Điểm Trung bình</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="entry" items="${scoreTrends}">
                        <tr>
                            <td><fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatNumber value="${entry.value}" type="number" maxFractionDigits="2"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Nút quay lại Dashboard -->
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/jsp/teacher.jsp" class="btn btn-default">
                <i class="fas fa-arrow-left"></i> Quay lại Dashboard
            </a>
        </div>
    </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- Bootstrap JS và jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- Script vẽ biểu đồ -->
<script>
    // Dữ liệu biểu đồ từ JSP
    const labels = [
        <c:forEach var="entry" items="${scoreTrends}">
            '<fmt:formatDate value="${entry.key}" pattern="yyyy-MM-dd"/>',
        </c:forEach>
    ];

    const data = [
        <c:forEach var="entry" items="${scoreTrends}">
            ${entry.value},
        </c:forEach>
    ];

    const ctx = document.getElementById('scoreTrendChart').getContext('2d');
    const scoreTrendChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Điểm trung bình',
                data: data,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                fill: true,
                tension: 0.1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        suggestedMax: 100
                    }
                }]
            }
        }
    });
</script>

</body>
</html>
