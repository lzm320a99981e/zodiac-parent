package com.github.lzm320a99981e.compnent.office.word;

import lombok.Data;

@Data
public class WordProcessContext {

    private DocType docType;
    private DataType dataType;
    private String templateVariable;

    public enum DocType {
        PARAGRAPH,
        TABLE
    }

    public enum DataType {
        TEXT,
        BYTE
    }
}
