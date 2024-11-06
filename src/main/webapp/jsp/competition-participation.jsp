<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Competition Participation - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/comp.css">

  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
  <!-- Pass contextPath to JavaScript -->
  <script type="text/javascript">
    var contextPath = '<%= request.getContextPath() %>';
  </script>
</head>
<body>

<main>
  <div class="competition-container">
    <h1>Competition Details</h1>
    <div id="competition-details" class="mb-4">
      <!-- Competition details sẽ được tải bằng JavaScript -->
      <p>Loading competition details...</p>
    </div>
    <button id="start-competition-btn" class="btn btn-primary" style="display: none;">
      <i class="fas fa-play"></i> Start Competition
    </button>
  </div>
</main>


<!-- Include Bootstrap JS và các script tùy chỉnh -->
<script src="<%= request.getContextPath() %>/js/bootstrap.bundle.min.js"></script>
<script src="<%= request.getContextPath() %>/js/competition-participation.js"></script>
</body>
</html>
