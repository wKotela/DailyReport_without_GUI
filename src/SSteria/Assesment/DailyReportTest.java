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
    //Private fields below are used to handle test cases
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
                {"testInput1.csv","temp\\testOutput1.csv","supply,1812\nbuy,1455\nresult,357","supply,1812\nbuy,1455\nresult,357",true,1}, //1812 - 1455 - result 357, valid output filepath
                {"testInput1.csv","temp\\testOutput1","supply,1812\nbuy,1455\nresult,357","",true,-1}, //wrong output filepath
                {"testInput1","temp\\testOutput1","Wrong input file extension, please choose a proper input file with .csv extension.","",false,0}, //wrong input filepath
                {"","temp\\testOutput1","No filepath selected, please choose a proper input file with .csv extension.","",false,0}, //empty input filepath
                {"testInput1.txt","temp\\testOutput1.csv","Wrong input file extension, please choose a proper input file with .csv extension.","",false,0}, //wrong file extension
                {"testInput2.csv","temp\\testOutput2.csv","supply,0\nbuy,864\nresult,-864","supply,0\nbuy,864\nresult,-864",true,1}, //0 - 864 - result -864
                {"testInput3.csv","temp\\testOutput3.csv","supply,6282\nbuy,0\nresult,6282","supply,6282\nbuy,0\nresult,6282",true,1}, //6282 - 0 - result 6282
                {"testInput4.csv","temp\\testOutput4.csv","supply,2946368\nbuy,92323\nresult,2854045","supply,2946368\nbuy,92323\nresult,2854045",true,1}, //2946368 - 92323 - result 2854045
                {"testInput5.csv","temp\\testOutput5.csv","Wrong file contents, please choose a file with each row starting with buy/supply entry, then a coma separated integer value.","",false,0}, //wrong file contents
                {"C:\\Java\\ItemX.csv","temp\\ItemXOutput.csv","supply,172\nbuy,112\nresult,60","supply,172\nbuy,112\nresult,60",true,1}, //172 - 112 - result 60, selecting a file by specifying full input filepath
                {"C:\\Java\\ItemY.csv","temp\\ItemYOutput.csv","supply,435\nbuy,279\nresult,156","supply,435\nbuy,279\nresult,156",true,1}, //435 - 279 - result 156, selecting a file by specifying full input filepath
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