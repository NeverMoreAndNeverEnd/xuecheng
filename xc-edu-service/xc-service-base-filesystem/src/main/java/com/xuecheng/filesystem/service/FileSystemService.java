package com.xuecheng.filesystem.service;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    public UploadFileResult upload(MultipartFile multipartFile, String fileTag, String businessKey, String metaData);
}
