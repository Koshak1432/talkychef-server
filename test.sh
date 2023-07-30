#DB
export DB_PLATFORM=org.hibernate.dialect.H2Dialect
export DATASOURCE_URL=jdbc:h2:mem:testdb
export DATASOURCE_USERNAME=sa
export DATASOURCE_PASSWORD=password
export SCHEMA_NAME=public
#SSL
export ENABLE_SSL=false
export KEYSTORE_PATH=
export KEYSTORE_PASSWORD=
export KEYSTORE_TYPE=
export KEYSTORE_KEY_PASSWORD=
export KEYSTORE_KEY_ALIAS=
#OTHER
export SERVER_PORT=8080
export ENABLE_HTTP2=false

./gradlew test