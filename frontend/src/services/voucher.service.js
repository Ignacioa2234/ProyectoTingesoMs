import http from '../http-common'

const create = body => http.post('/reservas/voucher', body)

export default { create }
