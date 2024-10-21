package Servlet;

import Module.AnswersReader;
import dao.QuizDAO;
import entity.quiz;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "TakeQuizServlet", value = "/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {

    public void shuffleList(List<AnswersReader> list) {
        Collections.shuffle(list);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if (service == null || service.isEmpty()) {
            // Forward to appropriate JSP based on quiz type
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Missing quizId parameter.");
                return;
            }

            int quizId;
            try {
                quizId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid quizId parameter.");
                return;
            }

            // Retrieve quiz data
            QuizDAO quizDAO = new QuizDAO();
            quiz quizData = null;
            try {
                quizData = quizDAO.getQuizById(quizId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Database error.");
                return;
            }

            if (quizData == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Quiz not found.");
                return;
            }

            // Parse answerJson to get total number of questions
            ObjectMapper mapper = new ObjectMapper();
            List<AnswersReader> allQuestions = null;
            try {
                String answerJson = quizData.getAnswer();
                try {
                    allQuestions = mapper.readValue(answerJson, mapper.getTypeFactory().constructCollectionType(List.class, AnswersReader.class));
                } catch (IOException e) {
                    // If not a list, try as a map
                    Map<String, AnswersReader> answerMap = mapper.readValue(answerJson, mapper.getTypeFactory().constructMapType(Map.class, String.class, AnswersReader.class));
                    allQuestions = new ArrayList<>(answerMap.values());
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error parsing quiz data.");
                return;
            }

            int maxQuestionCount = (allQuestions != null) ? allQuestions.size() : 0;

            // Set attributes for JSP
            request.setAttribute("quizId", id);
            request.setAttribute("maxQuestionCount", maxQuestionCount);

            // Check the type_id
            int typeId = quizData.getType_id();

            if (typeId == 3) {
                // If type_id == 2, forward to match-quiz.jsp
                request.getRequestDispatcher("jsp/match-quiz.jsp").forward(request, response);
            } else if (typeId == 1) {
                // If type_id == 1, forward to quiz.jsp
                request.getRequestDispatcher("jsp/quiz.jsp").forward(request, response);
            } else {
                // Unsupported type_id
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Unsupported quiz type.");
            }
            return;
        }

        // Handle "loadQuiz" service
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(true);

        if ("loadQuiz".equals(service)) {
            String quizIdStr = request.getParameter("id");
            if (quizIdStr == null || quizIdStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing quizId parameter.\"}");
                return;
            }

            int quizId;
            try {
                quizId = Integer.parseInt(quizIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid quizId parameter.\"}");
                return;
            }

            // Read 'time' parameter
            String timeStr = request.getParameter("time");
            int timeLimit;
            if (timeStr != null && !timeStr.isEmpty()) {
                try {
                    timeLimit = Integer.parseInt(timeStr);
                    if (timeLimit > 3600) {
                        timeLimit = 3600;
                    }
                } catch (NumberFormatException e) {
                    timeLimit = 15 * 60; // Default 15 minutes
                }
            } else {
                timeLimit = 15 * 60; // Default 15 minutes
            }

            // Read 'questionCount' parameter
            String questionCountStr = request.getParameter("questionCount");
            int questionCount = -1; // Default to all questions
            if (questionCountStr != null && !questionCountStr.isEmpty()) {
                try {
                    questionCount = Integer.parseInt(questionCountStr);
                } catch (NumberFormatException e) {
                    questionCount = -1; // Invalid, use all questions
                }
            }

            // Read 'shuffle' parameter
            String shuffleStr = request.getParameter("shuffle");
            boolean shuffleQuestions = false; // Default no shuffle
            if (shuffleStr != null && !shuffleStr.isEmpty()) {
                shuffleQuestions = Boolean.parseBoolean(shuffleStr);
            }

            // Retrieve quiz data
            QuizDAO quizDAO = new QuizDAO();
            quiz quizData = null;
            try {
                quizData = quizDAO.getQuizById(quizId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error.\"}");
                return;
            }

            if (quizData == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Quiz not found.\"}");
                return;
            }

            // Parse answerJson to get all questions
            ObjectMapper mapper = new ObjectMapper();
            List<AnswersReader> allQuestions = null;
            try {
                String answerJson = quizData.getAnswer();
                try {
                    allQuestions = mapper.readValue(answerJson, mapper.getTypeFactory().constructCollectionType(List.class, AnswersReader.class));
                } catch (IOException e) {
                    // If not a list, try as a map
                    Map<String, AnswersReader> answerMap = mapper.readValue(answerJson, mapper.getTypeFactory().constructMapType(Map.class, String.class, AnswersReader.class));
                    allQuestions = new ArrayList<>(answerMap.values());
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Error parsing quiz data.\"}");
                return;
            }

            if (allQuestions == null || allQuestions.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("[]");
                return;
            }

            // Shuffle if required
            if (shuffleQuestions) {
                shuffleList(allQuestions);
            }

//            shuffleOptions(allQuestions);
            for (AnswersReader question : allQuestions) {
                question.shuffleOptions();
            }

            // Validate and limit questionCount
            int totalAvailableQuestions = allQuestions.size();
            if (questionCount < 1 || questionCount > totalAvailableQuestions) {
                questionCount = totalAvailableQuestions;
            }

            // Limit the number of questions
            if (questionCount > 0 && questionCount < allQuestions.size()) {
                allQuestions = allQuestions.subList(0, questionCount);
            }

            int totalQuestions = allQuestions.size();

            // Store correct answers in session
            session.setAttribute("correctAnswers", allQuestions);

            // Prepare data to send to client
            QuizResponse quizResponse = new QuizResponse();
//            quizResponse.setTitle(quizData.getTitle());
            quizResponse.setTimeLimit(timeLimit);
            quizResponse.setTotalQuestions(totalQuestions);
            quizResponse.setQuestions(allQuestions);

            // Convert to JSON and send response
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonQuiz = mapper.writeValueAsString(quizResponse);
            response.getWriter().write(jsonQuiz);
        }
    }

    private List<AnswersReader> parseAnswerJson(String answerJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<AnswersReader> allQuestions = null;
        try {
            allQuestions = mapper.readValue(answerJson, mapper.getTypeFactory().constructCollectionType(List.class, AnswersReader.class));
        } catch (IOException e) {
            try {
                Map<String, AnswersReader> answerMap = mapper.readValue(answerJson, mapper.getTypeFactory().constructMapType(Map.class, String.class, AnswersReader.class));
                allQuestions = new ArrayList<>(answerMap.values());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return allQuestions;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // If needed, handle POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // DTO class to send data to client
    public static class QuizResponse {
        private String title;
        private int timeLimit; // time in seconds
        private int totalQuestions;
        private List<AnswersReader> questions;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        public int getTotalQuestions() {
            return totalQuestions;
        }

        public void setTotalQuestions(int totalQuestions) {
            this.totalQuestions = totalQuestions;
        }

        public List<AnswersReader> getQuestions() {
            return questions;
        }

        public void setQuestions(List<AnswersReader> questions) {
            this.questions = questions;
        }
    }
}
