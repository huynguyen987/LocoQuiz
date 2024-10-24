<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, entity.classs, dao.ClassDAO, dao.UsersDAO, dao.ClassUserDAO, dao.ClassQuizDAO, dao.QuizDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.JoinRequest" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Teacher Dashboard - QuizLoco</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
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
            if (currentUser == null || !"teacher".equalsIgnoreCase(currentUser.getRoleName())) {
                response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
                return;
            }

            // Fetch teacher's classes
            ClassDAO classDAO = new ClassDAO();
            List<classs> classes = null;
            try {
                classes = classDAO.getClassByTeacherId(currentUser.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Determine action
            String action = request.getParameter("action");
            if (action == null) {
                action = "dashboard"; // Default action
            }

            // Message for notifications
            String message = request.getParameter("message");
        %>

        <!-- Display messages -->
        <% if (message != null) { %>
        <% if ("classCreated".equals(message)) { %>
        <div class="success-message">Class created successfully.</div>
        <% } else if ("classEdited".equals(message)) { %>
        <div class="success-message">Class edited successfully.</div>
        <% } else if ("quizAssigned".equals(message)) { %>
        <!-- Popup Modal for Quiz Assignment Success -->
        <div id="quizAssignedModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <i class="fas fa-check-circle modal-icon success-icon"></i>
                <h2>Quiz Assigned Successfully!</h2>
                <p>The quiz has been successfully assigned to the class.</p>
            </div>
        </div>
        <% } else if ("studentEnrolled".equals(message)) { %>
        <!-- Popup Modal for Student Enrollment Success -->
        <div id="studentEnrolledModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <i class="fas fa-user-plus modal-icon success-icon"></i>
                <h2>Student Enrolled Successfully!</h2>
                <p>The student has been successfully enrolled in the class.</p>
            </div>
        </div>
        <% } else if ("joinRequestApproved".equals(message)) { %>
        <div class="success-message">Join request approved successfully.</div>
        <% } else if ("joinRequestRejected".equals(message)) { %>
        <div class="success-message">Join request rejected successfully.</div>
        <% } else if ("studentRemoved".equals(message)) { %>
        <div class="success-message">Student removed from class successfully.</div>
        <% } else if ("createError".equals(message)) { %>
        <div class="error-message">An error occurred while creating the class. Please try again.</div>
        <% } else if ("editError".equals(message)) { %>
        <div class="error-message">An error occurred while editing the class. Please try again.</div>
        <% } else if ("assignError".equals(message)) { %>
        <div class="error-message">An error occurred while assigning the quiz. Please try again.</div>
        <% } else if ("enrollError".equals(message)) { %>
        <div class="error-message">An error occurred while enrolling the student. Please try again.</div>
        <% } else if ("approveError".equals(message)) { %>
        <div class="error-message">An error occurred while approving the join request. Please try again.</div>
        <% } else if ("rejectError".equals(message)) { %>
        <div class="error-message">An error occurred while rejecting the join request. Please try again.</div>
        <% } else if ("removeError".equals(message)) { %>
        <div class="error-message">An error occurred while removing the student. Please try again.</div>
        <% } else { %>
        <div class="info-message"><%= message %></div>
        <% } %>
        <% } %>

        <% if ("dashboard".equals(action)) { %>
        <!-- Dashboard View -->
        <h1>Teacher Dashboard</h1>
        <!-- Dashboard Statistics -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-chalkboard"></i></div>
                <div class="stat-info">
                    <h3>Total Classes</h3>
                    <p><%= classes != null ? classes.size() : 0 %></p>
                </div>
                <!-- View Details Button -->
                <div class="stat-actions">
                    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=viewAllClasses" class="view-button">
                        <i class="fas fa-eye"></i> View Details
                    </a>
                </div>
            </div>
            <!-- You can add more stat cards for students, quizzes, etc. -->
        </div>

        <!-- Action Links -->
        <div class="action-links">
            <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=createClass" class="action-btn">
                <i class="fas fa-plus"></i> Create New Class
            </a>
        </div>

        <!-- Classes List -->
        <section class="classes-section">
            <h2>Your Classes</h2>
            <% if (classes != null && !classes.isEmpty()) { %>
            <div class="classes-grid">
                <% for (classs classEntity : classes) { %>
                <div class="class-card">
                    <h3><%= classEntity.getName() %></h3>
                    <div class="class-actions">
                        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="view-button">
                            <i class="fas fa-eye"></i> View Details
                        </a>
                        <!-- Optional: Add more action buttons here, e.g., Edit, Delete -->
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <p>You have not created any classes yet.</p>
            <% } %>
        </section>
        <% } else if ("viewAllClasses".equals(action)) { %>
        <!-- View All Classes Action -->
        <h1>All Your Classes</h1>
        <section class="classes-section">
            <% if (classes != null && !classes.isEmpty()) { %>
            <div class="classes-grid">
                <% for (classs classEntity : classes) { %>
                <div class="class-card">
                    <h3><%= classEntity.getName() %></h3>
                    <div class="class-actions">
                        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="view-button">
                            <i class="fas fa-eye"></i> View Details
                        </a>
                        <!-- Optional: Add more action buttons here, e.g., Edit, Delete -->
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <p>You have not created any classes yet.</p>
            <% } %>
        </section>
        <!-- Back to Dashboard Link -->
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
        <% } else if ("classDetails".equals(action)) { %>
        <!-- Class Details View -->
        <%
            String classIdStr = request.getParameter("classId");
            if (classIdStr == null || classIdStr.trim().isEmpty()) {
                out.println("<p>Class ID is missing.</p>");
                return;
            }

            int classId;
            try {
                classId = Integer.parseInt(classIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.println("<p>Invalid Class ID.</p>");
                return;
            }

            classs classEntity = classDAO.getClassById(classId);
            if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
                UsersDAO usersDAO = new UsersDAO();
                ClassUserDAO classUserDAO = new ClassUserDAO();
                ClassQuizDAO classQuizDAO = new ClassQuizDAO();
                Users teacher = usersDAO.getUserById(classEntity.getTeacher_id());
                List<Users> students = classUserDAO.getUsersByClassId(classId);
                List<quiz> quizzes = classQuizDAO.getQuizzesByClassId(classId);
                List<JoinRequest> pendingRequests = classUserDAO.getPendingJoinRequests(classId);
        %>
        <h1>Class Details: <%= classEntity.getName() %></h1>
        <div class="class-details-container">
            <!-- Class Information Card -->
            <div class="card">
                <h2>Class Information</h2>
                <p><strong>Description:</strong> <%= classEntity.getDescription() %></p>
                <p><strong>Class Code:</strong> <%= classEntity.getClass_key() %></p>
                <button class="copy-button" data-clipboard-text="<%= classEntity.getClass_key() %>">
                    <i class="fas fa-copy"></i> Copy Class Code
                </button>
                <p><strong>Teacher:</strong> <%= teacher.getUsername() %></p>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <a href="<%= request.getContextPath() %>/EditClassServlet?classId=<%= classEntity.getId() %>" class="button">
                    <i class="fas fa-edit"></i> Edit Class
                </a>
                <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=assignQuiz&classId=<%= classEntity.getId() %>" class="button">
                    <i class="fas fa-clipboard-list"></i> Assign Quiz
                </a>
                <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=enrollStudents&classId=<%= classEntity.getId() %>" class="button">
                    <i class="fas fa-user-plus"></i> Enroll Students
                </a>
                <form action="<%=request.getContextPath()%>/DeleteClassServlet" method="post" class="delete-form">
                    <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
                    <button type="submit" class="delete-button">
                        <i class="fas fa-trash"></i> Delete Class
                    </button>
                </form>
            </div>

            <!-- Pending Join Requests Section -->
            <div class="card">
                <h2>Pending Join Requests</h2>
                <% if (pendingRequests != null && !pendingRequests.isEmpty()) { %>
                <ul class="list">
                    <% for (JoinRequest request1 : pendingRequests) { %>
                    <li>
                        <%= request1.getUsername() %> (<%= request1.getEmail() %>)
                        <form action="<%= request.getContextPath() %>/HandleJoinRequestServlet" method="post" class="inline-form">
                            <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
                            <input type="hidden" name="userId" value="<%= request1.getUserId() %>">
                            <button type="submit" name="action" value="approve" class="approve-button">Approve</button>
                            <button type="submit" name="action" value="reject" class="reject-button">Reject</button>
                        </form>
                    </li>
                    <% } %>
                </ul>
                <% } else { %>
                <p>No pending join requests.</p>
                <% } %>
            </div>

            <!-- Students Section -->
            <div class="card">
                <h2>Enrolled Students</h2>
                <% if (students != null && !students.isEmpty()) { %>
                <ul class="list">
                    <% for (Users student : students) { %>
                    <li>
                        <%= student.getUsername() %> (<%= student.getEmail() %>)
                        <form action="<%= request.getContextPath() %>/RemoveStudentServlet" method="post" class="inline-form">
                            <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
                            <input type="hidden" name="userId" value="<%= student.getId() %>">
                            <button type="submit" class="remove-button">
                                <i class="fas fa-user-minus"></i> Remove
                            </button>
                        </form>
                    </li>
                    <% } %>
                </ul>
                <% } else { %>
                <p>No students enrolled in this class yet.</p>
                <% } %>
            </div>

            <!-- Quizzes Section -->
            <div class="card">
                <h2>Assigned Quizzes</h2>
                <% if (quizzes != null && !quizzes.isEmpty()) { %>
                <ul class="list">
                    <% for (quiz quiz : quizzes) { %>
                    <li><%= quiz.getName() %></li>
                    <% } %>
                </ul>
                <% } else { %>
                <p>No quizzes assigned to this class yet.</p>
                <% } %>
            </div>
        </div>

        <!-- Back to Dashboard Link -->
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
        <%
        } else {
        %>
        <p>Class not found or access denied.</p>
        <%
            }
        %>
        <% } else if ("assignQuiz".equals(action)) { %>
        <!-- Assign Quiz View -->
        <%
            String classIdStr = request.getParameter("classId");
            if (classIdStr == null || classIdStr.trim().isEmpty()) {
                out.println("<p>Class ID is missing.</p>");
                return;
            }

            int classId;
            try {
                classId = Integer.parseInt(classIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.println("<p>Invalid Class ID.</p>");
                return;
            }

            classs classEntity = null;
            try {
                classEntity = classDAO.getClassById(classId);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
                // Get quizzes
                QuizDAO quizDAO = new QuizDAO();
                List<quiz> quizzes = quizDAO.getQuizzesByUserId(currentUser.getId());
        %>
        <h1>Assign Quiz to Class: <%= classEntity.getName() %></h1>
        <form action="<%=request.getContextPath()%>/AssignQuizServlet" method="post" class="form-container">
            <input type="hidden" name="classId" value="<%= classEntity.getId() %>">

            <label for="quizId">Select Quiz:</label>
            <select id="quizId" name="quizId" required>
                <% for (quiz quiz : quizzes) { %>
                <option value="<%= quiz.getId() %>"><%= quiz.getName() %></option>
                <% } %>
            </select>

            <button type="submit" class="submit-btn">
                <i class="fas fa-clipboard-list"></i> Assign Quiz
            </button>
        </form>
        <% if ("assignError".equals(message)) { %>
        <p class="error-message">An error occurred while assigning the quiz. Please try again.</p>
        <% } %>

        <!-- Back to Class Details Link -->
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Class Details
        </a>
        <%
        } else {
        %>
        <p>Class not found or access denied.</p>
        <%
            }
        %>
        <% } else if ("enrollStudents".equals(action)) { %>
        <!-- Enroll Students View -->
        <%
            String classIdStr = request.getParameter("classId");
            if (classIdStr == null || classIdStr.trim().isEmpty()) {
                out.println("<p>Class ID is missing.</p>");
                return;
            }

            int classId;
            try {
                classId = Integer.parseInt(classIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                out.println("<p>Invalid Class ID.</p>");
                return;
            }

            classs classEntity = classDAO.getClassById(classId);
            if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
                // Get students not enrolled
                UsersDAO usersDAO = new UsersDAO();
                List<Users> students = usersDAO.getAllStudentsNotInClass(classId);
        %>
        <h1>Enroll Students to Class: <%= classEntity.getName() %></h1>
        <form action="<%=request.getContextPath()%>/EnrollStudentServlet" method="post" class="form-container">
            <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
            <label for="studentId">Select Student:</label>
            <select id="studentId" name="studentId" required>
                <% for (Users student : students) { %>
                <option value="<%= student.getId() %>"><%= student.getUsername() %> (<%= student.getEmail() %>)</option>
                <% } %>
            </select>

            <button type="submit" class="submit-btn">
                <i class="fas fa-user-plus"></i> Enroll Student
            </button>
        </form>
        <% if ("enrollError".equals(message)) { %>
        <p class="error-message">An error occurred while enrolling the student. Please try again.</p>
        <% } %>
        <% if ("studentEnrolled".equals(message)) { %>
        <!-- Popup Modal for Student Enrollment Success -->
        <div id="studentEnrolledModal" class="modal">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <i class="fas fa-user-plus modal-icon success-icon"></i>
                <h2>Student Enrolled Successfully!</h2>
                <p>The student has been successfully enrolled in the class.</p>
            </div>
        </div>
        <% } %>

        <!-- Back to Class Details Link -->
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Class Details
        </a>
        <%
        } else {
        %>
        <p>Class not found or access denied.</p>
        <%
            }
        %>
        <% } else if ("createClass".equals(action)) { %>
        <!-- Create Class View -->
        <h1>Create New Class</h1>
        <form action="<%=request.getContextPath()%>/CreateClassServlet" method="post" class="form-container">
            <label for="name">Class Name:</label>
            <input type="text" id="name" name="name" required>

            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea>

            <button type="submit" class="submit-btn">
                <i class="fas fa-plus"></i> Create Class
            </button>
        </form>
        <% if ("createError".equals(message)) { %>
        <p class="error-message">An error occurred while creating the class. Please try again.</p>
        <% } %>

        <!-- Back to Dashboard Link -->
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
        <% } else { %>
        <!-- Default or Unknown Action -->
        <p>Invalid action specified.</p>
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
            <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
        <% } %>
    </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
</body>
</html>
