## Steps to follow

Create a simple spring boot app.  
Its metrics end point will publish metric in prometheus format.  
Prometheus will scrape metrics endpoint.  
Prometheus will save data in time series database.  
Grafana will load up this data and visualize.  

User Docker command:  
`docker run -d -p 9090:9090 -v /Users/Prashant/LOCAL/Data/github/java/springboot-prometheus-grafana/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus`  

`docker run -d -p 3000:3000 grafana/grafana`  

Hit these end points to generate data in grafana:  
`http://localhost:8080/rasa/bot1`  
`http://localhost:8080/rasa/bot2`  
`http://localhost:8080/rasa/bot3`  
`http://localhost:8080/rasa/bot$`  

`http://localhost:8080/botformation/bot1`  
`http://localhost:8080/botformation/bot2`  
`http://localhost:8080/botformation/bot3`  
`http://localhost:8080/botformation/bot$`

Check grafana dashboard on : http://localhost:3000  
Check Prometheus on : http://localhost:9090  


#### Metrics  
These are the metrics collected at the moment.  
* Count how many requests we receive on particular endpoint for each bot.  
* Count how many requests error out on particular endpoint for each bot.  
* How long does it take to process each endpoint per bot (Latency).  
* Average message size on each endpoint per bot (mocked request size).  


Source:  
`https://stackabuse.com/monitoring-spring-boot-apps-with-micrometer-prometheus-and-grafana/`  
`https://www.youtube.com/watch?v=1_BZAA8T1G0`  
`https://tomgregory.com/spring-boot-default-metrics/`  
