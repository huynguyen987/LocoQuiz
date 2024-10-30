package Servlet;

import dao.FlashcardDAO;
import entity.Flashcard;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "FlashcardController", urlPatterns = {"/FlashcardController"})
public class FlashcardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FlashcardDAO flashcardDAO = new FlashcardDAO();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Flashcard> flashcards = flashcardDAO.getAllFlashcards();
            JSONArray flashcardArray = new JSONArray();

            for (Flashcard flashcard : flashcards) {
                JSONObject flashcardJson = new JSONObject();
                flashcardJson.put("id", flashcard.getId());
                flashcardJson.put("frontText", flashcard.getFrontText());
                flashcardJson.put("backText", flashcard.getBackText());
                flashcardJson.put("category", flashcard.getCategory());
                flashcardArray.put(flashcardJson);
            }

            response.getWriter().write(flashcardArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FlashcardDAO flashcardDAO = new FlashcardDAO();
        String action = request.getParameter("action");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("add".equals(action)) {
                String frontText = request.getParameter("frontText");
                String backText = request.getParameter("backText");
                String category = request.getParameter("category");

                Flashcard flashcard = new Flashcard();
                flashcard.setFrontText(frontText);
                flashcard.setBackText(backText);
                flashcard.setCategory(category);

                int id = flashcardDAO.insertFlashcard(flashcard);
                if (id != -1) {
                    flashcard.setId(id);
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("id", id);
                    responseJson.put("frontText", frontText);
                    responseJson.put("backText", backText);
                    responseJson.put("category", category);
                    response.getWriter().write(responseJson.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String frontText = request.getParameter("frontText");
                String backText = request.getParameter("backText");
                String category = request.getParameter("category");

                Flashcard flashcard = new Flashcard(id, frontText, backText, category);
                if (flashcardDAO.updateFlashcard(flashcard)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (flashcardDAO.deleteFlashcard(id)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
