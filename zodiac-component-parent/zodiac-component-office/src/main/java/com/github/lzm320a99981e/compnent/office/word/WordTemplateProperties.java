package com.github.lzm320a99981e.compnent.office.word;

import lombok.Data;

@Data
public class WordTemplateProperties {
    /**
     * 模板变量前缀
     */
    private String templateVariablePrefix = "${";

    /**
     * 模板变量后缀
     */
    private String templateVariableSuffix = "}";
}
