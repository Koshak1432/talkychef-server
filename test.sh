#DB
export DB_PLATFORM=org.hibernate.dialect.H2Dialect
export DATASOURCE_URL=jdbc:h2:mem:testdb
export DATASOURCE_USERNAME=sa
export DATASOURCE_PASSWORD=password
export SCHEMA_NAME=public
#SSL
export ENABLE_SSL=false
#export KEYSTORE_PATH=src/main/resources/voicerecipe.p12
#export KEYSTORE_PASSWORD=e621e522
#export KEYSTORE_TYPE=pkcs12
#export KEYSTORE_KEY_PASSWORD=e621e522
#export KEYSTORE_KEY_ALIAS=voicerecipe
#OTHER
export SERVER_PORT=8080
./gradlew test
