//package controller;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.*;
//
//public class SQLiJDBC {
//
//    private static final String DRIVER = "org.sqlite.JDBC";
//    private static final String DB_URL = "jdbc:sqlite:ex.db";
//
//    private Connection conn;
//    private Statement stat;
//
//    public SQLiJDBC() {
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            this.conn = DriverManager.getConnection(DB_URL);
//            this.stat = conn.createStatement();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean createTable() {
//        String createMentors = "CREATE TABLE IF NOT EXISTS mentors(ID INTEGER PRIMARY KEY UNIQUE, first TEXT, last TEXT, nick TEXT, phone TEXT, email TEXT, city TEXT, num INT)";
//        String createApplicants = "CREATE TABLE IF NOT EXISTS applicants(ID INTEGER PRIMARY KEY UNIQUE, first TEXT, last TEXT, phone TEXT, email TEXT, code INTEGER)";
//
//        try {
//            this.stat.execute(createMentors);
//            this.stat.execute(createApplicants);
//        } catch (SQLException e) {
//            System.err.println("Invalid table creation");
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public void importMentorsFromCsv(String toTableName) {
//        String mentorsInsert = "INSERT INTO mentors VALUES (NULL,?,?,?,?,?,?,?);";
//        String applicantsInsert = "INSERT INTO applicants VALUES (NULL,?,?,?,?,?);";
//        String mentorsCSV = "mentors.csv";
//        String applicantsCSV = "applicants.csv";
//        String finalTable = mentorsInsert;
//        String csvFile = mentorsCSV;
//        if (toTableName.equalsIgnoreCase("applicants")) {
//            finalTable = applicantsInsert;
//            csvFile = applicantsCSV;
//        }
//
//        try {
//            FileReader fr = new FileReader(csvFile);
//            BufferedReader br = new BufferedReader(fr);
//            String line;
//            PreparedStatement preparedStatement = this.conn.prepareStatement(finalTable);
//            while ((line = br.readLine()) != null) {
//                int counter = 1;
//                String[] values = line.split(",");
//                for (String s : values) {
//                    if (s.matches("[0-9]{1,}")) {
//                        preparedStatement.setInt(counter, Integer.parseInt(values[counter - 1]));
//                    } else {
//                        String finalString = s.replace("'", "");
//                        preparedStatement.setString(counter, finalString);
//                    }
//                    counter++;
//                }
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        } catch (FileNotFoundException e) {
//            System.err.println("File doesn't exist ");
//            e.printStackTrace();
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean getMentorsName() {
//        String query = "select first, last from mentors";
//        try {
//            ResultSet result = this.stat.executeQuery(query);
//            while (result.next()) {
//                System.out.println(String.format("name:%-14s lastName:%s", result.getString(1),result.getString(2)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean getMentorsFromMiscolc() {
//        String query = "select nick from mentors where city is 'Miskolc'";
//        try {
//            ResultSet result = this.stat.executeQuery(query);
//            System.out.println("\nMentors from Miskolc:");
//            while (result.next()) {
//                System.out.println(result.getString(1));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean getCarolNumber() {
//        String query = "select first || ' ' || last as fullName, phone from applicants where first is 'Carol'";
//        try {
//            ResultSet result = this.stat.executeQuery(query);
//            while (result.next()) {
//                System.out.println(String.format("\nfullName:%-10s phone:%-10s",result.getString(1), result.getString(2)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean getHatOwner() {
//        String query = "select first || ' ' || last as fullName, phone from applicants where email like '%@adipi%'";
//        try {
//            ResultSet result = this.stat.executeQuery(query);
//            while (result.next()) {
//                System.out.println(String.format("\nfullName:%-10s phone:%-10s",result.getString(1), result.getString(2)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean addNewApp() {
//        String query = "insert into applicants(first, last, phone, email, code) values('Markus', 'Szafarzyk','003620/725-2666' ,'djnovus@groovecoverage.com', 54823)";
//        try {
//            this.stat.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Add new mentor failed");
//            return false;
//        }
//        return true;
//    }
//
//    public boolean updateJemimaNumber() {
//        String query = "update applicants set phone = '003670/223-7459' where first is 'Jemima' and last is 'Foreman'";
//        try {
//            this.stat.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public boolean deleteApp() {
//        String query = "delete from applicants where email like '%@mauriseu%'";
//        try {
//            this.stat.executeUpdate(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static void main(String[] args) throws SQLException {
//        SQLiJDBC sql = new SQLiJDBC();
//        sql.createTable();
//        sql.stat.execute("DELETE FROM mentors");
//        sql.stat.execute("delete from applicants");
//        sql.importMentorsFromCsv("mentors");
//        sql.importMentorsFromCsv("applicants");
//        sql.getMentorsName();
//        sql.getMentorsFromMiscolc();
//        sql.getCarolNumber();
//        sql.getHatOwner();
//        sql.addNewApp();
//        sql.updateJemimaNumber();
//        sql.deleteApp();
//    }
//}
