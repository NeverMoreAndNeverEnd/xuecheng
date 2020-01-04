package com.xuecheng.manage.cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

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
}
