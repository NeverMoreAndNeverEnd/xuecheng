package com.xuecheng.manage.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator.
 */
@Component
public interface CourseMapper extends BaseMapper<CourseBase> {
   CourseBase findCourseBaseById(String id);
}
