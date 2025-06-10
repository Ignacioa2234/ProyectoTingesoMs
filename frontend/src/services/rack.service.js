import http from '../http-common';

const getDayRack = (day) => http.get(`/rack/day/${day}`);

export default { getDayRack };
