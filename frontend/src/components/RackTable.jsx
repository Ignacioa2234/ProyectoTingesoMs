import React, { useEffect, useState } from "react";
import RackService from "../services/rack.service";

// Días en español (puedes traducir o dejar en inglés)
const daysOfWeek = [
  "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
];

const hourBlocks = [
  "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", 
  "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
  "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"
]; // ajusta según tu horario real

const RackTable = () => {
  const [rack, setRack] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchRack = async () => {
      let weekRack = {};
      for (let day of daysOfWeek) {
        try {
          const res = await RackService.getDayRack(day);
          weekRack[day] = res.data;
        } catch (e) {
          weekRack[day] = [];
        }
      }
      setRack(weekRack);
      setLoading(false);
    };
    fetchRack();
  }, []);

  if (loading) return <div>Cargando rack semanal...</div>;

  return (
    <div style={{ overflowX: "auto" }}>
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Hora</th>
            {daysOfWeek.map(day => (
              <th key={day}>{day.slice(0, 3)}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {hourBlocks.map(hour => (
            <tr key={hour}>
              <td>{hour}</td>
              {daysOfWeek.map(day => {
                // Encuentra si el bloque está reservado
                const slot = rack[day]?.find(
                  b => b.startTime.slice(0,5) === hour
                );
                const ocupado = slot && slot.reservationCode;
                return (
                  <td
                    key={day + hour}
                    style={{
                      background: ocupado ? "#ccc" : "#eaffea",
                      minWidth: 40,
                      textAlign: "center",
                      fontWeight: ocupado ? "bold" : "normal"
                    }}
                  >
                    {ocupado ? "X" : ""}
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RackTable;
