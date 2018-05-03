import controller.SqlController;
import view.View;

import java.sql.SQLException;

public class App {

    private SqlController sqlite = new SqlController();

    private static final int MENTORS_BY_FULL_NAME = 0;
    private static final int MENTORS_BY_NICK_NAME = 1;
    private static final int ADD_FULL_NAME_COLUMN_FOR_APPLICANTS = 2;
    private static final int APPLICANT_FULL_NAME_AND_PHONE_BY_NAME = 3;
    private static final int ALL_MENTORS = 4;
    private static final int ALL_APPLICANTS = 5;
    private static final int ADD_NEW_APPLICANT = 6;
    private static final int DELETE_SELECTED_APPLICANT = 7;
    private static final int USER_COMMAND = 8;
    private static final int USER_QUERY = 9;
    private static final int QUIT = 10;

    public static void main(String[] args) {
        App app = new App();
        boolean start = true;
        app.sqlite.clearRecords();
        app.sqlite.createTable();
        app.sqlite.importMentorsDataFromCsv();
        app.sqlite.importApplicantsDataFromCsv();

        while (start) {
            try {
                start = app.run();
            } catch (SQLException e) {
                System.err.println("Error while adding new applicant");
                e.printStackTrace();
            }
        }
    }

    private boolean run() throws SQLException {
        View.Options.printOptions();
        int userChoice = View.getUserChoice();

        switch (userChoice) {
            case MENTORS_BY_FULL_NAME: sqlite.displayFullNameForMentors();
                break;
            case MENTORS_BY_NICK_NAME: sqlite.displayNickForMentors();
                break;
            case ADD_FULL_NAME_COLUMN_FOR_APPLICANTS: sqlite.addFullNameColumnForApplicants();
                break;
            case APPLICANT_FULL_NAME_AND_PHONE_BY_NAME: sqlite.displayFullNameAndPhoneForApplicantByName(View.input());
                break;
            case ALL_MENTORS: sqlite.displayAllMentors();
                break;
            case ALL_APPLICANTS: sqlite.displayAllApplicants();
                break;
            case ADD_NEW_APPLICANT: sqlite.addNewApplicant();
                break;
            case DELETE_SELECTED_APPLICANT: sqlite.deleteApplicantByID();
                break;
            case USER_COMMAND: sqlite.changeDB(View.input());
                break;
            case USER_QUERY: sqlite.selectDB(View.input());
                break;
            case QUIT:
                return false;
        }
        return true;
    }
}