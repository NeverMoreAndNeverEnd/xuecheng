package com.xuecheng.manage.cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface PageService {

    //分页条件查询
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    //新增页面
    CmsPageResult add(CmsPage cmsPage);

    //根据id查询
    CmsPage getById(String id);

    //修改
    CmsPageResult update(String id, CmsPage cmsPage);

    //删除
    ResponseResult delete(String id);

    //页面静态化
    String getPageHtml(String pageId);

    //页面发布
    ResponseResult postPage(String pageId);

    //保存页面
    CmsPageResult save(CmsPage cmsPage);

    //一建发布页面
    CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
