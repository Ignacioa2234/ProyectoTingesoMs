package com.tutorial.reservas_service.service;

import com.tutorial.reservas_service.entity.ReservationEntity;
import com.tutorial.reservas_service.repository.ReservationRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import java.awt.Color;


import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;      // debe estar anotado @LoadBalanced
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    // URLs base (podrían moverse a yml)
    private static final String TARIFAS_URL        = "lb://tarifas-service/tarifa?laps={laps}";
    private static final String DESC_GRUPO_URL     = "lb://descuentos-personas-service/discount/grupo?size={size}";
    private static final String DESC_FREC_URL      = "lb://descuentos-frecuentes-service/discount/frecuente?user={user}";
    private static final String DESC_CUMPLE_URL    = "lb://tarifas-especiales-service/discount/cumple?user={user}";

    public ReservationEntity createReservation(ReservationEntity r) {
        // 1) Código y fecha
        r.setReservationCode("RES" + System.currentTimeMillis());
        r.setReservationDate(LocalDateTime.now());

        // 2) Duración en minutos
        int duration = (int) Duration.between(r.getStartTime(), r.getEndTime()).toMinutes();
        r.setVuelTime(duration);

        // 3) Extraer nombres
        List<String> names = r.getGroupEmails().stream()
                .map(e -> e.substring(0, e.indexOf("@")))
                .collect(Collectors.toList());
        r.setIntegrantesNombres(String.join(",", names));
        r.setNumberOfPersons(names.size());

        // 4) Llamadas a microservicios
        Integer baseFare = restTemplate.getForObject(TARIFAS_URL, Integer.class, duration);
        Integer grpDisc  = restTemplate.getForObject(DESC_GRUPO_URL, Integer.class, names.size());

        List<Integer> freqDiscs = names.stream()
                .map(u -> restTemplate.getForObject(DESC_FREC_URL, Integer.class, u))
                .collect(Collectors.toList());
        List<Integer> bdayDiscs = names.stream()
                .map(u -> restTemplate.getForObject(DESC_CUMPLE_URL, Integer.class, u))
                .collect(Collectors.toList());

        // 5) CSVs
        String csvBase  = names.stream().map(n -> baseFare.toString()).collect(Collectors.joining(","));
        String csvGrp   = names.stream().map(n -> grpDisc.toString()).collect(Collectors.joining(","));
        String csvFreq  = freqDiscs.stream().map(String::valueOf).collect(Collectors.joining(","));
        String csvBday  = bdayDiscs.stream().map(String::valueOf).collect(Collectors.joining(","));
        r.setIntegrantesTarifaBase(csvBase);
        r.setIntegrantesDescGrupo(csvGrp);
        r.setIntegrantesDescFrecuente(csvFreq);
        r.setIntegrantesDescCumple(csvBday);

        // 6) Neto, IVA y totales
        List<Double> netos = IntStream.range(0, names.size())
                .mapToObj(i -> baseFare - grpDisc - freqDiscs.get(i) - bdayDiscs.get(i))
                .map(Integer::doubleValue)
                .collect(Collectors.toList());
        List<Double> ivas = netos.stream()
                .map(net -> net * 0.19)
                .collect(Collectors.toList());
        List<Double> tots = IntStream.range(0, netos.size())
                .mapToObj(i -> netos.get(i) + ivas.get(i))
                .collect(Collectors.toList());

        r.setIntegrantesNeto(netos.stream().map(String::valueOf).collect(Collectors.joining(",")));
        r.setIntegrantesIva(ivas.stream().map(d -> String.format("%.2f", d)).collect(Collectors.joining(",")));
        r.setIntegrantesTotal(tots.stream().map(d -> String.format("%.2f", d)).collect(Collectors.joining(",")));

        // 7) Totales globales
        double sumNet = netos.stream().mapToDouble(Double::doubleValue).sum();
        double sumIva = ivas.stream().mapToDouble(Double::doubleValue).sum();
        r.setTotalNet(sumNet);
        r.setTotalIva(sumIva);
        r.setTotalAmount(sumNet + sumIva);

        // 8) (Opcional) totalPrice bruto
        r.setTotalPrice(Double.valueOf(baseFare * r.getNumberOfPersons()));

        // 9) Guardar antes de enviar el voucher
        ReservationEntity saved = reservationRepository.save(r);

        String rackServiceUrl = "http://localhost:8086/rack/occupy" // cambia el puerto si es distinto en tu local

                + "?day=" + saved.getStartTime().getDayOfWeek()
                + "&startTime=" + saved.getStartTime().toLocalTime()
                + "&reservationCode=" + saved.getReservationCode();

        restTemplate.postForObject(rackServiceUrl, null, Object.class);

        // 10) Enviar e-mail (si falla el mail, la transacción se revierte)
        sendVoucherEmailWithPdf(saved);

        return saved;
    }

    private void sendVoucherEmailWithPdf(ReservationEntity r) {
        byte[] pdf = generateVoucherPdf(r);
        r.getGroupEmails().forEach(to -> {
            try {
                MimeMessage msg = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
                helper.setFrom(mailFrom);
                helper.setTo(to);
                helper.setSubject("Comprobante Reserva #" + r.getReservationCode());
                helper.setText(buildVoucherText(r));
                helper.addAttachment("voucher-" + r.getReservationCode() + ".pdf",
                        new ByteArrayResource(pdf));
                mailSender.send(msg);
            } catch (MessagingException ex) {
                // log.warn("No se pudo enviar voucher a {}", to, ex);
                throw new RuntimeException("Error enviando voucher por email", ex);
            }
        });
    }

    private byte[] generateVoucherPdf(ReservationEntity r) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            // 1) Encabezado
            doc.add(new Paragraph("Comprobante de Reserva #" + r.getReservationCode()));
            doc.add(new Paragraph("Fecha inicio: " + r.getStartTime()));
            doc.add(new Paragraph("Fecha fin:    " + r.getEndTime()));
            doc.add(new Paragraph("Número de personas: " + r.getNumberOfPersons()));
            doc.add(new Paragraph("Cliente(s): " + r.getIntegrantesNombres()));
            doc.add(new Paragraph(" ")); // línea en blanco

            // 2) Construcción de la tabla
            PdfPTable table = new PdfPTable(8); // 8 columnas
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 2, 2, 2, 2, 2, 2, 2});

            // Cabecera
            Stream.of("Nombre",
                            "Tarifa Base",
                            "Desc. Grupo",
                            "Desc. Frecuente",
                            "Desc. Cumple",
                            "Neto",
                            "IVA",
                            "Total")
                    .forEach(header -> {
                        PdfPCell h = new PdfPCell(new Phrase(header));
                        h.setBackgroundColor(Color.LIGHT_GRAY);
                        h.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(h);
                    });

            // 3) Rellenar filas
            // Partimos cada CSV en listas
            List<String> names      = List.of(r.getIntegrantesNombres().split(","));
            List<String> bases      = List.of(r.getIntegrantesTarifaBase().split(","));
            List<String> grpDisc    = List.of(r.getIntegrantesDescGrupo().split(","));
            List<String> freqDisc   = List.of(r.getIntegrantesDescFrecuente().split(","));
            List<String> bdayDisc   = List.of(r.getIntegrantesDescCumple().split(","));
            List<String> netos      = List.of(r.getIntegrantesNeto().split(","));
            List<String> ivas       = List.of(r.getIntegrantesIva().split(","));
            List<String> totals     = List.of(r.getIntegrantesTotal().split(","));

            for (int i = 0; i < names.size(); i++) {
                table.addCell(names.get(i));
                table.addCell(bases.get(i));
                table.addCell(grpDisc.get(i));
                table.addCell(freqDisc.get(i));
                table.addCell(bdayDisc.get(i));
                table.addCell(netos.get(i));
                table.addCell(ivas.get(i));
                table.addCell(totals.get(i));
            }

            // 4) Añadimos la tabla al documento
            doc.add(table);

            // 5) Pie con totales generales
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total Neto: " + r.getTotalNet()));
            doc.add(new Paragraph("Total IVA:  " + r.getTotalIva()));
            doc.add(new Paragraph("Monto Total (USD): " + r.getTotalAmount()));

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF del voucher", e);
        }
    }


    private String buildVoucherText(ReservationEntity r) {
        return new StringBuilder()
                .append("Tu reserva #").append(r.getReservationCode()).append(" ha sido confirmada.\n")
                .append("Inicio: ").append(r.getStartTime()).append("\n")
                .append("Fin:    ").append(r.getEndTime()).append("\n")
                .append("Personas: ").append(r.getNumberOfPersons()).append("\n")
                .append("Total: ").append(r.getTotalAmount()).append("\n")
                .append("¡Gracias por elegirnos!")
                .toString();
    }

    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<ReservationEntity> findByUsername(String username) {
        return reservationRepository.findByGroupEmailsContaining(username);
    }

    public ReservationEntity getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + id));
    }

    public ReservationEntity updateReservation(Long id, ReservationEntity r) {
        ReservationEntity e = getReservationById(id);
        e.setStartTime(r.getStartTime());
        e.setEndTime(r.getEndTime());
        e.setGroupEmails(r.getGroupEmails());
        // aquí podrías recalcular o no
        return reservationRepository.save(e);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
