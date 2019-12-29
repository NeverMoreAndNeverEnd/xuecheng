package com.xuecheng.manage.cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage.cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping
@RestController("cms/page")
public class CmsPageController implements CmsPageControllerApi {

    private PageService pageService;

    @Autowired
    public CmsPageController(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    @GetMapping("list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

        //测试数据
       /* QueryResult<CmsPage> queryResult = new QueryResult<>();
        List<CmsPage> cmsPages = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        cmsPages.add(cmsPage);
        queryResult.setList(cmsPages);
        queryResult.setTotal(1L);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);*/

        return pageService.findList(page,size,queryPageRequest);
    }
}
