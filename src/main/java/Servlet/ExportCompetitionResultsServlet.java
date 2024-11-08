package Servlet;

import dao.ClassDAO;
import dao.CompetitionDAO;
import dao.CompetitionResultDAO;
import entity.classs;
import entity.Competition;
import entity.CompetitionResult;
import entity.Users;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "ExportCompetitionResultsServlet", urlPatterns = {"/ExportCompetitionResultsServlet"})
public class ExportCompetitionResultsServlet extends HttpServlet {

    private CompetitionResultDAO competitionResultDAO;
    private ClassDAO classDAO;

    @Override
    public void init() throws ServletException {
        competitionResultDAO = new CompetitionResultDAO();
        classDAO = new ClassDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int competitionId = Integer.parseInt(request.getParameter("competitionId"));

        try {
            CompetitionDAO competitionDAO = new CompetitionDAO();
            Competition competition = competitionDAO.getCompetitionById(competitionId);

            if (competition == null) {
                response.sendRedirect(request.getContextPath() + "/CompetitionController?action=list&message=competitionNotFound");
                return;
            }

            // Retrieve competition results
            List<CompetitionResult> competitionResults = competitionResultDAO.getCompetitionResultsByCompetitionId(competitionId);

            if (competitionResults.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/CompetitionController?action=view&id=" + competitionId + "&message=noResults");
                return;
            }

            // Retrieve students
            List<Users> students = competitionResultDAO.getStudentsByCompetitionIdAndClassId(competitionId, competition.getClassId());

            // Create a map of userId to Users
            Map<Integer, Users> usersMap = new HashMap<>();
            for (Users user : students) {
                usersMap.put(user.getId(), user);
            }

            // Create Excel Workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Competition Results");

            // Create Header Row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("STT");
            headerRow.createCell(1).setCellValue("Tên Sinh Viên");
            headerRow.createCell(2).setCellValue("Điểm");
            headerRow.createCell(3).setCellValue("Thời Gian (giây)");

            // Populate Data Rows
            int rowNum = 1;
            for (CompetitionResult cr : competitionResults) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                Users student = usersMap.get(cr.getUserId());
                String studentName = student != null ? student.getUsername() : "Unknown";
                row.createCell(1).setCellValue(studentName);
                row.createCell(2).setCellValue(cr.getScore());
                row.createCell(3).setCellValue(cr.getTimeTaken());
            }

            // Adjust column widths
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set the content type and attachment header.
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=CompetitionResults.xlsx");

            // Write the workbook to the output stream
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
            out.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception
            response.sendRedirect(request.getContextPath() + "/CompetitionController?action=view&id=" + competitionId + "&message=exportFailed");
        }
    }
}
