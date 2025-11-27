echo "Deploy in server"
chmod +x deploy.sh
./deploy.sh

echo "Import database"
docker exec -i mysql-cvconnect mysql -u root -proot --default-character-set=utf8mb4 < mysql.sql

echo "Mysql"
docker exec -it mysql-cvconnect mysql -u root -proot --default-character-set=utf8mb4
show databases;
use `cvconnect-user-service`;
show tables;

echo "Postgres"
docker exec -it postgres-cvconnect psql -U postgres
\l
\c cvconnect-core-service
\dt
