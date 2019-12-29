package com.xuecheng.manage.cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);

    }

    @Test
    public void testFindPage(){
        int page =0;
        int size =10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if(optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("cccc");
            cmsPageRepository.save(cmsPage);
            System.out.println("success");
        }
    }
}
