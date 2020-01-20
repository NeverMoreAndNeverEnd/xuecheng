package com.xuecheng.manage.cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage.cms.service.PageService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PageService pageService;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);

    }

    @Test
    public void testFindPage() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("cccc");
            cmsPageRepository.save(cmsPage);
            System.out.println("success");
        }
    }

    @Test
    public void testFindAllByExample() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CmsPage cmsPage = new CmsPage();
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");
        cmsPage.setPageAliase("轮播图");
        ExampleMatcher matching = ExampleMatcher.matching();
        matching = matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getTotalElements());
        System.out.println(all.getContent());
    }

    @Test
    public void testRestTemplate() {
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        System.out.println(forEntity);
    }

    @Test
    public void testGrids() throws FileNotFoundException {
        File file = new File("E:\\BaiduNetdiskDownload\\学成在线\\4_页面静态化\\代码\\index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId id = gridFsTemplate.store(fileInputStream, "轮播图测试文件01");
        System.out.println(id);
    }

    @Test
    public void testGetPageHtml(){
        String pageHtml = pageService.getPageHtml("5e1d803044bc6e024f4321dd");
        System.out.println(pageHtml);
    }

    @Test
    public void testGrids02() throws FileNotFoundException {
        File file = new File("E:\\workspaceforgithubdemo\\xuecheng\\static\\course.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId id = gridFsTemplate.store(fileInputStream, "课程详情模板文件","");
        System.out.println(id);
    }

}
