package view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class View {

    public enum Options {
        NO1("Display nick names for all mentors"),
        NO2("Add fullName column in applicants table"),
        NO3("Display full name and phone number of selected person by name"),
        NO4("Display full name and phone number of selected person by email"),
        NO5("Display full name for all mentors"),
        NO6("Display nick names for all mentors"),
        NO7("Add new applicant"),
        NO8("Delete selected applicant"),
        NO9("Display all mentors"),
        NO10("Display all applicants"),
        NO11("Quit");

        private String option;

        Options(String option) {
            this.option = option;
        }

        public String getOption() {
            return option;
        }

        @Override
        public String toString() {
            return this.getOption();
        }

        public static void printOptions() {
            System.out.println("Menu:");
            for (Enum o : Options.values()) {
                System.out.printf("No.%d : %5s\n",o.ordinal(), o);
            }
        }
    }

    public int getUserChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number option: ");
        while (!sc.hasNextInt()) {
            System.err.println("Error input");
            sc.next();
        }
        int userChoice = sc.nextInt();
        return userChoice;
    }

    public static void main(String[] args) {
        View view = new View();
        view.getUserChoice();
        Options.printOptions();

    }

}
