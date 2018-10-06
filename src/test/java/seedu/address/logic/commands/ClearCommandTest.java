package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalItems.getTypicalStockList;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.StockList;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyStockList_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitStockList();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyStockList_success() {
        Model model = new ModelManager(getTypicalStockList(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalStockList(), new UserPrefs());
        expectedModel.resetData(new StockList());
        expectedModel.commitStockList();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
