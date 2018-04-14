package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INTERNSHIPS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalInternships.BUSINESS1;
import static seedu.address.testutil.TypicalInternships.BUSINESS2;
import static seedu.address.testutil.TypicalInternships.BUSINESS3;
import static seedu.address.testutil.TypicalInternships.BUSINESS4;
import static seedu.address.testutil.TypicalInternships.DATASCIENCE;
import static seedu.address.testutil.TypicalInternships.ENGINEERING1;
import static seedu.address.testutil.TypicalInternships.ENGINEERING2;
import static seedu.address.testutil.TypicalInternships.ENGINEERING3;
import static seedu.address.testutil.TypicalInternships.KEYWORD_MATCHING_AUDIT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Filter;

import org.junit.Test;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.internship.Internship;
import seedu.address.model.internship.exceptions.DuplicateInternshipException;
import seedu.address.model.internship.exceptions.InternshipNotFoundException;
import seedu.address.model.internship.exceptions.TagNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
//@@author niloc94
public class FilterCommandSystemTest extends JobbiBotSystemTest {

    @Test
    public void filter() throws DuplicateInternshipException, InternshipNotFoundException {

        Model expectedModel = getModel();
        Model modelBeforeFiltering = getModel();

    /* -------------------------Filtering on an unsearched list ---------------------------------------------------- */

        /* Case: filter multiple internships in internship book, command with leading spaces and trailing spaces
         * -> 2 internships found
         */
        String command = "   " + FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_AUDIT + "  ";
        ModelHelper.setFilteredList(expectedModel, BUSINESS2, BUSINESS4);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous filter command where internship list is displaying the internships we are finding
         * -> 2 internships found
         */
        command = FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_AUDIT;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter internship where internship list is not displaying the internship we are finding -> 1 internship
         * found
         */
        command = FilterCommand.COMMAND_WORD + " Data";
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter internship with 1 repeated keyword entered -> 1 internship found */
        command = FilterCommand.COMMAND_WORD + " Data Data";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with single keywords -> 3 internships found */
        command = FilterCommand.COMMAND_WORD + " Engineering";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1, ENGINEERING2, ENGINEERING3);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with multiple keywords -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " ST Engineering";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with same 2 keywords in reverse order -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " Engineering ST";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with 3 keywords one of which is repeated -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " Engineering ST ST";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with 1 non-matching keywords -> 0 internships found */
        command = FilterCommand.COMMAND_WORD + " ST Engineering NonKeyWord";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from salary attribute -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getSalary().value;
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from name attribute -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getName().fullName;
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from role attribute -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getRole().value;
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from address attribute -> 0 internships found
         * filter does not look through address string of internship
         */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getAddress().value;
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from email attribute -> 0 internships found
         * filter does not look through email string of internship
         */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getEmail().value;
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from role attribute -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + BUSINESS1.getTags().toString().
                replaceAll("[\\[+\\]+]", " ");
        ModelHelper.setFilteredList(expectedModel, BUSINESS1);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keywords from role attribute -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + DATASCIENCE.getIndustry().value;
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous filter command -> success */
        command = UndoCommand.COMMAND_WORD;
        assertCommandSuccess(command, modelBeforeFiltering, UndoCommand.MESSAGE_SUCCESS);

        /* Case: redo previous filter command -> success */
        command = RedoCommand.COMMAND_WORD;
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, modelBeforeFiltering, RedoCommand.MESSAGE_SUCCESS);

        /* Case: filter with mixed case keywords -> 1 internships found */
        command = FilterCommand.COMMAND_WORD + " " + "DaTA";
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with mixed case keywords -> 1 internships found */
        command = "fiLtEr" + " " + "Data";
        ModelHelper.setFilteredList(expectedModel, DATASCIENCE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with attribute string to be searched as substring of keyword -> 0 internships found */
        command = FilterCommand.COMMAND_WORD + " " + "DataEXTRA";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: filter with keyword as substring of attribute in internship > 0 internships found */
        command = FilterCommand.COMMAND_WORD + " " + "Dat";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* -------------------------Filtering on a searched list ---------------------------------------------------- */

        /* Initialize Search List to limit filtering range */
        command = FindCommand.COMMAND_WORD + " " + "Engineering";
        expectedModel.updateInternship(ENGINEERING1, ENGINEERING1.addTagsToInternship("Engineering"));
        expectedModel.updateInternship(ENGINEERING2, ENGINEERING2.addTagsToInternship("Engineering"));
        expectedModel.updateInternship(ENGINEERING3, ENGINEERING3.addTagsToInternship("Engineering"));
        ModelHelper.setSearchedList(expectedModel, ENGINEERING1, ENGINEERING2, ENGINEERING3);
        assertCommandSuccess(command, expectedModel);

        /* Case: Filter keyword that exists in full list but not searched list -> 0 Internships found */
        expectedModel = getModel();
        modelBeforeFiltering = getModel();
        command = FilterCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_AUDIT;
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous filter command -> success */
        command = UndoCommand.COMMAND_WORD;
        assertCommandSuccess(command, modelBeforeFiltering, UndoCommand.MESSAGE_SUCCESS);

        /* Case: redo previous filter command -> success */
        command = RedoCommand.COMMAND_WORD;
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1, ENGINEERING2, ENGINEERING3);;
        assertCommandSuccess(command, modelBeforeFiltering, RedoCommand.MESSAGE_SUCCESS);

        /* Case: Filter keyword that exists in full list but not searched list -> 1 Internships found */
        command = FilterCommand.COMMAND_WORD + " " + "ST";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: Filter keyword that exists in full list but not searched list -> 2 Internships found */
        command = FilterCommand.COMMAND_WORD + " " + "Jurong";
        ModelHelper.setFilteredList(expectedModel, ENGINEERING1, ENGINEERING2);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }
    /* --------------------------------------- Helper Methods ----------------------------------------------------- */

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_INTERNSHIPS_LISTED_OVERVIEW} with the number of internship in the filtered
     * list, and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code JobbiBotSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see JobbiBotSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_INTERNSHIPS_LISTED_OVERVIEW, expectedModel.getFilteredInternshipList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
    }

    /**
     * Overloaded method to allow for different expected result message
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
    }
}