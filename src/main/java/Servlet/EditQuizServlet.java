// File: src/Servlet/EditQuizServlet.java
package Servlet;

import dao.QuizDAO;
import dao.TagDAO;
import dao.userQuizDAO;
import entity.Question;
import entity.Tag;
import entity.quiz;
import entity.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

// For file parsing
import org.apache.poi.xwpf.usermodel.*; // For Word files
import org.apache.poi.ss.usermodel.*;   // For Excel files
import java.io.InputStream;
import java.util.stream.Collectors;

@WebServlet(name = "EditQuizServlet", urlPatterns = {"/EditQuizServlet"})
@MultipartConfig
public class EditQuizServlet extends HttpServlet {

    private QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve user from session
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null || (!currentUser.hasRole("teacher") && !currentUser.hasRole("admin"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get quizId from request
        String stringQuizId = request.getParameter("quizId");
        if (stringQuizId == null || stringQuizId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(stringQuizId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editError");
            return;
        }

        try {
            quiz quizObj = quizDAO.getQuizById(quizId);

            if (quizObj == null) {
                // Quiz does not exist
                request.setAttribute("errorMessage", "Quiz does not exist.");
                // Get all tags
                TagDAO tagDAO = new TagDAO();
                List<Tag> tagList = tagDAO.getAllTags();
                request.setAttribute("tagList", tagList != null ? tagList : new ArrayList<>());
                request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
                return;
            }

            // Check permission to edit quiz
            if (quizObj.getUser_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                request.setAttribute("errorMessage", "You do not have permission to edit this quiz.");
                // Get all tags
                TagDAO tagDAO = new TagDAO();
                List<Tag> tagList = tagDAO.getAllTags();
                request.setAttribute("tagList", tagList != null ? tagList : new ArrayList<>());
                request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
                return;
            }

            // Get all tags
            TagDAO tagDAO = new TagDAO();
            List<Tag> tagList = tagDAO.getAllTags();
            request.setAttribute("tagList", tagList != null ? tagList : new ArrayList<>());

            // Set quiz object to request
            request.setAttribute("quiz", quizObj);

            // Parse the answer JSON to get questions
            String answerJson = quizObj.getAnswer();
            List<Question> questions = new ArrayList<>();
            if (answerJson != null && !answerJson.trim().isEmpty()) {
                JSONArray questionsArray = new JSONArray(answerJson);
                for (int i = 0; i < questionsArray.length(); i++) {
                    JSONObject questionObj = questionsArray.getJSONObject(i);
                    Question question = new Question();

                    // Handle missing "sequence" gracefully
                    if (questionObj.has("sequence")) {
                        question.setSequence(questionObj.getInt("sequence"));
                    } else {
                        // Assign sequence based on array position (1-based)
                        question.setSequence(i + 1);
                    }

                    question.setQuestion(questionObj.getString("question"));
                    if (questionObj.has("options")) {
                        JSONArray optionsArray = questionObj.getJSONArray("options");
                        List<String> options = new ArrayList<>();
                        for (int j = 0; j < optionsArray.length(); j++) {
                            options.add(optionsArray.getString(j));
                        }
                        question.setOptions(options);
                    }
                    if (questionObj.has("correct")) {
                        question.setCorrect(questionObj.getString("correct"));
                    }
                    questions.add(question);
                }
            }

            request.setAttribute("questions", questions);

            // Set quiz type
            int typeId = quizObj.getType_id();
            String quizType = determineQuizTypeString(typeId);
            request.setAttribute("quizType", quizType);

            // Forward to edit-quiz.jsp
            request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while retrieving quiz data.");
            // Get all tags
            TagDAO tagDAO = new TagDAO();
            List<Tag> tagList = null;
            try {
                tagList = tagDAO.getAllTags();
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            request.setAttribute("tagList", tagList != null ? tagList : new ArrayList<>());
            request.getRequestDispatcher("/jsp/edit-quiz.jsp").forward(request, response);
        }
    }

    // Handle POST request to update the quiz
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the form has multipart content
        if (request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/")) {
            try {
                // Set character encoding to handle Unicode characters
                request.setCharacterEncoding("UTF-8");

                // Retrieve user from session
                HttpSession session = request.getSession();
                Users currentUser = (Users) session.getAttribute("user");

                // Check permission
                if (currentUser == null || (!currentUser.hasRole("teacher") && !currentUser.hasRole("admin"))) {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return;
                }

                // Get parameters from form
                String stringQuizId = request.getParameter("quizId");
                String quizName = request.getParameter("quizName");
                String quizDescription = request.getParameter("quizDescription");
                String quizType = request.getParameter("quizType");
                String[] quizTagIdsArray = request.getParameterValues("quizTags");
                List<String> quizTagIds = quizTagIdsArray != null ? Arrays.asList(quizTagIdsArray) : new ArrayList<>();

                if (stringQuizId == null || quizName == null || quizDescription == null || quizType == null || quizTagIds.isEmpty()) {
                    request.setAttribute("errorMessage", "Please fill in all required fields and select at least one tag.");
                    doGet(request, response);
                    return;
                }

                int quizId;
                try {
                    quizId = Integer.parseInt(stringQuizId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    request.setAttribute("errorMessage", "Invalid Quiz ID.");
                    doGet(request, response);
                    return;
                }

                quiz quizObj = quizDAO.getQuizById(quizId);

                if (quizObj == null) {
                    request.setAttribute("errorMessage", "Quiz does not exist.");
                    doGet(request, response);
                    return;
                }

                // Check permission to edit quiz
                if (quizObj.getUser_id() != currentUser.getId() && !currentUser.hasRole("admin")) {
                    request.setAttribute("errorMessage", "You do not have permission to edit this quiz.");
                    doGet(request, response);
                    return;
                }

                List<Question> questions = new ArrayList<>();

                Part quizFilePart = request.getPart("quizFile");

                if (quizFilePart != null && quizFilePart.getSize() > 0) {
                    String fileName = getFileName(quizFilePart);
                    String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

                    InputStream fileContent = quizFilePart.getInputStream();

                    if (fileExtension.equals("docx")) {
                        questions = parseWordFile(fileContent, quizType);
                    } else if (fileExtension.equals("xlsx")) {
                        questions = parseExcelFile(fileContent, quizType);
                    } else {
                        request.setAttribute("errorMessage", "Unsupported file type. Please upload a .docx or .xlsx file.");
                        doGet(request, response);
                        return;
                    }
                } else {
                    // Handle manual input if no file is uploaded
                    String questionCountStr = request.getParameter("questionCount");
                    int questionCount = 0;
                    if (questionCountStr != null && !questionCountStr.trim().isEmpty()) {
                        try {
                            questionCount = Integer.parseInt(questionCountStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            questionCount = 0;
                        }
                    }

                    // Collect all questions and build the JSON array
                    for (int i = 1; i <= questionCount; i++) {
                        String questionContent = request.getParameter("questionContent" + i);

                        if (questionContent == null || questionContent.trim().isEmpty()) {
                            continue; // Skip incomplete questions
                        }

                        JSONObject questionObjJSON = new JSONObject();

                        if (quizType.equals("multiple-choice") || quizType.equals("matching")) {
                            // Handle multiple choice and matching questions
                            List<String> answerList = new ArrayList<>();
                            int j = 1;
                            while (true) {
                                String answer = request.getParameter("answer" + i + "_" + j);
                                if (answer == null || answer.trim().isEmpty()) {
                                    break;
                                }
                                answerList.add(answer);
                                j++;
                            }
                            String[] answers = answerList.toArray(new String[0]);

                            String correctAnswerIndexStr = request.getParameter("correctAnswer" + i);
                            int correctAnswerIndex = 1; // Default to first answer
                            if (correctAnswerIndexStr != null && !correctAnswerIndexStr.trim().isEmpty()) {
                                try {
                                    correctAnswerIndex = Integer.parseInt(correctAnswerIndexStr);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    correctAnswerIndex = 1;
                                }
                            }
                            String correctAnswerText = answers.length >= correctAnswerIndex ? answers[correctAnswerIndex - 1] : answers[0];

                            // Build the question JSON object
                            questionObjJSON.put("sequence", i);
                            questionObjJSON.put("question", questionContent);
                            questionObjJSON.put("options", Arrays.asList(answers));
                            questionObjJSON.put("correct", correctAnswerText);

                            // If it's a matching question, ensure options array has only one element
                            if (quizType.equals("matching")) {
                                // Combine all options into a single string
                                String matchingOption = String.join("; ", answers);
                                questionObjJSON.put("options", Collections.singletonList(matchingOption));
                                questionObjJSON.put("correct", matchingOption);
                            }
                        } else if (quizType.equals("fill-in-the-blank")) {
                            // Handle fill-in-the-blank questions
                            String correctAnswer = request.getParameter("correctAnswer" + i);
                            questionObjJSON.put("sequence", i);
                            questionObjJSON.put("question", questionContent);
                            questionObjJSON.put("correct", correctAnswer);
                        }

                        // Add the question object to the list
                        Question q = new Question();
                        q.setSequence(questionObjJSON.has("sequence") ? questionObjJSON.getInt("sequence") : i);
                        q.setQuestion(questionObjJSON.getString("question"));
                        if (questionObjJSON.has("options")) {
                            JSONArray optionsArray = questionObjJSON.getJSONArray("options");
                            List<String> options = new ArrayList<>();
                            for (int k = 0; k < optionsArray.length(); k++) {
                                options.add(optionsArray.getString(k));
                            }
                            q.setOptions(options);
                        }
                        if (questionObjJSON.has("correct")) {
                            q.setCorrect(questionObjJSON.getString("correct"));
                        }
                        questions.add(q);
                    }
                }

                // Convert questions list to JSON
                JSONArray questionsArray = new JSONArray();

                for (Question question : questions) {
                    JSONObject questionObjJSON = new JSONObject();
                    questionObjJSON.put("sequence", question.getSequence());
                    questionObjJSON.put("question", question.getQuestion());

                    if (quizType.equals("multiple-choice") || quizType.equals("matching")) {
                        questionObjJSON.put("options", question.getOptions());
                        questionObjJSON.put("correct", question.getCorrect());
                    } else if (quizType.equals("fill-in-the-blank")) {
                        questionObjJSON.put("correct", question.getCorrect());
                    }

                    questionsArray.put(questionObjJSON);
                }

                String questionsJson = questionsArray.toString();

                // Update quiz object
                quizObj.setName(quizName);
                quizObj.setDescription(quizDescription);
                quizObj.setType_id(determineQuizTypeId(quizType));
                quizObj.setAnswer(questionsJson);

                // Update the quiz in the database
                boolean isUpdated = quizDAO.updateQuiz(quizObj);

                if (isUpdated) {
                    // Update quiz tags
                    quizDAO.deleteQuizTags(quizId); // Remove existing tags
                    for (String tagIdStr : quizTagIds) {
                        int tagId = Integer.parseInt(tagIdStr);
                        quizDAO.insertQuizTag(quizId, tagId);
                    }

                    // Redirect to a success page
                    response.sendRedirect(request.getContextPath() + "/jsp/teacher.jsp?message=editSuccess");
                } else {
                    // Display error message if the quiz could not be updated
                    request.setAttribute("errorMessage", "Failed to update quiz.");
                    doGet(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error: " + e.getMessage());
                doGet(request, response);
            }
        } else {
            // Handle regular form submission without file upload
            request.setAttribute("errorMessage", "Form must have enctype='multipart/form-data'.");
            doGet(request, response);
        }
    }

    // Helper method to map quizType string to the corresponding type_id in the database
    private int determineQuizTypeId(String quizType) {
        switch (quizType) {
            case "multiple-choice":
                return 1; // Assuming '1' represents Multiple Choice
            case "fill-in-the-blank":
                return 2; // Assuming '2' represents Fill in the Blank
            case "matching":
                return 3; // Assuming '3' represents Matching
            default:
                return 1; // Default to Multiple Choice
        }
    }

    // Helper method to map type_id to quizType string
    private String determineQuizTypeString(int typeId) {
        switch (typeId) {
            case 1:
                return "multiple-choice";
            case 2:
                return "fill-in-the-blank";
            case 3:
                return "matching";
            default:
                return "multiple-choice";
        }
    }

    // Parsing Word files (same as in QuizController)
    private List<Question> parseWordFile(InputStream fileContent, String quizType) throws IOException {
        List<Question> questions = new ArrayList<>();

        XWPFDocument document = new XWPFDocument(fileContent);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        int sequence = 1;
        Question currentQuestion = null;
        for (XWPFParagraph para : paragraphs) {
            String text = para.getText().trim();

            if (text.startsWith("Q:")) {
                currentQuestion = new Question();
                currentQuestion.setSequence(sequence++);
                currentQuestion.setQuestion(text.substring(2).trim());
                questions.add(currentQuestion);
            } else if ((quizType.equals("multiple-choice") || quizType.equals("matching")) && currentQuestion != null && text.matches("[A-D]\\).*")) {
                // Option line
                if (currentQuestion.getOptions() == null) {
                    currentQuestion.setOptions(new ArrayList<>());
                }
                currentQuestion.getOptions().add(text.substring(2).trim());
            } else if (text.startsWith("Answer:") && currentQuestion != null) {
                String correctAnswer = text.substring(7).trim();
                if (quizType.equals("multiple-choice")) {
                    currentQuestion.setCorrect(correctAnswer);
                } else if (quizType.equals("fill-in-the-blank")) {
                    currentQuestion.setCorrect(correctAnswer);
                } else if (quizType.equals("matching")) {
                    // For matching, combine options into a single string
                    String matchingOption = String.join("; ", currentQuestion.getOptions());
                    currentQuestion.setOptions(Collections.singletonList(matchingOption));
                    currentQuestion.setCorrect(matchingOption);
                }
            }
        }

        document.close();
        return questions;
    }

    // Parsing Excel files (same as in QuizController)
    private List<Question> parseExcelFile(InputStream fileContent, String quizType) throws IOException {
        List<Question> questions = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(fileContent);
        Sheet sheet = workbook.getSheetAt(0); // Assume first sheet

        Iterator<Row> rowIterator = sheet.iterator();
        int sequence = 1;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell questionCell = row.getCell(0);

            if (questionCell != null) {
                Question question = new Question();
                question.setSequence(sequence++);
                question.setQuestion(getCellValue(questionCell));

                if (quizType.equals("multiple-choice") || quizType.equals("matching")) {
                    List<String> options = new ArrayList<>();
                    for (int i = 1; i <= 4; i++) {
                        Cell optionCell = row.getCell(i);
                        if (optionCell != null) {
                            options.add(getCellValue(optionCell));
                        }
                    }
                    if (quizType.equals("matching")) {
                        // Combine options into a single string
                        String matchingOption = String.join("; ", options);
                        question.setOptions(Collections.singletonList(matchingOption));
                        question.setCorrect(matchingOption);
                    } else {
                        question.setOptions(options);
                        Cell correctCell = row.getCell(5);
                        if (correctCell != null) {
                            question.setCorrect(getCellValue(correctCell));
                        }
                    }
                } else if (quizType.equals("fill-in-the-blank")) {
                    Cell correctCell = row.getCell(1);
                    if (correctCell != null) {
                        question.setCorrect(getCellValue(correctCell));
                    }
                }

                questions.add(question);
            }
        }

        workbook.close();
        return questions;
    }

    // Helper method to get the file name from the Part
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return null;
        }
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    // Helper method to get cell value as String
    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // If the cell is numeric, but represents an integer, format without decimal
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
                // Handle other cell types as needed
            default:
                return "";
        }
    }
}
