package com.dwarfeng.fdrh.node.record.configuration;

import com.dwarfeng.fdr.sdk.util.ServiceExceptionCodes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ExceptionCodeOffsetConfiguration {

    @Value("${fdrh.exception_code_offset}")
    private int exceptionCodeOffset;
    @Value("${fdrh.exception_code_offset.subgrade}")
    private int subgradeExceptionCodeOffset;
    @Value("${fdrh.exception_code_offset.snowflake}")
    private int snowflakeExceptionCodeOffset;
    @Value("${fdrh.exception_code_offset.fdr}")
    private int fdrExceptionCodeOffset;

    @PostConstruct
    public void init() {
        ServiceExceptionCodes.setExceptionCodeOffset(exceptionCodeOffset);
        com.dwarfeng.subgrade.sdk.exception.ServiceExceptionCodes.setExceptionCodeOffset(subgradeExceptionCodeOffset);
        com.dwarfeng.sfds.sdk.util.ServiceExceptionCodes.setExceptionCodeOffset(snowflakeExceptionCodeOffset);
        ServiceExceptionCodes.setExceptionCodeOffset(fdrExceptionCodeOffset);
    }
}
