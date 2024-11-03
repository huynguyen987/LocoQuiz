package Servlet;

import dao.QuizDAO;
import dao.TagDAO;
import dao.userQuizDAO;
import entity.Competition;
import entity.quiz;
import entity.Tag;
import entity.Question;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

// For file parsing
import org.apache.poi.xwpf.usermodel.*; // For Word files
import org.apache.poi.ss.usermodel.*;   // For Excel files
import java.io.InputStream;
import java.util.stream.Collectors;

@WebServlet(name = "QuizController", urlPatterns = {"/QuizController"})
@MultipartConfig
public class QuizController extends HttpServlet {

    public QuizController() {
    }

    // Handle GET request to display the quiz creation form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách các tags từ cơ sở dữ liệu để hiển thị trong dropdown
        TagDAO tagDAO = new TagDAO();
        try {
            List<Tag> tagList = tagDAO.getAllTags();
            request.setAttribute("tagList", tagList);
            System.out.println("Number of tags retrieved: " + tagList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chuyển tiếp đến quiz-creator.jsp
        request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
    }


    // Handle POST request to process the form submission
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if the form has multipart content
        if (request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart/")) {
            try {
                // Set character encoding to handle Unicode characters
                request.setCharacterEncoding("UTF-8");

                // Get form fields
                String quizName = request.getParameter("quizName");
                String quizDescription = request.getParameter("quizDescription");
                String quizType = request.getParameter("quizType");
                System.out.println("Quiz type: " + quizType);
                String[] quizTagIdsArray = request.getParameterValues("quizTags");
                List<String> quizTagIds = quizTagIdsArray != null ? Arrays.asList(quizTagIdsArray) : new ArrayList<>();

                Integer userId = Integer.parseInt(request.getParameter("userId"));

                if (userId == null) {
                    request.setAttribute("errorMessage", "User is not logged in. Please log in to create a quiz.");
                    request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                    return;
                }

                // Validate required fields
                if (quizName == null || quizDescription == null || quizType == null || quizTagIds.isEmpty()) {
                    request.setAttribute("errorMessage", "Please fill in all required fields and select at least one tag.");
                    request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
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
                        request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
                        return;
                    }
                } else {
                    // Handle manual input if no file is uploaded
                    int questionCount = Integer.parseInt(request.getParameter("questionCount"));

                    // Collect all questions and build the JSON array
                    for (int i = 1; i <= questionCount; i++) {
                        String questionContent = request.getParameter("questionContent" + i);
                        JSONObject questionObj = new JSONObject();

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
                            int correctAnswerIndex = Integer.parseInt(correctAnswerIndexStr);
                            String correctAnswerText = answers[correctAnswerIndex - 1];

                            // Build the question JSON object
                            questionObj.put("sequence", i);
                            questionObj.put("question", questionContent);
                            questionObj.put("options", Arrays.asList(answers));
                            questionObj.put("correct", correctAnswerText);

                            // If it's a matching question, ensure options array has only one element
                            if (quizType.equals("matching")) {
                                // Combine all options into a single string
                                String matchingOption = String.join("; ", answers);
                                questionObj.put("options", Collections.singletonList(matchingOption));
                                questionObj.put("correct", matchingOption);
                            }
                        } else if (quizType.equals("fill-in-the-blank")) {
                            // Handle fill-in-the-blank questions
                            String correctAnswer = request.getParameter("correctAnswer" + i);
                            questionObj.put("sequence", i);
                            questionObj.put("question", questionContent);
                            questionObj.put("correct", correctAnswer);
                        }

                        // Add the question object to the list
                        Question q = new Question();
                        q.setSequence(i);
                        q.setQuestionText(questionObj.getString("question"));
                        if (quizType.equals("multiple-choice")) {
                            q.setOptions(questionObj.getJSONArray("options").toList().stream()
                                    .map(Object::toString)
                                    .collect(Collectors.toList()));
                            q.setCorrectAnswer(questionObj.getString("correct"));
                        } else if (quizType.equals("fill-in-the-blank")) {
                            q.setCorrectAnswer(questionObj.getString("correct"));
                        } else if (quizType.equals("matching")) {
                            q.setOptions(questionObj.getJSONArray("options").toList().stream()
                                    .map(Object::toString)
                                    .collect(Collectors.toList()));
                            q.setCorrectAnswer(questionObj.getString("correct"));
                        }
                        questions.add(q);
                    }
                }

                // Convert questions list to JSON
                JSONArray questionsArray = new JSONArray();

                for (Question question : questions) {
                    JSONObject questionObj = new JSONObject();
                    questionObj.put("sequence", question.getSequence());
                    questionObj.put("question", question.getQuestionText());

                    if (quizType.equals("multiple-choice") || quizType.equals("matching")) {
                        questionObj.put("options", question.getOptions());
                        questionObj.put("correct", question.getCorrectAnswer());
                    } else if (quizType.equals("fill-in-the-blank")) {
                        questionObj.put("correct", question.getCorrectAnswer());
                    }

                    questionsArray.put(questionObj);
                }

                String questionsJson = questionsArray.toString();

                // Create quiz object
                quiz newQuiz = new quiz();
                newQuiz.setName(quizName);
                newQuiz.setDescription(quizDescription);
                newQuiz.setUser_id(userId);
                newQuiz.setType_id(determineQuizTypeId(quizType));
                newQuiz.setAnswer(questionsJson);

                // Insert the quiz and retrieve the generated quiz ID
                QuizDAO quizDAO = new QuizDAO();
                int quizId = quizDAO.insertQuiz(newQuiz);

                if (quizId != -1) {
                    // Insert into user_quiz table for each selected tag
                    userQuizDAO userQuizDAO = new userQuizDAO();
                    for (String tagIdStr : quizTagIds) {
                        int tagId = Integer.parseInt(tagIdStr);
                        // Insert into quiz_tag table
                        quizDAO.insertQuizTag(quizId, tagId);
                        // Insert into user_quiz table
                        userQuizDAO.insertUserQuiz(userId, quizId, tagId);
                    }

                    // Redirect to a success page
                    request.setAttribute("successMessage", "Quiz created successfully!");
                    request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
                } else {
                    // Display error message if the quiz could not be created
                    request.setAttribute("errorMessage", "Failed to create quiz.");
                    request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error: " + e.getMessage());
                request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
            }
        } else {
            // Handle regular form submission without file upload
            request.setAttribute("errorMessage", "Form must have enctype='multipart/form-data'.");
            request.getRequestDispatcher("/jsp/quiz-creator.jsp").forward(request, response);
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

    // Parsing Word files
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
                currentQuestion.setQuestionText(text.substring(2).trim());
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
                    currentQuestion.setCorrectAnswer(correctAnswer);
                } else if (quizType.equals("fill-in-the-blank")) {
                    currentQuestion.setCorrectAnswer(correctAnswer);
                } else if (quizType.equals("matching")) {
                    // For matching, combine options into a single string
                    String matchingOption = String.join("; ", currentQuestion.getOptions());
                    currentQuestion.setOptions(Collections.singletonList(matchingOption));
                    currentQuestion.setCorrectAnswer(matchingOption);
                }
            }
        }

        document.close();
        return questions;
    }

    // Parsing Excel files
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
                question.setQuestionText(getCellValue(questionCell));

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
                        question.setCorrectAnswer(matchingOption);
                    } else {
                        question.setOptions(options);
                        Cell correctCell = row.getCell(5);
                        if (correctCell != null) {
                            question.setCorrectAnswer(getCellValue(correctCell));
                        }
                    }
                } else if (quizType.equals("fill-in-the-blank")) {
                    Cell correctCell = row.getCell(1);
                    if (correctCell != null) {
                        question.setCorrectAnswer(getCellValue(correctCell));
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
                return String.valueOf(cell.getNumericCellValue());
            // Handle other cell types as needed
            default:
                return "";
        }
    }


}




