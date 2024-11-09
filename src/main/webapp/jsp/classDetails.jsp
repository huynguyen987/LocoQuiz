<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.CompetitionResultDAO" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="dao.CompetitionDAO" %>
<%@ page import="java.sql.SQLException" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/jsp/components/header.jsp" %>
<%@ include file="/jsp/components/sidebar.jsp" %>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Font Awesome for Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classDetails.css">

<%
  // Retrieve and set attributes
  classs classEntity = (classs) request.getAttribute("classEntity");
  List<Users> classmates = (List<Users>) request.getAttribute("classmates");
  CompetitionResultDAO competitionResultDAO = new CompetitionResultDAO();
  UsersDAO usersDAO = new UsersDAO();
  int teacherId = classEntity.getTeacher_id();
  String teacherName = usersDAO.getUserById(teacherId).getUsername();
  System.out.println("Teacher name: " + teacherName);
  Users currentUser = (Users) request.getAttribute("currentUser");
  if (currentUser == null) {
    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    return;
  }
  System.out.println("Current user view class: " + currentUser.getUsername());

  // Fetch Assigned Quizzes
  List<quiz> assignedQuizzes = null;
  try {
    QuizDAO quizDAO = new QuizDAO();
    assignedQuizzes = quizDAO.getAssignedQuizzesByClassId(classEntity.getId());
  } catch (SQLException | ClassNotFoundException e) {
    e.printStackTrace();
    assignedQuizzes = null;
  }

  // Fetch Assigned Competitions
  List<Competition> assignedCompetitions = null;
    CompetitionDAO competitionDAO = new CompetitionDAO();
    assignedCompetitions = competitionDAO.getCompetitionByClassId(classEntity.getId());

    // Filter out competitions the user has already taken
  List<Competition> filteredCompetitions = new ArrayList<>();
  if (assignedCompetitions != null) {
    for (Competition competition : assignedCompetitions) {
      if (!competitionResultDAO.hasUserTakenCompetition(currentUser.getId(), competition.getId())) {
        filteredCompetitions.add(competition);
      }
    }
  }

  System.out.println("User has not taken competitions: " + filteredCompetitions.size());

  // Set attributes for JSP
  request.setAttribute("classEntity", classEntity);
  request.setAttribute("classmates", classmates);
  request.setAttribute("assignedQuizzes", assignedQuizzes); // Pass quizzes directly
  request.setAttribute("assignedCompetitions", filteredCompetitions); // Pass filtered competitions
  request.setAttribute("teacherName", teacherName);
%>

<div class="container-fluid">
  <div class="row">
    <!-- Sidebar Column (Included via sidebar.jsp) -->

    <!-- Main Content Column -->
    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
      <div class="container my-5">
        <h2 class="mb-4 text-center">Class Details</h2>

        <div class="row">
          <!-- Class Information -->
          <div class="col-lg-4 mb-4">
            <div class="card h-100 shadow-sm">
              <div class="card-header bg-primary text-white">
                <h4>Class Information</h4>
              </div>
              <div class="card-body">
                <p><strong>Name:</strong> <c:out value="${classEntity.name}" /></p>
                <p><strong>Description:</strong> <c:out value="${classEntity.description}" /></p>
                <p><strong>Teacher:</strong> <c:out value="${teacherName}" /></p>
              </div>
            </div>
          </div>

          <!-- Classmates -->
          <div class="col-lg-4 mb-4">
            <div class="card h-100 shadow-sm">
              <div class="card-header bg-success text-white">
                <h4>Classmates (<c:out value="${classmates.size()}" />)</h4>
              </div>
              <div class="card-body">
                <c:if test="${not empty classmates}">
                  <ul class="list-group list-group-flush">
                    <c:forEach var="student" items="${classmates}">
                      <li class="list-group-item"><c:out value="${student.username}" /></li>
                    </c:forEach>
                  </ul>
                </c:if>
                <c:if test="${empty classmates}">
                  <p class="text-muted">No classmates found.</p>
                </c:if>
              </div>
            </div>
          </div>

          <!-- Assigned Quizzes -->
          <div class="col-lg-4 mb-4">
            <div class="card h-100 shadow-sm">
              <div class="card-header bg-warning text-white">
                <h4>Assigned Quizzes</h4>
              </div>
              <div class="card-body">
                <c:if test="${not empty assignedQuizzes}">
                  <ul class="list-group list-group-flush">
                    <c:forEach var="quiz" items="${assignedQuizzes}">
                      <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                          <h5 class="mb-1"><c:out value="${quiz.name}" /></h5>
                          <p class="mb-1"><c:out value="${quiz.description}" /></p>
                        </div>
                        <form action="${pageContext.request.contextPath}/TakeQuizServlet" method="get" class="mb-0">
                          <input type="hidden" name="id" value="<c:out value='${quiz.id}' />">
                          <button type="submit" class="btn btn-primary btn-sm">
                            <i class="fas fa-pencil-alt"></i> Take Quiz
                          </button>
                        </form>
                      </li>
                    </c:forEach>
                  </ul>
                </c:if>
                <c:if test="${empty assignedQuizzes}">
                  <p class="text-muted">No quizzes assigned for this class.</p>
                </c:if>
              </div>
            </div>
          </div>
        </div>

        <!-- Assigned Competitions -->
        <div class="row mb-4">
          <div class="col-12">
            <div class="card shadow-sm">
              <div class="card-header bg-info text-white">
                <h4>Assigned Competitions</h4>
              </div>
              <div class="card-body">
                <c:if test="${not empty assignedCompetitions}">
                  <div class="list-group">
                    <c:forEach var="competition" items="${assignedCompetitions}">
                      <div class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                          <h5 class="mb-1"><c:out value="${competition.name}" /></h5>
                          <p class="mb-1"><c:out value="${competition.description}" /></p>
                        </div>
                        <a href="${pageContext.request.contextPath}/TakeCompetitionController?competitionId=${competition.id}" class="btn btn-success btn-sm">
                          <i class="fas fa-tasks"></i> Take Competition
                        </a>
                      </div>
                    </c:forEach>
                  </div>
                </c:if>
                <c:if test="${empty assignedCompetitions}">
                  <p class="text-muted">No competitions assigned or you've already completed them.</p>
                </c:if>
              </div>
            </div>
          </div>
        </div>

        <!-- Back Button -->
        <div class="text-center">
          <a href="${pageContext.request.contextPath}/jsp/student.jsp?action=Classrooms" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Back to Classes
          </a>
        </div>
      </div>
    </main>
  </div>
</div>

<%@ include file="/jsp/components/footer.jsp" %>

<!-- Bootstrap JS and dependencies -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Optional: Include any specific JavaScript for class details -->
<script src="${pageContext.request.contextPath}/js/classDetails.js"></script>
