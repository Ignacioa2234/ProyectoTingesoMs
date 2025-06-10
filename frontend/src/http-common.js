import axios from 'axios';

export default axios.create({
  baseURL: 'http://localhost:8080', // tu API Gateway
  headers: { 'Content-Type': 'application/json' }
});
