# The cassandra UDT template

## This guides aims for the following
* Creating a UDT
* Saving the UDT or List of UDTs to cassandra
* Fetching the UDT from cassandra
* Fetching the List of UDT from cassandra

Before running the sbt run make sure you have cassandra running on localhost and port 9042, if not don't worry follow the steps below to get it done

Run using docker

##### Start Cassandra docker container

```docker run -p9042:9042 cassandra:latest```

##### Run the project

```sbt run```

Database created by the project looks like
```
CREATE TABLE user_movies.user (
       name text PRIMARY KEY,
       movies list<frozen<movie>>
   )
``` 
 and here is how movies look like once they are saved
 
```

 name    | movies
---------+-----------------------------------------------------------------------
 Shubham | [{name: 'Intersteller', year: 2004}, {name: 'Mahabhart', year: 2000}]

```