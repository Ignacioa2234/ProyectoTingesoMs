package com.tutorial.reservas_service.service;

import com.tutorial.reservas_service.entity.ReservationEntity;
import com.tutorial.reservas_service.repository.ReservationRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final JavaMailSender mailSender;

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationEntity getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public ReservationEntity createReservation(ReservationEntity reservation) {
        ReservationEntity saved = reservationRepository.save(reservation);
        sendVoucherEmailWithPdf(saved);
        return saved;
    }

    public ReservationEntity updateReservation(Long id, ReservationEntity details) {
        ReservationEntity existing = getReservationById(id);
        existing.setStartTime(details.getStartTime());
        existing.setEndTime(details.getEndTime());
        existing.setNumberOfPersons(details.getNumberOfPersons());
        existing.setTotalPrice(details.getTotalPrice());
        existing.setGroupEmails(details.getGroupEmails());
        return reservationRepository.save(existing);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Reservation not found");
        }
        reservationRepository.deleteById(id);
    }


    private void sendVoucherEmailWithPdf(ReservationEntity r) {
        byte[] pdf = generateVoucherPdf(r);

        for (String to : r.getGroupEmails()) {
            try {
                MimeMessage msg = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
                helper.setTo(to);
                helper.setSubject("Comprobante Reserva #" + r.getId());
                helper.setText(buildVoucherText(r));
                helper.addAttachment("voucher-" + r.getId() + ".pdf",
                        new ByteArrayResource(pdf));
                mailSender.send(msg);
            } catch (MessagingException e) {
                throw new RuntimeException("Error enviando voucher por email", e);
            }
        }
    }

    private byte[] generateVoucherPdf(ReservationEntity r) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(new Paragraph("Comprobante de Reserva #" + r.getId()));
            doc.add(new Paragraph("Fecha inicio: " + r.getStartTime()));
            doc.add(new Paragraph("Fecha fin:    " + r.getEndTime()));
            doc.add(new Paragraph("Personas:     " + r.getNumberOfPersons()));
            doc.add(new Paragraph("Precio total: " + r.getTotalPrice()));
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF del voucher", e);
        }
    }

    private String buildVoucherText(ReservationEntity r) {
        return new StringBuilder()
                .append("Tu reserva #").append(r.getId()).append(" ha sido confirmada.\n")
                .append("Inicio: ").append(r.getStartTime()).append("\n")
                .append("Fin:    ").append(r.getEndTime()).append("\n")
                .append("Personas: ").append(r.getNumberOfPersons()).append("\n")
                .append("Total: ").append(r.getTotalPrice()).append("\n")
                .append("¡Gracias por tu preferencia!")
                .toString();
    }
}
