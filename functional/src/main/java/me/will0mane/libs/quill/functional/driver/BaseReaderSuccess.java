package me.will0mane.libs.quill.functional.driver;

public class BaseReaderSuccess extends AbstractReader {

    private final boolean success;
    private final int updateCount;

    public BaseReaderSuccess(boolean success) {
        this.success = success;
        this.updateCount = -1;
    }

    public BaseReaderSuccess(int updateCount) {
        this.success = true;
        this.updateCount = updateCount;
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public int updateCount() {
        return updateCount;
    }
}

