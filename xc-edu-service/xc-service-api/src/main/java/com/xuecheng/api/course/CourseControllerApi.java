package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程管理接口")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachPlanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("分页查询课程信息")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);
}
