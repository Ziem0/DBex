package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class SqlController {

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:school.db";

    private Connection conn;
    private Statement stat;

    public SqlController() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Incorrect driver");
            e.printStackTrace();
        }

        try {
            this.conn = DriverManager.getConnection(DB_URL);
            this.stat = this.conn.createStatement();
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Invalid connection");
            e.printStackTrace();
        }
    }

    public boolean createTable() {
        String createMentorsTable = "create table if not exists mentors(" +
                "ID integer primary key unique, first text, last text, nick text, phone text, email text, city text, num integer)";
        String createApplicantsTable = "create table if not exists applicants(" +
                "ID integer primary key unique, first text, last text, phone text, email text, code integer)";

        try {
            this.stat.execute(createMentorsTable);
            this.stat.execute(createApplicantsTable);
        } catch (SQLException e) {
            System.err.println("Table creator is invalid");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean importMentorsDataFromCsv() {
        String path = "mentors.csv";
        String command = "insert into mentors values(NULL,?,?,?,?,?,?,?)";
        return this.fileLoader(path, command);
    }

    public boolean importApplicantsDataFromCsv() {
        String path = "applicants.csv";
        String command = "insert into applicants values(NULL, ?,?,?,?,?)";
        return this.fileLoader(path, command);
    }

    private boolean fileLoader(String path, String command) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            PreparedStatement preparedStatement = this.conn.prepareStatement(command);
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int counter = 1;
                for (String v : values) {
                    if (v.matches("[0-9]{1,}")) {
                        preparedStatement.setInt(counter, Integer.parseInt(v));
                    } else {
                        String cleanV = v.replace("'", "");
                        preparedStatement.setString(counter, cleanV);
                    }
                    counter++;
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (FileNotFoundException e) {
            System.err.println("Invalid file path");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Invalid IO");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Invalid sql command");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void displaySelect(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        Integer counter = rsmd.getColumnCount();
        String strToPrint = "%-20s";

        while (result.next()) {
            for (int i = 1; i <= counter; i++) {
                System.out.printf(strToPrint, result.getString(i));
                if (i == counter) {
                    System.out.println("\t");
                }
            }
        }
    }

    public void selectDB(String query) {
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
            e.printStackTrace();
        }
    }

    


    private boolean changeDB(String command) {
        try {
            this.stat.executeUpdate(command);
        } catch (SQLException e) {
            System.err.println("Invalid command");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        try {
            this.stat.close();
            this.conn.commit();
            this.conn.close();
        } catch (SQLException e) {
            System.err.println("Close error");
            e.printStackTrace();
        }
        System.exit(0);
    }


    public static void main(String[] args) throws SQLException {
        SqlController controller = new SqlController();
        controller.stat.execute("DROP TABLE mentors");
        controller.stat.execute("DROP TABLE applicants");
        controller.createTable();
        controller.importMentorsDataFromCsv();
        controller.importApplicantsDataFromCsv();
        controller.selectDB("select * from mentors;");

        controller.close();
    }
}
