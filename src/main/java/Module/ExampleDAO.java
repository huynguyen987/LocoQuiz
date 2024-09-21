package Module;

import java.sql.*;
import java.util.Vector;

public class ExampleDAO {
    // New method to return a single result from the query
    public String getSingleResult(String sql, String... parameters) {
        String result = null;
        try {
            Connection con = new DBConnect().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);  // Get the first column of the result
            }
        } catch (SQLException e) {
            System.out.println("SQL query execution failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
//    method get vector result
    public Vector<Vector<String>> getVectorResult(String sql, String... parameters) {
        Vector<Vector<String>> result = new Vector<>();
        try {
            Connection con = new DBConnect().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                stmt.setString(i + 1, parameters[i]);
            }
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                result.add(row);
            }
        } catch (SQLException e) {
            System.out.println("SQL query execution failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
        // giải thích: hàm này trả về một vector chứa các vector string, mỗi vector string chứa các giá trị của một hàng trong bảng
//        ví dụ: bảng có 2 cột, 3 hàng thì kết quả trả về sẽ là một vector chứa 3 vector string, mỗi vector string chứa 2 giá trị
//       cách đọc dữ liệu từ kết quả trả về: result.get(i).get(j) với i là số thứ tự hàng, j là số thứ tự cột
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ExampleDAO ex = new ExampleDAO();
    }
}
