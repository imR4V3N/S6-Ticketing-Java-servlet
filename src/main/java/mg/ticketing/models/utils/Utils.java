package mg.ticketing.models.utils;

import jakarta.servlet.http.Part;
import mg.ticketing.models.flight.Booking;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String format(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSS");
        return dateTime.format(outputFormatter);
    }

    public static void restartData(Connection connection, String tableName) throws Exception{
        String sql = "TRUNCATE TABLE "+tableName+" RESTART IDENTITY CASCADE";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Donnee supprimé avec succès !");
            }
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public static String getFileExtension(Part picture) {
        String file_name = picture.getSubmittedFileName();
        if (file_name != null && file_name.contains(".")){
            return file_name.substring(file_name.lastIndexOf("."));
        }
        return "";
    }

    public static LocalDate getDate(Timestamp timestamp){
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String[] getDateAndTime(Timestamp timestamp){
        return timestamp.toString().split(" ");
    }

    public static String formatNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(number);
    }

    public static byte[] exportPdf(Booking booking) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Ajout du logo
            Image logo = Image.getInstance("https://imgs.search.brave.com/WATTJzqnqWeSmIHi-dgSPDjKVk-QC8059JLsTtKtcog/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly90aGVs/b2dvY29tcGFueS5u/ZXQvd3AtY29udGVu/dC91cGxvYWRzLzIw/MjIvMDMvYWlycGxh/bmVzLWZvci1zYWxl/LTMtMzAweDMwMC5w/bmc");
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);

            // Titre SkyBooking
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("SkyBooking", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Espace

            // Infos passager
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Passenger Name: " + booking.getUser().getUsername(), labelFont));
            document.add(new Paragraph("Email: " + booking.getUser().getEmail(), labelFont));
            document.add(new Paragraph("Flight ID: FL" + booking.getId(), labelFont));
            document.add(new Paragraph("From: "+booking.getFlight().getStartCity().getName()+" → To: "+ booking.getFlight().getDestinationCity().getName(), labelFont));

            document.add(new Paragraph(" ")); // Espace

            // Tableau des détails
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerBg = new BaseColor(0, 121, 182);

            String[] headers = {"DateTime", "Class", "Plane", "Seats", "Total (Ar)"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            table.addCell(booking.getFlight().getStartDate().toString());
            table.addCell(booking.getPlace().getName());
            table.addCell(booking.getFlight().getPlane().getPlaneModel().getName());
            table.addCell(String.valueOf(booking.getPlaceNumber()));
            table.addCell(String.valueOf(booking.getPrice()));

            document.add(table);

            document.add(new Paragraph(" "));

            Paragraph signature = new Paragraph("SkyBooking Company\nSignature & Seal", labelFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            document.close();
        }
        System.out.println("PDF generated successfully");
        return baos.toByteArray();
    }

    public static void exportPdf(byte[] pdfBytes, String file_name) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("/home/raven/"+file_name)) {
            fos.write(pdfBytes);
        }
    }

}
