<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="entity.quiz, entity.Tag, entity.Users" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="java.util.List" %>
<%
  // Kiểm tra xem tham số "id" có tồn tại và hợp lệ hay không
  String idParam = request.getParameter("id");
  if (idParam == null || idParam.trim().isEmpty()) {
    out.println("<p>Invalid Quiz ID.</p>");
    return;
  }

  int quizId = 0;
  try {
    quizId = Integer.parseInt(idParam);
  } catch (NumberFormatException e) {
    out.println("<p>Invalid Quiz ID format.</p>");
    return;
  }

  QuizDAO quizDAO = new QuizDAO();
  quiz q = null;
  try {
    q = quizDAO.getQuizById(quizId);
  } catch (Exception e) {
    e.printStackTrace();
    out.println("<p>Error retrieving quiz details.</p>");
    return;
  }

  if (q == null) {
    out.println("<p>Quiz not found.</p>");
    return;
  }

  // Lấy thông tin người dùng từ session để kiểm tra quyền hạn
  Users currentUser = (Users) session.getAttribute("user");
  String userRole = (currentUser != null) ? currentUser.getRoleName() : "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><%= q.getName() %> - Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/quiz-details.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="<%= request.getContextPath() %>/css/fonts.css" rel="stylesheet">
  <!-- Google Fonts (Optional for better typography) -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
  <!-- Font Awesome for Icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
  <style>
    /* Bạn có thể thêm các kiểu CSS tùy chỉnh tại đây hoặc trong tệp quiz-details.css */
    .btn-edit, .btn-delete, .btn-quiz {
      display: inline-block;
      padding: 10px 20px;
      margin: 5px;
      text-decoration: none;
      color: #fff;
      border-radius: 4px;
      transition: background-color 0.3s ease;
    }
    .btn-edit {
      background-color: #007bff;
    }
    .btn-edit:hover {
      background-color: #0056b3;
    }
    .btn-delete {
      background-color: #dc3545;
    }
    .btn-delete:hover {
      background-color: #c82333;
    }
    .btn-quiz {
      background-color: #28a745;
    }
    .btn-quiz:hover {
      background-color: #218838;
    }
    .active {
      color: #28a745;
    }
    .inactive {
      color: #dc3545;
    }
    .toggle-description {
      cursor: pointer;
      color: #007bff;
      text-decoration: underline;
      font-size: 0.9em;
    }
  </style>
</head>
<body>

<main class="container">
  <div class="quiz-header">
    <h1><%= q.getName() %></h1>
    <div class="quiz-meta">
      <span><i class="fas fa-calendar-alt"></i> Created At: <%= q.getCreated_at() %></span>
      <span><i class="fas fa-sync-alt"></i> Updated At: <%= q.getUpdated_at() %></span>
      <span><i class="fas fa-eye"></i> Views: <span id="viewCount"><%= q.getViews() %></span></span>
      <span><i class="fas fa-tags"></i> Type: <%= q.getType_id() %></span>
      <span>
        <i class="fas fa-circle <% if(q.isStatus()) { %>active<% } else { %>inactive<% } %>"></i>
        <%= q.isStatus() ? "Active" : "Inactive" %>
      </span>
    </div>
  </div>

  <section class="quiz-details">
    <h2>Description <span id="toggleDesc" class="toggle-description"></span></h2>
    <p id="quizDescription"><%= q.getDescription() %></p>

    <!-- Display Tags -->
    <div class="quiz-tags">
      <h3>Tags:</h3>
      <ul>
        <%
          List<Tag> tags = q.getTag();
          if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
        %>
        <li>
          <a href="<%= request.getContextPath() %>/SearchByTagServlet?tag=<%= tag.getName() %>">
            <%= tag.getName() %>
          </a>
        </li>
        <%
          }
        } else {
        %>
        <li>No tags available.</li>
        <%
          }
        %>
      </ul>
    </div>

    <!-- Action Buttons -->
    <div class="quiz-actions">
      <a href="<%= request.getContextPath() %>/TakeQuizServlet?id=<%= q.getId() %>" class="btn-quiz">
        <i class="fas fa-play"></i> Take Quiz
      </a>
      <%
        // Display Edit and Delete buttons if user is admin or teacher
        if (currentUser != null) {
          String role = currentUser.getRoleName();
          if (role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("teacher"))) {
      %>
      <a href="<%= request.getContextPath() %>/EditQuizServlet?quizId=<%= q.getId() %>" class="btn-edit">
        <i class="fas fa-edit"></i> Edit Quiz
      </a>

      <a href="<%= request.getContextPath() %>/DeleteQuizServlet?id=<%= q.getId() %>" class="btn-delete" onclick="return confirm('Are you sure you want to delete this quiz?');">
        <i class="fas fa-trash-alt"></i> Delete Quiz
      </a>
      <%
          }
        }
      %>
    </div>
  </section>
</main>

<!-- Include JavaScript -->
<script src="<%= request.getContextPath() %>/js/quiz-details.js"></script>
</body>
</html>
