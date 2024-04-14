package photo.hub.service;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.model.Statistic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {
    private final StatisticService statisticService;

    @Autowired
    public ReportService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @SneakyThrows
    public ByteArrayOutputStream generateReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDFont font = PDType0Font.load(document, new File("src/main/resources/CaviarDreams.ttf")); // TODO в настройки
        contentStream.setFont(font, 16);
        contentStream.beginText();

        List<Statistic> statistics = statisticService.getAll();

        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("statistic");

        for (Statistic statistic : statistics) {
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("count of likes: " + statistic.getTotalLikes());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("count of views: " + statistic.getTotalViews());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("count of comments: " + statistic.getTotalComments());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("date: " + statistic.getCreatedAt());
        }
        contentStream.endText();
        contentStream.close();
        document.save(outputStream);
        document.close();

        return outputStream;
    }
}
