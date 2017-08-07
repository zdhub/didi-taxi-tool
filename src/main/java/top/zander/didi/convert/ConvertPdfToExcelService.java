package top.zander.didi.convert;

import com.google.common.collect.Lists;
import top.zander.didi.Common.Constant;
import top.zander.didi.dto.DidiSheetDTO;
import top.zander.didi.storage.StorageProperties;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/5 10:08
 */
@Service
public class ConvertPdfToExcelService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Path rootLocation;

    @Autowired
    public ConvertPdfToExcelService(StorageProperties properties){
        this.rootLocation = Paths.get(properties.getLocation());
    }

    private static final Pattern pattern = Pattern.compile("[0-9]{1,2}+\\s[\\u4e00-\\u9fa5]+\\s([0-9\\-]+)\\s([0-9:]+)\\s[^\\s]+\\s+" +
            "[^\\s]+\\s([^\\s]+)\\s([^\\s]+)\\s[0-9\\.]+\\s([0-9\\.]+).*");

    public static void main(String[] args) throws IOException, ParseException{
        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("D:\\公司\\滴滴出行行程报销单.pdf")));
        for (DidiSheetDTO didiSheetDTO : parseDidiPdf(inputStream))
            System.out.println(didiSheetDTO);
    }
    public HSSFWorkbook store(MultipartFile file, String name, String costTime) throws IOException,ParseException{
        List<DidiSheetDTO> didiSheetDTOList = Lists.newArrayList();
        logger.info("开始转换pdf，file{}",file.getName());
        didiSheetDTOList = parseDidiPdf(file.getInputStream());
        logger.info("文件{}装换完成{}",file.getName(),didiSheetDTOList.toString());

        for (DidiSheetDTO didiSheetDTO : didiSheetDTOList) {
            didiSheetDTO.setName(name);
            String endTime = DateFormatUtils.format(DateUtils.addMinutes(DateUtils.parseDate(didiSheetDTO
            .getTripDuration(), Constant.DIDI_TIME_FORMAT),Integer.valueOf(costTime)), Constant.DIDI_TIME_FORMAT);

            didiSheetDTO.setTripDuration(didiSheetDTO.getTripDuration()+ "-" + endTime);
            logger.info("didiSheetDTO:{}",didiSheetDTO.toString());
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        logger.info("开始生成excel");
        workbook = buildExcel(didiSheetDTOList);
        logger.info("得到workbork{}",workbook.toString());
        return workbook;
    }

    private HSSFWorkbook buildExcel(List<DidiSheetDTO> didiSheetDTOList) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(4);
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("日期");
        cell = row.createCell(1);
        cell.setCellValue("起止时间");
        cell = row.createCell(2);
        cell.setCellValue("事由");
        cell = row.createCell(3);
        cell.setCellValue("起点");
        cell = row.createCell(4);
        cell.setCellValue("终点");
        cell = row.createCell(5);
        cell.setCellValue("金额");
        cell = row.createCell(6);
        cell.setCellValue("票据张数");
        cell = row.createCell(7);
        cell.setCellValue("报销人");
        cell = row.createCell(8);
        cell.setCellValue("备注");
        for (int i = 0; i < didiSheetDTOList.size(); i++){
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(didiSheetDTOList.get(i).getDate());
            row.createCell(1).setCellValue(didiSheetDTOList.get(i).getTripDuration());
            row.createCell(2).setCellValue(didiSheetDTOList.get(i).getReason());
            row.createCell(3).setCellValue(didiSheetDTOList.get(i).getStart());
            row.createCell(4).setCellValue(didiSheetDTOList.get(i).getEnd());
            row.createCell(5).setCellValue(didiSheetDTOList.get(i).getCost());
            row.createCell(6).setCellValue(didiSheetDTOList.get(i).getNumber());
            row.createCell(7).setCellValue(didiSheetDTOList.get(i).getName());
            row.createCell(8).setCellValue(didiSheetDTOList.get(i).getRemark());
        }
        return workbook;
    }

    public static List<DidiSheetDTO> parseDidiPdf(InputStream inputStream) throws IOException{
        if (inputStream == null) {
            return Collections.emptyList();
        }
        PDDocument pdDocument = PDDocument.load(inputStream);
        StringWriter write =  new StringWriter();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        pdfTextStripper.writeText(pdDocument, write);
        String writeBuffer = write.getBuffer().toString();
        List<DidiSheetDTO> didiSheetDTOList = Lists.newArrayList();
        didiSheetDTOList = parse(writeBuffer);
        return didiSheetDTOList;
    }

    private static List<DidiSheetDTO> parse(String writeBuffer) {
        Matcher matcher = pattern.matcher(writeBuffer);
        List<DidiSheetDTO> didiSheetDTOList = Lists.newArrayList();
        while (matcher.find()) {
            DidiSheetDTO didiSheetDTO = new DidiSheetDTO();
            didiSheetDTO.setDate(matcher.group(1));
            didiSheetDTO.setTripDuration(matcher.group(2));
            didiSheetDTO.setStart(matcher.group(3));
            didiSheetDTO.setEnd(matcher.group(4));
            didiSheetDTO.setCost(matcher.group(5));
            didiSheetDTOList.add(didiSheetDTO);
        }
        return didiSheetDTOList;
    }
}
