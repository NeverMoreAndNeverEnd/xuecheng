package com.xuecheng.manage.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.springframework.stereotype.Component;

@Component
public interface CategoryMapper extends BaseMapper<Category> {

    public CategoryNode selectList();
}
