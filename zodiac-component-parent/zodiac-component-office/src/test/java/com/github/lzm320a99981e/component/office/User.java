package com.github.lzm320a99981e.component.office;

import com.github.lzm320a99981e.component.office.excel.metadata.ExcelColumn;
import com.github.lzm320a99981e.component.office.excel.metadata.ExcelTable;
import lombok.Data;

@Data
@ExcelTable(limit = {1, 2})
public class User {
    @ExcelColumn
    private String name;
    @ExcelColumn
    private String sex;
    @ExcelColumn
    private String age;
    @ExcelColumn
    private String birth;
}
