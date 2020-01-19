package com.xuecheng.manage.course.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.course.dao.CourseBaseRepository;
import com.xuecheng.manage.course.dao.CourseMapper;
import com.xuecheng.manage.course.dao.TeachPlanRepository;
import com.xuecheng.manage.course.dao.TeachplanMapper;
import com.xuecheng.manage.course.service.CourseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private TeachplanMapper teachplanMapper;

    private CourseBaseRepository courseBaseRepository;

    private TeachPlanRepository teachPlanRepository;

    private CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(TeachplanMapper teachplanMapper, CourseBaseRepository courseBaseRepository, TeachPlanRepository teachPlanRepository, CourseMapper courseMapper) {
        this.teachplanMapper = teachplanMapper;
        this.courseBaseRepository = courseBaseRepository;
        this.teachPlanRepository = teachPlanRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public TeachplanNode findTeachPlanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    @Override
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachPlanRoot(courseid);
        }
        Optional<Teachplan> optional = teachPlanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplanParent = optional.get();
        String parentGrade = teachplanParent.getGrade();
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachPlanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String getTeachPlanRoot(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachPlans = teachPlanRepository.findByCourseidAndAndParentid(courseid, "0");
        if (teachPlans == null || teachPlans.size() == 0) {
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            //teachPlanRepository.save(teachplan);
            teachplanMapper.insert(teachplan);
            return teachplan.getId();
        }
        return teachPlans.get(0).getId();
    }


    @Override
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page <= 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 20;
        }
        Page<CourseInfo> courseInfoPage = new Page<>(page, size);
        IPage<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseInfoPage, courseListRequest);
        List<CourseInfo> records = courseListPage.getRecords();
        long total = courseListPage.getTotal();
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(records);
        queryResult.setTotal(total);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}
