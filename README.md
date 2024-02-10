# Kotlin World

Use only in dev mode.

### K6 Load Test

_**Before all, ensure that you have Docker running on your machine.**_

Start application in dev mode and run the following command:

```bash
quarkus dev
```

Then run the following command for K6 test:

```bash
docker run --add-host=host.docker.internal:host-gateway \
  --rm -i grafana/k6 run - <k6/script.js
```
