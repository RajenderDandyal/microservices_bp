# Use a lightweight Node.js image
FROM node:18-alpine

# Set the working directory
WORKDIR /usr/src/app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the application code
COPY . .

# The service will listen on this port
EXPOSE 8080

# The command to start the service
CMD [ "node", "index.js" ]