package hello;

import hello.convert.ConvertPdfToExcelService;
import hello.storage.StorageFileNotFoundException;
import hello.storage.StorageService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/2 11:52
 */
@Controller
public class FileUploadController {
    private final StorageService storageService;
    private final ConvertPdfToExcelService convertPdfToExcelService;

    private org.slf4j.Logger logger = LoggerFactory.getLogger("FileUploadControllerBegin");


    @Autowired
    public FileUploadController(StorageService storageService,ConvertPdfToExcelService convertPdfToExcelService) {
        this.convertPdfToExcelService = convertPdfToExcelService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
//        storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,"serveFile",path.getFileName().toString()));
        logger.info("开始,{}", model.toString());
        model.addAttribute("files",storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class
        ,"serveFile",path.getFileName().toString()).build().toString()).collect(Collectors.toList()));
        logger.info("得到model,{}", model.toString());
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        logger.info("文件名:{}",filename);
        Resource file = storageService.loadAsResource(filename);
        logger.info("运行之后的文件源:%s",file);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getFilename()+"\"").body(file);
    }

    @PostMapping("/")
    public String handlerFileUpload (@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
                                     @RequestParam("costTime") String costTime, HttpServletResponse resp,
                                     RedirectAttributes redirectAttributes)throws IOException,ParseException {
//        storageService.store(file);
        HSSFWorkbook workbook = convertPdfToExcelService.store(file,name,costTime);
        OutputStream outputStream = null;
        outputStream = new FileOutputStream("D:\\公司\\交通费报销扆震东.xlsx");
        workbook.write(outputStream);
        /**
        ByteArrayOutputStream os = null;
        String excelName = java.net.URLEncoder.encode("滴滴车费单据","UTF-8");
        resp.setContentType("application/msexcel;charset=GBK");
        resp.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");
        try {
            os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] result = os.toByteArray();
            return result;
        } finally {
            if (os != null) {
                os.close();
            }
        }
         */
//        redirectAttributes.addFlashAttribute("message","You successfully uploaded " + file.getOriginalFilename() + "!");
        return  "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
