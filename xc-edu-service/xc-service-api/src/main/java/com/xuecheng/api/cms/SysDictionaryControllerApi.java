package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典接口")
public interface SysDictionaryControllerApi {

    @ApiOperation(value = "数据字典查询接口")
    public SysDictionary getByType(String type);
}
