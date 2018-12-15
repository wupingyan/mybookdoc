private static  XSSFCellStyle setXSSFCellStyleAttribute(XSSFWorkbook workbook){
		//生成一个样式，用来设置标题样式  
		XSSFCellStyle style = workbook.createCellStyle();  
        //设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);  
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);  
        //生成一个字体  
        XSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  
        //把字体应用到当前的样式  
        style.setFont(font);  
      
		return style;
	}
  
  XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		sheet.setColumnWidth(0, 512*3);
		sheet.setColumnWidth(1, 512*4);
		sheet.setColumnWidth(2, 512*8);
		sheet.setColumnWidth(3, 512*8);
		sheet.setColumnWidth(4, 512*6);
		sheet.setColumnWidth(5, 512*6);
		sheet.setColumnWidth(6, 512*6);
		sheet.setColumnWidth(7, 512*10);
		sheet.setColumnWidth(8, 512*6);
		sheet.setColumnWidth(9, 512*7);
		workbook.setSheetName(0, title);
		XSSFCellStyle style = setXSSFCellStyleAttribute(workbook);
    
    
    //单元格值居中 begin
		XSSFCellStyle centerCellStyle=workbook.createCellStyle();
		centerCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		//单元格值居中 end
    
    
    //第一行标题   
		XSSFRow row0=sheet.createRow((short)0);
		XSSFCell row0Cel=row0.createCell(0);
		row0Cel.setCellStyle(style);
		row0Cel.setCellValue(companyName);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length-1));  //合并单元格
