# Build stage
FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# Serve stage
FROM nginx:stable-alpine
# Copia build estático al sitio de nginx
COPY --from=build /app/dist /usr/share/nginx/html
# Exponer puerto 80
EXPOSE 80
# No hace falta ENTRYPOINT, nginx ya lo tiene
