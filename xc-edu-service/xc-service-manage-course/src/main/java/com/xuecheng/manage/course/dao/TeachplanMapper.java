package com.xuecheng.manage.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.springframework.stereotype.Component;

@Component
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    public TeachplanNode selectList(String courseId);
}
