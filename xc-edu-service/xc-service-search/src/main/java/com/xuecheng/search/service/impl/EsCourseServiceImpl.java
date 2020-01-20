package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EsCourseServiceImpl implements EsCourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCourseServiceImpl.class);

    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;

    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;

    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public EsCourseServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
        //设置索引

        return null;
    }
}
