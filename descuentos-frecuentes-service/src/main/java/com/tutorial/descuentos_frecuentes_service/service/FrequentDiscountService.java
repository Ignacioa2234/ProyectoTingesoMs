package com.tutorial.descuentos_frecuentes_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FrequentDiscountService {

    private final RestTemplate restTemplate;

    /**
     * Calcula el descuento frecuente para un usuario basado
     * en cuántas reservas ha hecho en los últimos 30 días.
     */
    public int calculateFrequentDiscount(String username) {
        // 1) Llamada a reservas-service usando Eureka/LoadBalancer
        ResponseEntity<List<ReservationDto>> resp = restTemplate.exchange(
                "lb://reservas-service/reservas?user={user}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReservationDto>>() {},
                username
        );
        List<ReservationDto> reservas = resp.getBody();
        if (reservas == null) {
            return 0;
        }

        // 2) Filtrar sólo las reservas de los últimos 30 días
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        long visitas = reservas.stream()
                .filter(r -> r.getReservationDate().isAfter(haceUnMes))
                .count();

        // 3) Aplicar la tabla de descuentos
        if (visitas >= 7) return 30;
        if (visitas >= 5) return 20;
        if (visitas >= 2) return 10;
        return 0;
    }

    /**
     * DTO interno para mapear sólo los campos necesarios
     * de la respuesta de /reservas del microservicio de reservas.
     */
    public static class ReservationDto {
        private String reservationCode;
        private LocalDateTime reservationDate;

        public String getReservationCode() {
            return reservationCode;
        }

        public void setReservationCode(String reservationCode) {
            this.reservationCode = reservationCode;
        }

        public LocalDateTime getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(LocalDateTime reservationDate) {
            this.reservationDate = reservationDate;
        }
    }
}
