package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage.cms.dao.CmsPageRepository;
import com.xuecheng.manage.cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageServiceImpl implements PageService {

    private CmsPageRepository cmsPageRepository;

    @Autowired
    public PageServiceImpl(CmsPageRepository cmsPageRepository) {
        this.cmsPageRepository = cmsPageRepository;
    }

    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setTotal(all.getTotalElements());      //数据总记录数
        queryResult.setList(all.getContent());     //数据列表
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
