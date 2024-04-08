package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import photo.hub.service.ReportService;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadReport(){

            ByteArrayOutputStream reportStream = reportService.generateReport();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "report.pdf");
            headers.setContentLength(reportStream.size());
            return new ResponseEntity<>(reportStream.toByteArray(), headers, HttpStatus.OK);

    }
}
