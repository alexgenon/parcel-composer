# Stage 1: Build an Angular Docker Image
FROM node as build
WORKDIR /app
COPY package*.json /app/
RUN npm ci
COPY src /app/src
COPY angular.json tsconfig* ngsw-config.json package.json /app/
ARG configuration=production
RUN npm run build -- --outputPath=./dist/out --configuration $configuration

# Stage 2, use the compiled app, ready for production with Nginx
FROM nginx
EXPOSE 80 443
RUN openssl req -x509 -nodes -days 365 -subj "/C=BE/ST=LU/O=Au fil de mes coutures/CN=aufildemescoutures.be" -addext "subjectAltName=IP:192.168.1.7" -newkey rsa:2048 -keyout /etc/ssl/private/nginx-selfsigned.key -out /etc/ssl/certs/nginx-selfsigned.crt;
COPY --from=build /app/dist/out/ /usr/share/nginx/html
COPY ./Infrastructure/nginx-custom.conf /etc/nginx/conf.d/default.conf