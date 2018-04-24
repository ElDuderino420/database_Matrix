# database_Matrix

### setup of experiment

I use 2 docker containers one with postgresql and one with neo4j.

for neo4j:
1. first start docker
```bash
docker run \
-d --name neo4j \
--publish=7474:7474 \
--publish=7687:7687 \
--env NEO4J_AUTH=neo4j/class \
--env=NEO4J_dbms_memory_pagecache_size4G \
--env=NEO4J_dbms_memory_heap_initial__size4G \
--env=NEO4J_dbms_memory_heap_max__size4G \
neo4j
```
2. copy data
docker cp "path\to\nodes\social_network_nodes.csv" neo4j:/nodes.csv
docker cp "path\to\edges\social_network_edges.csv" neo4j:/edges.csv
3. docker exec -it neo4j bash
4. neo4j stop 
5. rm -rf data/databases/graph.db
6. import data
```bash
neo4j-admin import \
    --nodes:Users nodes.csv \
    --relationships:ENDORSES edges.csv \
    --ignore-missing-nodes=true \
    --ignore-duplicate-nodes=true \
    --id-type=INTEGER
```
7. go to neo4j.conf and change all heap sizes to 8G or more
8. neo4j start
9. go to http://localhost:7474/browser/ and run  create INDEX ON :Users(id)


For postgresql
1. first start docker
```bash
docker run -p 5432:5432 -d --name psql postgres:alpine
```
2. copy data
docker cp "path\to\nodes\social_network_nodes.csv" neo4j:/nodes.csv
docker cp "path\to\edges\social_network_edges.csv" neo4j:/edges.csv
3. docker exec -it psql bash -c "psql -U postgres"
4. create table:
```bash
create table t_user(id int primary key,name varchar(100), job varchar(100), birthday date);
copy t_user(id,name,job,birthday) from 'nodes.csv' DELIMITER ',' CSV HEADER;

create table t_edges(source_node_id int references t_user(id),target_node_id int references t_user(id));
copy t_edges(source_node_id,target_node_id) from '/edges.csv' DELIMITER ',' CSV HEADER;
```

after this is done you should have 2 containers with databases with data in them
now run the main.java file and it should start printing lots of numbers.

calculated times:

| depth   | mean SQL | Median SQL | Mean Neo4j | Median Neo4j |
|---------|----------|------------|------------|--------------|
| depth 1 | 440      | 349        | 377        | 268          |
| depth 2 | 1436     | 1075       | 308        | 242          |
| depth 3 | 1892     | 1913       | 279        | 274          |

depth 4 and 5 takes way too long (several hours) to complete all 20 random nodes so those results will not be shown


depth 4 and 5 takes way too long (several hours) to complete all 20 random nodes so those results will not be shown
the pictures below show times to get each user from either database in order.
![data_1](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d1.png "depth 1 data")
![data_2](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d2.png "depth 2 data")
![data_3](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d3.png "depth 3 data")

