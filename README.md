# Hedera Fee Calculator

This tool provides the library, REST API and UI for calculating fee for various Hedera APIs

# Configuration

## Backend
1. Update the `fee-app/src/main/resources/application.properties` file to configure 
- The server address and port on which the backend runs 
- Allow CORS ports so that the frontend can access the backend
   
Here is a sample configuration:
```aiignore
server.port=${BACKEND_PORT:8080}
server.address=${BACKEND_HOST:0.0.0.0}
frontend.allowed-origins=http://localhost:5173,http://localhost:3000,http://localhost:80
```

## Frontend
### Configuration to inform frontend about where to reach the backend
- Update `fee-ui/.env` file.

Here is a sample configuration:
```aiignore
VITE_BACKEND_URL=http://localhost:8080/api/v1/transactions
```
Update this VITE_BACKEND_URL variable to point to the host:port of the backend

### Configure the port on which the frontend runs
- Update the `fee-ui/vite.config.js` file

Here is a sample configuration:
```aiignore
export default defineConfig({
  plugins: [react()],
    server: {
      port: 3000
    }
})
```
Update the port where you want the frontend to run.

