package com.example.UrbanPetHouse.Controller;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.UrbanPetHouse.Entity.Book;
import com.example.UrbanPetHouse.Service.Bookservice;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class NormalController {

    @Autowired
    Bookservice bookservice;

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/book")
    public String getBooking(Model model) {
        model.addAttribute("book", new Book());
        return "book";
    }

    @GetMapping("/user/{id}")
    public String getuserdetails(@PathVariable("id") int id, Model model) {
        Optional<Book> optionalbook =bookservice.getbyid(id);
        Book book =optionalbook.get();
        model.addAttribute("book", book);
        return "userd";
    }
    

    @PostMapping("/process")
    public String processBooking(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        bookservice.savedata(book);
        int bookingId = book.getId();

        redirectAttributes.addFlashAttribute("msg", "Booking Successful! Download your PDF receipt below.");
        redirectAttributes.addFlashAttribute("bookingId", bookingId);
        return "redirect:/book";
    }

    @GetMapping("/generatePdf")
    public void generatePdf(@RequestParam("bookingId") int bookingId, HttpServletResponse response) throws IOException {
        Book book = bookservice.findById(bookingId);
        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
            return;
        }

        String date = book.getDate().toString().replace('T', ' ');

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=UrbanPetHouse_UPH" + bookingId + ".pdf");

        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title and Logo Section
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, new Color(112, 91, 48));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL, Color.BLACK);
            Paragraph title = new Paragraph("UrbanPetHouse", titleFont);
            title.setAlignment(Element.ALIGN_RIGHT);
            document.add(title);

            Paragraph subtitle = new Paragraph("Get Everything You Need\nPune, Maharashtra\n7387004328", subTitleFont);
            subtitle.setAlignment(Element.ALIGN_RIGHT);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);

            // Invoice Header Section
            Font invoiceHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(56, 56, 56));
            Paragraph invoiceLabel = new Paragraph("INVOICE", invoiceHeaderFont);
            invoiceLabel.setSpacingBefore(20);
            invoiceLabel.setSpacingAfter(10);
            document.add(invoiceLabel);

            // Customer and Invoice Info
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);
            infoTable.setSpacingAfter(10);
            infoTable.setWidths(new float[] { 1, 1 });

            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Phrase("Invoice to:", boldFont));
            leftCell.addElement(new Phrase(book.getName(), infoFont));
            leftCell.addElement(new Phrase(book.getPhoneno(), infoFont));
            leftCell.addElement(new Phrase(book.getAddress(), infoFont));

            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.addElement(new Phrase("Invoice  " + "#UPH" + bookingId, boldFont));
            rightCell.addElement(new Phrase("Date:  " + date, boldFont));

            infoTable.addCell(leftCell);
            infoTable.addCell(rightCell);

            document.add(infoTable);

            // Services Table with Padding
            PdfPTable serviceTable = new PdfPTable(4);
            serviceTable.setWidthPercentage(100);
            serviceTable.setSpacingBefore(10);
            serviceTable.setWidths(new float[] { 2, 2, 1, 1 });

            PdfPCell serviceHeader = new PdfPCell(new Phrase("Service", boldFont));
            serviceHeader.setPadding(8);
            PdfPCell typeHeader = new PdfPCell(new Phrase("Type", boldFont));
            typeHeader.setPadding(8);
            PdfPCell priceHeader = new PdfPCell(new Phrase("Price", boldFont));
            priceHeader.setPadding(8);
            PdfPCell totalHeader = new PdfPCell(new Phrase("Total", boldFont));
            totalHeader.setPadding(8);

            serviceTable.addCell(serviceHeader);
            serviceTable.addCell(typeHeader);
            serviceTable.addCell(priceHeader);
            serviceTable.addCell(totalHeader);

            // Adding service details with padding
            PdfPCell serviceCell = new PdfPCell(new Phrase(book.getService(), infoFont));
            serviceCell.setPadding(8);
            serviceTable.addCell(serviceCell);

            PdfPCell typeCell = new PdfPCell(new Phrase(book.getType(), infoFont));
            typeCell.setPadding(8);
            serviceTable.addCell(typeCell);

            PdfPCell priceCell = new PdfPCell(new Phrase(book.getPrice() + " Rs", infoFont));
            priceCell.setPadding(8);
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceTable.addCell(priceCell);

            PdfPCell totalCell = new PdfPCell(new Phrase(book.getPrice() + " Rs", infoFont));
            totalCell.setPadding(8);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            serviceTable.addCell(totalCell);

            document.add(serviceTable);

            // Total and Tax Calculation within Table
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(100);
            totalTable.setWidths(new float[] { 6, 1 });
            totalTable.setSpacingBefore(10);

            PdfPCell taxCell = new PdfPCell(new Phrase("Tax (0%)", boldFont));
            taxCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(taxCell);

            PdfPCell taxValueCell = new PdfPCell(new Phrase("0 Rs", infoFont));
            taxValueCell.setBorder(Rectangle.NO_BORDER);
            taxValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(taxValueCell);

            PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total", boldFont));
            totalLabelCell.setBorder(Rectangle.NO_BORDER);
            totalTable.addCell(totalLabelCell);

            PdfPCell totalValueCell = new PdfPCell(new Phrase(book.getPrice() + " Rs", boldFont));
            totalValueCell.setBorder(Rectangle.NO_BORDER);
            totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalValueCell);
            totalTable.setSpacingAfter(20);
            document.add(totalTable);

            // QR  Code 
            PdfPTable qrTable = new PdfPTable(1);
            qrTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            qrTable.setSpacingBefore(25);

            // Add QR Code Label
            PdfPCell qrLabelCell = new PdfPCell(new Phrase("QR CODE", boldFont));
            qrLabelCell.setBorder(Rectangle.NO_BORDER);
            qrLabelCell.setPaddingBottom(2f); 
            qrTable.addCell(qrLabelCell);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode("Booking ID: UPH" + bookingId, BarcodeFormat.QR_CODE, 100, 100);
            Path qrCodePath = Paths.get(System.getProperty("java.io.tmpdir") + "/qrcode_" + bookingId + ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodePath);

            Image qrImage = Image.getInstance(qrCodePath.toAbsolutePath().toString());
            qrImage.setAlignment(Image.ALIGN_LEFT);
            qrImage.scaleToFit(100, 100);
            document.add(qrImage);

            // Right-aligned Administrator
            Paragraph adminInfo = new Paragraph("Piyush Zagade\nAdministrator", boldFont);
            adminInfo.setAlignment(Element.ALIGN_RIGHT);
            adminInfo.setSpacingBefore(2);
            document.add(adminInfo);

            // Bottom "Thank You" Message and Terms
            Paragraph thankYou = new Paragraph("Thank you for Online Booking!", infoFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            thankYou.setSpacingBefore(30);
            document.add(thankYou);

            Font conditions = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new Color(137, 207, 240));
            Paragraph termsandcon = new Paragraph(
                    "Terms & Conditions",
                    conditions);
            Paragraph terms = new Paragraph(
                    "1. For home service visits, please present the QR code provided on this invoice. Our representative will scan the QR code to confirm and validate your booking before proceeding with payment. \n 2. For boarding services, this QR code must be scanned upon check-in to mark it as valid.",
                    infoFont);
            termsandcon.setAlignment(Element.ALIGN_CENTER);
            terms.setAlignment(Element.ALIGN_CENTER);
            termsandcon.setSpacingBefore(10);
            termsandcon.setSpacingAfter(10);
            document.add(termsandcon);
            document.add(terms);

            document.close();
            Files.deleteIfExists(qrCodePath);

        } catch (DocumentException | WriterException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/admin")
    public String getadmin(Model model) {

        List<Book> book = bookservice.getAllBooking();
        model.addAttribute("book", book);
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deletebyid(@PathVariable("id") int id) {
        bookservice.deleteBookbyid(id);
        return "redirect:/admin";
    }
    
    
}
