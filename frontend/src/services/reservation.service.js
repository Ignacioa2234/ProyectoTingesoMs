import http from '../http-common.js';

const create = payload => http.post('/reservas', payload);

export default { create };