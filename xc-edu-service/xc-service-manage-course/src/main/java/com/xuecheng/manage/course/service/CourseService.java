package com.xuecheng.manage.course.service;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CourseService {

    TeachplanNode findTeachPlanList(String courseId);

    ResponseResult addTeachPlan(Teachplan teachplan);
}
