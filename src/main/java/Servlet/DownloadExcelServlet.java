package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/download-excel")
public class DownloadExcelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu câu hỏi từ session
        HttpSession session = request.getSession();
        JSONArray questions = (JSONArray) session.getAttribute("questions");
        if (questions == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Không có dữ liệu để tải xuống.");
            return;
        }

        // Tạo tệp Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Questions");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Question");
        headerRow.createCell(1).setCellValue("Option 1");
        headerRow.createCell(2).setCellValue("Option 2");
        headerRow.createCell(3).setCellValue("Option 3");
        headerRow.createCell(4).setCellValue("Option 4");
        headerRow.createCell(5).setCellValue("Correct Answer");

        // Điền dữ liệu
        for (int i = 0; i < questions.length(); i++) {
            JSONObject questionObj = questions.getJSONObject(i);
            Row row = sheet.createRow(i + 1);

            // Câu hỏi
            row.createCell(0).setCellValue(questionObj.getString("question"));

            // Các lựa chọn
            JSONArray options = questionObj.getJSONArray("options");
            for (int j = 0; j < 4; j++) {
                if (j < options.length()) {
                    row.createCell(j + 1).setCellValue(options.getString(j));
                } else {
                    row.createCell(j + 1).setCellValue("");
                }
            }

            // Đáp án đúng
            row.createCell(5).setCellValue(questionObj.getString("correct"));
        }

        // Thiết lập header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=questions.xlsx");

        // Ghi workbook vào output stream
        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }

        workbook.close();
    }
}
