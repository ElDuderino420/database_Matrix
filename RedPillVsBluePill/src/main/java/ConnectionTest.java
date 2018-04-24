import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
public class ConnectionTest {

    private String queryGeneratorTemplate9000(String id, String sub) {
        String temp = "(SELECT distinct " + id + " FROM t_user "
                + "INNER JOIN t_edges ON (t_edges.target_node_id = t_user.id) "
                + "WHERE t_edges.source_node_id IN " + sub + ")";
        return temp;
    }

    public void print(ResultSet rs) throws SQLException {

        ResultSetMetaData rsm = rs.getMetaData();
        System.out.printf("%5s", "row nr.");
        for (int i = 0; i < rsm.getColumnCount(); i++) {
            if (i == 1 || i == 2) {
                System.out.printf("%40s", rsm.getColumnName(i + 1));
            } else {
                System.out.printf("%15s", rsm.getColumnName(i + 1));
            }
        }
        while (rs.next()) {
            System.out.printf("%5s", rs.getRow());
            for (int i = 0; i < rsm.getColumnCount(); i++) {
                if (i == 1 || i == 2) {
                    System.out.printf("%40s", rs.getString(i + 1));
                } else {
                    System.out.printf("%15s", rs.getString(i + 1));
                }
            }
            System.out.printf("\n");
        }
    }

    public ResultSet endorseDepth(int id, int depth) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "";

        String q1 = "(SELECT distinct id FROM t_user "
                + "INNER JOIN t_edges ON (t_edges.target_node_id = t_user.id) "
                + "WHERE t_edges.source_node_id = " + id + ")";

        String query = "";
        switch (depth) {
            case 1:
                query = "(SELECT distinct * FROM t_user "
                        + "INNER JOIN t_edges ON (t_edges.target_node_id = t_user.id) "
                        + "WHERE t_edges.source_node_id = " + id + ")";
                break;
            case 2:
                query = queryGeneratorTemplate9000("*", q1);
                break;
            case 3:
                query = queryGeneratorTemplate9000("*", queryGeneratorTemplate9000("id", q1));
                break;
            case 4:
                query = queryGeneratorTemplate9000("*", queryGeneratorTemplate9000("id", queryGeneratorTemplate9000("id", q1)));
                break;
            case 5:
                query = queryGeneratorTemplate9000("*", queryGeneratorTemplate9000("id", queryGeneratorTemplate9000("id", queryGeneratorTemplate9000("id", q1))));
                break;
            default:
                query = "(SELECT distinct * FROM t_user "
                        + "INNER JOIN t_edges ON (t_edges.target_node_id = t_user.id) "
                        + "WHERE t_edges.source_node_id = " + id + ")";
                break;
        }
        try {

            con = DriverManager.getConnection(url, user, password);

            st = con.createStatement();
            rs = st.executeQuery(query);

            return rs;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.err.println(ex);

        } finally {
            try {
                if (rs != null) {
                    //rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                System.err.println(ex);
            }
        }
        return null;
    }

    public Long time(int id, int depth){
        Long start = System.currentTimeMillis();
        endorseDepth(id,depth);
        return System.currentTimeMillis()-start;

    }

    public static void main(String[] args) throws SQLException {
        //endorseDepth(223395,1);
    }
}