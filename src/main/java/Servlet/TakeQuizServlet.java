package Servlet;

import Module.AnswersReader;
import dao.QuizDAO;
import entity.quiz;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TakeQuizServlet", value = "/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if (service == null || service.isEmpty()) {
            // Forward to quiz.jsp
            String id = request.getParameter("id");
            request.setAttribute("quizId", id);
            request.getRequestDispatcher("jsp/quiz.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(true);

        if ("loadQuiz".equals(service)) {
            String quizIdStr = request.getParameter("id");
            int quizId = Integer.parseInt(quizIdStr);

            // Fetch quiz data using QuizDAO
            QuizDAO quizDAO = new QuizDAO();
            quiz quizData = null;
            try {
                quizData = quizDAO.getQuizById(quizId);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (quizData == null || quizData.getAnswer() == null) {
                // No quiz found or no answers available
                response.getWriter().write("[]");
                return;
            }

            // Parse the JSON 'answer' field into a List<AnswersReader>
            ObjectMapper mapper = new ObjectMapper();
            List<AnswersReader> quizQuestions = null;
            try {
                String answerJson = quizData.getAnswer();
                quizQuestions = mapper.readValue(answerJson, mapper.getTypeFactory().constructCollectionType(List.class, AnswersReader.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (quizQuestions == null || quizQuestions.isEmpty()) {
                response.getWriter().write("[]");
                return;
            }

            // Store correct answers in session
            session.setAttribute("correctAnswers", quizQuestions);

            // Send quiz questions to the client (without 'correct' field)
            String jsonQuiz = mapper.writeValueAsString(quizQuestions);
            response.getWriter().write(jsonQuiz);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
