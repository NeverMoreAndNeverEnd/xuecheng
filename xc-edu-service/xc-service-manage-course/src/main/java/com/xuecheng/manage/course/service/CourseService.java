package com.xuecheng.manage.course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CourseService {

    TeachplanNode findTeachPlanList(String courseId);

    ResponseResult addTeachPlan(Teachplan teachplan);

    QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    AddCourseResult addCourseBase(CourseBase courseBase);

    CourseBase getCourseBaseById(String courseId);

    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    CourseMarket getCourseMarketById(String courseId);

    ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);

    ResponseResult saveCoursePic(String courseId,String pic);

    CoursePic findCoursePicById(String courseId);

    ResponseResult deleteCoursePicById(String courseId);

    CourseView getCourseView(String courseId);

    CoursePublishResult preview(String courseId);

    CoursePublishResult publish(String courseId);
}
