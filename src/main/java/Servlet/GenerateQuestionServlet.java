package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/generate-questions")
@MultipartConfig
public class GenerateQuestionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to generateQuestions.jsp
        request.getRequestDispatcher("/jsp/generateQuestions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get file and number of questions from request
        Part filePart = request.getPart("document");
        String numQuestions = request.getParameter("numQuestions");

        if (filePart == null || numQuestions == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Vui lòng cung cấp tài liệu và số lượng câu hỏi.");
            return;
        }

        // Gửi file và số lượng câu hỏi tới Flask server
        String flaskUrl = "http://127.0.0.1:5000/generate_questions_from_document";
        HttpURLConnection connection = (HttpURLConnection) new URL(flaskUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", filePart.getContentType());
        connection.setRequestProperty("numQuestions", numQuestions);

        try (OutputStream outputStream = connection.getOutputStream()) {
            IOUtils.copy(filePart.getInputStream(), outputStream);
        }

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            // Read data from Flask server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();

            // Convert JSON data to JSONArray
            JSONArray questions = new JSONArray(content.toString());

            // Save questions to session for Excel download
            HttpSession session = request.getSession();
            session.setAttribute("questions", questions);

            // Forward to displayQuestions.jsp to show the questions
            request.setAttribute("questions", questions);
            request.getRequestDispatcher("/jsp/displayQuestions.jsp").forward(request, response);
        } else {
            // Read error message from Flask server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            StringBuilder errorContent = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                errorContent.append(line);
            }
            in.close();

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorContent.toString());
        }
    }
}
