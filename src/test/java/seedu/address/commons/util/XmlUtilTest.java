package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.JobbiBot;
import seedu.address.storage.XmlAdaptedInternship;
import seedu.address.storage.XmlAdaptedTag;
import seedu.address.storage.XmlSerializableJobbiBot;
import seedu.address.testutil.InternshipBuilder;
import seedu.address.testutil.JobbiBotBuilder;
import seedu.address.testutil.TestUtil;

public class XmlUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.xml");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.xml");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "validInternshipBook.xml");
    private static final File MISSING_INTERNSHIP_FIELD_FILE = new File(TEST_DATA_FOLDER + "missingInternshipField.xml");
    private static final File INVALID_INTERNSHIP_FIELD_FILE = new File(TEST_DATA_FOLDER + "invalidInternshipField.xml");
    private static final File VALID_INTERNSHIP_FILE = new File(TEST_DATA_FOLDER + "validInternship.xml");
    private static final File TEMP_FILE = new File(TestUtil.getFilePathInSandboxFolder("tempInternshipBook.xml"));

    private static final String INVALID_SALARY = "9482asf424";

    private static final String VALID_NAME = "ABC Company";
    private static final String VALID_SALARY = "1000";
    private static final String VALID_EMAIL = "ABC@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final String VALID_INDUSTRY = "Engineering";
    private static final String VALID_REGION = "Geylang";
    private static final String VALID_ROLE = "Safety Officer";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("saved"));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, JobbiBot.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, JobbiBot.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, JobbiBot.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        JobbiBot dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableJobbiBot.class).toModelType();
        assertEquals(9, dataFromFile.getInternshipList().size());
        assertEquals(0, dataFromFile.getTagList().size());
    }

    @Test
    public void xmlAdaptedInternshipFromFile_fileWithMissingInternshipField_validResult() throws Exception {
        XmlAdaptedInternship actualInternship = XmlUtil.getDataFromFile(
                MISSING_INTERNSHIP_FIELD_FILE, XmlAdaptedInternshipWithRootElement.class);
        XmlAdaptedInternship expectedInternship = new XmlAdaptedInternship(
                null, VALID_SALARY, VALID_EMAIL, VALID_ADDRESS, VALID_INDUSTRY, VALID_REGION,
                VALID_ROLE, VALID_TAGS);
        assertEquals(expectedInternship, actualInternship);
    }

    @Test
    public void xmlAdaptedInternshipFromFile_fileWithInvalidInternshipField_validResult() throws Exception {
        XmlAdaptedInternship actualInternship = XmlUtil.getDataFromFile(
                INVALID_INTERNSHIP_FIELD_FILE, XmlAdaptedInternshipWithRootElement.class);
        XmlAdaptedInternship expectedInternship = new XmlAdaptedInternship(
                VALID_NAME, INVALID_SALARY, VALID_EMAIL, VALID_ADDRESS, VALID_INDUSTRY, VALID_REGION, VALID_ROLE,
                VALID_TAGS);
        assertEquals(expectedInternship, actualInternship);
    }

    @Test
    public void xmlAdaptedInternshipFromFile_fileWithValidInternship_validResult() throws Exception {
        XmlAdaptedInternship actualInternship = XmlUtil.getDataFromFile(
                VALID_INTERNSHIP_FILE, XmlAdaptedInternshipWithRootElement.class);
        XmlAdaptedInternship expectedInternship = new XmlAdaptedInternship(
                VALID_NAME, VALID_SALARY, VALID_EMAIL, VALID_ADDRESS, VALID_INDUSTRY, VALID_REGION, VALID_ROLE,
                VALID_TAGS);
        assertEquals(expectedInternship, actualInternship);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new JobbiBot());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new JobbiBot());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        TEMP_FILE.createNewFile();
        XmlSerializableJobbiBot dataToWrite = new XmlSerializableJobbiBot(new JobbiBot());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableJobbiBot dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableJobbiBot.class);
        assertEquals(dataToWrite, dataFromFile);

        JobbiBotBuilder builder = new JobbiBotBuilder(new JobbiBot());
        dataToWrite = new XmlSerializableJobbiBot(
                builder.withInternship(new InternshipBuilder().build()).withTag("Friends").build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableJobbiBot.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to
     * {@code XmlAdaptedInternship} objects.
     */
    @XmlRootElement(name = "internship")
    private static class XmlAdaptedInternshipWithRootElement extends XmlAdaptedInternship {}
}
