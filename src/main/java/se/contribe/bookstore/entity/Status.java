package se.contribe.bookstore.entity;

/**
 * Status enumerations for the results of buying books.
 */
public enum Status {

    OK(0),
    NOT_IN_STOCK(1),
    DOES_NOT_EXIST(2);

    private int index;

    private Status(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Status getStatus(int index) {
        for (Status status : Status.values()) {
            if (status.index == index) {
                return status;
            }
        }
        return null;
    }
}
