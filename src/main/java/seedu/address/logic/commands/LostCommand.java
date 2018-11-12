package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITEMS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.item.Item;
import seedu.address.model.item.Loststatus;
import seedu.address.model.item.Quantity;
import seedu.address.model.item.Status;


//@@author HeHaowei

/**
 * Lost an existing item in the stock list.
 */

public class LostCommand extends Command {
    public static final String COMMAND_WORD = "lost";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lost a item from the stock list identified  "
            + "by the index number used in the displayed item list"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_QUANTITY + "QUANTITY\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_QUANTITY + "25";


    public static final String MESSAGE_LOST_ITEM_SUCCESS = "Lost Item: %1$s";
    public static final String MESSAGE_INVALID_QUANTITY = "The lost quantity input is invalid";
    public static final String MESSAGE_LOST_LARGER_THAN_READY = "The lost quantity much be larger than "
            + "or equal to the quantity of Ready items";

    private final Index targetIndex;
    private final LostDescriptor lostDescriptor;

    public LostCommand(Index targetIndex, LostDescriptor lostDescriptor) {
        requireNonNull(targetIndex);
        requireNonNull(lostDescriptor);
        this.targetIndex = targetIndex;
        this.lostDescriptor = new LostDescriptor(lostDescriptor);

    }
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.getLoginStatus()) {
            throw new CommandException(MESSAGE_LOGIN);
        }

        List<Item> lastShownList = model.getFilteredItemList();


        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        Item itemToLost = lastShownList.get(targetIndex.getZeroBased());
        Item lostItem = createLostItem(itemToLost, lostDescriptor);

        if (!itemToLost.isSameItem(lostItem) && model.hasItem(lostItem)) {
            throw new CommandException(MESSAGE_INVALID_QUANTITY);
        }
        model.updateItem(itemToLost, lostItem);
        model.updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);
        model.commitStockList();
        return new CommandResult(String.format(MESSAGE_LOST_ITEM_SUCCESS, lostItem));
    }

    /**
     * Creates and returns a {@code Item} with the details of {@code itemToLost}
     * edited with {@code LostDescriptor}.
     */
    private static Item createLostItem(Item itemToLost, LostDescriptor lostDescriptor)
            throws CommandException {
        assert itemToLost != null;
        Loststatus currentLoststatus = itemToLost.getLoststatus();
        Loststatus updatedLoststatus;
        Integer updatedLost = currentLoststatus.getLoststatusLost();
        Integer updatedFound = currentLoststatus.getLoststatusFound();

        Integer updatedValue = lostDescriptor.getLostQuantity();
        Integer initialValue = itemToLost.getQuantity().toInteger();
        Integer initialReadyValue = itemToLost.getStatus().getStatusReady();
        if (initialReadyValue - updatedValue < 0) {
            throw new CommandException(MESSAGE_LOST_LARGER_THAN_READY);
        }
        updatedLost += updatedValue;
        updatedFound -= updatedValue;
        updatedLoststatus = new Loststatus(updatedLost, updatedFound);

        Quantity updatedQuantity = new Quantity(Integer.toString(initialValue - updatedValue));
        Status updatedStatus = new Status(initialReadyValue - updatedValue,
                itemToLost.getStatus().getStatusOnLoan(), itemToLost.getStatus().getStatusFaulty());


        return new Item(itemToLost.getName(), updatedQuantity,
                itemToLost.getMinQuantity(), updatedLoststatus, updatedStatus, itemToLost.getTags());
    }

    /**
     * Stores the details to lost the item with.
     */

    public static class LostDescriptor {
        private Integer lostQuantity;

        public LostDescriptor(){}

        public LostDescriptor(Integer lostQuantity)
        {
            this.lostQuantity = lostQuantity;
        }

        public LostDescriptor(LostDescriptor toCopy) {
            setLostQuantity(toCopy.lostQuantity);
        }
        public void setLostQuantity(Integer lostQuantity) {
            this.lostQuantity = lostQuantity; }

        public Integer getLostQuantity() {
            return lostQuantity; }

    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LostCommand // instanceof handles nulls
                && targetIndex.equals(((LostCommand) other).targetIndex)); // state check
    }
}
