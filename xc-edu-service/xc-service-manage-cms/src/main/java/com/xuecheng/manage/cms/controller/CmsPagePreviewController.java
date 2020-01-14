package com.xuecheng.manage.cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage.cms.service.PageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePreviewController extends BaseController {

    private PageService pageService;

    @Autowired
    public CmsPagePreviewController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping(value = "/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        try {
            String pageHtml = pageService.getPageHtml(pageId);
            if (StringUtils.isNotEmpty(pageHtml)) {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
