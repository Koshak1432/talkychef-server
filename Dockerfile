FROM gradle:8.2.0-jdk17
WORKDIR /app
COPY . /app

RUN gradle --no-daemon --warning-mode all --console=plain getDeps

EXPOSE 8080
CMD ["sh", "start.sh"]


