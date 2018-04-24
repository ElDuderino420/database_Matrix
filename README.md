# database_Matrix

### Setup of experiment

I use 2 docker containers one with postgresql and one with neo4j.

For neo4j:
1. First start docker
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
2. Copy data
docker cp "path\to\nodes\social_network_nodes.csv" neo4j:/nodes.csv
docker cp "path\to\edges\social_network_edges.csv" neo4j:/edges.csv
3. docker exec -it neo4j bash
4. neo4j stop 
5. rm -rf data/databases/graph.db
6. Import data
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
1. First start docker
```bash
docker run -p 5432:5432 -d --name psql postgres:alpine
```
2. Copy data
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

After this is done you should have 2 containers with databases with data in them
Now run the main.java file and it should start printing lots of numbers.

### Results

| depth   | mean SQL | Median SQL | Mean Neo4j | Median Neo4j |
|---------|----------|------------|------------|--------------|
| depth 1 | 1066      | 850        | 1334        | 455          |
| depth 2 | 1912     | 1696       | 516        | 414          |
| depth 3 | 4039     | 3706       | 2294        | 635          |

Depth 4 and 5 takes way too long (several hours) to complete all 20 random nodes so those results will not be shown

| Depth 1 | Depth 2 | Depth 3 | Depth 4+5 |
|---------|----------|------------|------------|
| ![data_1](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d1.png "depth 1 data") | ![data_2](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d2.png "depth 2 data") | ![data_3](https://github.com/Thug-Lyfe/database_Matrix/blob/master/pics/d3.png "depth 3 data") | to be continued |

### Conclusion and discussion
With a small resultset (around depth 3 and down) neo4j is marginally faster, while after this it falls dratiscally in efficiency
While SQL increases more or less linear with the amount of data handled.
I might have done something horribly wrong in my queries or code, but neo4j is generally much more inconsistent in the time spent on the queries. 
From the pictures can it can be seen how neo4j's times swing much more than the SQL times. 

As it is rare to go to this high depth. I would recomend a neo4j database as it is generally faster in the lower depths, plus the queries are much simpler to write compared to SQL. If features such as shortest path between nodes or something similar is needed then neo4j is highly recomended.
