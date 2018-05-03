package view;

import java.util.Scanner;

public class View {

    public enum Options {
        NO5("Display full name for all mentors"),     //done
        NO1("Display nick names for all mentors"),    //done
        NO2("Add fullName column in applicants table"), //done
        NO3("Display full name and phone number of selected applicant by name"), //done
        NO9("Display all mentors"),  //done
        NO10("Display all applicants"), //done
        NO7("Add new applicant"),   //done
        NO8("Delete selected applicant"), //done
        NO13("User command"),       //done
        NO12("User query"),          //done
        NO11("Quit");            //done

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
                System.out.printf("No.%d : %-5s\n", o.ordinal(), o);
            }
        }
    }

    public static Integer getUserChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number option: ");
        while (!sc.hasNextInt()) {
            System.err.println("Error input");
            sc.next();
        }
        return sc.nextInt();
    }

    public static String input() {
        Scanner sc = new Scanner(System.in);
        String value;
        while (!sc.hasNext()) {
            System.err.println("Error input");
            sc.next();
        }
        value = sc.nextLine();
        return value;
    }
}
