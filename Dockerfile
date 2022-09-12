FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./target/TinkoffService-0.0.1.jar .

#EXPOSE 8004 - не пробросит а просто декларирует об этом
#ENV TZ Europe/Moscow - харкод

CMD ["java","-jar","TinkoffService-0.0.1.jar"]