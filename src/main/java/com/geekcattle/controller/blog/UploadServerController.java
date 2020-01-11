package com.geekcattle.controller.blog;

import com.geekcattle.model.BaseReq;
import com.geekcattle.util.BASE64DecodedMultipartFile;
import com.geekcattle.util.BaseController;

import com.geekcattle.util.BaseResp;
import com.geekcattle.util.DateUtil2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/***
 * @Author : Jack Fang
 */
@Controller
@RequestMapping("/uploadService/")
public class UploadServerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UploadServerController.class);

    @Value("${my-file-path.root-path}")
    private String rootPath;

    @Value("${my-file-path.image-path}")
    private String imagePath;

    @Value("${my-file-path.file-path}")
    private String filePath;

    private HashMap<String, String> extMap = new HashMap<String, String>();

    public UploadServerController() {
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,zip,rar,gz,bz2");
    }

    /**
     * 上传单图片（文件流）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadImg")
    @ResponseBody
    public BaseResp uploadImg(HttpServletRequest request, HttpServletResponse response, BaseReq req) {
        try {
            Map<String, Object> outMap = upload(request, initFilePath(imagePath), "image");

            return BaseResp.success("上传成功", outMap);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResp.validError("上传文件出现异常：" + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return BaseResp.validError(e.getMessage());
        }
    }

    /**
     * 上传单图片（文件流） todo 这里做成专门为wangeditor返回图片
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "uploadImgp", method = RequestMethod.POST)
    @ResponseBody
    public Object UpLoadImg(HttpServletRequest request,@RequestParam(value="myFileName")MultipartFile mf) {
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        //获取源文件

        String filename = mf.getOriginalFilename();

        String[] names=filename.split("\\.");//

        String tempNum=(int)(Math.random()*100000)+"";

        String uploadFileName=tempNum +System.currentTimeMillis()+"."+names[names.length-1];

        //todo 图片保存路径
        File targetFile=new File(rootPath+imagePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();//创建目录
        }
        File uploadedFile = new File(targetFile, filename );
        //开始从源文件拷贝到目标文件

        //传图片一步到位
        try {
            //上传
            mf.transferTo(uploadedFile);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<String, String>();

        map.put("data","http://localhost:8004/advertIMG/"+filename);//这里应该是项目路径
        return map;//将图片地址返回

    }

    /**
     * 上传图片 Base64格式
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadImgBase64")
    @ResponseBody
    public BaseResp uploadImgBase64(HttpServletRequest request, HttpServletResponse response, BaseReq req) {

        Map<String, Object> outMap = new HashMap<>();
        // 上传图片base64
        String imgBase64 = request.getParameter("base64Code");
        if (StringUtils.isEmpty(imgBase64)) {    // 图像数据为空
            return BaseResp.validError("未获取到上传图片内容");
        }
        // 转型为MultipartHttpRequest：
        MultipartFile file = BASE64DecodedMultipartFile.base64ToMultipart(imgBase64);
        if (file == null) {
            imgBase64 = "data:image/png;base64," + imgBase64;
            MultipartFile file2 = BASE64DecodedMultipartFile.base64ToMultipart(imgBase64);
            if (file2 == null) {
                return BaseResp.validError("图片base64格式转换失败。");
            } else {
                file = file2;
            }
        }

        // 创建保存路径
        String filePath = initFilePath(imagePath);

        String fileName = DateUtil2.getFormatDate("yyyyMMddHHmmss", new Date()) + "_"
                + new Random().nextInt(1000);
        String fileRealName = file.getOriginalFilename();// 原始名称
        String[] str = fileRealName.split("\\.");
        try {
            File uploadedFile = new File(rootPath + filePath, fileName + "." + str[str.length - 1]);
            file.transferTo(uploadedFile);

            String imgUrl = filePath + fileName + "." + str[str.length - 1];
            imgUrl = imgUrl.replaceAll("\\\\", "/").replaceAll("//", "/");
            outMap.put("filePath", imgUrl);
            outMap.put("fileName", fileRealName);
            return BaseResp.success("上传成功", outMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return BaseResp.paramsError("上传文件失败。");
        }

    }

    /**
     * 批量上传图片
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadifyImg")
    @ResponseBody
    public BaseResp uploadifyImg(HttpServletRequest request, HttpServletResponse response, BaseReq req) {
        try {
            List<Map<String, Object>> list = uploadify(request, initFilePath(imagePath), "image");
            return BaseResp.success("上传成功", list);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResp.validError("上传文件出现异常：" + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return BaseResp.validError(e.getMessage());
        }

    }

    /**
     * 上传单文件（文件流）
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadFile")
    @ResponseBody
    public BaseResp uploadFile(HttpServletRequest request, HttpServletResponse response, BaseReq req) {
        try {
            Map<String, Object> outMap = upload(request, initFilePath(filePath), null);
            return BaseResp.success("上传成功", outMap);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResp.validError("上传文件出现异常：" + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return BaseResp.validError(e.getMessage());
        }

    }

    /**
     * 批量上传文件
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("uploadifyFile")
    @ResponseBody
    public BaseResp uploadifyFile(HttpServletRequest request, HttpServletResponse response, BaseReq req) {
        try {
            List<Map<String, Object>> list = uploadify(request, initFilePath(filePath), null);
            return BaseResp.success("上传成功", list);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResp.validError("上传文件出现异常：" + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return BaseResp.validError(e.getMessage());
        }

    }

    /**
     * 文件流上传
     *
     * @param request
     * @param
     * @param type    指定上传的文件类型 如：image 类型
     * @return
     */
    public Map<String, Object> upload(HttpServletRequest request, String filePath, String type) throws RuntimeException, IOException {
        Map<String, Object> map = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Iterator<String> it = fileMap.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            MultipartFile multipartFile = (MultipartFile) fileMap.get(key);
            String fileName = DateUtil2.getFormatDate("yyyyMMddHHmmss", new Date()) + "_"
                    + new Random().nextInt(1000);
            String fileRealName = multipartFile.getOriginalFilename();// 原始名称
            String[] str = fileRealName.split("\\.");

            if (!validateFileType(fileRealName, type)) {
                throw new RuntimeException("非法的文件类型.");
            }
            //todo 保存到项目路径下
            String url = request.getSession().getServletContext().getRealPath("/");

            File uploadedFile = new File(url, fileName + "." + str[str.length - 1]);
            //  File uploadedFile = new File(rootPath + filePath, fileName + "." + str[str.length-1]);
            multipartFile.transferTo(uploadedFile);

            String imgUrl = filePath + fileName + "." + str[str.length - 1];
            map.put("filePath", imgUrl);
            map.put("fileName", fileRealName);
        }
        return map;
    }


    //todo fininput 多图片上传后台还需要修改！！！！！没有返回值
    /**
     * 批量上传
     *
     * @param request
     * @param filePath
     * @param type     指定上传的文件类型 如：image 类型
     * @return
     */
    public List<Map<String, Object>> uploadify(HttpServletRequest request, String filePath, String type) throws IOException, RuntimeException {
        List<Map<String, Object>> imagePathList = new ArrayList<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Iterator<String> itValidate = fileMap.keySet().iterator();
        Iterator<String> it = fileMap.keySet().iterator();

        // 先验证文件类型
        while (itValidate.hasNext()) {
            String key = (String) itValidate.next();
            MultipartFile multipartFile = (MultipartFile) fileMap.get(key);
            String fileRealName = multipartFile.getOriginalFilename();// 原始名称
            // 验证上传文件类型
            if (!validateFileType(fileRealName, type)) {
                throw new RuntimeException("非法的文件类型.");
            }
        }
        while (it.hasNext()) {
            String key = (String) it.next();
            MultipartFile multipartFile = (MultipartFile) fileMap.get(key);
            String fileName = DateUtil2.getFormatDate("yyyyMMddHHmmss", new Date()) + "_"
                    + new Random().nextInt(1000);
            String fileRealName = multipartFile.getOriginalFilename();// 原始名称
            String[] str = fileRealName.split("\\.");
            File uploadedFile = new File(rootPath + filePath, fileName + "." + str[str.length - 1]);
            multipartFile.transferTo(uploadedFile);
            String imgUrl = filePath + fileName + "." + str[str.length - 1];
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("filePath", imgUrl);
            map.put("fileName", fileRealName);
            imagePathList.add(map);
        }
        return imagePathList;
    }

    /**
     * 验证上传的文件类型 是否在指定的文件类型列表中
     *
     * @param fileName
     * @param type     指定文件类型
     * @return
     */
    public boolean validateFileType(String fileName, String type) {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (type != null) {
            if (extMap.containsKey(type)) {
                String extType = extMap.get(type);
                String extTypes[] = extType.split(",");
                return Arrays.asList(extTypes).contains(fileType);
            } else {
                return false;
            }
        } else {
            String extType = "";
            for (String value : extMap.values()) {
                extType = extType + "," + value;
            }
            String extTypes[] = extType.split(",");
            return Arrays.asList(extTypes).contains(fileType);
        }
    }

    /**
     * 初始化 当天文件路径 或 文件夹
     *
     * @param path
     */
    public String initFilePath(String path) throws RuntimeException {
        try {
            // 当天日期文件夹
            String filePath = path + DateUtil2.getFormatDate("yyyyMMdd", new Date()) + "/";// 头像图片保存路径
            // 文件保存目录路径
            String savePath = rootPath + filePath;
            savePath.replaceAll("\\\\", "/").replaceAll("//", "/");

            // 根目录是否存在 不存在则创建
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
            // 判断当天文件夹是否存在 不存在则创建
            File dirFile = new File(savePath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            return filePath;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("创建文件异常:错误的文件路径。");
        }
    }

}
