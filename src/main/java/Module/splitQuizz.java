package Module;
import dao.QuizDAO;
import java.util.ArrayList;
import java.io.StringReader;
import java.util.List;
import entity.quiz;

public class splitQuizz {
//    test
    public static void main(String[] args) {
        QuizDAO quizDAO = new QuizDAO();
        try {
            List<quiz> quizs = quizDAO.getAllQuiz();
            String answer = quizDAO.getAnswerById(1);
            System.out.println(answer);
//            get each {} in answer string
//            test castAnswerToJson

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    split String [{},{},{}] to List each {}
    public static List<String> castAnswerToList(String answer){
        List<String> list = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == '{') {
                start = i;
            }
            if (answer.charAt(i) == '}') {
                end = i;
                list.add(answer.substring(start, end + 1));
            }
        }
        return list;
    }
}
