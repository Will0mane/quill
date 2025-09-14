package me.will0mane.libs.quill.functional.driver;

public class BaseReaderSuccess extends AbstractReader {

    private final boolean success;

    public BaseReaderSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }
}

