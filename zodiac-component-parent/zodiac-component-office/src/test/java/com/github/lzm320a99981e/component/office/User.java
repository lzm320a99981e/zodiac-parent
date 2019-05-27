package com.github.lzm320a99981e.component.office;

import com.github.lzm320a99981e.component.office.excel.metadata.ExcelColumn;
import com.github.lzm320a99981e.component.office.excel.metadata.ExcelTable;
import lombok.Data;

@Data
@ExcelTable(limit = {1, 2})
public class User {
    @ExcelColumn
    private String name;
    @ExcelColumn(index = 2)
    private String age;
    @ExcelColumn
    private String birth;
}
