package seedu.address.model.item;

import java.util.ArrayList;
import java.util.List;

//@@author ChewKinWhye

/**
 * Represents an Item's status in the stock list.
 */
public class Status {
    public static final int STATUS_READY = 0;
    public static final int STATUS_ON_LOAN = 1;
    public static final int STATUS_FAULTY = 2;
    private final List<Integer> status = new ArrayList<>(3);

    public Status() {
        this(0, 0, 0);
    }
    public Status(int ready, int onLoan, int faulty) {
        status.add(ready);
        status.add(onLoan);
        status.add(faulty);
    }
    public Integer getStatusReady() {
        return status.get(STATUS_READY);
    }
    public Integer getStatusOnLoan() {
        return status.get(STATUS_ON_LOAN);
    }
    public Integer getStatusFaulty() {
        return status.get(STATUS_FAULTY);
    }

    public void setStatusReady(Integer ready) {
        status.set(STATUS_READY, ready);
    }
    public void setStatusOnLoan(Integer onLoan) {
        status.set(STATUS_ON_LOAN, onLoan);
    }
    public void setStatusFaulty(Integer faulty) {
        status.set(STATUS_FAULTY, faulty);
    }
    public void setDefaultValues(int quantity) {
        status.set(STATUS_READY, quantity);
        status.set(STATUS_ON_LOAN, 0);
        status.set(STATUS_FAULTY, 0);
    }
}

