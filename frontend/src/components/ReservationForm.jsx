// frontend/src/components/ReservationForm.jsx

import React, { useState } from 'react';
import ReservationService from '../services/reservation.service';

const ReservationForm = () => {
  const [startDate, setStartDate] = useState('');
  const [startTime, setStartTime] = useState('');
  const [numPersons, setNumPersons] = useState(1);
  const [emails, setEmails] = useState(['']);
  const [error, setError] = useState(null);

  const handleEmailChange = (idx, value) => {
    const copy = [...emails];
    copy[idx] = value;
    setEmails(copy);
  };

  const addParticipant = () => {
    setEmails([...emails, '']);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    // 1) Construimos el payload EXACTO que espera tu back:
    const payload = {
      startTime: `${startDate}T${startTime}`,
      // en tu entidad usas endTime, aquí asumimos 2h de reserva fija (ajustar según tu UI)
      endTime: `${startDate}T${String((parseInt(startTime, 10) + 2) % 24).padStart(2, '0')}:00`,
      numberOfPersons: numPersons,
      groupEmails: emails.filter(email => email.trim() !== '')
    };

    try {
      const resp = await ReservationService.create(payload);
      console.log('Reserva creada:', resp.data);
      // aquí podrías limpiar el form o redirigir al usuario
    } catch (err) {
      console.error(err);
      setError('Error al crear la reserva.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {error && <div className="alert alert-danger">{error}</div>}

      {/* Fecha y hora */}
      <div className="mb-3">
        <label>Fecha de Reserva</label>
        <input
          type="date"
          className="form-control"
          value={startDate}
          onChange={e => setStartDate(e.target.value)}
          required
        />
      </div>
      <div className="mb-3">
        <label>Hora de Reserva</label>
        <input
          type="time"
          className="form-control"
          value={startTime}
          onChange={e => setStartTime(e.target.value)}
          required
        />
      </div>

      {/* Número de personas */}
      <div className="mb-3">
        <label># Personas</label>
        <input
          type="number"
          className="form-control"
          value={numPersons}
          min="1"
          onChange={e => setNumPersons(parseInt(e.target.value, 10))}
          required
        />
      </div>

      {/* Emails del grupo */}
      <h5>Participantes</h5>
      {emails.map((email, idx) => (
        <div className="mb-2" key={idx}>
          <label>Email #{idx + 1}</label>
          <input
            type="email"
            className="form-control"
            value={email}
            onChange={e => handleEmailChange(idx, e.target.value)}
            required
          />
        </div>
      ))}
      <button
        type="button"
        className="btn btn-link"
        onClick={addParticipant}
      >
        + Añadir participante
      </button>

      <hr/>

      <button type="submit" className="btn btn-primary">
        Reservar
      </button>
    </form>
  );
};

export default ReservationForm;
