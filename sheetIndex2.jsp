<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" type="text/css" href="resources/css/spread/gc.spread.sheets.excel2013white.11.2.2.css">
        <link rel="stylesheet" type="text/css" href="resources/css/buttons.css">
        <style type="text/css">
       	 	.container{
        		margin:0 auto;
        		width:950px;
        		height:510px;
        	}
        	.buttonsBar{
        		margin:10 auto;
        	}
        	.buttonsBar a{
        		margin-right: 20px;
        	}
        </style>
		<script src="resources/js/jquery/jquery-1.8.2.min.js" type="text/javascript"></script>
		<script src="resources/js/spread/gc.spread.sheets.all.11.2.2.min.js" type="text/javascript"></script>
		<script src="resources/js/spread/gc.spread.sheets.resources.zh.11.2.2.min.js" type="text/javascript"></script>
		<script src="resources/js/utils.js" type="text/javascript"></script>
    </head>
    <body>
    	<input type="text" id="inputValue">
    	<input id="btnSearch" type="button" value="搜索" />
    	<span>性别</span>
    	<select id="sex">
		  <option value ="">全部</option>
		  <option value ="男">男</option>
		  <option value ="女">女</option>
		</select>
		<span>城市</span>
    	<select id="city">
		  <option value ="">全部</option>
		  <option value ="深圳">深圳</option>
		  <option value ="广州">广州</option>
		  <option value ="武汉">武汉</option>
		</select>
		<div id="ss" style="margin:0 auto;width:1200px;height:580px;border:1px solid black;"></div>
    </body>
    
	<script src="resources/js/searchSheet.js" type="text/javascript"></script>
    <script type="text/javascript">
    var spread = new GC.Spread.Sheets.Workbook(document.getElementById("ss"));
    var spreadNS = GC.Spread.Sheets;
    var sheet = spread.getActiveSheet();
    spread.options.newTabVisible=false;
    	sheet.suspendPaint();
    	var obj;
    $(function(){
	    $.ajax({
			type: "POST",
	        url: "/getData?num=20",
	        async: false,
	        success: function(data){
	        	obj=data;
	        	sheet.setDataSource(data);
	        	sheet.resumePaint();
	        }
		});
//	    sheet.setRowCount(3, GC.Spread.Sheets.SheetArea.colHeader);
	    //设置行列宽高
//	    sheet.getRange(1, -1,  2, -1, GC.Spread.Sheets.SheetArea.viewport).width(40);
//	    sheet.getRange(-1, 1, -1, 1, GC.Spread.Sheets.SheetArea.viewport).width(120);
	    
	    sheet.setRowCount(3, GC.Spread.Sheets.SheetArea.colHeader);
//	    sheet.setColumnCount(4, GC.Spread.Sheets.SheetArea.rowHeader);
	    sheet.setValue(0, 0, "龙城中学", GC.Spread.Sheets.SheetArea.colHeader);
	    //标题设置颜色
	    var row = sheet.getRange(2, -1, 1, -1, GC.Spread.Sheets.SheetArea.colHeader);
	    row.backColor("yellow");
/* 	    sheet.frozenRowCount(2);
	    sheet.frozenColumnCount(2);
	    sheet.getRange(0, -1, 2, -1).backColor("LightCyan");
	    sheet.getRange(-1, 0, -1, 2).backColor("LightCyan"); */
	    sheet.setColumnResizable(1, false);
//	    sheet.setRowHeight(0, 90.0,GC.Spread.Sheets.SheetArea.colHeader);
//	    sheet.setColumnWidth(0, 90.0,GC.Spread.Sheets.SheetArea.rowHeader);
	    sheet.setRowHeight(0, 90.0,GC.Spread.Sheets.SheetArea.viewport);

	    //Change the width of the second column.
	    sheet.setColumnWidth(5, 200.0,GC.Spread.Sheets.SheetArea.viewport);
	    
	    //自动换行
 		var style = new GC.Spread.Sheets.Style();
		style.wordWrap = true;
		style.vAlign = GC.Spread.Sheets.VerticalAlign.center; 
		style.hAlign = GC.Spread.Sheets.HorizontalAlign.center;
	    for(var i=0;i<obj.length;i++){
			sheet.setStyle(i,1,style,GC.Spread.Sheets.SheetArea.viewport);
		    sheet.getCell(i,5).wordWrap(true);
		    sheet.autoFitRow(i);
	    }
//	    sheet.autoFitColumn(i);

		var filter = new spreadNS.Filter.HideRowFilter(new spreadNS.Range(-1, 1, -1, 3));
   		 sheet.rowFilter(filter);
   		 filter.filterButtonVisible(false);
		
		range = sheet.getRange(0, 0, 20, 8);
		range.setBorder(new spreadNS.LineBorder('black', spreadNS.LineStyle.thin), { all: true });

/* 	    spread.options.allowUserZoom = false;
	    sheet.zoom(3); */
//	    spread.options.allowUserDragDrop = true;
	   	 sheet.bind(GC.Spread.Sheets.Events.EditEnded, function (sender, args) {
           sheet.autoFitRow(args.row);
       });    
	   	 
	   	 
	   	 
	   	$("#sex").change(function(){
	   	 var sheet = spread.getActiveSheet(),
         filter = sheet.rowFilter(),
         value = $("#sex").val();
         col = 1;
         if (filter) {
        	 
        	 if(''==value){
        		 value="*";
        	 }
        	 
             filter.removeFilterItems(col);
             var condition = new spreadNS.ConditionalFormatting.Condition(spreadNS.ConditionalFormatting.ConditionType.textCondition, {
                 compareType: spreadNS.ConditionalFormatting.TextCompareType.contains,
                 expected: value
             });
             filter.addFilterItem(col, condition);

             filter.filter();

             sheet.invalidateLayout();
             sheet.repaint();
         }
	   	});
	   	 
	   	$("#city").change(function(){
	   	 var sheet = spread.getActiveSheet(),
         filter = sheet.rowFilter(),
         value = $("#city").val();
         col = 3;
         if (filter) {
        	 
        	 if(''==value){
        		 value="*";
        	 }
        	 
             filter.removeFilterItems(col);
             var condition = new spreadNS.ConditionalFormatting.Condition(spreadNS.ConditionalFormatting.ConditionType.textCondition, {
                 compareType: spreadNS.ConditionalFormatting.TextCompareType.contains,
                 expected: value
             });
             filter.addFilterItem(col, condition);

             filter.filter();
             sheet.invalidateLayout();
             sheet.repaint();
         }
	   	});
    });
    
    $("#top").click(function () {
        var activeSheet = spread.getActiveSheet();
        activeSheet.showRow(9, GC.Spread.Sheets.VerticalPosition.top);
    });
    </script>
</html>