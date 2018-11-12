package seedu.address.logic.commands;
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORIGINAL_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_ITEMS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.item.Item;
import seedu.address.model.item.Name;
import seedu.address.model.item.Status;


//@@author ChewKinWhye

/**
 * Updates the status of an existing item in the stock list.
 */

public class ChangeStatusCommand extends Command {
    public static final String COMMAND_WORD = "changeStatus";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the status of the item identified "
            + "by the name of the item.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_QUANTITY + "QUANTITY "
            + PREFIX_ORIGINAL_STATUS + "ORIGINAL STATUS "
            + PREFIX_NEW_STATUS + "NEW STATUS \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Arduino "
            + PREFIX_QUANTITY + "5 "
            + PREFIX_ORIGINAL_STATUS + "Ready "
            + PREFIX_NEW_STATUS + "Faulty";
    public static final String MESSAGE_CHANGE_STATUS_SUCCESS = "Changed Status: %1$s";
    public static final String MESSAGE_INVALID_STATUS_FIELD = "The status description is invalid";
    public static final String MESSAGE_INVALID_NAME_FIELD = "The item does not exist";
    public static final String MESSAGE_STATUS_CONSTRAINTS =
            "The updated value of each status field has to be positive";

    private Index index;
    private final ChangeStatusDescriptor changeStatusDescriptor;
    public ChangeStatusCommand(ChangeStatusDescriptor changeStatusDescriptor) {
        requireNonNull(changeStatusDescriptor);
        this.changeStatusDescriptor = new ChangeStatusDescriptor(changeStatusDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.getLoginStatus()) {
            throw new CommandException(MESSAGE_LOGIN);
        }

        List<Item> lastShownList = model.getFilteredItemList();

        index = getIndex(lastShownList, changeStatusDescriptor);

        Item itemToUpdate = lastShownList.get(index.getZeroBased());

        Item updatedItem = createUpdatedItem(itemToUpdate, changeStatusDescriptor);

        model.updateItem(itemToUpdate, updatedItem);
        model.updateFilteredItemList(PREDICATE_SHOW_ALL_ITEMS);
        model.commitStockList();
        return new CommandResult(String.format(MESSAGE_CHANGE_STATUS_SUCCESS, updatedItem));


    }

    public Index getIndex(List<Item> lastShownList, ChangeStatusDescriptor changeStatusDescriptor)
            throws CommandException {
        Index index;
        int counter = 0;
        for (Item item:lastShownList) {
            if (item.getName().equals(changeStatusDescriptor.getName())) {
                index = Index.fromZeroBased(counter);
                return index;
            }
            counter++;
        }
        throw new CommandException(MESSAGE_INVALID_NAME_FIELD);
    }
    /**
     * Creates and returns a {@code Item} with the details of {@code itemToUpdate}
     * edited with {@code changeStatusDescriptor}.
     */
    public Item createUpdatedItem(Item itemToUpdate,
                                   ChangeStatusDescriptor changeStatusDescriptor) throws CommandException {
        assert itemToUpdate != null;
        Status currentStatus = itemToUpdate.getStatus();
        Status updatedStatus;
        Integer updatedReady = currentStatus.getStatusReady();
        Integer updatedOnLoan = currentStatus.getStatusOnLoan();
        Integer updatedFaulty = currentStatus.getStatusFaulty();

        Integer changeStatusValue = changeStatusDescriptor.getQuantity();
        switch (changeStatusDescriptor.getInitialStatus()) {
        case "Ready":
            updatedReady -= changeStatusValue;
            break;
        case "On_Loan":
            updatedOnLoan -= changeStatusValue;
            break;
        case "Faulty":
            updatedFaulty -= changeStatusValue;
            break;
        default:
            throw new CommandException(MESSAGE_INVALID_STATUS_FIELD);
        }

        switch (changeStatusDescriptor.getUpdatedStatus()) {
        case "Ready":
            updatedReady += changeStatusValue;
            break;
        case "On_Loan":
            updatedOnLoan += changeStatusValue;
            break;
        case "Faulty":
            updatedFaulty += changeStatusValue;
            break;
        default:
            throw new CommandException(MESSAGE_INVALID_STATUS_FIELD);
        }
        if (updatedReady < 0 || updatedOnLoan < 0 || updatedFaulty < 0) {
            throw new CommandException(MESSAGE_STATUS_CONSTRAINTS);
        }
        updatedStatus = new Status(updatedReady, updatedOnLoan, updatedFaulty);

        return new Item(itemToUpdate.getName(), itemToUpdate.getQuantity(), itemToUpdate.getMinQuantity(),
                itemToUpdate.getLoststatus(), updatedStatus, itemToUpdate.getTags());
    }
    /**
     * Stores the details to update the item with.
     */
    public static class ChangeStatusDescriptor {
        private Name name;
        private Integer changeStatusQuantity;
        private String initialStatus;
        private String updatedStatus;

        public ChangeStatusDescriptor() {
        }

        public ChangeStatusDescriptor(Name name, Integer changeStatusQuantity,
                                      String initialStatus, String updatedStatus) {
            this.name = name;
            this.changeStatusQuantity = changeStatusQuantity;
            this.initialStatus = initialStatus;
            this.updatedStatus = updatedStatus;
        }
        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public ChangeStatusDescriptor (ChangeStatusDescriptor toCopy) {
            setName(toCopy.name);
            setQuantity(toCopy.changeStatusQuantity);
            setInitialStatus(toCopy.initialStatus);
            setUpdatedStatus(toCopy.updatedStatus);
        }
        public void setName(Name name) {
            this.name = name;
        }
        public Name getName() {
            return name;
        }
        public void setQuantity(Integer changeStatusQuantity) {
            this.changeStatusQuantity = changeStatusQuantity;
        }
        public Integer getQuantity() {
            return changeStatusQuantity;
        }
        public void setInitialStatus(String initialStatus) {
            this.initialStatus = initialStatus;
        }
        public String getInitialStatus() {
            return initialStatus;
        }

        public void setUpdatedStatus(String updatedStatus) {
            this.updatedStatus = updatedStatus;
        }
        public String getUpdatedStatus() {
            return updatedStatus;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof ChangeStatusDescriptor)) {
                return false;
            }

            ChangeStatusDescriptor otherItem = (ChangeStatusDescriptor) other;
            return (otherItem.getName().equals(this.getName())
                    && otherItem.getQuantity().equals(this.getQuantity())
                    && otherItem.getInitialStatus().equals(this.getInitialStatus())
                    && otherItem.getUpdatedStatus().equals(this.getUpdatedStatus()));
        }

    }
}
