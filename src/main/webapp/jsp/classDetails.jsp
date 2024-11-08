<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.UsersDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.CompetitionResultDAO" %>
<%@ page import="java.security.cert.Extension" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/jsp/components/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/classDetails.css">
<%
  classs classEntity = (classs) request.getAttribute("classEntity");
  List<Users> classmates = (List<Users>) request.getAttribute("classmates");
  List<Competition> assignedQuizzes = (List<Competition>) request.getAttribute("assignedQuizzes");
  CompetitionResultDAO competitionResultDAO = new CompetitionResultDAO();
  UsersDAO usersDAO = new UsersDAO();
  int teacherId = classEntity.getTeacher_id();
  String teacherName = usersDAO.getUserById(teacherId).getUsername();
  System.out.println("Teacher name: " + teacherName);
  Users currentUser = (Users) request.getAttribute("currentUser");
  System.out.println("Current user view class: " + currentUser.getUsername());

    request.setAttribute("classEntity", classEntity);
    request.setAttribute("classmates", classmates);
    request.setAttribute("assignedQuizzes", assignedQuizzes);
    request.setAttribute("teacherName", teacherName);
//    if user has taken competition, do not put it in the list assignedQuizzes
    List<Competition> assignedQuizzesTemp = new ArrayList<>();
    for (Competition competition : assignedQuizzes) {
      if (!competitionResultDAO.hasUserTakenCompetition(currentUser.getId(), competition.getId())) {
            assignedQuizzesTemp.add(competition);
        }
    }
    request.setAttribute("assignedQuizzes", assignedQuizzesTemp);
%>

<div class="container">
  <h2>Class Details</h2>

  <div class="class-details-container">
    <!-- Class Information -->
    <div class="class-info">
      <h3>Class Information</h3>
      <p><strong>Name:</strong> <c:out value="${classEntity.name}" /></p>
      <p><strong>Description:</strong> <c:out value="${classEntity.description}" /></p>
      <p><strong>Teacher:</strong> <c:out value="${teacherName}" /></p>
    </div>

    <!-- Classmates -->
    <div class="classmates">
      <h3>Classmates (<c:out value="${classmates.size()}" />)</h3>
      <c:if test="${not empty classmates}">
        <ul class="list">
          <c:forEach var="student" items="${classmates}">
            <li><c:out value="${student.username}" /></li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty classmates}">
        <p>No classmates found.</p>
      </c:if>
    </div>

    <!-- Assigned Competitions -->
    <div class="assigned-competitions">
      <h3>Assigned Competitions</h3>
      <c:if test="${not empty assignedQuizzes}">
        <ul class="list">
          <c:forEach var="competition" items="${assignedQuizzes}">
            <li>
              <strong>Competition:</strong> <c:out value="${competition.name}" /><br>
              <a href="<%= request.getContextPath() %>/TakeCompetitionController?competitionId=<c:out value='${competition.id}' />" class="button">
                <i class="fas fa-tasks"></i> Take Quiz
              </a>
            </li>
          </c:forEach>
        </ul>
      </c:if>
      <c:if test="${empty assignedQuizzes}">
        <p>No competitions assigned.</p>
      </c:if>
    </div>

    <!-- Back Button -->
    <div class="action-buttons">
      <a href="<%= request.getContextPath() %>/jsp/student.jsp?action=Classrooms" class="button back-btn">
        <i class="fas fa-arrow-left"></i> Back to Classes
      </a>
    </div>
  </div>
</div>

<%@ include file="/jsp/components/footer.jsp" %>

<!-- Optional: Include any specific JavaScript for class details -->
<script src="<%= request.getContextPath() %>/js/classDetails.js"></script>
