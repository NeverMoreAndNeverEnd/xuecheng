package com.xuecheng.manage.cms.service.impl;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage.cms.dao.SysDictionaryRepository;
import com.xuecheng.manage.cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {

    private SysDictionaryRepository sysDictionaryRepository;

    @Autowired
    public SysDictionaryServiceImpl(SysDictionaryRepository sysDictionaryRepository) {
        this.sysDictionaryRepository = sysDictionaryRepository;
    }

    @Override
    public SysDictionary findDictionaryByType(String type) {
        return sysDictionaryRepository.findBydType(type);
    }
}
