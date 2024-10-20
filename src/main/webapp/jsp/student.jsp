<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, dao.UsersDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="jakarta.servlet.ServletException" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.classs" %>
<%@ page import="entity.quiz" %>
<%@ page import="dao.ClassDAO" %>
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

<main>
    <div class="dashboard-content">
        <%
            // Session and user authentication
            session = request.getSession(false);
             currentUser = null;
            if (session != null) {
                currentUser = (Users) session.getAttribute("user");
            }
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

            // Get action parameter
            String action = request.getParameter("action");
        %>

        <% if ("Classrooms".equals(action)) { %>
        <%-- Fetch classes the student is enrolled in --%>
        <%
            List<classs> classList = null;
            try {
                ClassDAO classDAO = new ClassDAO();
                classList = classDAO.getClassesByStudentId(user.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        %>


        <!-- Display the table with search bar -->
        <section id="my-classes">
            <h2>My Classes</h2>
            <input type="text" id="searchInput" placeholder="Search by class name or teacher's name...">
            <table id="classTable">
                <thead>
                <tr>
                    <th>Class Name</th>
                    <th>Teacher's Name</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% if (classList != null && !classList.isEmpty()) {
                    for (classs cls : classList) { %>
                <tr>
                    <td><%= cls.getName() %></td>
                    <td><%= cls.getTeacher_name() %></td>
                    <td>
                        <!-- View button -->
                        <form action="student.jsp" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="viewClass">
                            <input type="hidden" name="classId" value="<%= cls.getId() %>">
                            <button type="submit">View</button>
                        </form>
                    </td>
                </tr>
                <% } } else { %>
                <tr>
                    <td colspan="3">You are not enrolled in any classes.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
        <% } else if ("viewClass".equals(action)) { %>
        <%
            // Get classId parameter
            String classIdParam = request.getParameter("classId");
            int classId = Integer.parseInt(classIdParam);

            // Initialize DAOs
            ClassDAO classDAO = new ClassDAO();

            // Initialize variables
            classs classEntity = null;
            List<Users> classmates = null;
            List<quiz> assignedQuizzes = null;

            try {
                // Fetch class details
                classEntity = classDAO.getClassById(classId);

                // Fetch classmates
                classmates = classDAO.getStudentsByClassId(classId);

                // Fetch assigned quizzes
                assignedQuizzes = classDAO.getQuizzesByClassId(classId);

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        %>

        <!-- Display Class Details -->
        <section id="class-details">
            <h2>Class Details</h2>
            <div class="class-info">
                <h3>Class Information</h3>
                <p><strong>Name:</strong> <%= classEntity.getName() %></p>
                <p><strong>Description:</strong> <%= classEntity.getDescription() %></p>
                <p><strong>Teacher:</strong> <%= classEntity.getTeacher_name() %></p>
            </div>
            <div class="classmates">
                <h3>Classmates</h3>
                <% if (classmates != null && !classmates.isEmpty()) { %>
                <ul>
                    <% for (Users student : classmates) { %>
                    <li><%= student.getUsername() %></li>
                    <% } %>
                </ul>
                <% } else { %>
                <p>No classmates found.</p>
                <% } %>
            </div>
            <div class="assigned-quizzes">
                <h3>Assigned Quizzes</h3>
                <% if (assignedQuizzes != null && !assignedQuizzes.isEmpty()) { %>
                <ul>
                    <% for (quiz quiz : assignedQuizzes) { %>
                    <li><%= quiz.getName() %></li>
                    <% } %>
                </ul>
                <% } else { %>
                <p>No quizzes assigned.</p>
                <% } %>
            </div>
        </section>
        <% } else { %>
        <!-- Existing dashboard content -->

        <!-- Student Dashboard -->
        <section id="dashboard" class="dashboard">
            <% if (message != null) { %>
            <div class="success-message"><%= message %></div>
            <% } %>
            <h1>Welcome, <%= user.getUsername() %>!</h1>
            <p>Access your classes, quizzes, and track your progress all in one place.</p>

            <!-- Grid of Cards -->
            <div class="grid">
                <!-- My Classes Card -->
                <div class="card">
                    <h2>My Classes</h2>
                    <p>View your enrolled classes.</p>
                    <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=Classrooms" class="button">View</a>
                </div>

                <!-- Recent Quizzes Card -->
                <div class="card">
                    <h2>Recent Quizzes</h2>
                    <p>Continue where you left off.</p>
                    <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=viewRecentQuiz" class="button">View Quizzes</a>
                </div>

                <!-- Join Class -->
                <div class="card">
                    <h2>Join A Class</h2>
                    <p>Join class with your teacher class code</p>
                    <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=JoinClass" class="button">Join</a>
                </div>

                <!-- Take Quiz Card -->
                <div class="card">
                    <h2>Quizzes</h2>
                    <p>Participate in quizzes to test your knowledge.</p>
                    <a href="<%= request.getContextPath() %>/AllQuizzesServlet" class="button">Take Quiz</a>
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
</body>
</html>
