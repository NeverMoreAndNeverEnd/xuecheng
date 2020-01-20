package com.xuecheng.search.service;

import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface EsCourseService {

    QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam);
}
