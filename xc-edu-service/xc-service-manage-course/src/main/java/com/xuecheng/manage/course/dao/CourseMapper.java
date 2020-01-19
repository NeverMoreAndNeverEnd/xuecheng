package com.xuecheng.manage.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator.
 */
@Component
public interface CourseMapper extends BaseMapper<CourseBase> {
   CourseBase findCourseBaseById(String id);
   IPage<CourseInfo> findCourseListPage(Page<?> page,CourseListRequest courseListRequest);
}
