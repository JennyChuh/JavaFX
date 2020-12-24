package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fillTableController implements Initializable {
    /**
     * static variable
     * get the result list
     * @authpr junjieZhu
     */
    static List<MSG> list;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
    private Main application;
    @FXML private TableView<MSG> table_01;
    @FXML private TableColumn id;
    @FXML private TableColumn email;
    @FXML private TableColumn content;

    /**
     * @throws Exception
     * @author junjieZhu
     */
    @FXML
    public void handle() throws Exception {
        //System.out.println("Button Pressed");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        email.setCellValueFactory(new PropertyValueFactory("email"));
        content.setCellValueFactory(new PropertyValueFactory("content"));
        table_01.getItems().clear();
        table_01.getItems().setAll(getInfo());
    }
    public void setApp(Main application){
        this.application = application;
    }

    public void chooseFile() throws Exception {
        final FileChooser fileChooser = new FileChooser();
        FileChooseController.targetfile = fileChooser.showOpenDialog(Main.stage);
        //如果没有选中任何文件，那么不做任何动作
        if(FileChooseController.targetfile==null) return;
        fillTableController tableHandler=Main.p3;
        tableHandler.handle();
    }

    /**
     * @author junjieZhu
     * export handler
     */
    @FXML
    public void exportHandle(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(Main.stage);
        String path = file.getPath();
        //创建一个HSSF,对应一个excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        //在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = workbook.createSheet("Message");
        //在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = workbook.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFCell cell = row.createCell(0);
        ((HSSFCell) cell).setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("短信内容");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("URL");
        cell.setCellStyle(style);
        //写入实体数据
        for (int i = 0; i < list.size(); ++i)
        {
            row = sheet.createRow((int) i + 1);
            //创建单元格，并设置值
            row.createCell(0).setCellValue(list.get(i).getId());
            row.createCell(1).setCellValue(list.get(i).getContent());
            row.createCell(2).setCellValue(list.get(i).getEmail());
        }
        //将文件存到指定位置
        try
        {
            String currTime=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            FileOutputStream fout = new FileOutputStream(path+"/"+currTime+"_export.xls");
            workbook.write(fout);
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String getValue(Cell cell){
        String cellValue = "";
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(cell.getDateCellValue());
                }else{
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }

    public static int readExcelValueRows(Sheet sheet) {
        int realRow = 0;// 返回的真实行数
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            //i从1开始，不判断第一行标题行
            Row row = sheet.getRow(i);
            if (row == null){
                continue;
            }
            for (Cell cell : row) {
                if (cell == null){
                    continue;
                }
                String value = getValue(cell).trim();
                if (value == null || "".equals(value)){
                    continue;
                } else{
                    realRow++;
                    break;
                }
            }
        }
        return realRow;
    }
    public List<MSG> getInfo() throws Exception{
        List<MSG>res = new LinkedList<>();
        int count=0;
        //new File("D:\\WeChat\\WeChat Files\\wxid_dy1fpxnblmaq22\\FileStorage\\File\\2020-12\\网址集合202011.xls");
        File xlsFile = FileChooseController.targetfile;
        if(xlsFile==null) return null;
        Workbook workbook = WorkbookFactory.create(xlsFile);
        int numberOfSheets = workbook.getNumberOfSheets();
        for(int i=0;i<numberOfSheets;i++){
            Sheet sheet = workbook.getSheetAt(i);
            //统计实际行数
            int rowNumber=readExcelValueRows(sheet)+1;
            Row temp = sheet.getRow(0);
            if(temp==null){
                continue;
            }
            for(int row=1;row<rowNumber;row++){
                Row r = sheet.getRow(row);
                String txt = r.getCell(1).toString().replaceAll("[\u4E00-\u9FA5]", "  ");
                //System.out.println(txt.trim());
                String regex = "(((https|http)?://)?(([a-zA-Z0-9]+((.)|(/)|(-)))|(www.))+([?])?([a-zA-Z0-9])*((.)|(/)|(-)|(=))?([a-zA-Z0-9])*)";
                Pattern p = Pattern.compile(regex);
                Matcher matcher = p.matcher(txt);
                while(matcher.find()){
                    boolean status = matcher.group(0).contains(".");
                    if(status){
                        String s1=matcher.group(0);
                        s1=s1.replaceAll(" +","");
                        while(s1.endsWith(".")||s1.endsWith("?")){
                            s1=s1.substring(0,s1.length()-1);
                        }
                        String s2 = s1.replace(".","");
                        if(!s2.equals("svip") && !s2.equals("vip")){
                            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                            if(!pattern.matcher(s1).matches()){
                                //网址
                                //System.out.println(s1.trim());
                                MSG msg=new MSG((++count)+"",txt,s1.trim());
                                res.add(msg);
                            }
                        }
                    }
                }
            }
        }
        list=res;
        return res;
    }
}
