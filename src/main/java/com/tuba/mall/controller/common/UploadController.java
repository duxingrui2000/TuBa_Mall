package com.tuba.mall.controller.common;

import com.tuba.mall.common.Constants;
import com.tuba.mall.util.Result;
import com.tuba.mall.util.ResultGenerator;
import com.tuba.mall.util.TuBaMallUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author dxr
 * @Description 上传文件
 * @Date 15:28 6.23
 **/
@Controller
@RequestMapping("/admin")
public class UploadController {
    @Autowired
    private StandardServletMultipartResolver standardServletMultipartResolver;
    
    @PostMapping({"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新文件名
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random random = new Random();
        StringBuilder temp = new StringBuilder();
        temp.append(sdf.format(new Date())).append(random.nextInt(100)).append(suffixName);
        String newFileName = temp.toString();
        //创建文件
        File directory = new File(Constants.FILE_UPLOAD_DIC);
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    throw new IOException("文件夹创建失败，路径为" + directory);
                }
            }
            file.transferTo(destFile);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(TuBaMallUtils.getHost(new URI(request.getRequestURL()+"")) + "/upload/"+newFileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }
    
    /*允许用户利用multipart请求将本地文件上传到服务器，
    而这正是Grails的坚固基石——SpringMVC其中的一个优势。
    Spring通过对ServletAPI的HttpServletRequest接口进行扩展，
    使其能够很好地处理文件上传。
    扩展后的接口名为org.springframework.web.multipart.MultipartHttpServletRequest*/
    @PostMapping({"/upload/files"})
    @ResponseBody
    public Result uploadV2(HttpServletRequest httpServletRequest) throws URISyntaxException {
        List<MultipartFile> multipartFiles = new ArrayList<>(8);
        if (standardServletMultipartResolver.isMultipart(httpServletRequest)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) httpServletRequest;
            Iterator<String> iter = multiRequest.getFileNames();
            int total = 0;
            while (iter.hasNext()) {
                if (total > 5) {
                    return ResultGenerator.genFailResult("最多上传5张图片");
                }
                total += 1;
                MultipartFile file = multiRequest.getFile(iter.next());
                multipartFiles.add(file);
            }
        }
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (multipartFiles != null && multipartFiles.size() > 5) {
            return ResultGenerator.genFailResult("最多上传5张图片");
        }
        List<String> fileNames = new ArrayList(multipartFiles.size());
        for (int i = 0; i < multipartFiles.size(); i++) {
            String fileName = multipartFiles.get(i).getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //生成文件名称通用方法
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
            String newFileName = tempName.toString();
            File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
            //创建文件
            File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
            try {
                if (!fileDirectory.exists()) {
                    if (!fileDirectory.mkdir()) {
                        throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                    }
                }
                multipartFiles.get(i).transferTo(destFile);
                fileNames.add(TuBaMallUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultGenerator.genFailResult("文件上传失败");
            }
        }
        Result resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(fileNames);
        return resultSuccess;
    }
}
