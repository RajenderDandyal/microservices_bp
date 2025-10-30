const express = require('express');
const jwt = require('jsonwebtoken');

const app = express();
const port = 8080;

// The secret key should be stored securely, e.g., in an environment variable.
const JWT_SECRET = process.env.JWT_SECRET;
// console.log("Using JWT_SECRET:", JWT_SECRET)

if (!JWT_SECRET) {
    console.error("FATAL ERROR: JWT_SECRET environment variable is not set.");
    process.exit(1);
}

app.get('/validate', (req, res) => {
    const authHeader = req.headers['authorization'];

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        console.log('Validation failed: No Bearer token provided.');
        return res.status(401).send('Unauthorized: No token provided.');
    }

    const token = authHeader.split(' ')[1];

    try {
        // Verify the token using the secret key
        const decoded = jwt.verify(token, JWT_SECRET);

        // If verification is successful, send a 200 OK response.
        // Also, pass claims to the backend service via response headers.
        // These header names must match what's in the Ingress annotation.
        res.setHeader('X-User-ID', decoded.userId || '');
        res.setHeader('X-User-Roles', decoded.roles || '');
        
        console.log(`Validation successful for userId: ${decoded.userId}`);
        res.status(200).send('OK');

    } catch (err) {
        // If the token is invalid (expired, wrong signature, etc.)
        console.log(`Validation failed: ${err.message}`);
        res.status(401).send('Unauthorized: Invalid token.');
    }
});

app.listen(port, () => {
    console.log(`JWT Validator service listening on port ${port}`);
});