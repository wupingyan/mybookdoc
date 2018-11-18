package com.grapecity.controller;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.grapecity.bean.HistoryDataBean;
import com.grapecity.bean.TemplateBean;
import com.grapecity.service.DatabaseHelper;

@Controller
public class ViewController {
    
	private static String[] ma={"name","age","sex"};
	private static Map<String,Integer> enKeyMap;
	private static Map<String,String> cnKeyMap;
	
	static{
		enKeyMap=new HashMap<String,Integer>();
		enKeyMap.put("name", 1);
		enKeyMap.put("sex", 2);
		enKeyMap.put("age", 3);
		
		cnKeyMap=new HashMap<String,String>();
		cnKeyMap.put("姓名", "name");
		cnKeyMap.put("性别", "sex");
		cnKeyMap.put("年龄", "age");
//		cnKeyMap.put("爱好", null);
//		cnKeyMap.put("邮箱", "age");
//		cnKeyMap.put("地址", "age");
//		cnKeyMap.put("联系电话", "age");
//		cnKeyMap.put("紧急联系人", "age");
	}
	/**
	 * 
	 * 跳转至指定页面接口
	 * 
	 * 		访问此接口需要带path参数，path指定跳转到页面。
	 * 
	 * */
    @RequestMapping("view")
    public ModelAndView view(HttpServletRequest request){
        String path = request.getParameter("path") + "";
        ModelAndView mav = new ModelAndView();
        // 打印数据文件路径
//        System.out.println(DatabaseHelper.fileDB);
//        System.out.println(DatabaseHelper.fileHFD);
        mav.setViewName(path);
        return mav;
    }
    
    @RequestMapping(value = "/getData",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getData(@RequestParam("num")Integer dataCount){
    	List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    	generateMapData(dataCount,list);
    	return list;
    }
    
    public  void generateMapData(int dataCount,List<Map<String,Object>> list){
    	for(int i=0;i<dataCount;i++){
    		Map<String,Object> map=new HashMap<String,Object>();
    		map.put("姓名", "张三"+i);
        	map.put("年龄", 18+i);
        	map.put("性别", i%2==0?"男":"女");
        	map.put("爱好", "小编作为一名即将毕业的学生，签下了厦门的一家软件公司，心想暂时可以不用操心毕业以后的工作问题了，心情还是挺舒坦的！也是在这个桂花飘香的时节鼓足了勇气对喜欢已久的女生表白了，只是感情的事谁也捉摸不透，被婉拒了...好吧，感情的事就先放一放。");
        	map.put("邮箱","" );
        	map.put("地址","" );
        	map.put("联系电话","" );
        	map.put("紧急联系人","" );
        	list.add(sortMapByKey(map));
//        	list.add(map);
    	}
    }
    
    public  Map<String,Object> sortMapByKey(Map<String,Object> map){
    	Map<String,Object> sortMap=new TreeMap<String,Object>(new MapKeyCompararator());
    	sortMap.putAll(map);
    	return sortMap;
    }
    
    class MapKeyCompararator implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			int t1=cnKeyMap.get(o1)==null?10:enKeyMap.get(cnKeyMap.get(o1));
			int t2=cnKeyMap.get(o2)==null?10:enKeyMap.get(cnKeyMap.get(o2));
			int i=0;
			if( t1 > t2){
				i=1;
			}else if(t1 <= t2){
				i=-1;
			}
			return i;
		}
    	
    }
    @RequestMapping("sheetIndex")
    public String indexSheet(HttpServletRequest req,Model model){
    	return "sheetIndex";
    }
    
    /**
     * 		测试用：获取文件中的模板json信息。
     * 
     * */
    @RequestMapping(value = "getTemp2",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getTemp2(HttpServletRequest request)throws Exception{

    	return DatabaseHelper.getFileCon("Form2.ssjson");
    }

    
    /**
     * 		测试用：将前台设置好的模板导出到文件。
     * 
     * */
    @RequestMapping(value = "saveTemp2",produces = "application/json;charset=utf-8")
    @ResponseBody
    public boolean saveTemp2(HttpServletRequest request, String data)throws Exception{

    	return DatabaseHelper.writeToFile("Form2_F.ssjson", data);
    }

    
    /**
     * 		测试用：将模板信息写入到存储模板信息的文件中。
     * 
     * */
    @RequestMapping("test")
    @ResponseBody
    public List<String> testFileData(HttpServletRequest request)throws Exception{
    	
    	TemplateBean tmp = new TemplateBean();
    	tmp.setTempDept("DS");
    	tmp.setTempName("tmpName");
    	tmp.setTempType("2");
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    	String date = sdf.format(new Date());
    	
    	tmp.setCreateDate(date);
    	tmp.setUpdateDate(date);
    	tmp.setTempJson(DatabaseHelper.getFileCon("Form2.ssjson"));
    	
        DatabaseHelper.addData(tmp);
        
        List<String> tmps = DatabaseHelper.getAll(DatabaseHelper.fileDB);
        
        return tmps;
    }
    

    
    /**
     * 		跳转到模板编辑页面。
     * 
     * */
    @RequestMapping("toEdit")
    public ModelAndView toEdit(HttpServletRequest request)throws Exception{
    	
    	String path = "edit";
    	String tempId = request.getParameter("tempId") + "";
        ModelAndView mav = new ModelAndView();
        mav.setViewName(path);
        mav.addObject("tempId", tempId);
        return mav;
    }
    

    
    /**
     * 		跳转到模板数据填报页面。
     * 		所需参数：
     * 			tempId 模板ID
     * 			tempName 模板名称
     * 			tempType 模板类型（固定行、动态行）
     * 			hisId 模板数据ID
     * 
     * */
    @RequestMapping(value = "toForm",produces = "application/json;charset=utf-8")
    public ModelAndView toForm(HttpServletRequest request, HttpServletResponse response)throws Exception{

    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String path = request.getParameter("path") + "";
    	String tempId = request.getParameter("tempId") + "";
    	String tempName = request.getParameter("tempName") + "";
    	tempName = URLDecoder.decode(tempName, "UTF-8");
    	String tempType = request.getParameter("tempType") + "";
    	String hisId = request.getParameter("hisId") + "";
        ModelAndView mav = new ModelAndView();
        mav.setViewName(path);
        mav.addObject("tempId", tempId);
        mav.addObject("tempName", tempName);
        mav.addObject("tempType", tempType);
        mav.addObject("hisId", hisId);
        
        return mav;
    }    
    
    /**
     * 		跳转到模板历史数据列表页面。
     * 		所需参数：
     * 			tempId 模板ID
     * 
     * */
    @RequestMapping("toHis")
    public ModelAndView toHis(HttpServletRequest request)throws Exception{
    	
    	String path = "hisData";
    	String tempId = request.getParameter("tempId") + "";
        ModelAndView mav = new ModelAndView();
        mav.setViewName(path);
        mav.addObject("tempId", tempId);
        
        return mav;
    }
    
    
    /**
     * 		保存模板数据。
     * 		所需参数：
     * 			
     * 
     * */
    @RequestMapping(value = "saveTemp",produces = "application/json;charset=utf-8")
    @ResponseBody
    public boolean saveTemp(HttpServletRequest request,
    		HistoryDataBean bean)throws Exception{
    	
    	String filePath = request.getParameter("filePath") + "";
    	String tempName = request.getParameter("tempName") + "";
    	String tempDept = request.getParameter("tempDept") + "";
    	String tempType = request.getParameter("tempType") + "";
    	String tempJson = request.getParameter("tempJson") + "";
    	String tempDataSource = request.getParameter("tempDataSource") + "";
    	
    	TemplateBean tmp = new TemplateBean();
    	tmp.setTempDept(tempDept);
    	tmp.setTempName(tempName);
    	tmp.setTempType(tempType);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    	String date = sdf.format(new Date());
    	
    	tmp.setCreateDate(date);
    	tmp.setUpdateDate(date);
    	if(StringUtils.isEmpty(tempJson)) {
        	tmp.setTempJson(DatabaseHelper.getFileCon(filePath));
    	}else {
    		tmp.setTempJson(tempJson);
    	}
    	tmp.setTempDataSource(tempDataSource);
    	
        DatabaseHelper.addData(tmp);
        return true;
    }
    
    /**
     * 		保存报表填报的数据。
     * 		所需参数：
     * 			Ajax提交HistoryDataBean的各个字段
     * 
     * */
    @RequestMapping("saveFormData")
    @ResponseBody
    public boolean saveFormData(HttpServletRequest request,
    		HistoryDataBean bean)throws Exception{
    	
    	boolean flag = DatabaseHelper.addHisData(bean);
        
        return flag;
    }
    

    /**
     * 		查询所有模板数据。
     * 
     * */
    @RequestMapping(value = "queryAll",produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<String> queryAll(HttpServletRequest request, HttpServletResponse response)throws Exception{

    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	List<String> tmps = DatabaseHelper.getAll(DatabaseHelper.fileDB);
        
        return tmps;
    }


    /**
     * 		根据模板编号，查询并返回对应的模板数据。
     * 		所需参数：
     * 			Ajax提交tempId  模板编号
     * 
     * */
    @RequestMapping(value = "queryDataById",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String queryDataById(HttpServletRequest request, HttpServletResponse response)throws Exception{
    	
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String tempId = request.getParameter("tempId") + "";
    	return DatabaseHelper.getDataByTempId(tempId);
    }
    

    /**
     * 		根据模板编号，查询并返回对应的填报数据（多条）。
     * 		所需参数：
     * 			Ajax提交tempId  模板编号
     * 
     * */
    @RequestMapping(value = "queryHisData",produces = "application/json;charset=utf-8")
    @ResponseBody
    public List<String> queryHisData(HttpServletRequest request, HttpServletResponse response)throws Exception{

    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String tempId = request.getParameter("tempId") + "";
    	List<String> tmps = DatabaseHelper.getHisDataByTempId(tempId);
        
        return tmps;
    }


    /**
     * 		根据填报数据编号，查询并返回对应的填报数据。
     * 		所需参数：
     * 			Ajax提交hisId  填报数据编号
     * 
     * */
    @RequestMapping(value = "queryHisDataById",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String queryHisDataById(HttpServletRequest request, HttpServletResponse response)throws Exception{

    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String hisId = request.getParameter("hisId") + "";
    	String hisData = DatabaseHelper.getHisDataById(hisId);
        
        return hisData;
    }
    

    /**
     * 		获取前台上传文件。新建模板
     * 
     * */
    @RequestMapping(value = "addTemp",produces = "application/json;charset=utf-8")
	public ModelAndView addTemp(HttpServletRequest request, 
			HttpServletResponse response) throws IllegalStateException, IOException {
    	
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("newTemp");
    	String tempName = request.getParameter("tempName");
    	String tempDept = request.getParameter("tempDept");
    	String tempType = request.getParameter("tempType");
    	mav.addObject("tempName", tempName);
    	mav.addObject("tempDept", tempDept);
    	mav.addObject("tempType", tempType);
    	if(StringUtils.isEmpty(tempName) || StringUtils.isEmpty(tempDept) || StringUtils.isEmpty(tempType) ) {
    		mav.addObject("errorMsg", "提交信息不完整");
    		return mav;
    	}
    	String filePath = "";
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					if(file.getSize()>5000000) {
				        mav.addObject("errorMsg", "文件内容不能大于5M");
						return mav;
					}
					if(file.getSize()<=0) {
				        mav.addObject("errorMsg", "文件内容不能为空");
						return mav;
					}
					filePath = DatabaseHelper.filePath + file.getOriginalFilename();
					//上传
					file.transferTo(new File(filePath));
					// 处理上传的模板文件，将path返回前端
					// DatabaseHelper.printAllData(file.getOriginalFilename());
					mav.addObject("filePath", filePath);
				}else {
					mav.addObject("errorMsg", "文件不能为空");
					return mav;
				}

			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("上传文件运行时间：" + String.valueOf(endTime - startTime) + "ms");
		
		TemplateBean tmp = new TemplateBean();
    	tmp.setTempDept(tempDept);
    	tmp.setTempName(tempName);
    	tmp.setTempType(tempType);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    	String date = sdf.format(new Date());
    	
    	tmp.setCreateDate(date);
    	tmp.setUpdateDate(date);
    	tmp.setTempJson(DatabaseHelper.getFileCon(filePath));
    	
        DatabaseHelper.addData(tmp);
		
		mav.setViewName("results");
		return mav;
	}
    
    /**
     * 		获取前台上传文件
     * 
     * */
    @RequestMapping(value = "upload",produces = "application/json;charset=utf-8")
	public ModelAndView upload(HttpServletRequest request, 
			HttpServletResponse response) throws IllegalStateException, IOException {
    	
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	ModelAndView mav = new ModelAndView();
        mav.setViewName("newTemp");
    	String tempName = request.getParameter("tempName");
    	String tempDept = request.getParameter("tempDept");
    	String tempType = request.getParameter("tempType");
    	mav.addObject("tempName", tempName);
    	mav.addObject("tempDept", tempDept);
    	mav.addObject("tempType", tempType);
    	String filePath = "";
		long startTime = System.currentTimeMillis();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					if(file.getSize()>5000000) {
				        mav.addObject("errorMsg", "文件内容不能大于5M");
						return mav;
					}
					if(file.getSize()<=0) {
				        mav.addObject("errorMsg", "文件内容不能为空");
						return mav;
					}
					filePath = DatabaseHelper.filePath + file.getOriginalFilename();
					//上传
					file.transferTo(new File(filePath));
					// 处理上传的模板文件，将path返回前端
					mav.addObject("fileJson", DatabaseHelper.printAllData(filePath));
					mav.addObject("filePath", filePath);
				}else {
					mav.addObject("errorMsg", "文件不能为空");
					return mav;
				}

			}

		}
		long endTime = System.currentTimeMillis();
		System.out.println("上传文件运行时间：" + String.valueOf(endTime - startTime) + "ms");
		return mav;
	}
    
    
    /**
     * 		下载模板ssjson文件
     * @throws IOException 
     * 
     * */
    @RequestMapping(value = "downloadTemp",produces = "application/json;charset=utf-8")
	public ResponseEntity<byte[]> downloadTemp(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
    	
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	
    	String tempId = request.getParameter("tempId");
    	String tempName = request.getParameter("tempName");
    	
    	String tempData = DatabaseHelper.getDataByTempId(tempId);
    	JsonValue jv = Json.parse(tempData);
    	String tempJson = jv.asObject().get("tempJson").toString();
    	
    	byte[] bytes = tempJson.getBytes("UTF-8");
    	
    	HttpHeaders headers = new HttpHeaders();
    	String downloadFielName = new String(tempName.getBytes("UTF-8"),"iso-8859-1") + ".ssjson";
    	headers.setContentDispositionFormData("attachment", downloadFielName);
    	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }
    
}