package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.item.Item;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Item> PREDICATE_SHOW_ALL_ITEMS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyStockList newData);

    /** Returns the StockList */
    ReadOnlyStockList getStockList();

    //@@author kelvintankaiboon
    /** Saves the current version of the StockList */
    void saveStockList(String fileName);
    //@@author

    /**
     * Returns true if a item with the same identity as {@code item} exists in the stock list.
     */
    boolean hasItem(Item item);

    /**
     * Deletes the given item.
     * The item must exist in the stock list.
     */
    void deleteItem(Item target);

    /**
     * Adds the given item.
     * {@code item} must not already exist in the stock list.
     */
    void addItem(Item item);

    /**
     * Replaces the given item {@code target} with {@code editedItem}.
     * {@code target} must exist in the stock list.
     * The item identity of {@code editedItem} must not be the same as another existing item in the stock list.
     */
    void updateItem(Item target, Item editedItem);

    /** Returns an unmodifiable view of the filtered item list */
    ObservableList<Item> getFilteredItemList();

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemList(Predicate<Item> predicate);

    /**
     * Updates the filter of the filtered item list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredItemListByTag(Predicate<Item> predicate);

    /**
     * Returns true if the model has previous stock list states to restore.
     */

<<<<<<< HEAD
    /**
     * Lost the given item.
     * The item must exist in the stock list.
     */
    //void lostItem(Item target);
=======
    boolean canUndoStockList();
>>>>>>> 6acad81de0d1611cb1916b635cd7085f6f847a82

    /**
     * Returns true if the model has undone stock list states to restore.
     */
<<<<<<< HEAD
    //void foundItem(Item target);
=======
    boolean canRedoStockList();
>>>>>>> 6acad81de0d1611cb1916b635cd7085f6f847a82

    /**
     * Restores the model's stock list to its previous state.
     */
    void undoStockList();

    /**
     * Restores the model's stock list to its previously undone state.
     */
    void redoStockList();

    /**
     * Saves the current stock list state for undo/redo.
     */
    void commitStockList();
}
