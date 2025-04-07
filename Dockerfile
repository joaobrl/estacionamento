# Usar a imagem oficial do Java 17 como base
FROM openjdk:17-jdk-slim

# Instalar PostgreSQL
RUN apt-get update && apt-get install -y postgresql postgresql-contrib

# Criar diretórios para a aplicação e o banco de dados
RUN mkdir -p /app /var/lib/postgresql/data /var/log/postgresql

# Ajustar permissões do diretório do banco de dados e do log
RUN chown -R postgres:postgres /var/lib/postgresql/data /var/log/postgresql

# Copiar o arquivo JAR da aplicação para o diretório de trabalho
COPY target/api-0.0.1-SNAPSHOT.jar /app/api-0.0.1-SNAPSHOT.jar

# Configurar PostgreSQL
USER postgres
RUN /usr/lib/postgresql/13/bin/initdb -D /var/lib/postgresql/data && \
    /usr/lib/postgresql/13/bin/pg_ctl -D /var/lib/postgresql/data -l /var/log/postgresql/logfile start && \
    psql --command "CREATE USER root WITH PASSWORD 'root123';" && \
    createdb -O root estacionamento

# Expor as portas que a aplicação e o banco de dados irão rodar
EXPOSE 8080 5432

# Comando para iniciar o PostgreSQL e a aplicação
CMD /usr/lib/postgresql/13/bin/pg_ctl -D /var/lib/postgresql/data -l /var/log/postgresql/logfile start && java -jar /app/api-0.0.1-SNAPSHOT.jar