package SSteria.Assesment;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DailyReportTest {

    private DailyReport report;
    private String testInputFilepath;
    private String testOutputFilepath;
    private String expectedOutputString;
    private String expectedOutputFileContents;
    private boolean expectedInputFilepathValidity;
    private int expectedWriteReturnCode;

    public DailyReportTest(String testInputFilepath, String testOutputFilepath, String expectedOutputString, String expectedOutputFileContents,
                           boolean expectedInputFilepathValidity, int expectedWriteReturnCode) {
        this.testInputFilepath = testInputFilepath;
        this.testOutputFilepath = testOutputFilepath;
        this.expectedOutputString = expectedOutputString;
        this.expectedOutputFileContents = expectedOutputFileContents;
        this.expectedInputFilepathValidity = expectedInputFilepathValidity;
        this.expectedWriteReturnCode = expectedWriteReturnCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testCases() {
        return Arrays.asList(new Object[][]{
                {"testInput1.csv","temp\\testOutput1.csv","supply,1812\nbuy,1455\nresult,357","supply,1812\nbuy,1455\nresult,357",true,1}, //1812 - 1455 - result 357
                {"testInput1.txt","temp\\testOutput1.csv","Wrong input file extension, please choose a proper input file with .csv extension.","",false,0}, //wrong file extension
        });
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @org.junit.Before
    public void setup(){
        report = new DailyReport(testInputFilepath);
    }

    @org.junit.Test
    public void getInputFilepath() {
        assertEquals(testInputFilepath,report.getInputFilepath());
    }

    @org.junit.Test
    public void testToString() {
        assertEquals(expectedOutputString,report.toString());
    }

    @org.junit.Test
    public void isInputFilepathValid() {
        assertEquals(expectedInputFilepathValidity,report.isInputFilepathValid());
    }

    @org.junit.Test
    public void writeReportToFileReturnCode() throws IOException{
        folder.newFolder("temp");
        int writeResult = report.writeReportToFile(folder.getRoot() + "\\" + testOutputFilepath);
        assertEquals(expectedWriteReturnCode,writeResult);
    }

    @org.junit.Test
    public void writeReportToFileContentsCheck() throws IOException{
        folder.newFolder("temp");
        report.writeReportToFile(folder.getRoot() + "\\" + testOutputFilepath);
        String contents = "";
        try{
            byte[] contentsBytes = Files.readAllBytes(Path.of(folder.getRoot() + "\\" + testOutputFilepath));
            contents = new String(contentsBytes);
        }
        catch (NoSuchFileException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            assertEquals(expectedOutputFileContents, contents);
        }
    }
}