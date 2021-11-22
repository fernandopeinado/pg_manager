# pg_manager

PostgreSQL Manager, para Postgresql 9.6+ 

## Infraestrutura

- Agente PGMAN: extrai as informações do banco de dados e envia para o ELASTICSEARCH
- Elastic Search: é o repositório dos dados coletados 
- Kibana: interface para o monitoramento e descoberta

## PostgreSQL Environment

postgres.conf: carrege a extesão contrib `pg_stat_staments`

```
shared_preload_libraries = 'pg_stat_statements'
pg_stat_statements.max = 10000
pg_stat_statements.track = all
track_io_timing = on
track_activity_query_size = 10240
```

no banco de dados: 
```
CREATE EXTENSION PG_STAT_STATEMENTS
```

para a conexão do agente com o banco será necessário um usuário com permissões para ler e resetar a PG_STAT_STATEMENTS, 
e o deve haver liberação para a conexão do agente com o banco de dados no `pg_hba.conf`. 

## Fazendo o build

- maven 3.3+
- Java 11+
- docker

```bash
mvn clean install
mvn docker:push
```

## Subindo tudo com docker-compose

Use a pasta `deploy` como estrutura base para a criação de um ambiente completo. Nele temos as configurações do 
Elasticsearch, Kibana e do agente. 

Edite o arquivo `pgman.env` modificando principalmente os dados do acesso ao banco de dados:

```
PGMAN_DATA_SOURCE_URL=jdbc:postgresql://172.17.42.1:5432/postgres
PGMAN_DATA_SOURCE_USERNAME=postgres
PGMAN_DATA_SOURCE_PASSWORD=postgres
```

Se necessário modifique a versão do agente do pgman que deseja usar:

```yaml
  pgman:
    container_name: pgman
    image: "pgman:<VERSAO_AQUI>"
```

Rode `docker-compose up -d`

## Instalando os Paineis

Carregue os paineis importanto o arquivo `kibana-saved-objects.ndjson` da pasta deploy no kibana. Algus ajustes 
posteriores deverão ser feitos, principalmente em relação a thresholds nos gráficos.