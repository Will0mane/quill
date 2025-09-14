package me.will0mane.libs.quill.functional.driver;

import me.will0mane.libs.quill.results.ResultConstants;

import java.sql.ResultSet;

public class BaseReaderSet extends AbstractReader {

    public BaseReaderSet(ResultSet set) {
        add(ResultConstants.DEFAULT_SPACE, set);
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}
