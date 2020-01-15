package com.xuecheng.manage.cms.client.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage.cms.client.dao.CmsPageRepository;
import com.xuecheng.manage.cms.client.dao.CmsSiteRepository;
import com.xuecheng.manage.cms.client.service.PageService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageServiceImpl.class);

    private CmsPageRepository cmsPageRepository;

    private GridFsTemplate gridFsTemplate;

    private GridFSBucket gridFSBucket;

    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    public PageServiceImpl(CmsPageRepository cmsPageRepository, GridFsTemplate gridFsTemplate, GridFSBucket gridFSBucket, CmsSiteRepository cmsSiteRepository) {
        this.cmsPageRepository = cmsPageRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFSBucket = gridFSBucket;
        this.cmsSiteRepository = cmsSiteRepository;
    }

    //保存html页面到服务器物理路径
    @Override
    public void savePageToServerPath(String pageId) {
        //得到html文件id
        CmsPage cmsPage = this.findCmsPageById(pageId);
        if (cmsPage == null) {
            LOGGER.error("findCmsPageById cmsPage is null ,pageId:{}", pageId);
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
            return;
        }
        String htmlFileId = cmsPage.getHtmlFileId();
        //从gridFs查询html文件
        InputStream inputStream = this.getFieldById(htmlFileId);
        if (inputStream == null) {
            LOGGER.error("getFieldById inputStream is null ,htmlFileId:{}", htmlFileId);
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            return;
        }
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        if (cmsSite == null) {
            LOGGER.error("findCmsSiteById cmsSite is null ,siteId:{}", siteId);
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
            return;
        }
        //得到站点物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //将html文件保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //根据文件id从gridFs查询文件内容
    private InputStream getFieldById(String fieldId) {
        try {
            GridFSFile gridFsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fieldId)));
            assert gridFsFile != null;
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFsFile, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
        return null;
    }

}
