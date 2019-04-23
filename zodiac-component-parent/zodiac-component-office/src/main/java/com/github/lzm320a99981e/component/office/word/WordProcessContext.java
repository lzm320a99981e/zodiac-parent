package com.github.lzm320a99981e.component.office.word;

import lombok.Data;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import java.util.Map;

@Data
public class WordProcessContext {

    private DocContentType docContentType;
    private DataType dataType;
    private XWPFDocument template;
    private XWPFParagraph paragraph;
    private XWPFTableCell cell;


    private String templateVariable;
    private Object templateVariableValue;
    private Map<String, Object> templateData;

    private Exception exception;

    public enum DocContentType {
        PARAGRAPH,
        TABLE
    }

    public enum DataType {
        TEXT,
        BYTE
    }
}
