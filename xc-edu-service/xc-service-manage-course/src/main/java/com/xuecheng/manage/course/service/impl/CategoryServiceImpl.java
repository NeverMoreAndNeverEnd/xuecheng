package com.xuecheng.manage.course.service.impl;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage.course.dao.CategoryMapper;
import com.xuecheng.manage.course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryNode findList() {
        return categoryMapper.selectList();
    }
}
