package SSteria.Assesment;

import java.util.Scanner;

/**
 * It's a program which enables the user to generate a report from .csv supply/buy data and write it to specified location
 * using DailyReport class. User specifies input and output file location by typing input/output filepaths into the console.
 * @author Wiktor Kotela
 */
public class Main {

    public static void main(String[] args) {
        boolean repeatReportGeneration = true;
        Scanner userInput = new Scanner(System.in);
        while(repeatReportGeneration) {
            System.out.println("Please specify filepath of the input .csv file: (f.e.: C:\\Users\\PC\\input.csv)");
            DailyReport report = new DailyReport(userInput.nextLine());
            if(report.isInputFilepathValid()){
                System.out.println("Please specify write filepath of the output .csv report file: (f.e.: C:\\Users\\PC\\report.csv)");
                report.writeReportToFile(userInput.nextLine());
            }
            System.out.println("Would you like to prepare a report from another input file? (Y/N or any other)");
            if(userInput.nextLine().toLowerCase().equals("y")){
                repeatReportGeneration = true;
            }
            else{
                repeatReportGeneration = false;
            }
        }
    }
}
