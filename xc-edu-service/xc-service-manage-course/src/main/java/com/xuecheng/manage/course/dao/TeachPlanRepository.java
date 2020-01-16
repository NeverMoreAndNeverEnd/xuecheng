package com.xuecheng.manage.course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachPlanRepository extends JpaRepository<Teachplan,String> {

    //根据课程id和父节点id查询出节点列表,可以使用此方法实现查询根节点
    public List<Teachplan> findByCourseidAndAndParentid(String courseId,String parentId);
}
