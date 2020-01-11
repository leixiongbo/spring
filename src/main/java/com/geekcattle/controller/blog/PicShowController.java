package com.geekcattle.controller.blog;

import com.geekcattle.core.shiro.AdminShiroUtil;
import com.geekcattle.mapper.blog.LifeShareMapper;
import com.geekcattle.mapper.blog.PicShowMapper;
import com.geekcattle.model.blog.LifeShare;
import com.geekcattle.model.blog.PicShow;
import com.geekcattle.model.console.Admin;
import com.geekcattle.util.DateUtil;
import com.geekcattle.util.DateUtil2;
import com.geekcattle.util.ReturnUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

//@RestController
@Controller
@RequestMapping("/api/picshow")
public class PicShowController {

    @Value("${my-file-path.root-path}")
    private String rootPath;

    @Value("${my-file-path.image-path}")
    private String imagePath;

    @Value("${my-file-path.file-path}")
    private String filePath;


    @Resource
    private PicShowMapper picShowMapper;

    @Resource
    private LifeShareMapper lifeShareMapper;
    //h获取图片数据
    @RequestMapping(value = "/picshowalldata", method = RequestMethod.GET)
    public ModelMap getPicShowDta(HttpServletRequest request) {
        try {
            List<PicShow> picShowList = picShowMapper.selectAll();
            List<PicShow> picShowList1 = new ArrayList<PicShow>();
            for (int i=0;i<picShowList.size();i++){
             //   picShow.setSrc(request.getContextPath()+picShow);
                String[] src =picShowList.get(i).getSrc().split(",");
                for (int j = 0; j < src.length; j++) {
                    System.out.println(src[j]);
                    PicShow picShow = new PicShow();
                    picShow =picShowList.get(i) ;
                    picShow.setSrc("http://127.0.0.1:8004/advertIMG/"+src[j]);
                    picShow.setResource("http://127.0.0.1:8004/advertIMG/"+src[j]);
                    picShowList1.add(picShow);
                }
            }

            return ReturnUtil.Success(null, picShowList1, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error(null, null, null);
        }
    }

    //todo h获取博文数据
    @RequestMapping(value = "/aitlealldata", method = RequestMethod.GET)
    public ModelMap getAlitleDta(HttpServletRequest request) {
        try {
            List<LifeShare> lifeShareList = lifeShareMapper.selectAll();
            return ReturnUtil.Success(null, lifeShareList, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error(null, null, null);
        }
    }


    // todo  http://127.0.0.1:8004/api/picshow/uploader
    @RequestMapping(value = "/uploader")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> json = new HashMap<String, Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        /** 页面控件的文件流* */
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);
        }
        /** 获取文件的后缀* */
        String filename = multipartFile.getOriginalFilename();

        InputStream inputStream;
        String outPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String newVersionName = "";
        String fileMd5 = "";
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;

        try {
            inputStream = multipartFile.getInputStream();
            fileOutputStream = new FileOutputStream(rootPath + imagePath + filename);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("data", filename);
        json.put("fileMd5", fileMd5);
        json.put("message", "图片上传成功");
        json.put("status", true);
        return json;
    }


    /**
     * 获取酒店图片地址
     *
     * @return
     * @throws Exception
     */
    public String getHotelPictrueAddress() throws Exception {
//        Properties prop =  new  Properties();
//        InputStream in= this.getClass().getResourceAsStream("/ticketConfig.properties");
//        prop.load(in);
        String pictureAddress = "upLoadHotelPicture";
        // prop.getProperty("upLoadHotelPicturePath");

        File file = new File(pictureAddress);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return pictureAddress;
    }


    /**
     * 上传文件
     *
     * @param request
     * @param response
     * @param file     上传的文件，支持多文件
     * @throws Exception
     */
    @RequestMapping(value = "/saveImages")
    @ResponseBody
    public Map<String, Object> saveImages(HttpServletRequest request, HttpServletResponse response
            , @RequestParam("file") MultipartFile[] file) throws Exception {

        Map<String, Object> rsMap = new HashMap<String, Object>();
        String uploadPath = getHotelPictrueAddress();
        if (file != null && file.length > 0) {
            //组合image名称，“,隔开”
            List<String> fileName = new ArrayList<String>();

            try {
                for (int i = 0; i < file.length; i++) {
                    if (!file[i].isEmpty()) {
                        //上传文件
                        fileName.add(uploadImage(request, uploadPath, file[i], false));
                    }
                }

                if (fileName != null && fileName.size() > 0) {
                    System.out.println("上传成功！");
                    rsMap.put("result", "1");
                    rsMap.put("fileNames", fileName);


                } else {
                    rsMap.put("result", "-1");
                    rsMap.put("msg", "上传失败！文件格式错误！");
                }

                ReturnUtil.Success("成功", rsMap, null);
            } catch (Exception e) {
                e.printStackTrace();
                rsMap.put("result", "-1");
                rsMap.put("msg", "上传出现异常！异常出现在：class.UploadController.insert()");
            }
        } else {
            rsMap.put("result", "-1");
            rsMap.put("msg", "没有检测到文件！");
        }
        return rsMap;
    }


    /**
     * 上传图片
     * 原名称
     *
     * @param request      请求
     * @param path_deposit 存放位置(路径)
     * @param file         文件
     * @param isRandomName 是否随机名称
     * @return 完整文件路径
     */
    public String uploadImage(HttpServletRequest request, String path_deposit, MultipartFile file, boolean isRandomName) {
        //上传
        try {
            String[] typeImg = {"gif", "png", "jpg"};

            if (file != null) {
                String origName = file.getOriginalFilename();// 文件原名称
                System.out.println("上传的文件原名称:" + origName);
                // 判断文件类型
                String type = origName.indexOf(".") != -1 ? origName.substring(origName.lastIndexOf(".") + 1, origName.length()) : null;
                if (type != null) {
                    boolean booIsType = false;
                    for (int i = 0; i < typeImg.length; i++) {
                        if (typeImg[i].equals(type.toLowerCase())) {
                            booIsType = true;
                        }
                    }
                    //类型正确
                    if (booIsType) {
                        //存放图片文件的路径
                        String path = rootPath; //自己定义的路径
                        //组合名称
                        String fileSrc = "";
                        //是否随机名称
                        if (isRandomName) {
                            origName = UUID.randomUUID().toString() + origName.substring(origName.lastIndexOf("."));
                        } else {
                            Date currentTime = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                            String dateString = formatter.format(currentTime);
                            origName = dateString;

                        }
                        String fileName = DateUtil2.getFormatDate("yyyyMMddHHmmss", new Date()) + "_"
                                + new Random().nextInt(1000);
//                        //判断是否存在目录
//                        ServletContext cxt = request.getServletContext();
//                        String qrParentPath = cxt.getRealPath("/static");
                        //   File parPath = new File(qrParentPath + "/upLoadHotelPicture");
                        //   File targetFile=new File(parPath,origName);
                        File targetFile = new File(rootPath + imagePath);
                        if (!targetFile.exists()) {
                            targetFile.mkdirs();//创建目录
                        }
                        String fileRealName = file.getOriginalFilename();// 原始名称
                        String[] str = fileRealName.split("\\.");
                        File uploadedFile = new File(rootPath + imagePath, file.getOriginalFilename());

                        //上传
                        file.transferTo(uploadedFile);
                        //完整路径
                        //   fileSrc=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/static/"+path_deposit+"/"+origName;
                        fileSrc = file.getOriginalFilename();
                        System.out.println("图片上传成功:" + fileSrc);
                        return fileSrc;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //todo  以上是图片上传的-----------------------------------------

    /**
     * 图片展示picshow提交
     */
    @Transactional
    @RequestMapping(value = "/picSave", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) {
        Admin admin = AdminShiroUtil.getUserInfo();
        Map<String, Object> json = new HashMap<String, Object>();
        PicShow picShow = new PicShow();
        picShow.setSrc(request.getParameter("uploadHotelPicture1Names"));
        picShow.setResource(request.getParameter("uploadHotelPicture1Names"));
        picShow.setSpecies(request.getParameter("picType"));
        picShow.setRemark(request.getParameter("remark"));
        picShow.setPictime(DateUtil.getCurrentTime());
        picShow.setType("image");
        picShow.setUserid(admin.getUid());
      //  picShow.setId("4");
        picShowMapper.insert(picShow);
        System.out.print("picShow:" + picShow);
        json.put("picShow", picShow);

        return json;
    }


    /**
     * todo 文章发布
     */
    @RequestMapping(value = "/aticleShow")
    public ModelAndView  aticleShow(HttpServletRequest request, HttpServletResponse response) {
        System.out.print("title:" + request.getParameter("title"));
        System.out.print("content:" + request.getParameter("content"));
        ModelAndView mav=new ModelAndView("blog/shuaxinyemian");
        mav.addObject("title",request.getParameter("title"));
        mav.addObject("content", request.getParameter("content"));

        LifeShare lifeShare = new LifeShare();
        lifeShare.setTitle(request.getParameter("title"));
        lifeShare.setContent(request.getParameter("content"));
        lifeShare.setCreattime(DateUtil.getCurrentTime());
        lifeShare.setZan("55");
        lifeShare.setUpdatastatu("1");
        lifeShareMapper.insert(lifeShare);

        return mav;
    }

    /**
     * todo 点击加载文章内容
     */
    @RequestMapping(value = "/dianjiaticleShow")
    public ModelAndView  dianjiaticleShow(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav=new ModelAndView("blog/shuaxinyemians");
        //这里前台不能传model类型回来吗 ，只能是string？？？
  //      LifeShare lifeShare =request.getParameter("model");
        mav.addObject("list", request.getParameter("model"));
        return mav;
    }


    /**
     * todo 文章列表展示
     */
    @RequestMapping(value = "/bloglist")
    public String   bloglistShow(Model map) {
     //   Map<String, LifeShare> json = new HashMap<String, LifeShare>();
        List<LifeShare> lifeShareList = lifeShareMapper.selectAll();
         map.addAttribute("listmap", lifeShareList);
        return "blog/bloglist";
    }
}
