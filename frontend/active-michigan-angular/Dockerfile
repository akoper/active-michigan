# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Production stage
FROM nginx:stable-alpine
COPY --from=build /app/dist/active-michigan-angular/browser /usr/share/nginx/html
# Custom nginx config to handle SPA routing
COPY nginx.conf /etc/nginx/conf.d/config.template
ENV PORT=8080
CMD ["sh", "-c", "envsubst \"\\$PORT\" < /etc/nginx/conf.d/config.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
