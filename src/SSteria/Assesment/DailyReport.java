package SSteria.Assesment;

import java.io.*;

/**
 * <class>DailyReport</class> class enables the user to load buy/supply data from .csv file and then save a summary report to a .csv file.
 * @author Wiktor Kotela
 */

public class DailyReport {
    private String inputFilepath;
    private String outputData;
    private boolean inputFilepathValid;

    /**
     * Valid filepath of existing .csv file must be specified in the DailyReport constructor.
     *
     * @param inputFilepath of existing .csv file must be specified in a valid format, f.e. C:\\Users\\PC\\Downloads\\ItemX.csv .
     *                      Each row of the .csv file must start with <b>buy/supply</b> entry (not case-sensitive), then a coma separated value.
     *                      Invalid file contents will make <param>outputData</param> to contain only information about invalid input.
     */
    public DailyReport(String inputFilepath) {
        this.inputFilepath = inputFilepath;
        //Checking if input filepath ends with .csv extension
        if (inputFilepath.endsWith(".csv")) {
            long supply = 0;
            long buy = 0;
            long result = 0;
            //Using a BufferedReader to get file contents
            try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFilepath))) {
                boolean invalidFormat = false;
                String line;
                //Loop for reading each row of the file and checking if it's valid
                while ((line = fileReader.readLine()) != null && !invalidFormat) {
                    /*
                    Splitting data of each row to String array:
                    inputData[0] contains data header: supply/buy
                    inputData[1] contains integer value
                     */
                    String[] inputData = line.split(",");
                    //Value is parsed from string to get long type value
                    if (inputData[0].toLowerCase().equals("supply"))
                        supply += Long.parseLong(inputData[1]);
                    else if (inputData[0].toLowerCase().equals("buy"))
                        buy += Long.parseLong(inputData[1]);
                    else
                        invalidFormat = true;
                }
                //Case when all contents of the input file are valid
                if (!invalidFormat) {
                    inputFilepathValid = true;
                    result = supply - buy;
                    outputData = "supply," + supply + "\nbuy," + buy + "\nresult," + result;
                    System.out.println("Input .csv file loaded successfully from: " + inputFilepath);
                    System.out.println("Summary:");
                    System.out.println(outputData);
                }
                //Invalid contents of an input file
                else {
                    inputFilepathValid = false;
                    outputData = "Wrong file contents, please choose a file with each row starting with buy/supply entry, then a coma separated integer value.";
                    System.out.println(outputData + " (selected filepath: " + inputFilepath + ")");
                }
            }
            catch (IOException ex) {
                inputFilepathValid = false;
                outputData = "Wrong input filepath, please specify a valid one.";
                System.out.println(outputData + " (" + ex.getMessage() + ")");
            }
        }
        //Case when a file is specified properly, but it's not .csv
        else if(inputFilepath.length() == 0){
            inputFilepathValid = false;
            outputData = "No filepath selected, please choose a proper input file with .csv extension.";
            System.out.println(outputData);
        }
        else {
            inputFilepathValid = false;
            outputData = "Wrong input file extension, please choose a proper input file with .csv extension.";
            System.out.println(outputData + " (selected filepath: " + inputFilepath + ")");
        }
    }

    public String getInputFilepath() {
        return inputFilepath;
    }

    public String toString(){
        return "[" + this.getInputFilepath() + "\n" + outputData + "]";
    }

    public String getOutputData() {
        return outputData;
    }

    public boolean isInputFilepathValid() {
        return inputFilepathValid;
    }

    public void printIfInputFilepathIsValid() {
        if (inputFilepathValid)
            System.out.println("Input filepath is valid (true)");
        else
            System.out.println("Input filepath isn't valid (false)");
    }

    public void printInputFilepath() {
        System.out.println(inputFilepath);
    }

    public void printOutputData() {
        System.out.println(outputData);
    }

    /**
     * Method to save summary report as a .csv file at indicated filepath.
     *
     * @param outputFilepath indicates filepath of newly created report, f.e. C:\\Users\\PC\\Downloads\\report.csv .If indicated filepath already exists,
     *                       it will be overwritten - user must ensure it won't overwrite important existing documents. Current user also must have an OS
     *                       permission to write in a specified localization.
     * @return <b>-1</b> - if output filepath wasn't specified correctly and write to file cannot be performed
     * @return <b>0</b> - if input filepath wasn't specified correctly and write to file cannot be performed
     * @return <b>1</b> - if write to file completed successfully
     */
    public int writeReportToFile(String outputFilepath) {
        if (inputFilepathValid) {
            if (outputFilepath.endsWith(".csv")) {
                try (FileWriter fileWriter = new FileWriter(outputFilepath)) {
                    fileWriter.write(outputData);
                    fileWriter.close();
                    System.out.println("Report write performed succesfully to: " + outputFilepath);
                    return 1;
                }
                catch (IOException ex) {
                    System.out.println("Write failed.\nWrong output filepath, please specify a valid one, ending with .csv extension." + " (" + ex.getMessage()+")");
                    return -1;
                }
            }
            else {
                System.out.println("Write failed.\nWrong output filepath, please specify a valid one, ending with .csv extension.");
                return -1;
            }
        }
        else {
            System.out.println("Write failed.\nPlease specify correct input filename, before writing report to .csv file.");
            return 0;
        }
    }
}
