package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface PageService {

    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
