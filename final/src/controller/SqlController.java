package controller;

import model.Applicant;
import model.Mentor;
import view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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
        }

        try {
            this.conn = DriverManager.getConnection(DB_URL);
            this.stat = this.conn.createStatement();
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Invalid connection");
        }
    }

    public boolean createTable() {
        String createMentorsTable = "CREATE TABLE IF NOT EXISTS mentors(" +
                "ID INTEGER PRIMARY KEY UNIQUE, first TEXT, last TEXT, nick TEXT, phone TEXT, email TEXT, city TEXT, num INTEGER)";
        String createApplicantsTable = "CREATE TABLE IF NOT EXISTS applicants(" +
                "ID INTEGER PRIMARY KEY UNIQUE, first TEXT, last TEXT, phone TEXT, email TEXT, code INTEGER)";

        try {
            this.stat.execute(createMentorsTable);
            this.stat.execute(createApplicantsTable);
        } catch (SQLException e) {
            System.err.println("Table creator is invalid");
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
            return false;
        } catch (IOException e) {
            System.err.println("Invalid IO");
            return false;
        } catch (SQLException e) {
            System.err.println("Invalid sql command");
            return false;
        }
        return true;
    }

    private void displaySelect(ResultSet result) throws SQLException {
        ResultSetMetaData rsmd = result.getMetaData();
        Integer counter = rsmd.getColumnCount();
        String strToPrint = "%-23s";
        System.out.println("\n");
        System.out.println(rsmd.getTableName(1).isEmpty() ? "MENTORS data" : rsmd.getTableName(1).toUpperCase() + " data:");
        for (Integer i = 1; i <= counter; i++) {
            System.out.printf(strToPrint, rsmd.getColumnLabel(i).toUpperCase());
            if (i == counter) {
                System.out.println("\t");
            }
        }
        while (result.next()) {
            for (int i = 1; i <= counter; i++) {
                System.out.printf(strToPrint, result.getString(i).length() > 20 ? result.getString(i).subSequence(0, 20) : result.getString(i));
                if (i == counter) {
                    System.out.println("\t");
                }
            }
        }
    }

    public void displayAllMentors() {
        String query = "SELECT * FROM mentors";
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void displayAllApplicants() {
        String query = "SELECT * FROM applicants";
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void selectDB(String query) {
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void displayFullNameForMentors() {
        String query = "SELECT first || ' ' || last AS fullName FROM mentors";
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void displayNickForMentors() {
        String query = "SELECT first, nick FROM mentors";
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void addFullNameColumnForApplicants() {
        String command1 = "alter table applicants add fullName text";
        String command2 = "update applicants set fullName=first || ' ' || last";
        this.changeDB(command1);
        this.changeDB(command2);
    }

    public void displayFullNameAndPhoneForApplicantByName(String name) {
        String query = "select fullName, phone from applicants where first is '" + name + "'";
        try {
            ResultSet result = this.stat.executeQuery(query);
            this.displaySelect(result);
        } catch (SQLException e) {
            System.err.println("Invalid query");
        }
    }

    public void changeDB(String command) {
        try {
            this.stat.executeUpdate(command);
        } catch (SQLException e) {
            System.err.println("Invalid command");
        }
    }

    public void deleteApplicantByID() throws NumberFormatException {
        this.displayAllApplicants();
        System.out.println("Select ID for applicant you want to remove:");
        Integer num = Integer.valueOf(View.input());
        String command = "delete from applicants where id is " + num;
        this.changeDB(command);
    }


    public void addNewApplicant() throws SQLException {
        String command = "INSERT INTO applicants VALUES(NULL,?,?,?,?,?,?)";
        int counter = 0;
        PreparedStatement preparedStatement = this.conn.prepareStatement(command);
        ResultSet result = this.stat.executeQuery("SELECT * FROM applicants");
        ResultSetMetaData rsmd = result.getMetaData();

        while (++counter <= 6) {
            System.out.println("Enter " + rsmd.getColumnLabel(counter + 1).toUpperCase() + ": ");
            preparedStatement.setString(counter, View.input());
        }
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
    }

    public List<Mentor> createMentorsInstances() throws SQLException {
        List<Mentor> mentorsRecords = new LinkedList<>();
        ResultSet result = this.stat.executeQuery("SELECT * FROM mentors");
        while (result.next()) {
            String firstName = result.getString(1);
            String lastName = result.getString(2);
            String nickName = result.getString(3);
            String phoneNum = result.getString(4);
            String email = result.getString(5);
            String city = result.getString(6);
            int favNum = result.getInt(7);
            mentorsRecords.add(new Mentor(firstName, lastName, nickName, phoneNum, email, city, favNum));
        }
        return mentorsRecords;
    }

    public List<Applicant> createApplicantsInstances() throws SQLException {
        List<Applicant> applicantsRecords = new LinkedList<>();
        ResultSet result = this.stat.executeQuery("SELECT * FROM applicants");
        while (result.next()) {
            String firstName = result.getString(1);
            String lastName = result.getString(2);
            String phoneNum = result.getString(3);
            String email = result.getString(4);
            int code = result.getInt(5);
            applicantsRecords.add(new Applicant(firstName, lastName, phoneNum, email, code));
        }
        return applicantsRecords;
    }

    public void close() {
        try {
            this.stat.close();
            this.conn.commit();
            this.conn.close();
        } catch (SQLException e) {
            System.err.println("Close error");
        }
        System.exit(0);
    }

    public void clearRecords() {
        try {
            this.stat.execute("DROP TABLE mentors");
            this.stat.execute("DROP TABLE applicants");
        } catch (SQLException e) {
            System.err.println("Invalid delete");
        }
    }

//    public static void main(String[] args) throws SQLException {
//        SqlController controller = new SqlController();
//        controller.clearRecords();
//        controller.createTable();
//        controller.importMentorsDataFromCsv();
//        controller.importApplicantsDataFromCsv();
//        controller.displayAllMentors();
//        controller.displayFullNameForMentors();
//        controller.displayNickForMentors();
//        controller.addFullNameColumnForApplicants();
//        controller.displayFullNameAndPhoneForApplicantByName("Carol");
//        controller.addNewApplicant();
//        controller.deleteApplicantByID();
//        controller.displayAllApplicants();
//        controller.createMentorsInstances().forEach(System.out::println);
//        controller.createApplicantsInstances().forEach(System.out::println);
//        controller.close();
//    }
}
