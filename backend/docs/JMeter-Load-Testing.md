## JMeter Load Testing: Critical Endpoint

We will load test `POST /api/tasks` simulating concurrent users and analyze key metrics.

### Install JMeter
- Download Apache JMeter (`https://jmeter.apache.org`) and extract.
- Launch `bin/jmeter.bat` (Windows) or `bin/jmeter` (Mac/Linux).

Screenshot prompts:
- Screenshot 1: JMeter UI open.

### Create Test Plan
1. Test Plan → Add Thread Group
   - Users: 50, Ramp-up: 10s, Loop: 2
2. Add HTTP Request Defaults
   - Server Name: `localhost`, Port: `8080`
3. Add HTTP Header Manager
   - `Content-Type: application/json`
4. Add HTTP Request (POST /api/tasks)
   - Method: POST, Path: `/api/tasks`
   - Body Data: `{ "title":"JMeter Task", "description":"load", "priority":"LOW" }`
5. Add Listeners: Summary Report, Aggregate Report, View Results Tree

Screenshot prompts:
- Screenshot 2: Thread Group settings.
- Screenshot 3: HTTP Request sampler.

### Run Load Test
1. Start backend: `mvn spring-boot:run`
2. In JMeter, click Start.

Screenshot prompts:
- Screenshot 4: Summary Report with throughput and response times.
- Screenshot 5: Aggregate Report.

### Metrics to Capture
- Response times: average, 90th/95th percentile
- Throughput (requests/sec)
- Error rate

### Analysis & Bottlenecks
- If response time > target, check DB writes, synchronization, logging level.
- Use JVM profiling (VisualVM) and Spring Actuator metrics.

Screenshot prompt:
- Screenshot 6: Annotated report highlighting percentiles and error rate.

### Save Test Plan
- Save as `docs/jmeter/task_post_test_plan.jmx` (or a `jmeter/` folder).


