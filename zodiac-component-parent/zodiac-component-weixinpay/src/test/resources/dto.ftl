package com.github.lzm320a99981e.component.weixinpay.dto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
/**
* 接口名称：${name}(${type})
* 接口地址：@see <a href="${url!}">${name}-接口地址</a>
* 接口文档：@see <a href="${documentUrl}">${name}-接口文档</a>
*/
@XStreamAlias("xml")
@Data
public class ${className?cap_first}{
<#list tableMap?keys as key>
    <#if key == '${type}'>
        <#list tableMap[key] as item>
    /**
    * "字段名":"${item["字段名"]}",
    * "变量名":"${item["变量名"]}",
    * "必填":"${item["必填"]}",
    * "类型":"${item["类型"]}",
    * "示例值":"${item["示例值"]}",
    * "描述":"${item["描述"]}"
    */
    private <#if item["类型"]=="Int">Integer<#else>String</#if> ${item["变量名"]};
        </#list>
    </#if>
</#list>
}