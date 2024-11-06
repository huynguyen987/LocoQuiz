<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="jakarta.servlet.ServletException" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.ClassDAO" %>
<%@ page import="entity.*" %>
<%@ page import="dao.UsersDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - QuizLoco</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
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
<%
    // Đảm bảo rằng currentUser được lấy từ session trước khi sử dụng
    Users currentUser = null;
    // HttpSession session = request.getSession(false); // Giữ lại nếu cần
    System.out.println("session: " + session);
    if (session != null) {
        currentUser = (Users) session.getAttribute("user");
    }
    if (currentUser != null) {
        System.out.println("Student view dashboard: " + currentUser.getUsername());
    }
%>

<main>
    <div class="dashboard-content">
        <%
            // Verify user
            if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
                response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
                return;
            }

            // Fetch latest user info
            Users user = null;
            try {
                user = new UsersDAO().getUserById(currentUser.getId());
            } catch (SQLException | ClassNotFoundException e) {
                throw new ServletException(e);
            }

            // Message for notifications
            String message = request.getParameter("message");
            String error = request.getParameter("error");

            // Get action parameter
            String action = request.getParameter("action");
        %>

        <% if ("Classrooms".equals(action)) { %>
        <%-- Fetch classes the student is enrolled in --%>
        <%
            List<classs> classList = null;
            try {
                ClassDAO classDAO = new ClassDAO();
                classList = classDAO.getApprovedClassesByStudent(user.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        %>

        <!-- My Classes Section -->
        <section id="my-classes">
            <h2>My Classes</h2>
            <div class="my-classes-container">
                <input type="text" id="searchInput" placeholder="Search by class name or teacher's name..." class="search-bar" onkeyup="filterClasses()">
                <div class="classes-grid" id="classesGrid">
                    <% if (classList != null && !classList.isEmpty()) {
                        for (classs cls : classList) {
                            // Lấy danh sách cuộc thi cho lớp học hiện tại
                            List<Competition> competitions = null;
                            try {
                                competitions = new ClassDAO().getCompetitionsByClassId(cls.getId());
                            } catch (SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                    %>
                    <div class="class-card">
                        <h3><%= cls.getName() %></h3>
                        <p><strong>Teacher:</strong> <%= cls.getTeacher_name() %></p>
                        <div class="class-actions">
                            <% if (competitions != null && !competitions.isEmpty()) { %>
                            <% for (Competition competition : competitions) { %>
                            <a href="<%= request.getContextPath() %>/jsp/competition-participation.jsp?competitionId=<%= competition.getId() %>" class="button">
                                <i class="fas fa-tasks"></i> Take Competition: <%= competition.getName() %>
                            </a>
                            <% } %>
                            <% } else { %>
                            <p>No competitions assigned.</p>
                            <% } %>
                        </div>
                    </div>
                    <% } } else { %>
                    <p>You are not enrolled in any classes.</p>
                    <% } %>
                </div>
            </div>
        </section>
        <% } else if ("viewClass".equals(action)) { %>
        <%
            // Get classId parameter
            String classIdParam = request.getParameter("classId");
            int classId = 0;
            if (classIdParam != null && !classIdParam.isEmpty()) {
                try {
                    classId = Integer.parseInt(classIdParam);
                } catch (NumberFormatException e) {
                    classId = 0;
                }
            }

            if (classId == 0) {
                response.sendRedirect(request.getContextPath() + "/jsp/student.jsp?action=Classrooms");
                return;
            }

            // Initialize DAOs
            ClassDAO classDAO = new ClassDAO();

            // Initialize variables
            classs classEntity = null;
            List<Users> classmates = null;
            List<Competition> assignedQuizzes = null;

            try {
                // Fetch class details
                classEntity = classDAO.getClassById(classId);

                // Fetch classmates
                classmates = classDAO.getStudentsByClassId(classId);

                // Fetch assigned competitions
                assignedQuizzes = classDAO.getCompetitionsByClassId(classId);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        %>

        <!-- Class Details Section -->
        <section id="class-details">
            <h2>Class Details</h2>
            <div class="class-details-container">
                <div class="class-info">
                    <h3>Class Information</h3>
                    <p><strong>Name:</strong> <%= classEntity != null ? classEntity.getName() : "N/A" %></p>
                    <p><strong>Description:</strong> <%= classEntity != null ? classEntity.getDescription() : "N/A" %></p>
                    <p><strong>Teacher:</strong> <%= classEntity != null ? classEntity.getTeacher_name() : "N/A" %></p>
                </div>
                <div class="classmates">
                    <h3>Classmates</h3>
                    <% if (classmates != null && !classmates.isEmpty()) { %>
                    <ul class="list">
                        <% for (Users student : classmates) { %>
                        <li><%= student.getUsername() %></li>
                        <% } %>
                    </ul>
                    <% } else { %>
                    <p>No classmates found.</p>
                    <% } %>
                </div>

                <!-- Assigned Competitions Section -->
                <div class="assigned-quizzes">
                    <h3>Assigned Competitions</h3>
                    <% if (assignedQuizzes != null && !assignedQuizzes.isEmpty()) { %>
                    <ul class="list">
                        <% for (Competition competition : assignedQuizzes) { %>
                        <li>
                            <strong>Competition:</strong> <%= competition.getName() %><br>
                            <strong>Time Limit:</strong> <%= competition.getTimeLimit() / 60 %> minutes
                            <!-- Sử dụng liên kết để tham gia cuộc thi -->
                            <a href="<%= request.getContextPath() %>/jsp/competition-participation.jsp?competitionId=<%= competition.getId() %>" class="button">
                                <i class="fas fa-tasks"></i> Take Competition
                            </a>
                        </li>
                        <% } %>
                    </ul>
                    <% } else { %>
                    <p>No competitions assigned.</p>
                    <% } %>
                </div>

                <div class="action-buttons">
                    <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=Classrooms" class="button back-btn">
                        <i class="fas fa-arrow-left"></i> Back to Classes
                    </a>
                </div>
            </div>
        </section>
        <% } else { %>
        <!-- Existing dashboard content -->

        <!-- Student Dashboard -->
        <section id="dashboard" class="dashboard">
            <% if (message != null) { %>
            <div class="success-message"><%= message %></div>
            <% } %>
            <% if (error != null) { %>
            <div class="error-message"><%= error %></div>
            <% } %>
            <h1>Welcome, <%= user.getUsername() %>!</h1>
            <p>Access your classes, quizzes, and track your progress all in one place.</p>

            <!-- Grid of Cards -->
            <div class="grid grid-2">
                <!-- My Classes Card -->
                <div class="card">
                    <h2>My Classes</h2>
                    <p>View your enrolled classes.</p>
                    <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=Classrooms" class="button">
                        <i class="fas fa-book"></i> View
                    </a>
                </div>

                <!-- Library -->
                <div class="card">
                    <h2>Library</h2>
                    <p>Library time, open the door!</p>
                    <a href="<%= request.getContextPath() %>/LibraryServlet" class="button">
                        <i class="fas fa-pencil-alt"></i> View Library
                    </a>
                </div>

                <!-- Join Class Card -->
                <div class="card">
                    <h2>Join A Class</h2>
                    <p>Join a class with your teacher's class code.</p>
                    <form action="<%= request.getContextPath() %>/SubmitJoinRequestServlet" method="POST" class="join-class-form">
                        <input type="text" name="classKey" placeholder="Enter Class Code" required>
                        <button type="submit" class="button">
                            <i class="fas fa-plus-circle"></i> Join
                        </button>
                    </form>
                </div>

                <!-- Take Quiz Card -->
                <div class="card">
                    <h2>Quizzes</h2>
                    <p>Participate in quizzes to test your knowledge.</p>
                    <a href="<%= request.getContextPath() %>/AllQuizzesServlet" class="button">
                        <i class="fas fa-tasks"></i> Take Quiz
                    </a>
                </div>
            </div>
        </section>
        <% } %>
    </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
<script src="<%= request.getContextPath() %>/js/student.js"></script>
<!-- New JavaScript for Search Functionality -->
<script>
    function filterClasses() {
        const input = document.getElementById('searchInput');
        const filter = input.value.toLowerCase();
        const classesGrid = document.getElementById('classesGrid');
        const classCards = classesGrid.getElementsByClassName('class-card');

        Array.from(classCards).forEach(card => {
            const className = card.getElementsByTagName('h3')[0].innerText.toLowerCase();
            const teacherName = card.getElementsByTagName('p')[0].innerText.toLowerCase();
            if (className.includes(filter) || teacherName.includes(filter)) {
                card.style.display = "";
            } else {
                card.style.display = "none";
            }
        });
    }
</script>
</body>
</html>
