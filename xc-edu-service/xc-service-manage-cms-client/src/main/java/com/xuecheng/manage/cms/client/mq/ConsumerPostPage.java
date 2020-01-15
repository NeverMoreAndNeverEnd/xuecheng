package com.xuecheng.manage.cms.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage.cms.client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerPostPage {

    private PageService pageService;

    @Autowired
    public ConsumerPostPage(PageService pageService) {
        this.pageService = pageService;
    }

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg) {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //得到消息中页面id
        String id = (String) map.get("id");
        //调用service将页面从gridFs中下载到服务器
        pageService.savePageToServerPath(id);

    }

}
