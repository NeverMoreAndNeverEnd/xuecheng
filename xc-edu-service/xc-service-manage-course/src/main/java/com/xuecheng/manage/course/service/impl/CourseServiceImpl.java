package com.xuecheng.manage.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage.course.client.CmsPageClient;
import com.xuecheng.manage.course.dao.*;
import com.xuecheng.manage.course.service.CourseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;

    @Value("${course-publish.pagePhysicalPath}")
    private String publish_pagePhysicalPath;

    @Value("${course-publish.pageWebPath}")
    private String publish_pageWebPath;

    @Value("${course-publish.previewUrl}")
    private String publish_previewUrl;

    @Value("${course-publish.templateId}")
    private String publish_templateId;

    @Value("${course-publish.sitId}")
    private String publish_sitId;


    private TeachplanMapper teachplanMapper;

    private CourseBaseRepository courseBaseRepository;

    private TeachPlanRepository teachPlanRepository;

    private CourseMapper courseMapper;

    private CourseMarketRepository courseMarketRepository;

    private CoursePicRepository coursePicRepository;

    private CmsPageClient cmsPageClient;

    private CoursePubRepository coursePubRepository;

    @Autowired
    public CourseServiceImpl(TeachplanMapper teachplanMapper, CourseBaseRepository courseBaseRepository,
                             TeachPlanRepository teachPlanRepository, CourseMapper courseMapper,
                             CourseMarketRepository courseMarketRepository, CoursePicRepository coursePicRepository,
                             CmsPageClient cmsPageClient, CoursePubRepository coursePubRepository) {
        this.teachplanMapper = teachplanMapper;
        this.courseBaseRepository = courseBaseRepository;
        this.teachPlanRepository = teachPlanRepository;
        this.courseMapper = courseMapper;
        this.courseMarketRepository = courseMarketRepository;
        this.coursePicRepository = coursePicRepository;
        this.cmsPageClient = cmsPageClient;
        this.coursePubRepository = coursePubRepository;
    }

    @Override
    public TeachplanNode findTeachPlanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    @Override
    @Transactional
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            parentid = this.getTeachPlanRoot(courseid);
        }
        assert parentid != null;
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

    @Override
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    @Override
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }

    @Override
    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getCourseBaseById(id);
        if (one == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        return optional.orElse(null);
    }

    @Override
    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());
            one.setEndTime(courseMarket.getEndTime());
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
        } else {
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            one.setId(id);
        }
        courseMarketRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (optional.isPresent()) {
            coursePic = optional.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    public CoursePic findCoursePicById(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        return optional.orElse(null);
    }

    @Override
    @Transactional
    public ResponseResult deleteCoursePicById(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
        optionalCourseBase.ifPresent(courseView::setCourseBase);

        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(courseId);
        optionalCourseMarket.ifPresent(courseView::setCourseMarket);

        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(courseId);
        optionalCoursePic.ifPresent(courseView::setCoursePic);

        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if (teachplanNode != null) {
            courseView.setTeachplanNode(teachplanNode);
        }

        return courseView;
    }

    @Override
    public CoursePublishResult preview(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);

        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_sitId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(publish_pageWebPath);
        cmsPage.setPagePhysicalPath(publish_pagePhysicalPath);
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);

        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        String pageId = cmsPageResult.getCmsPage().getPageId();
        String pageUrl = publish_previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    @Override
    @Transactional
    public CoursePublishResult publish(String courseId) {
        CourseBase one = this.getCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = this.publishPage(courseId);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //更新课程状态
        this.saveCoursePubState(courseId);
        //课程索引
        CoursePub coursePub = this.createCoursePub(courseId);
        CoursePub coursePubNew = this.saveCoursePub(courseId, coursePub);
        if (coursePubNew == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CREATE_INDEX_ERROR);
        }
        //课程缓存
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    @Override
    public CoursePub saveCoursePub(String courseId, CoursePub coursePub) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub coursePubNew = null;
        Optional<CoursePub> optional = coursePubRepository.findById(courseId);
        if (optional.isPresent()) {
            coursePubNew = optional.get();
        }
        if (coursePubNew == null) {
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub, coursePubNew);
        coursePubNew.setId(courseId);
        coursePubNew.setTimestamp(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }

    private CoursePub createCoursePub(String id) {
        CoursePub coursePub = new CoursePub();
        coursePub.setId(id);
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        optionalCourseBase.ifPresent(courseBase -> BeanUtils.copyProperties(coursePub, courseBase));
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        optionalCoursePic.ifPresent(coursePic -> BeanUtils.copyProperties(coursePub, coursePic));
        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(id);
        optionalCourseMarket.ifPresent(courseMarket -> BeanUtils.copyProperties(coursePub, courseMarket));
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String teachPlanString = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachPlanString);
        return coursePub;
    }


    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);
        courseBase.setStatus("202002");
        return courseBaseRepository.save(courseBase);
    }

    private CmsPostPageResult publishPage(String courseId) {
        CourseBase courseBase = this.getCourseBaseById(courseId);
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_sitId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(publish_pageWebPath);
        cmsPage.setPagePhysicalPath(publish_pagePhysicalPath);
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        return cmsPageClient.postPageQuick(cmsPage);
    }
}
